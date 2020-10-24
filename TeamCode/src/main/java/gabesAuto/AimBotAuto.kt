package gabesAuto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotHardware()

    override fun runOpMode() {
//        after init pressed
        robot.init(hardwareMap)

        waitForStart()
//        after start pressed

    }
}