package com.skaloot.skalootapp;




import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountActivity extends AppCompatActivity {


    public String strURL;
    public ImageLoader imgLoader;
    public String title;
    public String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        String HOST = getResources().getString(R.string.host);

        Intent intent = getIntent();
        String jsonObj = intent.getStringExtra("jsonArray");

        try {
            JSONObject json = new JSONObject(jsonObj);
            title = json.getString("title");
            imgUrl = json.getString("imgUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.laundry_text);
        textView.setText(title);
        Log.e("Image Title", "Image Title - " + title.toUpperCase() + " | " + imgUrl);

        strURL = HOST + "totus/images/" + imgUrl;

        ImageView imageView = (ImageView) findViewById(R.id.laundry_image);
        imgLoader = new ImageLoader(this.getBaseContext());
        imgLoader.DisplayImage(strURL, imageView);
    }


    @Override
    public void onBackPressed() {
        Log.e("Button Pressed", "Button Back");
        super.onBackPressed();
    }

    public void go_back(View view) {
        finish();
    }

}
