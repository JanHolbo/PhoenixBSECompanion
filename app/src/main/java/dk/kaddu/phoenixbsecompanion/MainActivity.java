package dk.kaddu.phoenixbsecompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity extends FragmentActivity implements DownloadCallBack {
    public static final String EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE";
    private static final String LOG_TAG = "MainActivity";

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;


    private String star_date="Not Available";
    private String status="Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");

        updateGameStatus(this.findViewById(R.id.button_updateStatus));
    }

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }

    public interface DownloadCallback<T> {
        interface Progress {
            int ERROR = -1;
            int CONNECT_SUCCESS = 0;
            int GET_INPUT_STREAM_SUCCESS = 1;
            int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
            int PROCESS_INPUT_STREAM_SUCCESS = 3;
        }

        /**
         * Indicates that the callback handler needs to update its appearance or information based on
         * the result of the task. Expected to be called from the main thread.
         */
        void updateFromDownload(T result);

        /**
         * Get the device's active network status in the form of a NetworkInfo object.
         */
        NetworkInfo getActiveNetworkInfo();

        /**
         * Indicate to callback handler any progress update.
         * @param progressCode must be one of the constants defined in DownloadCallback.Progress.
         * @param percentComplete must be 0-100.
         */
        void onProgressUpdate(int progressCode, int percentComplete);

        /**
         * Indicates that the download operation has finished. This method is called even if the
         * download hasn't completed successfully.
         */
        void finishDownloading();
    }

    @Override
    public void updateFromDownload(String result) {
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
            case DownloadCallback.Progress.ERROR:
// TODO implement DownloadCallback.Progress.ERROR
                break;
            case DownloadCallback.Progress.CONNECT_SUCCESS:
// TODO implement DownloadCallback.Progress.CONNECT_SUCCESS
                break;
            case DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:
// TODO implement DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS
                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
// TODO implement DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS
                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS:
// TODO implement DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS
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
        String xmlQueryUriString = "https://www.phoenixbse.co.uk/?a=xml&uid=197&code=3c3a6f899bd43b329152574c15190a27&sa=game_status";
// TODO replace the hardcoded uriBuilder string with the preferences driven uriBuilder string
//        String xmlQueryUriString = uriBuilder(
//                prefs.getString("nexus_uri", "www.phoenixbse.co.uk"),
//                prefs.getString("nexus_uid", "1"),
//                prefs.getString("nexus_code", "22d9b2c0316adab0f9104571c7ed8eb0"));
        Log.d(LOG_TAG,"xmlQueryUriString = " + xmlQueryUriString);

        try {
            URL xmlQueryUrl = new URL(xmlQueryUriString);

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


        TextView stardateTextView = (TextView) findViewById(R.id.textView_current_stardate);
        stardateTextView.setText(star_date);
        TextView statusTextView = (TextView) findViewById(R.id.textView_current_gameStatus);
        statusTextView.setText(status);
    }

    private void processParsing (XmlPullParser parser) throws IOException, XmlPullParserException {
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
