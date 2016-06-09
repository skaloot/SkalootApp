package com.skaloot.skalootapp;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
//import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    String[] ImgURL = null;
    String[] LaundryTitle = null;
    Integer[] ImgId = null;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    ListView list;
    String ret = null;
    String message = null;
    public static View rootView;
    public static Bundle bundle;

    // OnCreate
    // =============================================================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        ImageView btnback = (ImageView)findViewById(R.id.btn_back);
        btnback.setImageResource(R.drawable.button_drawer);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        readFile();
    }


    public static Context getContext(){
        return mContext;
    }



    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        message = editText.getText().toString();
        new SendMessage().execute();
    }


    public void list_this() {
        ImageListAdapter adapter = new ImageListAdapter(MainActivity.this, ImgURL, ImgId);
        list = (ListView)findViewById(R.id.Laundry_list_home);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra("jsonArray", "{\"title\":\""+LaundryTitle[+position]+"\",\"imgUrl\":\""+ImgURL[+position]+"\"}");
                startActivity(intent);
            }
        });
    }


    public void readFile() {
        String filename = getResources().getString(R.string.filename);
        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        if(ret == null) {
            Log.e("FILE Status", "FILE Status - NULL");
            new GetLaundry().execute();
        } else {
            Log.e("FILE Status", "FILE Status - EXIST");
            Log.e("JSON STRINGS", "JSON STRINGS - " + ret);
            new GetLaundryLocal().execute();
        }
    }



    // SendMessage
    // =============================================================================================================================================
    public class SendMessage extends AsyncTask<Void,Void,Void> {
        final String TAG = "SendMessage.java";
//        ScrollView scroll_view = (ScrollView) findViewById(R.id.scroll_msg);
        JSONArray dataJsonArr = null;
        String HOST = getResources().getString(R.string.host);
        String newMessage = null;


        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            progressDialog.setMessage("Sending Meesage...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    SendMessage.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "Sending Message - Started");
            try {
                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.getJSONFromUrl(HOST+"totus/laundry");
                dataJsonArr = json.getJSONArray("Laundry");
                newMessage = message;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Sending Message - Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Log.e(TAG, "Message Sent - " + newMessage);
            EditText editText = (EditText) findViewById(R.id.edit_message);
            editText.setText("");
            Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
        }
    }



    // GetLaundry
    // =============================================================================================================================================
    public class GetLaundry extends AsyncTask<Void,Void,Void> {
        final String TAG = "GetJson.java";
        JSONArray dataJsonArr = null;
        String HOST = getResources().getString(R.string.host);
        String filename = getResources().getString(R.string.filename);


        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetLaundry.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "Getting Json from URL - Started");
            try {
                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.getJSONFromUrl(HOST+"totus/laundry");
                dataJsonArr = json.getJSONArray("Laundry");
                ImgURL = new String[dataJsonArr.length()];
                LaundryTitle = new String[dataJsonArr.length()];
                ImgId = new Integer[dataJsonArr.length()];

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    ImgURL[i] = c.getString("url");
                    LaundryTitle[i] = c.getString("title");
                    ImgId[i] = MainActivity.this.getResources().getIdentifier(ImgURL[i],
                            "drawable", "com.skaloot.skalootapp");
                    Log.e("JSON", "JSON - " + ImgURL[i] + " | " + LaundryTitle[i] + " | " + ImgId[i]);
                }

                String string = json.toString();
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Getting Json from URL - Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            list_this();
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


    // GetLaundry Local
    // =============================================================================================================================================
    public class GetLaundryLocal extends AsyncTask<Void,Void,Void> {
        final String TAG = "GetJsonLocal.java";
        JSONArray dataJsonArr = null;
        String filename = getResources().getString(R.string.filename);


        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetLaundryLocal.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "Getting Json from Local - Started");
            try {
                JSONObject json = new JSONObject(ret);
                dataJsonArr = json.getJSONArray("Laundry");
                ImgURL = new String[dataJsonArr.length()];
                LaundryTitle = new String[dataJsonArr.length()];
                ImgId = new Integer[dataJsonArr.length()];

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    ImgURL[i] = c.getString("url");
                    LaundryTitle[i] = c.getString("title");
                    ImgId[i] = MainActivity.this.getResources().getIdentifier(ImgURL[i],
                            "drawable", "com.skaloot.skalootapp");
                    Log.e("JSON", "JSON - " + ImgURL[i] + " | " + LaundryTitle[i] + " | " + ImgId[i]);
                }

                String dir = getFilesDir().getAbsolutePath();
                File f0 = new File(dir, filename);
                boolean d0 = f0.delete();
                Log.e("Delete file", "Delete file - " + d0);

            } catch (JSONException e) {
                e.printStackTrace();
                String dir = getFilesDir().getAbsolutePath();
                File f0 = new File(dir, filename);
                boolean d0 = f0.delete();
                Log.e("JSON Error", "Delete file - " + d0);
            }
            Log.e(TAG, "Getting Json from Local - Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            list_this();
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


    // AppSectionsPagerAdapter
    // =============================================================================================================================================
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        String tabTitles[] = MainActivity.getContext().getResources().getStringArray(R.array.tabTitles);

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return Homepage.newInstance(1);
                case 1:
                    return Page_2.newInstance(2);
                case 2:
                    return Page_3.newInstance(3);
                case 3:
                    return Page_4.newInstance(4);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }



    // Homepage
    // =============================================================================================================================================
    public static class Homepage extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;

        public static Homepage newInstance(int sectionNumber) {
            Homepage fragment = new Homepage();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(rootView == null) {
                rootView = inflater.inflate(R.layout.activity_home_1, container, false);
            }
            return rootView;
        }
    }

    // 2nd Page
    // =============================================================================================================================================
    public static class Page_2 extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Page_2 newInstance(int sectionNumber) {
            Page_2 fragment = new Page_2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(savedInstanceState != null)  bundle = savedInstanceState;
            rootView = inflater.inflate(R.layout.activity_home_2, container, false);
            return rootView;
        }
    }

    // 3nd Page
    // =============================================================================================================================================
    public static class Page_3 extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Page_3 newInstance(int sectionNumber) {
            Page_3 fragment = new Page_3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(savedInstanceState != null)  bundle = savedInstanceState;
            rootView = inflater.inflate(R.layout.activity_home_3, container, false);
            return rootView;
        }
    }

    // 4nd Page
    // =============================================================================================================================================
    public static class Page_4 extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Page_4 newInstance(int sectionNumber) {
            Page_4 fragment = new Page_4();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(savedInstanceState != null)  bundle = savedInstanceState;
            rootView = inflater.inflate(R.layout.activity_home_4, container, false);
            return rootView;
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


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
