package dk.kaddu.phoenixbsecompanion.data

data class GameStatusList (val gameStatus: List<GameStatus>)

data class GameStatus (
        val current_date: Int?,
        val year_start: Int?,
        val turns_downloaded: Int?,
        val turns_processed: Int?,
        val turns_uploaded: Int?,
        val emails_sent: Int?,
        val specials_processed: Int?,
        val day_finished: Int?,
        val star_date: String?,
        val status: String?)
