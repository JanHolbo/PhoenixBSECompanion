package dk.kaddu.phoenixbsecompanion.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface GameStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameStatus: GameStatus)

    @Query ("SELECT * FROM game_status ORDER BY `current_date` DESC")
    fun getAllStatus(): LiveData<List<GameStatus>>
}