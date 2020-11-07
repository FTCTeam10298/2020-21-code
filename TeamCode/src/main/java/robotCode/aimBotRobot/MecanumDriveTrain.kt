package robotCode.aimBotRobot

import com.qualcomm.robotcore.hardware.DcMotor
import robotCode.AimBotHardware

open class MecanumDriveTrain(): AimBotHardware() {

    /**
     * DrivePowerAll sets all of the drive train motors to the specified power level.
     * @param power Power level to set all motors to
     */
    open fun drivePowerAll(power: Double) {
        driveSetPower(power, power, power, power)
    }

    /**
     * driveSetPower sets all of the drive train motors to the specified power levels.
     * @param lFPower Power level to set front left motor to
     * @param rFPower Power level to set front right motor to
     * @param lBPower Power level to set back left motor to
     * @param rBPower Power level to set back right motor to
     */
    open fun driveSetPower(lFPower: Double, rFPower: Double, lBPower: Double, rBPower: Double) {
        lFDrive.power = lFPower
        rFDrive.power = rFPower
        lBDrive.power = lBPower
        rBDrive.power = rBPower
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
     * @param lFPosition Position to set front left motor to run to
     * @param rFPosition Position to set front right motor to run to
     * @param lBPosition Position to set back left motor to run to
     * @param rBPosition Position to set back right motor to run to
     */
    open fun driveSetTargetPosition(lFPosition: Int, rFPosition: Int, lBPosition: Int, rBPosition: Int) {
        lFDrive.targetPosition = lFPosition
        rFDrive.targetPosition = rFPosition
        lBDrive.targetPosition = lBPosition
        rBDrive.targetPosition = rBPosition
    }

    open fun driveAddTargetPosition(lFPosition: Int, rFPosition: Int, lBPosition: Int, rBPosition: Int) {
        lFDrive.targetPosition = lFDrive.targetPosition + lFPosition
        rFDrive.targetPosition = rFDrive.targetPosition + rFPosition
        lBDrive.targetPosition = lBDrive.targetPosition + lBPosition
        rBDrive.targetPosition = rBDrive.targetPosition + rBPosition
    }

    open fun driveAllAreBusy(): Boolean {
        return (lFDrive.isBusy && rFDrive.isBusy && lBDrive.isBusy && rBDrive.isBusy)
    }

}