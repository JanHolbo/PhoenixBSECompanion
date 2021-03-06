package dk.kaddu.phoenixbsecompanion.data

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.URL

class Request(private val url: String) {

    private var currentGameStatus = GameStatus (0, 0, 0, 0, 0, 0, 0, 0, "", "")

    fun run(): GameStatus {

        var xmlStream = URL(url).openStream()

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
// TODO add code to set all values from the game_status XML file
                            when (name) {
                                "status" -> {
                                    Log.d(javaClass.simpleName, "Setting status value")
                                    currentGameStatus.status = parser.nextText()
                                    Log.d(javaClass.simpleName, "Setting status value = $currentGameStatus.status")
                                }
                                "star_date" -> {
                                    Log.d(javaClass.simpleName, "Setting star_date value")
                                    currentGameStatus.star_date = parser.nextText()
                                    Log.d(javaClass.simpleName, "Setting star_date value = $currentGameStatus.star_date")
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