package com.liaconis.com.googlebooksexample.utils;

import android.os.AsyncTask;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;

import java.text.NumberFormat;

/**
 * Created by Luca Iaconis on 15/04/2016.
 */
public class LIBooksFetchTask extends AsyncTask<String, Void, Volumes> {


    private static final String APPLICATION_NAME = "L.Iaconis - Google Books Eample";

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();

    private Exception _exception;
    private long _curFetchOffset;
    private String _curSearchKeyword;

    public LIBooksFetchTask() {
        super();
        this._curFetchOffset = 0;
        this._curSearchKeyword = "";
        this._exception = null;
    }

    public AsyncTask<String, Void, Volumes> performTask(long fetchOffset,String keyword){
        this._curFetchOffset = (fetchOffset < 0 ? 0 : fetchOffset);
        this._curSearchKeyword = (keyword == null ? "adventure" : keyword);
        return this.execute("");
    }

    protected Volumes doInBackground(String... queries) {
        try {

            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            System.out.println("-- Google Books Example --");

            // Set up Books client.
            final Books books = new Books.Builder(new NetHttpTransport(), jsonFactory, null)
                    .setApplicationName(APPLICATION_NAME)
                    /*.setGoogleClientRequestInitializer(new BooksRequestInitializer(LIBookConstants.API_KEY))*/
                    .build();

            Books.Volumes.List volumesList = books.volumes().list(this._curSearchKeyword);
            volumesList.setQ(this._curSearchKeyword);
            volumesList.setStartIndex(this._curFetchOffset);
            volumesList.setMaxResults(LIBookConstants.VOLUME_CHUNK_SIZE);
            //volumesList.setOrderBy("newest");

            // Execute the query.
            Volumes volumes = volumesList.execute();

            if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
                return null;
            }

            return volumes;

        } catch (Exception e) {
            this._exception = e;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Volumes volumes) {
        super.onPostExecute(volumes);
    }
}

