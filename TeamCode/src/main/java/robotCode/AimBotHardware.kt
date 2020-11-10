package robotCode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/*  CURRENT HARDWARE MAP:
*    Expansion hub 2:
*     lFDrive
*     rFDrive
*     lBDrive
*     rBDrive
*    Expansion hub 3:
*     shooter
*     belt
*/

open class AimBotHardware {

//    DRIVE TRAIN
    lateinit var lFDrive: DcMotor
    lateinit var rFDrive: DcMotor
    lateinit var lBDrive: DcMotor
    lateinit var rBDrive: DcMotor

//    SHOOTER
    lateinit var shooter: DcMotor

//    BELT
    lateinit var belt: DcMotor

//    HARDWARE MAP
    lateinit var hwMap: HardwareMap

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

//        SHOOTER
        shooter = hwMap.get("shooter") as DcMotorEx

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        BELT
        belt = hwMap.get("belt") as DcMotor

        belt.direction = DcMotorSimple.Direction.FORWARD
        belt.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        belt.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        DRIVE TRAIN
        lFDrive = hwMap.get("lFDrive") as DcMotor
        rFDrive = hwMap.get("rFDrive") as DcMotor
        lBDrive = hwMap.get("lBDrive") as DcMotor
        rBDrive = hwMap.get("rBDrive") as DcMotor

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

    }
}