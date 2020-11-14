package jamesTelemetryMenu

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MenuTest(): OpMode() {

    val menu = TelemetryMenu(telemetry)

    override fun init() {

        menu.addOption("Alliance", "Red")
        menu.addOption("Alliance", "Blue")
        menu.doMenus(gamepad1)

    }

    override fun loop() {
        
    }
}