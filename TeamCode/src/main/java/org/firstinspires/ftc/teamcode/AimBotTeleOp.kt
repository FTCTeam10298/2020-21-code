package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot= AimBotHardware()

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        
        if (gamepad1.left_stick_x > 0)
            robot.lBDrive?.power= 1.0

    }
}