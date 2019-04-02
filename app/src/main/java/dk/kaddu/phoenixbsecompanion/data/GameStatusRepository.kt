package dk.kaddu.phoenixbsecompanion.data

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class GameStatusRepository(private val gameStatusDao: GameStatusDao) {

    val allGameStatus: LiveData<List<GameStatus>> = gameStatusDao.getAllStatus()

    @WorkerThread
    suspend fun insert(gameStatus: GameStatus) {
        gameStatusDao.insert(gameStatus)
    }
}