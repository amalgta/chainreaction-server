package studio.styx.chainreaction.domain.model

class DefaultPlayerRequestDto(val nickname: String, val color: String)
class DefaultPlayerResponseDto(val nickname: String, val color: String, val isHost: Boolean, val turn: Boolean, val killed: Boolean)

