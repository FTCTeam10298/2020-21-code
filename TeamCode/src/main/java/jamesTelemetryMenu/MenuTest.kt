package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): LinearOpMode() {

    val menu = TelemetryMenu(telemetry)

    override fun runOpMode() {

        menu.addOption("Alliance", "Red")
        menu.addOption("Alliance", "Blue")
        menu.doMenus(gamepad1)

        waitForStart()
    }

}
