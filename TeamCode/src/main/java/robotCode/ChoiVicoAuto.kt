//package robotCode
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import robotCode.hardwareClasses.EncoderDriveMovement
//import robotCode.hardwareClasses.OdometryDriveMovement
//import telemetryWizard.TelemetryConsole
//
//@Autonomous(name="Auto ChoiVico", group="ChoiVico")
//class ChoiVicoAuto: LinearOpMode() {
//
//    val console = TelemetryConsole(telemetry)
//
//    val hardware = ChoiVicoHardware()
//    val robot = EncoderDriveMovement(console, hardware)
//
//    override fun runOpMode() {
//        hardware.init(hardwareMap)
//
//        waitForStart()
//
//    }
//}