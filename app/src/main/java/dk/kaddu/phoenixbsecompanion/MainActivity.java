package dk.kaddu.phoenixbsecompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE";
    private static final String LOG_TAG = "MainActivity";

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

        String xmlQueryUriString = uriBuilder(
                prefs.getString("nexus_uri", "www.phoenixbse.co.uk"),
                prefs.getString("nexus_uid", "1"),
                prefs.getString("nexus_code", "22d9b2c0316adab0f9104571c7ed8eb0"));
        Log.d(LOG_TAG,"xmlQueryUriString = " + xmlQueryUriString);

        Uri xmlQueryUri = Uri.parse(xmlQueryUriString);

        InputStream gameStatusInputStream = null;
        try {
            gameStatusInputStream = getContentResolver().openInputStream(xmlQueryUri);
            // TODO add XML parser code for updateGameStatus()
        } catch (Exception e) {
            // TODO Handle that the XML file is not available to updateGameStatus()

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
