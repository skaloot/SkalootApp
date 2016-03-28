package com.skaloot.skalootapp;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListActivity extends AppCompatActivity {

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

        new GetJson().execute();
    }



    public void show_data() {
        ArrayAdapter adapter = new ArrayAdapter(ListActivity.this, R.layout.activity_listview, stringArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }




    // GetJson
    // =============================================================================================================================================
    public class GetJson extends AsyncTask<Void,Void,Void> {
        final String TAG = "GetJson.java";
        JSONArray dataJsonArr = null;

        private ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
        protected void onPreExecute() {
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetJson.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "Getting Json from URL - Started");
            try {
                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.getJSONFromUrl(HOST+"totus/json");
                dataJsonArr = json.getJSONArray("OS");
                stringArray = new String[dataJsonArr.length()];

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    stringArray[i] = c.getString("brand");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Getting Json from URL - Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            show_data();
            progressDialog.dismiss();
        }
    }




    public void get_detail(View view) {
        new GetDetail().execute();
    }
    
    
    public class GetDetail extends AsyncTask<Void,Void,Void> {
        final String TAG = "GetDetail.java";

        private ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
        protected void onPreExecute() {
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetDetail.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
        	Log.e(TAG, "Getting Detail - Started");
            Log.e(TAG, "Getting Detail - Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            intent.putExtra(DetailText, "Skaloot");
            startActivity(intent);
            progressDialog.dismiss();
        }
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
