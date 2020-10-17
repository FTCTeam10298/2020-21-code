package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotMovement()

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()
        robot.side(1.0, 700)



    }
}