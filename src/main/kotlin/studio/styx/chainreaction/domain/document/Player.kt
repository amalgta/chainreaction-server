package studio.styx.chainreaction.domain.document

data class Player(
        val nickname: String,
        val color: String,
        val isHost: Boolean,
        val turn: Boolean,
        val killed: Boolean
)