package dk.kaddu.phoenixbsecompanion.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import dk.kaddu.phoenixbsecompanion.BuildConfig
import dk.kaddu.phoenixbsecompanion.GameStatusUpdateAlarm
import dk.kaddu.phoenixbsecompanion.PhoenixBSECompanion
import dk.kaddu.phoenixbsecompanion.R
import dk.kaddu.phoenixbsecompanion.data.*
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {


    // view variables
// TODO remove all references to gameStatusButton when possible
    internal lateinit var gameStatusButton: Button
    internal lateinit var mainActivityInfoTextView: TextView

    // database variables
    private lateinit var database: PhoenixDatabase
    private lateinit var gameStatusViewModel: GameStatusViewModel
    private lateinit var gameStatusDao: GameStatusDao

    var infoText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Room Database
        database = PhoenixBSECompanion.database!!
        gameStatusDao = database.gameStatusDao()
        val adapter = GameStatusListAdapter(this)


        // Setup View references
        mainActivityInfoTextView = findViewById<TextView>(R.id.mainActivityInfoTextView)
// TODO remove all references to gameStatusButton when possible
        gameStatusButton = findViewById<Button>(R.id.gameStatusButton)
        gameStatusButton.setOnClickListener { view ->
            checkGameStatus()
        }

// TODO Remove reference to the RecyclerView when possible. This is the remnants of the example this project has inspirations from
        gameStatusViewModel = ViewModelProviders.of(this).get(GameStatusViewModel::class.java)
        gameStatusViewModel.allGameStatus.observe(this, Observer {gameStatus ->
            infoText = infoText +
            getString(R.string.info_text_current_game_status, gameStatus?.get(0)?.star_date, gameStatus?.get(0)?.status)
            mainActivityInfoTextView.text=infoText

            // Update the cached copy of the gameStatus in the adapter
            gameStatus?.let { adapter.setGameStatusList(it)}
        })

// Display welcome text in the info Text View on the main screen
        infoText = infoText +
                getString(R.string.about_title, getString(R.string.app_name), getString(R.string.app_version_name)) + "\n"
        mainActivityInfoTextView.text=infoText

// Open the Game Status Database and read the records
        infoText = infoText +
                getString(R.string.info_text_game_status_open)
        mainActivityInfoTextView.text=infoText

        infoText = infoText +
               getString(R.string.info_text_game_status_loaded, "?")
        mainActivityInfoTextView.text=infoText

// Display the latest Star Date
        infoText = infoText +
                getString(R.string.info_text_star_date, "???.??.?") +
                getString(R.string.info_text_ready)
        mainActivityInfoTextView.text=infoText

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

    private fun setCheckGameStatusAlarm() {
        // get the AlarmManager
        var am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // create a new Intent specifying the Broadcast receiver
//        var intent = Intent(this, GameStatusUpdateAlarm)

        // create a PendingIntent using the newly created Intent
        var pendIntent = PendingIntent.getBroadcast(this, 0, intent, 0) as PendingIntent

        // setting the Alarm that will be fired
//        am.set ()
    }

    private fun checkGameStatus() {

        var currentGameStatus = GameStatus(0, 0, 0, 0, 0, 0, 0, 0, "", "")

        if (isNetworkConnected()) {
            doAsync {
                Log.d(javaClass.simpleName, "Send Request")
                currentGameStatus = Request("game_status").run()
                Log.d(javaClass.simpleName, "currentGameStatus = "+currentGameStatus.toString())
                gameStatusViewModel.insert(currentGameStatus)
// TODO remove all references to gameStatusButton when possible
                gameStatusButton.text = getString(R.string.game_status_current, currentGameStatus.star_date, currentGameStatus.status)
            }
        } else {
// TODO remove all references to gameStatusButton when possible
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
        const val newGameStatusActivityRequestCode = 1
    }
}
