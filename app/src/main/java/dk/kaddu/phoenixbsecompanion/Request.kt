package dk.kaddu.phoenixbsecompanion

import android.util.Log
import java.net.URL

class Request(private val url: String) {

    fun run() {
        // TODO Implement the XML parser code
        val xmlStreamStr = URL(url).readText()
        Log.d(javaClass.simpleName, xmlStreamStr)
    }

}