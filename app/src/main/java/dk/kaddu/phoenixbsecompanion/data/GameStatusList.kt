package dk.kaddu.phoenixbsecompanion.data

data class GameStatusList (var gameStatus: List<GameStatus>)

data class GameStatus (
        var current_date: Int?,
        var year_start: Int,
        var turns_downloaded: Int,
        var turns_processed: Int,
        var turns_uploaded: Int,
        var emails_sent: Int,
        var specials_processed: Int,
        var day_finished: Int,
        var star_date: String,
        var status: String)
