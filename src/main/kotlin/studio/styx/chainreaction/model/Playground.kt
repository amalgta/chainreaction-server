package studio.styx.chainreaction.model

import com.fasterxml.jackson.annotation.JsonProperty
import studio.styx.chainreaction.domain.model.Status

data class DefaultPlaygroundRequestDto(
        val host: DefaultPlayerRequestDto, @JsonProperty("max_allowed_players") val maxAllowedPlayers: Int, val private: Boolean)

data class DefaultPlaygroundResponseDto(
        val players: List<DefaultPlayerResponseDto>, val id: Long, val maxAllowedPlayers: Int, val status: Status)