package studio.styx.chainreaction.utils

import org.slf4j.LoggerFactory
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.sockjs.transport.session.PollingSockJsSession
import org.springframework.web.socket.sockjs.transport.session.StreamingSockJsSession
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession
import studio.styx.chainreaction.domain.model.ClientState
import studio.styx.chainreaction.domain.model.Constants.GSON
import java.net.http.WebSocket

// WebSocketSession extensions and utility methods, such as:
// - Business attributes: server side state, user name, game ID (0 means no actual game)
// - Sender methods

const val STATE = "state"
const val NAME = "name"
const val GAME_ID = "GAME_ID"

private val logger = LoggerFactory.getLogger("studio.styx.chainreaction.utils.WebSocket")

val WebSocketSession.description: String
    get() = "'$id' ('$nickname')"

var WebSocketSession.nickname: String
    get() = attributes[NAME] as String
    set(value) {
        attributes[NAME] = value
    }

var WebSocketSession.state: ClientState
    get() = attributes[STATE] as ClientState
    set(value) {
        attributes[STATE] = value
        logger.debug("Changed state to '{}' for {}{}",
                state,
                description,
                if (state == ClientState.OPENED) " with '${getTransportName()}' transport" else "")
    }

var WebSocketSession.gameId: Long?
    get() = attributes[GAME_ID] as Long?
    set(value) {
        attributes[GAME_ID] = value
        logger.debug("Changed state to '{}' for {}",
                gameId,
                description
        )
    }

fun WebSocketSession.getTransportName(): String = when (this) {
    is WebSocketServerSockJsSession -> "WebSocket"
    is StreamingSockJsSession -> "Streaming"
    is PollingSockJsSession -> "Polling"
    else -> "unknown"
}

fun WebSocketSession.sendObjectMessage(obj: Any) {
    sendStringMessage(GSON.toJson(obj))
}

fun WebSocketSession.sendStringMessage(msg: String) {
    try {
        sendMessage(TextMessage(msg))
    } catch (e: Exception) {
        val shortenedMsg = if (msg.length > 40) msg.substring(0, 40) + "..." else msg
        logger.error("Failed to send message '{}' to {}: {}", shortenedMsg, description, e.toString())
    }
}

fun sendMessage(sessions: Collection<WebSocketSession>, obj: Any) {
    val msg = GSON.toJson(obj)
    sessions.forEach { s -> s.sendStringMessage(msg) }
}
