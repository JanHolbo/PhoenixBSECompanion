package dk.kaddu.phoenixbsecompanion.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

data class GameStatusList (var gameStatus: List<GameStatus>)

@Entity(tableName = "game_status")
data class GameStatus (
        @PrimaryKey
        @ColumnInfo(name = "current_date") var current_date: Int?,
        @ColumnInfo(name = "year_start") var year_start: Int,
        @ColumnInfo(name = "turns_downloaded") var turns_downloaded: Int,
        @ColumnInfo(name = "turns_processed") var turns_processed: Int,
        @ColumnInfo(name = "turns_uploaded") var turns_uploaded: Int,
        @ColumnInfo(name = "emails_sent") var emails_sent: Int,
        @ColumnInfo(name = "specials_processed") var specials_processed: Int,
        @ColumnInfo(name = "day_finished") var day_finished: Int,
        @ColumnInfo(name = "star_date") var star_date: String,
        @ColumnInfo(name = "status") var status: String)
