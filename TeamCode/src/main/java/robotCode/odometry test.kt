package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import locationTracking.Coordinate
import pid.PID
import robotCode.hardwareClasses.MecOdometryHardware
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole



@Autonomous(name = "odometryTest", group = "Aim Bot")
class odometryTest: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = odometryHardware()
    val robot = OdometryDriveMovement(console, hardware)

    val target = Coordinate()

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

        target.setCoordinate(x = 0.0, y = 24.0, r = 0.0)
        robot.straightGoToPosition(target,1.0,0.5,this)
        //robot.turnGoToPosition(target, 1.0, 0.5, this)
    }
}
