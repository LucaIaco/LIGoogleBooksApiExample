package com.liaconis.com.googlebooksexample.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.liaconis.com.googlebooksexample.R;

import java.util.List;

/**
 * Created by Luca Iaconis on 15/04/2016.
 */
public class LIBookArrayAdapter extends BaseAdapter{
    private Context _context;
    private List<Volume> _datasource;
    private static LayoutInflater inflater = null;

    public LIBookArrayAdapter(Context context, List<Volume> data) {
        // TODO Auto-generated constructor stub
        this._context = context;
        this._datasource = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this._datasource.size();
    }

    @Override
    public Volume getItem(int position) {
        // TODO Auto-generated method stub
        return this._datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.main_table_row, null);

        Volume volumeX = this._datasource.get(position);

        TextView txtTitle = (TextView) vi.findViewById(R.id.bookTitle);
        TextView txtAuthor = (TextView) vi.findViewById(R.id.bookAuthor);

        String strTitle = volumeX.getVolumeInfo().getTitle();
        txtTitle.setText(strTitle != null ? strTitle : "N/A");

        List<String> authors = volumeX.getVolumeInfo().getAuthors();
        txtAuthor.setText(authors.size() > 0 ? authors.get(0) : "N/A");

        //TODO Implement thumbnail image loading using an async method and cache

        return vi;
    }
}
