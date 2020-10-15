package studio.styx.chainreaction.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import studio.styx.chainreaction.domain.model.*
import studio.styx.chainreaction.domain.model.Constants.MAX_NAME_LENGTH
import studio.styx.chainreaction.domain.model.Constants.MIN_NAME_LENGTH
import studio.styx.chainreaction.utils.*

@Service
class PlayService(private val playgroundService: PlaygroundService, private val applicationContext: ApplicationContext) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val games = mutableMapOf<Long, GameDescriptor>()
    val gamesCounter = Counter()

    @Synchronized
    fun createGame(session: WebSocketSession, gameDescriptor: GameDescriptor, host: PlayBoy) {
        when {
            host.nickname.length < MIN_NAME_LENGTH -> {
                throw Notification(TooShortNameNotification())
            }
            host.nickname.length > MAX_NAME_LENGTH -> {
                throw Notification(TooLongNameNotification())
            }
            else -> {
                with(session) {
                    state = ClientState.CREATED
                    gameId = gameDescriptor.id
                }
                gameDescriptor.players[host] = session
                games[gameDescriptor.id] = gameDescriptor
                gamesCounter.increment()
            }
        }
    }

    @Synchronized
    fun joinGame(session: WebSocketSession, gameDescriptorId: Long, playBoy: PlayBoy) {
        when {
            playBoy.nickname.length < MIN_NAME_LENGTH -> {
                throw Notification(TooShortNameNotification())
            }
            playBoy.nickname.length > MAX_NAME_LENGTH -> {
                throw Notification(TooLongNameNotification())
            }
            !games.containsKey(gameDescriptorId) -> {
                throw Notification(NoSuchGameNotification())
            }
            games[gameDescriptorId]!!.players.keys.firstOrNull { playBoy.nickname == it.nickname } != null -> {
                throw Notification(NameAlreadyTakenException())
            }
            else -> {
                games[gameDescriptorId]?.players?.set(playBoy, session)
                with(session) {
                    state = ClientState.JOINED
                    gameId = gameDescriptorId
                }
            }
        }
    }

    @Synchronized
    fun deleteGame(session: WebSocketSession) {
        val gameDescriptor = games[session.gameId]
        if (gameDescriptor != null) {
            games.remove(session.gameId)
            gamesCounter.decrement()
            session.state = ClientState.LOBBY
            gameDescriptor.players.forEach {
                it.value.sendObjectMessage(GameDeletedNotification())
                it.value.state = ClientState.LOBBY
            }
        }
    }

    @Synchronized
    fun startGame(session: WebSocketSession) {
        val descriptor = games[session.gameId]

        if (descriptor != null) {
            descriptor.players.forEach {
                it.value.sendObjectMessage(GameStartNotification())
                it.value.state = ClientState.PLAYING
            }

            // Build the game instance
            val game = applicationContext.getBean(descriptor.mode.type).apply {
                initializeGame(descriptor, players)
                startCountdown()
            }

            logger.info("Started game {} ({} player{}",
                    descriptor.id,
                    descriptor.players.size,
                    if (descriptor.players.size > 1) "s" else "")
        }
    }

    @Synchronized
    fun leaveGame(session: WebSocketSession) {
        val playBoy = games[session.gameId]?.players?.keys?.find { it.nickname == session.nickname }!!
        games[session.gameId]?.players?.remove(playBoy)
        session.state = (ClientState.LOBBY)
        session.gameId = null
        session.sendObjectMessage()
    }

    fun disconnect(session: WebSocketSession) {
        when (session.state) {
            ClientState.OPENED -> {
            }
            ClientState.LOBBY -> {
            }
            ClientState.CREATED -> deleteGame(session)
            ClientState.JOINED -> leaveGame(session)
            ClientState.PLAYING -> {
                synchronized(this) {
                    runningGames[session.gameId]?.let {
                        if (it.removeUser(session)) {
                            // No player left or race game ended, remove the game
                            endGame(it.id)
                        }
                    }
                }
            }
        }
    }


    data class PlayBoy(val nickname: String, val color: String, val isHost: Boolean)


}
