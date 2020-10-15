package studio.styx.chainreaction.service

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import studio.styx.chainreaction.domain.model.GameDescriptor

/**
 * A running race game.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ChainReactionGameService(
        private val playService: PlayService,
        scheduler: ThreadPoolTaskScheduler) {
    val players: MutableList<WebSocketSession> = mutableListOf()
    var id: Long = 0

    /**
     * Number of players that typed all their words. Used to detect the end of the round.
     */
    private var finishedPlayers = 0


    fun initializeGame(gameDescriptor: GameDescriptor) {
        id = gameDescriptor.id
    }

    fun startCountdown(){}
}
