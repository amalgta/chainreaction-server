package studio.styx.chainreaction.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import studio.styx.chainreaction.api.controller.reactive.EchoController
import studio.styx.chainreaction.api.controller.reactive.PlayController

@Configuration
@EnableWebSocket
class WebSocketConfig(private val playController: PlayController, private val echoController: EchoController) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(playController, "v1/api/websocket").setAllowedOrigins("*").withSockJS()
        registry.addHandler(echoController, "v1/api/echo").setAllowedOrigins("*").withSockJS()
    }
}
