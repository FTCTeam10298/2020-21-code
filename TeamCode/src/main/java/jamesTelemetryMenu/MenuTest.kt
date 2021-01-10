package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): OpMode() {
    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    override fun init() {

//        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"))
//        wizard.newMenu("powerShot", "Will we do the power shots?", listOf("Yes", "No"))
        wizard.newMenu("theirWobble", "Will we do our partner's wobble", listOf("Yes", "No"))
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), wizard.getMenu("theirWobble"))
        wizard.newMenu("startPos", "Which line are we starting in?", listOf("Closer to you", "Closer to the middle"), wizard.getMenu("ourWobble"))
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Red", "Blue"), wizard.getMenu("startPos"))
        wizard.newMenu("gameType", "Which kind of game is it?", listOf("Remote", "In-Person"), wizard.getMenu("alliance"), true)

        wizard.summonWizard(gamepad1)
    }

    override fun loop() {}
}
