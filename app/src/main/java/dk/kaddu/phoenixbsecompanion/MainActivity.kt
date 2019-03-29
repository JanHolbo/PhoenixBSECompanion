package dk.kaddu.phoenixbsecompanion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
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

        val xmlQueryUrlString = StringBuilder()
        xmlQueryUrlString.append("https://")                            // We want a secure connection to the server
        xmlQueryUrlString.append("www.phoenixbse.co.uk")                // Domain name
        xmlQueryUrlString.append("/?a=xml")                             // We are requesting an XML file
        xmlQueryUrlString.append("&sa=").append("game_status")          // We are requesting the game_status response
        xmlQueryUrlString.append("&uid=").append("1")                   // User ID
        xmlQueryUrlString.append("&code=")
        xmlQueryUrlString.append("22d9b2c0316adab0f9104571c7ed8eb0")    // "password" for the above user ID
        doAsync {
            Request(xmlQueryUrlString.toString()).run()
            uiThread { longToast("Request performed") }
        }

//        updateGameStatus(this.findViewById(R.id.button_updateStatus))
    }

    /** Called when the user taps the Update Game Status button  */
    fun updateGameStatus(view: View) {

// TODO Change hard coded reference to uid & code to use preference based options
// Please note that the old hard coded uid and code have now been removed from the Phoenix Nexus server
//  so that it causes no network security holes
        val xmlQueryUrlString = StringBuilder()
        xmlQueryUrlString.append("https://")                            // We want a secure connection to the server
        xmlQueryUrlString.append("www.phoenixbse.co.uk")                // Domain name
        xmlQueryUrlString.append("/?a=xml")                             // We are requesting an XML file
        xmlQueryUrlString.append("&sa=").append("game_status")          // We are requesting the game_status response
        xmlQueryUrlString.append("&uid=").append("1")                   // User ID
        xmlQueryUrlString.append("&code=")
        xmlQueryUrlString.append("22d9b2c0316adab0f9104571c7ed8eb0")    // "password" for the above user ID
        Log.d(javaClass.simpleName, "xmlQueryUriString = $xmlQueryUrlString")

        var gameStatusInputStream: InputStream? = null
        try {
            Log.d(javaClass.simpleName, "Opening gameStatusInputStream")
// TODO Change from opening a local ressources file to opening a java.net inputstream (?)
// TODO move XML parsing away from the main UI thread so as not to lock up this thread.
// TODO Generalize XML parser code so that it can be re-used
            gameStatusInputStream = getAssets().open("file.xml");
            val parserFactory: XmlPullParserFactory
            try {
                Log.d(javaClass.simpleName, "Instantiating XmlPullParserFactory")
                parserFactory = XmlPullParserFactory.newInstance()
                Log.d(javaClass.simpleName, "Instantiating XmlPullParser")
                val parser = parserFactory.newPullParser()
                Log.d(javaClass.simpleName, "Setting XmlPullParser feature")
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                Log.d(javaClass.simpleName, "Setting XmlPullParser input")
                parser.setInput(gameStatusInputStream, null)

                Log.d(javaClass.simpleName, "Parsing game_status XML")

                var event = parser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    val name = parser.name
                    when (event) {
                        XmlPullParser.START_TAG -> {
                            Log.d(javaClass.simpleName, "XML start tag name: $name")
                            when (name) {
                                "status" -> {
                                    Log.d(javaClass.simpleName, "Setting status value")
                                    status = parser.nextText()
                                    Log.d(javaClass.simpleName, "Setting status value = $status")
                                }
                                "star_date" -> {
                                    Log.d(javaClass.simpleName, "Setting star_date value")
                                    star_date = parser.nextText()
                                    Log.d(javaClass.simpleName, "Setting star_date value = $star_date")
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> Log.d(javaClass.simpleName, "XML end tag name: $name")
                    }
                    Log.d(javaClass.simpleName, "Next XML element")
                    event = parser.next()
                }
            } catch (e: XmlPullParserException) {
                Log.d(javaClass.simpleName, "Parsing XML -> XmlPullParserException : $e")

            } catch (e: Exception) {
                Log.d(javaClass.simpleName, "Parsing XML -> other Exception : $e")

            }

        } catch (e: Exception) {
            // TODO Handle that the XML file is not available to updateGameStatus()
            Log.d(javaClass.simpleName, "Opening XML file -> Exception : $e")

        } finally {
            if (gameStatusInputStream != null) {
                try {
                    gameStatusInputStream.close()
                } catch (ioex: IOException) {
                    //Very bad things just happened... handle it
                }

            }
        }

    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun processParsing(parser: XmlPullParser) {
        // TODO add XML parsing code for the Game Status XML response

    }


    companion object {
        val EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE"
        private val LOG_TAG = "MainActivity"
    }
}
