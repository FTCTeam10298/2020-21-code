package robotCode.aimBotRobot

import com.qualcomm.robotcore.hardware.DcMotor
import robotCode.AimBotHardware

open class MecanumDriveTrain() {

    val robot = AimBotHardware()

    /**
     * DrivePowerAll sets all of the drive train motors to the specified power level.
     * @param power Power level to set all motors to
     */
    open fun DrivePowerAll(power: Double) {
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
        robot.lFDrive.power = lFPower
        robot.rFDrive.power = rFPower
        robot.lBDrive.power = lBPower
        robot.rBDrive.power = rBPower
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified mode.
     * @param runmode RunMode to set motors to
     */
    open fun driveSetMode(runmode: DcMotor.RunMode?) {
        robot.lFDrive.mode = runmode
        robot.rFDrive.mode = runmode
        robot.lBDrive.mode = runmode
        robot.rBDrive.mode = runmode
    }

    open fun driveSetRunToPosition() {
        if (robot.lFDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || robot.rFDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || robot.lBDrive.mode != DcMotor.RunMode.RUN_TO_POSITION || robot.rBDrive.mode != DcMotor.RunMode.RUN_TO_POSITION) {
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
        robot.lFDrive.targetPosition = lFPosition
        robot.rFDrive.targetPosition = rFPosition
        robot.lBDrive.targetPosition = lBPosition
        robot.rBDrive.targetPosition = rBPosition
    }

    open fun driveAddTargetPosition(lFPosition: Int, rFPosition: Int, lBPosition: Int, rBPosition: Int) {
        robot.lFDrive.targetPosition = robot.lFDrive.targetPosition + lFPosition
        robot.rFDrive.targetPosition = robot.rFDrive.targetPosition + rFPosition
        robot.lBDrive.targetPosition = robot.lBDrive.targetPosition + lBPosition
        robot.rBDrive.targetPosition = robot.rBDrive.targetPosition + rBPosition
    }

    open fun driveAllAreBusy(): Boolean {
        return (robot.lFDrive.isBusy && robot.rFDrive.isBusy && robot.lBDrive.isBusy && robot.rBDrive.isBusy)
    }

}