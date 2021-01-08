package goalDetection

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import locationTracking.Coordinate
import robotCode.aimBotRobot.EncoderDriveMovement

//@TeleOp
class AutoAim(private val console: TelemetryConsole, opMode: OpMode, val robot: EncoderDriveMovement) {

    val detector = GoalDetector(console)
    val opencv = OpencvAbstraction(opMode)
    var aimed = false
    val robotPos = Coordinate(0.0)
    val goalPos = Coordinate(21.0)

    fun init() {
        opencv.init()
        opencv.start()
    }

    fun aim() {

//        Turn toward goal
        while (goalPos.r !== robotPos.r)
            robot.driveRobotTurn(1.0, 1.0)

//        fine tune aim
        while (!aimed) {
            detector.detectTrapezoid(opencv.frame)
            detector.goal
//            if (detector.x = )
        }

    }

}