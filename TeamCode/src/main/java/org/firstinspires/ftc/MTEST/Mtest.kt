package org.firstinspires.ftc.MTEST

//this was commited by gabe, who wants you to remember to set your launch speed

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.Range

@TeleOp(name="Mtest", group="Aim Bot")
class Mtest(): OpMode() {

    val robot = MTestHardware()
    var maxPower:Double = 0.1

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {

        maxPower = Range.clip(maxPower, 0.0, 1.0)
        robot.rFlywheel?.power = gamepad1.left_stick_y.toDouble() * maxPower
        robot.lFlywheel?.power = gamepad1.left_stick_y.toDouble() * maxPower

        when {
            gamepad1.dpad_up -> maxPower += 0.01
            gamepad1.dpad_down -> maxPower -= 0.01
            gamepad1.x -> stop()
        }


    }
}