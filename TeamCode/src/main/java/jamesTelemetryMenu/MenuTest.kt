package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class MenuTest(): LinearOpMode() {

    val menu = TelemetryMenu(telemetry)

    override fun runOpMode() {

//        menu.addOption("Alliance", "Red")
//        menu.addOption("Alliance", "Blue")
        menu.display(2, "hi")
        menu.doMenus(gamepad1)

        waitForStart()
    }

}