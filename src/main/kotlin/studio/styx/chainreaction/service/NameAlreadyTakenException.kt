package studio.styx.chainreaction.service

import studio.styx.chainreaction.domain.model.BaseNotification

class NameAlreadyTakenException : BaseNotification("nickname-taken") {

}
