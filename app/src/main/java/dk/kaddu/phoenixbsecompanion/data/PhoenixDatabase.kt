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
/*
            var gameStatus = GameStatus (4504, 4400, 0, 0, 0, 0, 0, 0, "219.13.5", "Upload Specials")
            gameStatusDao.insert(gameStatus)
            gameStatus = GameStatus (4506, 4400, 1554184142, 1554195721, 1554197460, 1554200828, 1554215821, 1554215983, "219.14.2", "Upload Specials")
            gameStatusDao.insert(gameStatus)
            gameStatus = GameStatus (4507, 4400, 1554270610, 1554278628, 1554279910, 1554281006, 1554300436, 1554300605, "219.14.3", "Upload Specials")
            gameStatusDao.insert(gameStatus)
*/
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

