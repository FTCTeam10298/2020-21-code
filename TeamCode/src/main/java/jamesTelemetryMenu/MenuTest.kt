package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): LinearOpMode() {
    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    override fun runOpMode() {

        wizard.newMenu("Alliance", "Alliance", listOf("Red", "Blue"))
        wizard.newMenu("GameType", "Which kind of game is it?", listOf("Remote", "In-Person"), true, wizard.getMenu("Alliance"))

        wizard.summonWizard(gamepad1)
        console.display(2, wizard.wasItemChosen("Red").toString())
        waitForStart()
    }
}
