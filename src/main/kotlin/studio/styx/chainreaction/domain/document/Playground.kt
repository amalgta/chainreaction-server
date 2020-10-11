package studio.styx.chainreaction.domain.document

import studio.styx.chainreaction.domain.model.Status
import java.util.concurrent.atomic.AtomicLong


class Playground(
        val private: Boolean, val maxAllowedPlayers: Int
) {
    companion object {
        val counter = AtomicLong()
    }

    val status: Status = Status.NEW
    val id: Long = counter.incrementAndGet()


    val players = mutableListOf<Player>()
}