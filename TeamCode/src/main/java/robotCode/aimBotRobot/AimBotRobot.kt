package robotCode.aimBotRobot

import robotCode.AimBotHardware

open class AimBotRobot(): AimBotHardware() {
    val drive = EncoderDriveMovement()
}