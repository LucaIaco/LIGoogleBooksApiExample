package com.liaconis.com.googlebooksexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.liaconis.com.googlebooksexample.utils.LIBookArrayAdapter;
import com.liaconis.com.googlebooksexample.utils.LIBookConstants;
import com.liaconis.com.googlebooksexample.utils.LIBooksFetchTask;

import java.util.List;

/**
 * Created by Luca Iaconis on 15/04/2016.
 */
public class LIMainActivity extends AppCompatActivity {

    private long _volumeOffset = 0;
    private List<Volume> _fetchedVolumes = null;
    private long _fetchedTotalItems = 0;

    private ListView _bookListView = null;

    private Boolean _bottomListReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limain);

        this._bookListView = (ListView) findViewById(R.id.bookList);

        final LIMainActivity that = this;

        this._bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Volume selectedBook = (Volume) parent.getItemAtPosition(position);

                Intent intent = new Intent(LIMainActivity.this, LIDetailActivity.class);

                // prepare necessary data to send into the detail activity
                Bundle b = new Bundle();
                b.putString("bookId",selectedBook.getId());
                b.putString("bookTitle",selectedBook.getVolumeInfo().getTitle());
                b.putString("bookAuthor",selectedBook.getVolumeInfo().getAuthors().get(0));
                b.putString("bookDescription",selectedBook.getVolumeInfo().getDescription());
                b.putString("bookPublisher",selectedBook.getVolumeInfo().getPublisher());
                b.putString("bookPublishedDate",selectedBook.getVolumeInfo().getPublishedDate());
                b.putLong("bookPageCount",selectedBook.getVolumeInfo().getPageCount().longValue());
                b.putString("bookLang",selectedBook.getVolumeInfo().getLanguage());
                b.putString("bookThumbLink",selectedBook.getVolumeInfo().getImageLinks().getThumbnail());
                Volume.SaleInfo.ListPrice listPrice = selectedBook.getSaleInfo().getListPrice();
                b.putDouble("bookPrice",listPrice == null ? 0 : listPrice.getAmount().doubleValue());
                b.putString("bookPriceCurrency",listPrice == null ? "" : listPrice.getCurrencyCode());
                intent.putExtras(b);

                startActivity(intent);
            }
        });

        this._bookListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (that._bottomListReached && that._volumeOffset < that._fetchedTotalItems){
                    that._runSearch(false);
                    that._bottomListReached = false;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if (!that._bottomListReached)
                        that._bottomListReached = true;
                }else{
                    that._bottomListReached = false;
                }

            }
        });

        this._runSearch(true);
    }

    /**
     * Incrementally Retrieve the information from Google Books Api in chunks, and populate the table
     * @param reset if true will move the loading offset list to zero, so will restart the fetch from the scratch
     */
    private void _runSearch(boolean reset){
        if (reset){
            this._volumeOffset = 0;
            this._fetchedTotalItems = 0;
            this._fetchedVolumes = null;
        }

        this._showProgressBar(true);

        final LIMainActivity that = this;

        // run call request to google book apis
        new LIBooksFetchTask(){

            @Override
            protected void onPostExecute(Volumes volumes) {
                super.onPostExecute(volumes);

                that._showProgressBar(false);

                // empty result or error
                if (volumes == null){
                    return;
                }

                that._fetchedTotalItems = volumes.getTotalItems().longValue();

                long nextInc = ( (that._fetchedTotalItems - that._volumeOffset) >= LIBookConstants.VOLUME_CHUNK_SIZE ?
                        LIBookConstants.VOLUME_CHUNK_SIZE :
                        (that._fetchedTotalItems - that._volumeOffset) );
                that._volumeOffset += nextInc;

                // append new items if needed
                if (that._fetchedVolumes == null){
                    that._fetchedVolumes = volumes.getItems();
                }else{
                    that._fetchedVolumes.addAll(volumes.getItems());
                }

                // populate list view with results
                if (that._bookListView.getAdapter() == null) {
                    that._bookListView.setAdapter(new LIBookArrayAdapter(that, that._fetchedVolumes));
                }else{
                    ((BaseAdapter)that._bookListView.getAdapter()).notifyDataSetChanged();
                }

            }
        }.performTask(this._volumeOffset,"thriller");

    }

    private void _showProgressBar(Boolean show){
        ProgressBar progress = (ProgressBar)this.findViewById(R.id.progressBarLoading);
        progress.setIndeterminate(show);
        progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


}
