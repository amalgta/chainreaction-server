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
class EchoController() : TextWebSocketHandler() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            logger.trace("Received message '{}' for {}", message.payload, session.description)
            session.sendStringMessage(message.payload)
        } catch (e: Notification) {
            session.sendObjectMessage(e)
        } catch (e: Exception) {
            logger.error("Caught an exception during message processing", e)
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.sendStringMessage("welcome")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.debug("Closed {} with code {} and reason '{}'",
                session.description,
                status.code,
                status.reason.orEmpty())
    }

}
