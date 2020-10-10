package studio.styx.chainreaction.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import studio.styx.chainreaction.model.PlaygroundDto
import studio.styx.chainreaction.service.PlaygroundService

@RestController
@RequestMapping("v1/playgrounds")
class PlaygroundController(private val playgroundService: PlaygroundService) {

    @GetMapping()
    fun getPlaygrounds(
    ): List<PlaygroundDto> {
        return playgroundService.getAllPlaygrounds()
    }
}
