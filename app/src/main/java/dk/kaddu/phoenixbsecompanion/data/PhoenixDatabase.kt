package dk.kaddu.phoenixbsecompanion.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(GameStatus::class), version = 1)
public abstract class PhoenixDatabase : RoomDatabase() {
    abstract fun gameStatusDao(): GameStatusDao

    companion object {
        @Volatile
        private var INSTANCE: PhoenixDatabase? = null

        fun getDatabase(context: Context): PhoenixDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhoenixDatabase::class.java,
                        "phoenix_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

