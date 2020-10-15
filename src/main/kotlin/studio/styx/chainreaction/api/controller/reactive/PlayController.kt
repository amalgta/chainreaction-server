package studio.styx.chainreaction.api.controller.reactive

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import studio.styx.chainreaction.domain.model.Action
import studio.styx.chainreaction.domain.model.ClientState
import studio.styx.chainreaction.domain.model.Constants.ACTION_CREATE_GAME
import studio.styx.chainreaction.domain.model.Constants.ACTION_DROP_GAME
import studio.styx.chainreaction.domain.model.Constants.ACTION_JOIN_GAME
import studio.styx.chainreaction.domain.model.Constants.ACTION_LEAVE_GAME
import studio.styx.chainreaction.domain.model.Constants.ACTION_START_GAME
import studio.styx.chainreaction.domain.model.GameDescriptor
import studio.styx.chainreaction.domain.model.Notification
import studio.styx.chainreaction.service.PlayService
import studio.styx.chainreaction.api.model.reactive.GameCreatedNotification
import studio.styx.chainreaction.api.model.reactive.GameJoinedNotification
import studio.styx.chainreaction.utils.*

@Service
class PlayController(private val playService: PlayService) : TextWebSocketHandler() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            logger.trace("Received message '{}' for {}", message.payload, session.description)
            var processed = false
            val action = Action(message.payload)
            when (session.state) {
                ClientState.LOBBY -> {
                    when (action.command) {
                        ACTION_CREATE_GAME -> {
                            processed = true
                            if (action.checkArgumentsCount(2)) {
                                val nickname = action.arguments[0]
                                val maxAllowedPlayers = action.arguments[1].toInt()
                                val gameDescriptor = GameDescriptor(maxAllowedPlayers)
                                playService.createGame(session, gameDescriptor, PlayService.PlayBoy(nickname, true))
                                session.nickname = nickname
                                session.sendObjectMessage(GameCreatedNotification(gameDescriptor.id));
                            }
                        }
                        ACTION_JOIN_GAME -> {
                            if (action.checkArgumentsCount(2)) {
                                processed = true
                                val nickname = action.arguments[0]
                                val gameId = action.arguments[1].toLong()
                                playService.joinGame(session, gameId, PlayService.PlayBoy(nickname, false))
                                session.nickname = nickname;
                                session.sendObjectMessage(GameJoinedNotification())
                            }
                        }
                    }
                }
                ClientState.CREATED -> {
                    when (action.command) {
                        ACTION_DROP_GAME -> {
                            processed = true
                            playService.deleteGame(session)
                        }
                        ACTION_START_GAME -> {
                            processed = true
                            playService.startGame(session)
                        }
                    }
                }
                ClientState.JOINED -> {
                    when (action.command) {
                        ACTION_LEAVE_GAME -> {
                            processed = true
                            playService.leaveGame(session)
                        }
                    }
                }
                ClientState.PLAYING -> {
                }
            }
            if (!processed) {
                logger.warn("Invalid message '{}' for state {} of {}", message.payload, session.state, session.description)
            }
        } catch (e: Notification) {
            session.sendObjectMessage(e)
        } catch (e: Exception) {
            logger.error("Caught an exception during message processing", e)
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.nickname = ""
        session.state = ClientState.LOBBY
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        playService.disconnect(session)
        logger.debug("Closed {} with code {} and reason '{}'",
                session.description,
                status.code,
                status.reason.orEmpty())
    }

}
