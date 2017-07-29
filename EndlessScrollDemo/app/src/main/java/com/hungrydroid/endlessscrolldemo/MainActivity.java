package com.hungrydroid.endlessscrolldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    String source = "";
    int pg = 1;
    ListView lv;
    ProgressDialog pDialog;
    ArrayList<String> prgmNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.mainlistView);
        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                    loadNextDataFromApi(page);
                    pg  =page;

                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        loadNextDataFromApi(pg);
    }

    public void loadData(int p) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.catfact.info/api/v1/facts.json?page=" + p, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started


                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading Data...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject testV = new JSONObject(new String(responseBody));


                    JSONArray contacts = testV.getJSONArray("facts");


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        prgmNameList.add(c.getString("details"));

                    }
                    lv.setAdapter(new CustomAdapter(MainActivity.this, prgmNameList));
                    lv.post(new Runnable() {
                        @Override
                        public void run() {
                            lv.setSelection(prgmNameList.size()-25);
                            lv.invalidate();
                        }
                    });
                    Toast.makeText(MainActivity.this, pg+"::"+prgmNameList.size(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    //lv.setAdapter(null);
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                //finish();
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }


        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int pp) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        if (Connectivity.isNetworkAvailable(MainActivity.this)) {
            loadData(pp);
        } else {
            Toast.makeText(MainActivity.this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }
}
