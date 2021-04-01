package robotCode

import com.qualcomm.robotcore.hardware.*
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
    lateinit var turret: DcMotor

    lateinit var lift1: Servo
    lateinit var lift2: Servo
    lateinit var roller: CRServo
    lateinit var claw1: Servo
    lateinit var claw2: Servo

    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {
        hwMap = ahwMap

        // ODOMETRY
        lOdom = hwMap.dcMotor.get("left collector")
        cOdom = hwMap.dcMotor.get("tape")
        rOdom = hwMap.dcMotor.get("left drive b")

        lOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        cOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        cOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

//        DRIVE TRAIN
        lFDrive = hwMap.get("left drive f") as DcMotor
        lBDrive = hwMap.get("left drive b") as DcMotor
        rFDrive = hwMap.get("right drive f") as DcMotor
        rBDrive = hwMap.get("right drive b") as DcMotor

        rFDrive.direction = DcMotorSimple.Direction.FORWARD
        lFDrive.direction = DcMotorSimple.Direction.REVERSE
        rBDrive.direction = DcMotorSimple.Direction.FORWARD
        lBDrive.direction = DcMotorSimple.Direction.REVERSE

        rFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

//        SHOOTER
        shooter = hwMap["shooter"] as DcMotorEx

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        TURRET
        turret = hwMap["turret"] as DcMotor

        turret.direction = DcMotorSimple.Direction.FORWARD
        turret.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        turret.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        COLLECTOR
        collector = hwMap["collector"] as DcMotor

        collector.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        collector.direction = DcMotorSimple.Direction.FORWARD
        collector.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

//        WOBBLE
        wobble = hwMap["wobble"] as DcMotor

        wobble.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        wobble.direction = DcMotorSimple.Direction.FORWARD
        wobble.mode = DcMotor.RunMode.RUN_USING_ENCODER

//        LIFT
        lift1 = hwMap["lift1"] as Servo

        lift1.direction = Servo.Direction.REVERSE
        lift1.position = 0.0

        lift2 = hwMap["lift1"] as Servo

        lift2.direction = Servo.Direction.REVERSE
        lift2.position = 0.0

//        SERVOS
        claw1 = hwMap["claw1"] as Servo
        claw2 = hwMap["claw2"] as Servo
        roller = hwMap["roller"] as CRServo

        claw1.direction = Servo.Direction.FORWARD
        claw2.direction = Servo.Direction.REVERSE
        roller.direction = DcMotorSimple.Direction.FORWARD

        claw1.position = 1.0
        claw2.position = 1.0
        roller.power = 0.0

    }
}