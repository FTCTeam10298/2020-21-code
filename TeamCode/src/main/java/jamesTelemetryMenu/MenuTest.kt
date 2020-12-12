package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class MenuTest(): LinearOpMode() {

    val menu = TelemetryConsole(telemetry)

    override fun runOpMode() {

//        menu.addOption("Alliance", "Red")
//        menu.addOption("Alliance", "Blue")
        menu.display(2, "hi")
        sleep(1000)
//        menu.doMenus(gamepad1)


        waitForStart()
    }

}