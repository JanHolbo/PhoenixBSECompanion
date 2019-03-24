package dk.kaddu.phoenixbsecompanion

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var star_date = "Not Available"
    private var status = "Not Available"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateGameStatus(this.findViewById(R.id.button_updateStatus))
    }

    /** Called when the user taps the Update Game Status button  */
    fun updateGameStatus(view: View) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(baseContext)

        val xmlQueryUriString = "http://www.phoenixbse.co.uk/?a=xml&uid=197&code=3c3a6f899bd43b329152574c15190a27&sa=game_status"
        //        String xmlQueryUriString = uriBuilder(
        //                prefs.getString("nexus_uri", "www.phoenixbse.co.uk"),
        //                prefs.getString("nexus_uid", "1"),
        //                prefs.getString("nexus_code", "22d9b2c0316adab0f9104571c7ed8eb0"));
        Log.d(LOG_TAG, "xmlQueryUriString = $xmlQueryUriString")

        val xmlQueryUri = Uri.parse(xmlQueryUriString)

        var gameStatusInputStream: InputStream? = null
        try {
            Log.d(LOG_TAG, "Opening gameStatusInputStream")
            // TODO Change to opening a java.net inputstream (?)
            gameStatusInputStream = contentResolver.openInputStream(xmlQueryUri)
            //            gameStatusInputStream = getAssets().open("file.xml");
            val parserFactory: XmlPullParserFactory
            try {
                Log.d(LOG_TAG, "Instantiating XmlPullParserFactory")
                parserFactory = XmlPullParserFactory.newInstance()
                Log.d(LOG_TAG, "Instantiating XmlPullParser")
                val parser = parserFactory.newPullParser()
                Log.d(LOG_TAG, "Setting XmlPullParser feature")
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                Log.d(LOG_TAG, "Setting XmlPullParser input")
                parser.setInput(gameStatusInputStream, null)

                Log.d(LOG_TAG, "Parsing game_status XML")

                var event = parser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    val name = parser.name
                    when (event) {
                        XmlPullParser.START_TAG -> {
                            Log.d(LOG_TAG, "XML start tag name: $name")
                            when (name) {
                                "status" -> {
                                    Log.d(LOG_TAG, "Setting status value")
                                    status = parser.nextText()
                                    Log.d(LOG_TAG, "Setting status value = $status")
                                }
                                "star_date" -> {
                                    Log.d(LOG_TAG, "Setting star_date value")
                                    star_date = parser.nextText()
                                    Log.d(LOG_TAG, "Setting star_date value = $star_date")
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> Log.d(LOG_TAG, "XML end tag name: $name")
                    }
                    Log.d(LOG_TAG, "Next XML element")
                    event = parser.next()
                }
            } catch (e: XmlPullParserException) {
                Log.d(LOG_TAG, "Parsing XML -> XmlPullParserException : $e")

            } catch (e: Exception) {
                Log.d(LOG_TAG, "Parsing XML -> other Exception : $e")

            }

        } catch (e: Exception) {
            // TODO Handle that the XML file is not available to updateGameStatus()
            Log.d(LOG_TAG, "Opening XML file -> Exception : $e")

        } finally {
            if (gameStatusInputStream != null) {
                try {
                    gameStatusInputStream.close()
                } catch (ioex: IOException) {
                    //Very bad things just happened... handle it
                }

            }
        }

        val stardateTextView = findViewById<View>(R.id.textView_current_stardate) as TextView
        stardateTextView.text = star_date
        val statusTextView = findViewById<View>(R.id.textView_current_gameStatus) as TextView
        statusTextView.text = status
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun processParsing(parser: XmlPullParser) {
        // TODO add XML parsing code for the Game Status XML response

    }


    private fun uriBuilder(uriBase: String, uid: String, code: String): String {
        val builder = Uri.Builder()
        builder.scheme("https")
                .authority(uriBase)
                .appendQueryParameter("a", "xml")
                .appendQueryParameter("uid", uid)
                .appendQueryParameter("code", code)
                .appendQueryParameter("sa", "game_status")
        return builder.build().toString()
    }

    companion object {
        val EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE"
        private val LOG_TAG = "MainActivity"
    }
}