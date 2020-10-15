package studio.styx.chainreaction.api.controller.rest

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping()
class RootController() {

    @GetMapping
    fun root(): ResponseEntity<String> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"version\":\"alpha\",\"name\":\"chain-reaction\"}")
    }
}