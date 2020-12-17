package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): LinearOpMode() {
    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    override fun runOpMode() {

        wizard.newMenu(TelemetryWizard.Menu("Alliance", "Alliance", listOf<Any>(), "hi", true))

        wizard.summonWizard(gamepad1)
        waitForStart()
    }
}
