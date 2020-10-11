package studio.styx.chainreaction.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import studio.styx.chainreaction.domain.document.Playground
import studio.styx.chainreaction.model.DefaultPlaygroundRequestDto
import studio.styx.chainreaction.model.DefaultPlaygroundResponseDto
import studio.styx.chainreaction.service.PlaygroundService
import javax.servlet.http.HttpServletRequest
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault

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

}
