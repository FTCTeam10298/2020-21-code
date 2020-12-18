package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): LinearOpMode() {
    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    override fun runOpMode() {

        wizard.newMenu(TelemetryWizard.Menu("Alliance", "Alliance", listOf("Red", "Blue")))
        wizard.newMenu(TelemetryWizard.Menu("GameType", "What kind of game is it?", listOf("Remote", "In-Person"), wizard.getMenu("Alliance"), true))

        wizard.summonWizard(gamepad1)
        waitForStart()
    }
}
