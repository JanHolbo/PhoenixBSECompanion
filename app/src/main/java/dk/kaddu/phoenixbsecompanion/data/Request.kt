package dk.kaddu.phoenixbsecompanion.data

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.URL

class Request(private val xmlRequest: String) {

    private var currentGameStatus = GameStatus (0, 0, 0, 0, 0, 0, 0, 0, "", "")

    fun run(): GameStatus {
        Log.d(javaClass.simpleName, "Building xmlQueryUrlString")
        val xmlQueryUrlString = StringBuilder()
        xmlQueryUrlString.append("https://")                            // We want a secure connection to the server
        xmlQueryUrlString.append("www.phoenixbse.co.uk")                // Domain name TODO change domain name to preference based value
        xmlQueryUrlString.append("/?a=xml")                             // We are requesting an XML file
        xmlQueryUrlString.append("&sa=").append(xmlRequest)             // The passed request
        xmlQueryUrlString.append("&uid=").append("1")                   // User ID TODO change uid to preference based value
        xmlQueryUrlString.append("&code=")
        xmlQueryUrlString.append("22d9b2c0316adab0f9104571c7ed8eb0")    // "password" for the above user ID TODO change code to preference based value

        var xmlStream = URL(xmlQueryUrlString.toString()).openStream()

        try {
            Log.d(javaClass.simpleName, "Opening xmlStream")
// TODO Generalize XML parser code so that it can be re-used
            val parserFactory: XmlPullParserFactory
            try {
                Log.d(javaClass.simpleName, "Instantiating XmlPullParserFactory")
                parserFactory = XmlPullParserFactory.newInstance()
                Log.d(javaClass.simpleName, "Instantiating XmlPullParser")
                val parser = parserFactory.newPullParser()
                Log.d(javaClass.simpleName, "Setting XmlPullParser feature")
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                Log.d(javaClass.simpleName, "Setting XmlPullParser input")
                parser.setInput(xmlStream, null)

                Log.d(javaClass.simpleName, "Parsing game_status XML")

                var event = parser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    val name = parser.name
                    when (event) {
                        XmlPullParser.START_TAG -> {
                            Log.d(javaClass.simpleName, "XML start tag name: $name")
                            when (name) {
                                "status" -> {
                                    currentGameStatus.status = parser.nextText()
                                }
                                "current_day" -> {
                                    currentGameStatus.current_date = parser.nextText().toInt()
                                }
                                "year_start" -> {
                                    currentGameStatus.year_start = parser.nextText().toInt()
                                }
                                "turns_downloaded" -> {
                                    currentGameStatus.turns_downloaded = parser.nextText().toInt()
                                }
                                "turns_processed" -> {
                                    currentGameStatus.turns_processed = parser.nextText().toInt()
                                }
                                "turns_uploaded" -> {
                                    currentGameStatus.turns_uploaded = parser.nextText().toInt()
                                }
                                "emails_sent" -> {
                                    currentGameStatus.emails_sent = parser.nextText().toInt()
                                }
                                "specials_processed" -> {
                                    currentGameStatus.specials_processed = parser.nextText().toInt()
                                }
                                "day_finished" -> {
                                    currentGameStatus.day_finished = parser.nextText().toInt()
                                }
                                "star_date" -> {
                                    currentGameStatus.star_date = parser.nextText()
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
            // TODO Handle that the XML file is not available
            Log.d(javaClass.simpleName, "Opening XML file -> Exception : $e")

        } finally {
            if (xmlStream != null) {
                try {
                    xmlStream.close()
                } catch (ioex: IOException) {
                    //Very bad things just happened... handle it
                }
            }
        }

        return currentGameStatus
    }
}