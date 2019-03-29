package dk.kaddu.phoenixbsecompanion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.kaddu.phoenixbsecompanion.data.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread

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

    }



    companion object {
        val EXTRA_MESSAGE = "dk.kaddu.phoenixbsecompanion.MESSAGE"
        private val LOG_TAG = "MainActivity"
    }
}
