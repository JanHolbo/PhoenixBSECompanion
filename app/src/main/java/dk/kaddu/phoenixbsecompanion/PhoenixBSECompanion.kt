package dk.kaddu.phoenixbsecompanion

import android.app.Application
import android.arch.persistence.room.Room
import dk.kaddu.phoenixbsecompanion.data.PhoenixDatabase

class PhoenixBSECompanion: Application() {

    companion object {
        var database: PhoenixDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        PhoenixBSECompanion.database =
                Room.databaseBuilder(this, PhoenixDatabase::class.java, "phoenix_database").build()
    }
}