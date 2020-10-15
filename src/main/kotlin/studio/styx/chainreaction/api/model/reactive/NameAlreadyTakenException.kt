package studio.styx.chainreaction.api.model.reactive

import studio.styx.chainreaction.domain.model.BaseNotification

class NameAlreadyTakenException : BaseNotification("nickname-taken") {

}
