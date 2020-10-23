package org.firstinspires.ftc.MTEST

//this was commited by gabe, who wants you to remember to set your launch speed

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.Range
import kotlin.math.max

@TeleOp(name="Mtest", group="Aim Bot")
class Mtest(): OpMode() {

    val robot = MTestHardware()
    var maxPower: Double = 0.05
    var lSpeed: Double = 0.0
    var prevPos: Int = 0
    var dt = 0.0
    var timeA = 0.0

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        dt = runtime - timeA
        timeA = runtime

        lSpeed = (prevPos - robot.rFlywheel!!.currentPosition).toDouble() / timeA
        prevPos = robot.rFlywheel!!.currentPosition
        telemetry.addLine(lSpeed.toString())
        telemetry.addLine("Power: ${robot.rFlywheel?.power}")

        maxPower = Range.clip(maxPower, 0.0, 1.0)
        robot.rFlywheel?.power = gamepad1.left_stick_y.toDouble() * maxPower
        robot.lFlywheel?.power = gamepad1.left_stick_y.toDouble() * maxPower

        when {
            gamepad1.dpad_up -> maxPower += 0.008
            gamepad1.dpad_down -> maxPower -= 0.008
            gamepad1.x -> requestOpModeStop()
        }

        if (lSpeed <= 0 && robot.lFlywheel!!.power > 0.01) {
            requestOpModeStop()
        }
    }
}