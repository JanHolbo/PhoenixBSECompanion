package dk.kaddu.phoenixbsecompanion.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import dk.kaddu.phoenixbsecompanion.data.GameStatus
import dk.kaddu.phoenixbsecompanion.data.GameStatusRepository
import dk.kaddu.phoenixbsecompanion.data.PhoenixDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

class GameStatusViewModel(application: Application) : AndroidViewModel(application){
    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    private val repository: GameStatusRepository
    val allGameStatus: LiveData<List<GameStatus>>

    init {
        val gameStatusDao = PhoenixDatabase.getDatabase(application, scope).gameStatusDao()
        repository = GameStatusRepository(gameStatusDao)
        allGameStatus = repository.allGameStatus
    }

    fun insert(gameStatus: GameStatus) = scope.launch(Dispatchers.IO) {
        repository.insert(gameStatus)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}