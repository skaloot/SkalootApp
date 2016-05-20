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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = null;
//    public final static String EXTRA_MESSAGE_2 = null;


//    public int status = -1;
//    public String responseText = null;
//    public String TAG = "GLOBAL RESPONSE";
//    public String ORDER = null;
//    Timer timer;
    String[] LaundryArray = null;
    String[] LaundryTitle = null;
    Integer[] ImgId = null;

//    String HOST = "http://www.traveltho.com/";
    String HOST = "http://192.168.0.10/";

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    ListView list;

    // OnCreate
    // =============================================================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView btnback = (ImageView)findViewById(R.id.btn_back);
        btnback.setImageResource(R.drawable.button_drawer);
//        ImageView btncart = (ImageView)findViewById(R.id.btn_cart);
//        btncart.setImageResource(R.drawable.button_cart);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        new GetLaundry().execute();
    }


    // SetTimer
    // =============================================================================================================================================
//    public void set_timer(View view) {
//        if(ORDER == null) {
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    new GetStatus().execute();
//                }
//            }, 0, 15000);
//            Log.e("timer", "timer has been created..");
//            ORDER = "Started";
//        } else {
//            Toast.makeText(MainActivity.this, "Order Status has already started",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }


    // GetStatus
    // =============================================================================================================================================
//    public class GetStatus extends AsyncTask<Void,Void,Void> {
//        JSONArray dataJsonArr = null;
//        int stat;
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Log.e("Response - ", "Getting response from URL - Started");
//            try {
//                JsonParser jParser = new JsonParser();
//                JSONObject json = jParser.getJSONFromUrl(HOST+"totus/status");
//                dataJsonArr = json.getJSONArray("response");
//                responseText = dataJsonArr.getJSONObject(0).getString("msg");
//                stat = dataJsonArr.getJSONObject(0).getInt("status");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if(stat != status) {
//                status = stat;
////                change_img();
//                Toast.makeText(MainActivity.this, responseText,
//                        Toast.LENGTH_SHORT).show();
//            }
//            if(stat == 6) {
////                timer.cancel();
//                status = -1;
//                ORDER = null;
//            }
//            Log.e(TAG, "Status - " + status);
//        }
//    }



    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    public void list_this() {
        ImageListAdapter adapter = new ImageListAdapter(MainActivity.this, LaundryArray, ImgId);
        list = (ListView)findViewById(R.id.Laundry_list_home);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra(EXTRA_MESSAGE, LaundryArray[+position]);
//                intent.putExtra(EXTRA_MESSAGE_2, LaundryTitle[+position]);
                startActivity(intent);
            }
        });
    }


    public void go_back(View view) {
        finish();
    }




    // AppSectionsPagerAdapter
    // =============================================================================================================================================
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private Context context;
        private String tabTitles[] = new String[] { "Offers", "Wash & Fold", "Dry Clean", "Wash" };

        public AppSectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Homepage();
                case 1:
                    return new Page_2();
                case 2:
                    return new Page_3();
                case 3:
                    return new Page_4();
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


    // GetLaundry
    // =============================================================================================================================================
    public class GetLaundry extends AsyncTask<Void,Void,Void> {
        final String TAG = "GetJson.java";
        JSONArray dataJsonArr = null;

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
                LaundryArray = new String[dataJsonArr.length()];
                LaundryTitle = new String[dataJsonArr.length()];
                ImgId = new Integer[dataJsonArr.length()];

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    LaundryArray[i] = c.getString("brand");
                    LaundryTitle[i] = c.getString("title");
                    ImgId[i] = MainActivity.this.getResources().getIdentifier(LaundryArray[i],
                            "drawable", getPackageName());
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


    // Homepage
    // =============================================================================================================================================
    public static class Homepage extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_home_1, container, false);
            return rootView;
        }
    }

    // 2nd Page
    // =============================================================================================================================================
    public static class Page_2 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_home_2, container, false);
            return rootView;
        }
    }

    // 3nd Page
    // =============================================================================================================================================
    public static class Page_3 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_home_3, container, false);
            return rootView;
        }
    }

    // 4nd Page
    // =============================================================================================================================================
    public static class Page_4 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_home_4, container, false);
            return rootView;
        }
    }




    // LISTENER
    // =============================================================================================================================================
    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    public void clearCache() {
//        MemoryCache memoryCache = new MemoryCache();
//        memoryCache.clear();
//        FileCache fileCache = new FileCache(this.getApplicationContext());
//        fileCache.clear();
//    }



    // OptionMenu
    // =============================================================================================================================================
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_my, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, SettingActivity.class);
//            startActivity(intent);
//            return true;
//        } else if (id == R.id.action_account) {
////            Intent intent = new Intent(this, AccountActivity.class);
////            startActivity(intent);
//            return true;
//        } else if (id == R.id.action_logout) {
//            finish();
//            Toast.makeText(MainActivity.this, "You have logged out.",
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
