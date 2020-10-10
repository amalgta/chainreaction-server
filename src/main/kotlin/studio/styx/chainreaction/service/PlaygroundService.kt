package studio.styx.chainreaction.service

import org.springframework.stereotype.Service
import studio.styx.chainreaction.model.PlaygroundDto

@Service
class PlaygroundService {
    fun getAllPlaygrounds(): List<PlaygroundDto> {
        val playgrounds = ArrayList<PlaygroundDto>()
        playgrounds.add(PlaygroundDto("1", "GTA", "new", "testbox123"))
        playgrounds.add(PlaygroundDto("2", "AGT", "new", "testbox123"))
        playgrounds.add(PlaygroundDto("3", "TGA", "new", "testbox123"))
        playgrounds.add(PlaygroundDto("4", "GAT", "new", "testbox123"))
        return playgrounds;
    }

}
