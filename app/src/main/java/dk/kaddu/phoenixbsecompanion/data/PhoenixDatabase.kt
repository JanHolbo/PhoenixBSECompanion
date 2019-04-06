package dk.kaddu.phoenixbsecompanion.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

@Database(entities = arrayOf(GameStatus::class), version = 1)
public abstract class PhoenixDatabase : RoomDatabase() {
    abstract fun gameStatusDao(): GameStatusDao

    companion object {
        @Volatile
        private var INSTANCE: PhoenixDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
            ): PhoenixDatabase {
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
                ).addCallback(PhoenixDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class PhoenixDatabaseCallback (
        private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

        fun populateDatabase (gameStatusDao: GameStatusDao) {
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.gameStatusDao())
                }
            }
        }
    }
}

