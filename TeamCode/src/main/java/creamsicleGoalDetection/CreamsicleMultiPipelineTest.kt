//package creamsicleGoalDetection
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import locationTracking.Coordinate
//import openCvAbstraction.OpenCvAbstraction
//import ringDetector.RingDetector
//import robotCode.ChoiVicoHardware
//import robotCode.hardwareClasses.OdometryDriveMovement
//import robotCode.OdometryTestHardware
//import telemetryWizard.TelemetryConsole
//
//@TeleOp(name="Multi Pipeline Test", group="Tests")
//class CreamsicleMultiPipelineTest: LinearOpMode() {
//
//    val console = TelemetryConsole(telemetry)
//    val hardware = ChoiVicoHardware()
//    val robot = OdometryDriveMovement(console, hardware)
//    val opencv = OpenCvAbstraction(this)
//
//    val goalDetector = CreamsicleGoalDetector(console)
//    val turret = UltimateGoalAimer(console, goalDetector, hardware)
//    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE
//
//
//    val target = Coordinate()
//
//    override fun runOpMode() {
//        hardware.init(hardwareMap)
//
//        opencv.init(hardwareMap)
//        opencv.start()
//
//        waitForStart()
//
//
//        opencv.onNewFrame(goalDetector::scoopFrame)
//        sleep(500000000000)
//    }
//}