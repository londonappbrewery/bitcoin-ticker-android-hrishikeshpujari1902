package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;


import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String public_key="NDFmNDBiNzVjZWYyNDgxYmFjYjNlMDg4OTUyMmFiZjE";

    // Member Variables:
    TextView mPriceTextView;
    String mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               
                Log.d("Bitcoinapp",""+parent.getItemAtPosition(position));
                String url=BASE_URL+parent.getItemAtPosition(position);
                Log.d("Bitcoinapp",""+url);

                letsDoSomeNetworking(url);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoinapp","No item selected");

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-ba-key",public_key);


        client.get(url,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoinapp", "JSON: " + response.toString());
                try{
                    int price=response.getInt("last");
                    mPrice=Integer.toString(price);
                    mPriceTextView.setText(mPrice);
                }catch (JSONException e){
                    e.printStackTrace();



                }

            }

            @Override
            public void onFailure(int statusCode,Header[] headers , Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoinapp", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoinapp", "Fail response: " + response);
                Log.e("ERROR", e.toString());

            }
        });


    }


}
