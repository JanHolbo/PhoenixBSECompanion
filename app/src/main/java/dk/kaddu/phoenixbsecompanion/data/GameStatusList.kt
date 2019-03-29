package dk.kaddu.phoenixbsecompanion.data

data class GameStatusList (val gameStatus: List<GameStatus>)

data class GameStatus (
        val star_date: String?,
        val status: String?)
