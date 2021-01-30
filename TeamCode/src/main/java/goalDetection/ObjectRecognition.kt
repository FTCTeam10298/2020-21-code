//package goalDetection
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import org.opencv.core.Core
//import org.opencv.core.Mat
//import org.opencv.imgcodecs.Imgcodecs
//import org.opencv.imgproc.Imgproc
//import telemetryWizard.TelemetryConsole
//
//@TeleOp
//class GoalTracker: LinearOpMode() {
//
//    val console = TelemetryConsole(telemetry)
//
//    val opencv = OpencvAbstraction(this)
//    val goalDetector = ObjectRecognition(console)
//
//    override fun runOpMode() {
//        opencv.init(hardwareMap)
//        opencv.start()
//
//        while (!isStarted) {
////            goalDetector.detectTrapezoid(opencv.frame)
//            opencv.onNewFrame(goalDetector::findGoal)
////            opencv.setReturn(goalDetector.display)
//        }
//
//        waitForStart()
//
//    }
//}
//
//class ObjectRecognition(private val console: TelemetryConsole) {
//
//    val classNames = listOf("redGoal", "blueGoal", "goal", "else")
//    val classesFile = "hi"
//
//    fun findGoal(frame: Mat): Mat {
//
//    }
//
//
//}