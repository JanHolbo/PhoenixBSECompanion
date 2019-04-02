package dk.kaddu.phoenixbsecompanion.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import dk.kaddu.phoenixbsecompanion.BuildConfig
import dk.kaddu.phoenixbsecompanion.R
import dk.kaddu.phoenixbsecompanion.data.GameStatus
import dk.kaddu.phoenixbsecompanion.data.Request
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

// TODO remove star_date and status when possible
    private var star_date = "Not Available"
    private var status = "Not Available"
    internal lateinit var gameStatusButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameStatusButton = findViewById<Button>(R.id.gameStatusButton)
        gameStatusButton.setOnClickListener { view ->
            checkGameStatus()
        }

        checkGameStatus()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_about) {
            showInfo()
        }
        return true
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
        val networkInfo = connectivityManager.activeNetworkInfo //2
        return networkInfo != null && networkInfo.isConnected //3
    }

    private fun checkGameStatus() {

        var currentGameStatus = GameStatus(0, 0, 0, 0, 0, 0, 0, 0, "", "")

        if (isNetworkConnected()) {

            val xmlQueryUrlString = StringBuilder()
            xmlQueryUrlString.append("https://")                            // We want a secure connection to the server
            xmlQueryUrlString.append("www.phoenixbse.co.uk")                // Domain name
            xmlQueryUrlString.append("/?a=xml")                             // We are requesting an XML file
            xmlQueryUrlString.append("&sa=").append("game_status")          // We are requesting the game_status response
            xmlQueryUrlString.append("&uid=").append("1")                   // User ID
            xmlQueryUrlString.append("&code=")
            xmlQueryUrlString.append("22d9b2c0316adab0f9104571c7ed8eb0")    // "password" for the above user ID
            doAsync {
                currentGameStatus = Request(xmlQueryUrlString.toString()).run()
                gameStatusButton.text = getString(R.string.game_status_current, currentGameStatus.star_date, currentGameStatus.status)
            }

        } else {
            gameStatusButton.text = getString(R.string.online_status_offline)
        }
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, getString(R.string.app_name), getString(R.string.app_version_name))
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    companion object {
    }
}
