/**
 * Copyright 2015 Google Inc. All Rights Reserved.
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

package gcm.play.android.samples.com.gcmquickstart.view;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gcm.play.android.samples.com.gcmquickstart.R;
import gcm.play.android.samples.com.gcmquickstart.models.Message;
import gcm.play.android.samples.com.gcmquickstart.service.QuickstartPreferences;
import gcm.play.android.samples.com.gcmquickstart.service.RegistrationIntentService;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private final String MAINFRAG = "mainfragment";
    private final String DETAILFRAG = "MessageDetailFragment";

    private ArrayList<Message> messages;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private AppBarLayout appBarLayout;

    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //messageintent onvangen en wegschrijven
        Message m = getIntent().getParcelableExtra(QuickstartPreferences.MESSAGES_INTENT_KEY);
        if(m != null){
            //MESSAGES WEGSCHRIJVEN:
            //check als pref bestaat, indien nodig lege lijst maken, lijst ophalen, lijst aanvullen, lijst wegschrijvenArrayList<Message> messages;
            Gson gson = new Gson();
            SharedPreferences prefs = getSharedPreferences(QuickstartPreferences.PREFERENCE_NAME, 0);

            String messagesJson = prefs.getString(QuickstartPreferences.MESSAGES_LIST_KEY, null);
            if(messagesJson == null) messages = new ArrayList<>();
            else messages = gson.fromJson(messagesJson, new TypeToken<ArrayList<Message>>(){}.getType());

            messages.add(m);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(QuickstartPreferences.MESSAGES_LIST_KEY, gson.toJson(messages));
            editor.commit();
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    mInformationTextView.setText(getString(R.string.gcm_send_message));


                    //EIGEN CODE
                    mInformationTextView.setVisibility(View.GONE);
                    mRegistrationProgressBar.setVisibility(View.GONE);
                    PrepareLayout();


                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            //ik zit terug in mijn eerste fragment.
            //gebruiker klikt nogmaals op back --> app afsluiten
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void PrepareLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance("test");

        fragmentTransaction.replace(R.id.frameLayout, mainFragment, MAINFRAG);
        //fragmentTransaction.addToBackStack("MainFrag");
        fragmentTransaction.commit();
    }


    @Override
    public void onListItemSelected(Message message) {
        appBarLayout.setExpanded(true);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(message);

        fragmentTransaction.replace(R.id.frameLayout, detailFragment, DETAILFRAG);
        fragmentTransaction.addToBackStack("DetailFrag");
        fragmentTransaction.commit();
    }

    @Override
    public void onMessageDelete(int index, Context context) {
        AlertDialog diaBox = AskOption(index, context);
        diaBox.show();

    }

    private AlertDialog AskOption(final int index, final Context context)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setIcon(android.R.drawable.ic_menu_delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        Gson gson = new Gson();
                        SharedPreferences prefs = getSharedPreferences(QuickstartPreferences.PREFERENCE_NAME, 0);

                        ArrayList<Message> tempMess = null;

                        String messagesJson = prefs.getString(QuickstartPreferences.MESSAGES_LIST_KEY, null);
                        if(messagesJson == null) tempMess = new ArrayList<>();
                        else tempMess = gson.fromJson(messagesJson, new TypeToken<ArrayList<Message>>(){}.getType());

                        tempMess.remove(index);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(QuickstartPreferences.MESSAGES_LIST_KEY, gson.toJson(tempMess));
                        editor.commit();

                        Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();

                        PrepareLayout();

                        //einde eigen code
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
