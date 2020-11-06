package robotCode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.PI

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

//        SHOOTER
        shooter = hwMap.get("lFDrive") as DcMotor

        shooter.direction = DcMotorSimple.Direction.REVERSE
        shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        shooter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

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

open class AimBotHardwarePlus(): AimBotHardware() {




    /**
     * DrivePowerAll sets all of the drive train motors to the specified power level.
     * @param power Power level to set all motors to
     */
    open fun DrivePowerAll(power: Double) {
        driveSetPower(power, power, power, power)
    }

    /**
     * driveSetPower sets all of the drive train motors to the specified power levels.
     * @param flPower Power level to set front left motor to
     * @param frPower Power level to set front right motor to
     * @param blPower Power level to set back left motor to
     * @param brPower Power level to set back right motor to
     */
    open fun driveSetPower(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        lFDrive.power = flPower
        rFDrive.power = frPower
        lBDrive.power = blPower
        rBDrive.power = brPower
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified mode.
     * @param runmode RunMode to set motors to
     */
    open fun driveSetMode(runmode: DcMotor.RunMode?) {
        lFDrive.mode = runmode
        rFDrive.mode = runmode
        lBDrive.mode = runmode
        rBDrive.mode = runmode
    }

    open fun driveSetRunToPosition() {
        if (lFDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || rFDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || lBDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || rBDrive.mode != DcMotor.RunMode.RUN_TO_POSITION) {
            driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
            driveSetMode(DcMotor.RunMode.RUN_TO_POSITION)
            // When the encoder is reset, also reset the target position, so it doesn't add an old
            // target position when using driveAddTargetPosition().
            driveSetTargetPosition(0, 0, 0, 0)
        }
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified positions.
     * @param flPosition Position to set front left motor to run to
     * @param frPosition Position to set front right motor to run to
     * @param blPosition Position to set back left motor to run to
     * @param brPosition Position to set back right motor to run to
     */
    open fun driveSetTargetPosition(flPosition: Int, frPosition: Int, blPosition: Int, brPosition: Int) {
        lFDrive.targetPosition = flPosition
        rFDrive.targetPosition = frPosition
        lBDrive.targetPosition = blPosition
        rBDrive.targetPosition = brPosition
    }

    open fun driveAddTargetPosition(flPosition: Int, frPosition: Int, blPosition: Int, brPosition: Int) {
        lFDrive.targetPosition = lFDrive.targetPosition + flPosition
        rFDrive.targetPosition = rFDrive.targetPosition + frPosition
        lBDrive.targetPosition = lBDrive.targetPosition + blPosition
        rBDrive.targetPosition = rBDrive.targetPosition + brPosition
    }

    open fun driveAllAreBusy(): Boolean {
        return (lFDrive.isBusy && rFDrive.isBusy && lBDrive.isBusy && rBDrive.isBusy)
    }

}