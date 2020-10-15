package studio.styx.chainreaction.api.controller.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import studio.styx.chainreaction.domain.document.Player
import studio.styx.chainreaction.domain.document.Playground
import studio.styx.chainreaction.domain.model.DefaultPlaygroundRequestDto
import studio.styx.chainreaction.domain.model.DefaultPlaygroundResponseDto
import studio.styx.chainreaction.service.PlaygroundService
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("v1/playgrounds")
class PlaygroundController(private val playgroundService: PlaygroundService) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createPlayground(@RequestBody defaultStationRequest: DefaultPlaygroundRequestDto, request: HttpServletRequest): ResponseEntity<DefaultPlaygroundResponseDto> {
        val station = playgroundService.createPlayground(defaultStationRequest);
        return created(ServletUriComponentsBuilder.fromContextPath(request)
                .path(request.requestURI.plus("/{playgroundId}"))
                .buildAndExpand(station.id)
                .toUri()).body(station);
    }

    @GetMapping
    fun getAllPlaygrounds(
            @PageableDefault(page = 0, size = 10, direction = Direction.DESC) page: Pageable): List<Playground> {
        return playgroundService.getAllPlaygrounds(page);
    }

    @GetMapping("/{playgroundId}")
    fun getPlayground(
            @PathVariable("playgroundId") playgroundId: Long): ResponseEntity<Playground> {
        return if (playgroundService.getPlayground(playgroundId) != null) {
            ok(playgroundService.getPlayground(playgroundId)!!)
        } else {
            notFound().build();
        }
    }

    @GetMapping("/{playgroundId}/players")
    fun getPlayersByPlayground(
            @PathVariable("playgroundId") playgroundId: Long): ResponseEntity<List<Player>> {
        return if (playgroundService.getPlayground(playgroundId) != null) {
            ok(playgroundService.getPlayground(playgroundId)!!.players.values.toList())
        } else {
            notFound().build();
        }
    }
}
