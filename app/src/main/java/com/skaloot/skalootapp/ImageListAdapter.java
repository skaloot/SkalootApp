package com.skaloot.skalootapp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
//    String HOST = "http://www.traveltho.com/";
     String HOST = "http://192.168.0.10/";

    private ImageView imgView;
    private ImageLoader imgLoader;
    public String strURL;

    public ImageListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.laundry, itemname);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    static class ViewHolderItem {
        ImageView imageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.laundry, parent, false);

            viewHolder  = new ViewHolderItem();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.laundry_image);
            convertView.setTag(viewHolder);
//            viewHolder.imageView.setImageResource(imgid[position]);
//            viewHolder.imageView.setTag(imgid[position]);
            Log.e("img", itemname[position]);
            strURL = HOST + "totus/images/" + itemname[position] + ".png";
            imgLoader = new ImageLoader(this.getContext());
            imgLoader.DisplayImage(strURL, viewHolder.imageView);
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.laundry_image))
                    .execute(strURL);
            Log.e("img", strURL);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        return convertView;
    }





    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            String uri = "@drawable/";
            int imageResource = bmImage.getContext().getResources().getIdentifier(uri, null, "com.skaloot.skalootapp");
            if (bmImage != null) {
                if (result != null) {
                    bmImage.setImageBitmap(result);
                } else {
                    Drawable placeholder = bmImage.getContext().getResources().getDrawable(imageResource);
                    bmImage.setImageDrawable(placeholder);
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}