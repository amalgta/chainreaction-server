package studio.styx.chainreaction.domain.model

/**
 * Server side view of the state of a client connection.
 */
enum class ClientState {
    /**
     * The user is identified and is in the lobby getting the list of games.
     */
    LOBBY,
    /**
     * The user created a game.
     */
    CREATED,
    /**
     * The user joined a game.
     */
    JOINED,
    /**
     * A game is in progress (starting or running or displaying result).
     */
    PLAYING
}
