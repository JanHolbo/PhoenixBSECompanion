package dk.kaddu.phoenixbsecompanion;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE";
    private static final String LOG_TAG = "MainActivity";

    private String star_date="Not Available";
    private String status="Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateGameStatus(this.findViewById(R.id.button_updateStatus));
    }

    /** Called when the user taps the Update Game Status button */
    public void updateGameStatus(View view) {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String xmlQueryUriString = "http://www.phoenixbse.co.uk/?a=xml&uid=197&code=3c3a6f899bd43b329152574c15190a27&sa=game_status";
//        String xmlQueryUriString = uriBuilder(
//                prefs.getString("nexus_uri", "www.phoenixbse.co.uk"),
//                prefs.getString("nexus_uid", "1"),
//                prefs.getString("nexus_code", "22d9b2c0316adab0f9104571c7ed8eb0"));
        Log.d(LOG_TAG,"xmlQueryUriString = " + xmlQueryUriString);

        Uri xmlQueryUri = Uri.parse(xmlQueryUriString);

        InputStream gameStatusInputStream = null;
        try {
            Log.d(LOG_TAG,"Opening gameStatusInputStream");
// TODO Change to opening a java.net inputstream (?)
            gameStatusInputStream = getContentResolver().openInputStream(xmlQueryUri);
//            gameStatusInputStream = getAssets().open("file.xml");
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
