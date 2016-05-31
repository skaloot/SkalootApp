package com.skaloot.skalootapp;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;



public class ImageListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] imgUrl;
    private final Integer[] imgId;

    public ImageLoader imgLoader;
    public String strURL;

    public ImageListAdapter(Activity context, String[] itemName, Integer[] imgId) {
        super(context, R.layout.laundry, itemName);
        this.context = context;
        this.imgUrl = itemName;
        this.imgId = imgId;
    }

    static class ViewHolderItem {
        ImageView imageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        String HOST = this.getContext().getResources().getString(R.string.host);

        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.laundry, parent, false);

            viewHolder  = new ViewHolderItem();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.laundry_image);
            strURL = HOST + "totus/images/" + imgUrl[position];
            imgLoader = new ImageLoader(this.getContext());
            imgLoader.DisplayImage(strURL, viewHolder.imageView);
            Log.e("img", strURL);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        return convertView;
    }

}