package robotCode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/*  CURRENT HARDWARE MAP:
*    Expansion hub 2:
*     lFDrive
*     rFDrive
*     lBDrive
*     rBDrive
*    Expansion hub 3:
*     none
* */


open class AimBotHardware {

//    DRIVE TRAIN
    lateinit var rFDrive: DcMotor
    lateinit var lFDrive: DcMotor
    lateinit var rBDrive: DcMotor
    lateinit var lBDrive: DcMotor

//    SHOOTER
    lateinit var shooter: DcMotor

//    HARDWARE MAP
    lateinit var hwMap: HardwareMap

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

//        DRIVE TRAIN
        lFDrive = hwMap.get("lFDrive") as DcMotor
        rFDrive = hwMap.get("rFDrive") as DcMotor
        lBDrive = hwMap.get("lBDrive") as DcMotor
        rBDrive = hwMap.get("rBDrive") as DcMotor

        rFDrive.direction = DcMotorSimple.Direction.REVERSE
        lFDrive.direction = DcMotorSimple.Direction.FORWARD
        rBDrive.direction = DcMotorSimple.Direction.FORWARD
        lBDrive.direction = DcMotorSimple.Direction.REVERSE

        rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER

        rFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

//        SHOOTER
        shooter = hwMap.get("lFDrive") as DcMotor

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }
}