//package goalDetection
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode
//import telemetryWizard.TelemetryConsole
//import locationTracking.Coordinate
//import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion
//import robotCode.hardwareClasses.EncoderDriveMovement
//
////@TeleOp
//class AutoAim(private val console: TelemetryConsole, private val opMode: OpMode, val robot: EncoderDriveMovement) {
//
//
//    lateinit var uGRectDetector: UGRectDetector
//
//    fun init(webcamName: String) {
//        uGRectDetector = UGRectDetector(opMode.hardwareMap, webcamName)
//        public void setTopRectangle(double topRectHeightPercentage, double topRectWidthPercentage)
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