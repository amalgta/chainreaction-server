package studio.styx.chainreaction.domain.model

import org.springframework.web.socket.WebSocketSession
import studio.styx.chainreaction.service.PlayService
import studio.styx.chainreaction.service.ChainReactionGameService
import java.util.concurrent.atomic.AtomicLong

/**
 * A declared game.
 */
class GameDescriptor(
        maxAllowedPlayers: Int) {

    companion object {
        val counter = AtomicLong()
    }

    val id: Long = counter.incrementAndGet()
    val players = mutableMapOf<PlayService.PlayBoy, WebSocketSession>()
    var game: ChainReactionGameService? = null;
}
