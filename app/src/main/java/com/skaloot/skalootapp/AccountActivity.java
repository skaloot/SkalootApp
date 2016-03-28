package com.skaloot.skalootapp;




import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AccountActivity extends AppCompatActivity {


//    String HOST = "http://www.traveltho.com/";
    String HOST = "http://192.168.0.10/";
    public String strURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

//        ImageView btnback = (ImageView)findViewById(R.id.btn_back);
//        btnback.setImageResource(R.drawable.button_back);
//        ImageView btncart = (ImageView)findViewById(R.id.btn_cart);
//        btncart.setImageResource(R.drawable.button_drawer);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.laundry_text);
        textView.setText(text);

        strURL = HOST + "totus/images/" + text + ".png";
        new DownloadImageTask((ImageView) findViewById(R.id.laundry_image))
                .execute(strURL);
    }




    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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
//                    Drawable placeholder = bmImage.getContext().getResources().getDrawable(imageResource);
//                    bmImage.setImageDrawable(placeholder);
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


        @Override
    public void onBackPressed() {
        Log.e("Button Pressed", "Button Back");
        // finish();
        super.onBackPressed();
    }

    public void go_back(View view) {
        finish();
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//        }
//        return true;
//    }

}
