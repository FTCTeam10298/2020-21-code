package robotCode

import com.qualcomm.robotcore.hardware.*
import robotCode.hardwareClasses.MecOdometryHardware

class ChoiVicoHardware: MecOdometryHardware {

    override lateinit var lOdom: DcMotor
    override lateinit var rOdom: DcMotor
    override lateinit var cOdom: DcMotor

    override lateinit var lFDrive: DcMotor
    override lateinit var rFDrive: DcMotor
    override lateinit var lBDrive: DcMotor
    override lateinit var rBDrive: DcMotor
    lateinit var collector: DcMotor
    lateinit var shooter: DcMotorEx

    lateinit var lift1: Servo
    lateinit var lift2: Servo
    lateinit var gate: Servo

    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {
        hwMap = ahwMap

//        ODOMETRY
        lOdom = hwMap.dcMotor.get("lFDrive")
        cOdom = hwMap.dcMotor.get("rFDrive")
        rOdom = hwMap.dcMotor.get("lBDrive")

//        DRIVE TRAIN
        lFDrive = hwMap.get("lFDrive") as DcMotor
        rFDrive = hwMap.get("rFDrive") as DcMotor
        lBDrive = hwMap.get("lBDrive") as DcMotor
        rBDrive = hwMap.get("rBDrive") as DcMotor

        rFDrive.direction = DcMotorSimple.Direction.FORWARD
        lFDrive.direction = DcMotorSimple.Direction.REVERSE
        rBDrive.direction = DcMotorSimple.Direction.FORWARD
        lBDrive.direction = DcMotorSimple.Direction.REVERSE

        rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER

        rFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

//        SHOOTER
        shooter = hwMap.get("shooter") as DcMotorEx

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        COLLECTOR
        collector = hwMap.get("collector") as DcMotor

        collector.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        collector.direction = DcMotorSimple.Direction.FORWARD

    }
}