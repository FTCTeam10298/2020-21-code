package robotCode

import com.qualcomm.robotcore.hardware.*
import robotCode.hardwareClasses.MecanumHardware

/*  CURRENT HARDWARE MAP:
*   Expansion hub 2:
*       motors
*           lFDrive
*           rFDrive
*           lBDrive
*           rBDrive
*       servos
*           gate
*           rClaw
*           lClaw
*    Expansion hub 3:
*       motors
*           shooter
*           belt
*           collector
*           wobbleArm
*       servos
*
*/

open class AimBotHardware: MecanumHardware {

//    MOTORS
    override lateinit var lFDrive: DcMotor
    override lateinit var rFDrive: DcMotor
    override lateinit var lBDrive: DcMotor
    override lateinit var rBDrive: DcMotor

    lateinit var shooter: DcMotorEx
    lateinit var belt: DcMotor
    lateinit var collector: DcMotor
    lateinit var wobbleArm: DcMotor

//    SERVOS
    lateinit var gate: Servo
    lateinit var lClaw: Servo
    lateinit var rClaw: Servo

//    HARDWARE MAP
    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

//        Claw
        lClaw = hwMap.get("lClaw") as Servo
        rClaw = hwMap.get("rClaw") as Servo
        lClaw.direction = Servo.Direction.FORWARD
        rClaw.direction = Servo.Direction.REVERSE
        lClaw.position = 1.0
        rClaw.position = 1.0

//        Gate
        gate = hwMap.get("gate") as Servo
        gate.direction = Servo.Direction.REVERSE
        gate.position = 0.0

//        SHOOTER
        shooter = hwMap.get("shooter") as DcMotorEx

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        BELT
        belt = hwMap.get("belt") as DcMotor

        belt.direction = DcMotorSimple.Direction.FORWARD
        belt.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        belt.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        COLLECTOR
        collector = hwMap.get("collector") as DcMotor

        collector.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        collector.direction = DcMotorSimple.Direction.FORWARD

//        WOBBLE ARM
        wobbleArm = hwMap.get("wobbleArm") as DcMotor
        wobbleArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

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

    }
}