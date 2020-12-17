package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): LinearOpMode() {
    val console = TelemetryConsole(telemetry)
    val menu = TelemetryMenu(console)

    override fun runOpMode() {

        menu.addOption("Alliance", "Red")
        menu.addOption("Alliance", "Blue")
        menu.firstOption("Alliance")

`
//        menu.display(6, "Alliance")

//        menu.display(4, menu.menu.options.listOfOptions.toString())
//        menu.display(1, menu.currentOption)
//        menu.display(2, menu.menu.getItems("[]").toString())

//        menu.doMenus(gamepad1)
        waitForStart()
    }
}
