package com.liaconis.com.googlebooksexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Luca Iaconis on 15/04/2016.
 */
public class LIDetailActivity extends AppCompatActivity {

    private Bundle _bookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lidetail);

        this._bookInfo = this.getIntent().getExtras();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView)this.findViewById(R.id.bookTitleDetail)).setText(this._bookInfo.getString("bookTitle"));
        ((TextView)this.findViewById(R.id.bookAuthorDetail)).setText(this._bookInfo.getString("bookAuthor"));

        String description = this._bookInfo.getString("bookDescription");
        if (description == null || description.trim().length() == 0 )
            description = "No description available";
        ((TextView)this.findViewById(R.id.bookDescDetail)).setText(description);
        ((TextView)this.findViewById(R.id.bookPublisherDetail)).setText(this._bookInfo.getString("bookPublisher"));
        ((TextView)this.findViewById(R.id.bookPublishedDateDetail)).setText(this._bookInfo.getString("bookPublishedDate"));
        Long pageCnt = this._bookInfo.getLong("bookPageCount");
        ((TextView)this.findViewById(R.id.bookPageCountDetail)).setText(pageCnt != null ? String.valueOf(pageCnt) : "N/A");
        ((TextView)this.findViewById(R.id.bookLanguageDetail)).setText(this._bookInfo.getString("bookLang"));
        String price = "Free";
        if (this._bookInfo.getDouble("bookPrice") != 0)
            price = String.valueOf(this._bookInfo.getDouble("bookPrice")) + " " + this._bookInfo.getString("bookPriceCurrency");
        ((TextView)this.findViewById(R.id.bookPriceDetail)).setText(price);

        //TODO no thumbnail has been loaded yet. Implement it and, like for the Book list, use a cache method

    }
}
