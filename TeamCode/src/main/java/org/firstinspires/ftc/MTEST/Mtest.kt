package org.firstinspires.ftc.MTEST

//this was commited by gabe, who wants you to remember to set your launch speed

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.abs

@TeleOp(name="Mtest", group="Aim Bot")
class Mtest(): OpMode() {

    val robot = MTestHardware()
    //we need more powah I CANT DO IT CAPTAIN
    var maxPower:Double = 0.0

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        
        if (abs(gamepad1.left_stick_y.toDouble()) <= maxPower) {
            robot.rFlywheel?.power = gamepad1.left_stick_y.toDouble()
            robot.lFlywheel?.power = gamepad1.left_stick_y.toDouble()
        }

        when {
            gamepad1.dpad_up -> maxPower ++
            gamepad1.dpad_down -> maxPower --
        }


    }
}