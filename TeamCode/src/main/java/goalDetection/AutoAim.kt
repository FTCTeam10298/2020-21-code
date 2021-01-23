//package goalDetection
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode
//import telemetryWizard.TelemetryConsole
//import locationTracking.Coordinate
//import robotCode.hardwareClasses.EncoderDriveMovement
//
////@TeleOp
//class AutoAim(private val console: TelemetryConsole, opMode: OpMode, val robot: EncoderDriveMovement) {
//
//    val detector = GoalDetector(console)
//    val opencv = OpencvAbstraction(opMode)
//    var aimed = false
//    val robotPos = Coordinate(0.0)
//    val goalPos = Coordinate(21.0)
//
//    fun init() {
//        opencv.init(hardwareMap)
//        opencv.start()
//    }
//
//    fun aim() {
//
////        Turn toward goal
//        while (goalPos.r.equals(robotPos.r))
//            robot.driveRobotTurn(1.0, 1.0)
//
////        fine tune aim
//        while (!aimed) {
////            detector.detectTrapezoid(opencv.frame)
//            detector.goal
////            if (detector.x = )
//        }
//
//    }
//
//}