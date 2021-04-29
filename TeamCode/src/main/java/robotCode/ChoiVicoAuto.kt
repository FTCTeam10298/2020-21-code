//package robotCode
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.hardware.HardwareMap
//import creamsicleGoalDetection.CreamsicleGoalDetector
//import creamsicleGoalDetection.UltimateGoalAimer
//import locationTracking.Coordinate
//import openCvAbstraction.OpenCvAbstraction
//import org.opencv.core.Mat
//import ringDetector.RingDetector
//import robotCode.hardwareClasses.OdometryDriveMovement
//import telemetryWizard.TelemetryConsole
//
//@Autonomous(name="Auto ChoiVico", group="ChoiVico")
//class ChoiVicoAuto: LinearOpMode() {
//
//    val opencv = OpenCvAbstraction(this)
//
//    val console = TelemetryConsole(telemetry)
//
//    val hardware = ChoiVicoHardware()
//    val robot = OdometryDriveMovement(console, hardware)
//    val target = Coordinate()
//
//    val ringDetector = RingDetector(150, 135, console)
//    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE
//
//    val goalDetector = CreamsicleGoalDetector(console)
//    val turret = UltimateGoalAimer(console, goalDetector, hardware)
//
//    class CameraWrap(val cameraName:String,
//                     val opencv:OpenCvAbstraction,
//                     val hardwareMap: HardwareMap){
//        private var hasInitialized = false
//
//        fun startWatching(onFirstFrame:((Mat)->Unit)?, onNewFrame: (Mat)->Mat){
//            if(!hasInitialized){
//                hasInitialized = true
//
//                opencv.cameraName = cameraName
//                if(onFirstFrame!=null){
//                    opencv.onFirstFrame(onFirstFrame)
//                }
//                opencv.onNewFrame(onNewFrame)
//                opencv.init(hardwareMap)
//                opencv.start()
//            }
//        }
//
//    }
//
//    override fun runOpMode() {
//        hardware.init(hardwareMap)
//
////        opencv.optimizeView = true
////        opencv.openCameraDeviceAsync = true
////        opencv.cameraName = "Webcam 1"
////        opencv.init(hardwareMap)
////        opencv.start()
////
////        opencv.onFirstFrame(ringDetector::init)
////        opencv.onNewFrame(ringDetector::processFrame)
//
//        val ringCamera = CameraWrap(cameraName = "Webcam 1", opencv, hardwareMap)
////        val aimCamera = CameraWrap(cameraName = "Webcam 2", opencv, hardwareMap)
//        val aimCamera = ringCamera
//
//        ringCamera.startWatching( onFirstFrame = ringDetector::init, onNewFrame = ringDetector::processFrame)
//
//        waitForStart()
//
////        position = ringDetector.position
////
////        opencv.optimizeView = false
////        opencv.openCameraDeviceAsync = false
////        opencv.cameraName = "Webcam 2"
////        opencv.init(hardwareMap)
////        opencv.start()
//
//        aimCamera.startWatching( onFirstFrame = null, onNewFrame = goalDetector::scoopFrame)
////
////        opencv.onNewFrame( goalDetector::scoopFrame )
//
////        object: Thread() {
////            override fun run() {
////                while (opModeIsActive()) {
////                    turret.updateAimAndAdjustRobot()
////                    sleep(10)
////                }
////
////            }
////        }.start()
//
//        if(position == RingDetector.RingPosition.NONE){
//            target.setCoordinate(0.0,60.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            target.addCoordinate(36.0,0.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            hardware.turret.targetPosition = 1
//            hardware.turret.power = 1.0
//            target.addCoordinate(0.0,12.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//        }
//        if(position == RingDetector.RingPosition.ONE){
//            target.setCoordinate(0.0,96.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            target.addCoordinate(36.0,-36.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            target.addCoordinate(0.0,12.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//
//        }
//        if (position == RingDetector.RingPosition.FOUR){
//            target.setCoordinate(0.0,114.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            target.addCoordinate(36.0,-54.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//            target.addCoordinate(0.0,12.0,0.0)
//            robot.fineTunedGoToPos(target, this)
//            sleep(1000)
//        }
//    }
//}
