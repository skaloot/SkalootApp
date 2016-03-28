package com.skaloot.skalootapp;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SettingActivity extends AppCompatActivity {

    public final static String DetailText = "com.skaloot.skalootapp.MESSAGE";
    final String TAG = "ListActivity.java";
    String HOST = "http://www.traveltho.com/";
//    String HOST = "http://192.168.0.10/";

    String[] stringArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        new GetJson().execute();
    }



    public void show_data() {
        ArrayAdapter adapter = new ArrayAdapter(SettingActivity.this, R.layout.activity_listview, stringArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }



    
    
    @Override
    public void onBackPressed() {
        Log.e(TAG, "Button Back");
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
