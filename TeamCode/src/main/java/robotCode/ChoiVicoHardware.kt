package robotCode

import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import robotCode.hardwareClasses.MecOdometryHardware

class ChoiVicoHardware(): MecOdometryHardware {

    override lateinit var lOdom: DcMotor
    override lateinit var rOdom: DcMotor
    override lateinit var cOdom: DcMotor

    override lateinit var lFDrive: DcMotor
    override lateinit var rFDrive: DcMotor
    override lateinit var lBDrive: DcMotor
    override lateinit var rBDrive: DcMotor
    lateinit var collector: DcMotor
    lateinit var wobble: DcMotor
    lateinit var shooter: DcMotorEx

    lateinit var lift: Servo
    lateinit var gate: Servo
    lateinit var claw1: Servo
    lateinit var claw2: Servo

    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {
        hwMap = ahwMap

//        ODOMETRY
//        lOdom = hwMap.dcMotor["lFDrive"]
//        cOdom = hwMap.dcMotor["rFDrive"]
//        rOdom = hwMap.dcMotor["lBDrive"]

//        DRIVE TRAIN

        lFDrive = hwMap.get("left drive f") as DcMotor
        rFDrive = hwMap["right drive f"] as DcMotor
        lBDrive = hwMap["left drive b"] as DcMotor
        rBDrive = hwMap["right drive b"] as DcMotor

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
//        shooter = hwMap.get("shooter") as DcMotorEx
//
//        shooter.direction = DcMotorSimple.Direction.REVERSE
//        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
//        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
//
////        COLLECTOR
//        collector = hwMap["collector"] as DcMotor
//
//        collector.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        collector.direction = DcMotorSimple.Direction.FORWARD
//        collector.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//
////        WOBBLE
//        wobble = hwMap["wobble"] as DcMotor
//
//        wobble.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        wobble.direction = DcMotorSimple.Direction.FORWARD
//        wobble.mode = DcMotor.RunMode.RUN_USING_ENCODER
//
////        SERVOS
//        lift = hwMap["lift1"] as Servo
//        claw1 = hwMap["claw1"] as Servo
//        claw2 = hwMap["claw2"] as Servo
//        gate = hwMap["gate"] as Servo
//
//        lift.direction = Servo.Direction.REVERSE
//        claw1.direction = Servo.Direction.FORWARD
//        claw2.direction = Servo.Direction.REVERSE
//        gate.direction = Servo.Direction.REVERSE
//
//        lift.position = 0.0
//        claw1.position = 1.0
//        claw2.position = 1.0
//        gate.position = 0.1

    }
}