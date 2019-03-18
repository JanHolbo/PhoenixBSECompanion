package dk.kaddu.phoenixbsecompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends FragmentActivity implements DownloadCallback {
    public static final String EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE";
    private static final String LOG_TAG = "MainActivity";

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO add whatever is needed on onCreate()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO do we need to instantiate/reconnect to the NetworkFragment on UI redraw?
//        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");

//        updateGameStatus(this.findViewById(R.id.button_updateStatus));
    }

//    @Override
    public void updateFromDownload(String result) {
        // Update your UI here based on result of download.
    }

    @Override
    public void updateFromDownload(Object result) {
        // Update your UI here based on result of download.
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                // TODO onProgressUpdate() add code in case of Progress.ERROR
                break;
            case Progress.CONNECT_SUCCESS:
                // TODO onProgressUpdate() add code in case of Progress.CONNECT_SUCCESS
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                // TODO onProgressUpdate() add code in case of Progress.GET_INPUT_STREAM_SUCCESS
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                // TODO onProgressUpdate() add code in case of Progress.PROCESS_INPUT_STREAM_IN_PROGRESS
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                // TODO onProgressUpdate() add code in case of Progress.PROCESS_INPUT_STREAM_SUCCESS
                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    /** Called when the user taps the Update Game Status button */
    public void updateGameStatus(View view) {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
// TODO replace the hardcoded xml query string with the preferences driven uriBuilder string
        String xmlQueryUrlString = "https://www.phoenixbse.co.uk/?a=xml&uid=197&code=3c3a6f899bd43b329152574c15190a27&sa=game_status";
/*
// TODO replace the hardcoded uriBuilder string with the preferences driven uriBuilder string
        String xmlQueryUrlString = uriBuilder(
                prefs.getString("nexus_uri", "www.phoenixbse.co.uk"),
                prefs.getString("nexus_uid", "1"),
                prefs.getString("nexus_code", "22d9b2c0316adab0f9104571c7ed8eb0"));
*/        Log.d(LOG_TAG,"xmlQueryUrlString = " + xmlQueryUrlString);

        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), xmlQueryUrlString);

        startDownload();

// TODO remove old network code that executed on the main UI thread
/* Old network code that executed on the main UI thread which is no longer allowed - TO BE REMOVED
        try {
            URL xmlQueryUrl = new URL(xmlQueryUrlString);

            InputStream gameStatusInputStream = null;
            try {
                Log.d(LOG_TAG,"Opening gameStatusInputStream");
// TODO Change to opening a java.net inputstream (?)
//            gameStatusInputStream = getAssets().open("file.xml");     // offline local file for development
                gameStatusInputStream = xmlQueryUrl.openStream();
                XmlPullParserFactory parserFactory;
                try {
                    Log.d(LOG_TAG,"Instantiating XmlPullParserFactory");
                    parserFactory = XmlPullParserFactory.newInstance();
                    Log.d(LOG_TAG,"Instantiating XmlPullParser");
                    XmlPullParser parser = parserFactory.newPullParser();
                    Log.d(LOG_TAG,"Setting XmlPullParser feature");
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    Log.d(LOG_TAG,"Setting XmlPullParser input");
                    parser.setInput(gameStatusInputStream, null);

                    Log.d(LOG_TAG,"Parsing game_status XML");

                    int event = parser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT)  {
                        String name=parser.getName();
                        switch (event){
                            case XmlPullParser.START_TAG:
                                Log.d(LOG_TAG,"XML start tag name: " + name);
                                switch (name) {
                                    case "status":
                                        Log.d(LOG_TAG,"Setting status value");
                                        status = parser.nextText();
                                        Log.d(LOG_TAG,"Setting status value = " + status);
                                        break;
                                    case "star_date":
                                        Log.d(LOG_TAG,"Setting star_date value");
                                        star_date = parser.nextText();
                                        Log.d(LOG_TAG,"Setting star_date value = " + star_date);
                                        break;
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                Log.d(LOG_TAG,"XML end tag name: " + name);
                                break;
                        }
                        Log.d(LOG_TAG,"Next XML element");
                        event = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    Log.d(LOG_TAG,"Parsing XML -> XmlPullParserException : " + e.toString());

                } catch (Exception e) {
                    Log.d(LOG_TAG,"Parsing XML -> other Exception : " + e.toString());

                }

            } catch (Exception e) {
                // TODO Handle that the XML file is not available to updateGameStatus()
                Log.d(LOG_TAG,"Opening XML file -> Exception : " + e.toString());

            } finally {
                if (gameStatusInputStream != null) {
                    try {
                        gameStatusInputStream.close();
                    }
                    catch(IOException ioex) {
                        //Very bad things just happened... handle it
                    }

                }
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"updateGameStatus: MalformedURLException = " + e.toString());
        } catch (Exception e) {
            Log.e(LOG_TAG,"updateGameStatus: other Exception = " + e.toString());

        }
Old network code that executed on the main UI thread which is no longer allowed - TO BE REMOVED */

// TODO replace old UI update code with code that reacts through the DownloadCallback code
/* Old code that updates the UI upon having done the XML parsing - TO BE REMOVED
        TextView stardateTextView = findViewById(R.id.textView_current_stardate);
        stardateTextView.setText(star_date);
        TextView statusTextView = findViewById(R.id.textView_current_gameStatus);
        statusTextView.setText(status);

        code that updates the UI upon having done the XML parsing - TO BE REMOVED */
    }

    private void processParsing (XmlPullParser parser) {
        // TODO add XML parsing code for the Game Status XML response

    }


    /** Called when the user taps the Preferences button */
    public void viewPreferences(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private String uriBuilder(String uriBase, String uid, String code) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(uriBase)
                .appendQueryParameter("a","xml")
                .appendQueryParameter("uid", uid)
                .appendQueryParameter("code", code)
                .appendQueryParameter("sa","game_status");
        String myUrl = builder.build().toString();
        return myUrl;
    }
}
