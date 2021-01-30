package robotCode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import robotCode.hardwareClasses.MecOdometryHardware
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

class odometryHardware: MecOdometryHardware {
    override lateinit var lOdom: DcMotor
    override lateinit var rOdom: DcMotor
    override lateinit var cOdom: DcMotor
    override lateinit var lFDrive: DcMotor
    override lateinit var rFDrive: DcMotor
    override lateinit var lBDrive: DcMotor
    override lateinit var rBDrive: DcMotor

    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        lOdom = hwMap.get("______") as DcMotor
    }

}

class odometryTest: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = odometryHardware()
    val robot = OdometryDriveMovement(console,hardware)

    override fun runOpMode() {

        waitForStart()

    }
}