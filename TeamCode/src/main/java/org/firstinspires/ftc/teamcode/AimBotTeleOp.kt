package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot = AimBotHardware()


    fun fourMotors(lFpower: Double, lBpower: Double, rFpower: Double, rBpower: Double) {
        robot.lBDrive?.power = lBpower
        robot.lFDrive?.power = lFpower
        robot.rBDrive?.power = rBpower
        robot.rFDrive?.power = rFpower
    }

    fun driveY(power: Double) {
        fourMotors(-power, -power, -power, -power)
    }

    fun driveX(power: Double) {
        fourMotors(power, -power, -power, power)
    }

    fun driveR(power: Double) {
        fourMotors(power, power, -power, -power)
    }

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        driveY(gamepad1.left_stick_y.toDouble())
        driveX(gamepad1.left_stick_x.toDouble())
        driveR(gamepad1.right_stick_x.toDouble())

    }
}