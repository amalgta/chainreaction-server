package studio.styx.chainreaction.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class DefaultPlaygroundRequestDto(
        val host: DefaultPlayerRequestDto, @JsonProperty("max_allowed_players") val maxAllowedPlayers: Int, val private: Boolean)

data class DefaultPlaygroundResponseDto(
        val players: List<DefaultPlayerResponseDto>, val id: Long, val maxAllowedPlayers: Int, val status: Status)