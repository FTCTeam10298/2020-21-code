package robotCode.hardwareClasses

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import locationTracking.Coordinate
import locationTracking.GlobalRobot
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import pid.PID
import telemetryWizard.TelemetryConsole
import kotlin.math.PI
import kotlin.math.abs

class EncoderDriveMovement(private val hardware: MecanumHardware, private val console: TelemetryConsole): MecanumDriveTrain(hardware), DriveMovement {

    lateinit var rangeSensor: ModernRoboticsI2cRangeSensor

    val COUNTS_PER_MOTOR_REV = 28.0 // Rev HD Hex v2.1 Motor encoder
    val GEARBOX_RATIO = 19.2 // 40 for 40:1, 20 for 20:1
    val DRIVE_GEAR_REDUCTION = 1 / 1 // This is > 1.0 if geared for torque
    val WHEEL_DIAMETER_INCHES = 3.77953 // For figuring circumference
    val DRIVETRAIN_ERROR = 1.015 // Error determined from testing
    val COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV * GEARBOX_RATIO * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES * PI) / DRIVETRAIN_ERROR
    val COUNTS_PER_DEGREE: Double = COUNTS_PER_INCH * 0.268 + 0.0 // Found by testing

    /**
     * DriveRobotTime drives the robot the set number of inches at the given power level.
     * @param ms How long to drive
     * @param power Power level to set motors to, negative will drive the robot backwards
     */
    override fun driveRobotTime(ms: Int, power: Double) {
        driveSetMode(DcMotor.RunMode.RUN_USING_ENCODER)
        drivePowerAll(power)
        Thread.sleep(ms.toLong())
        drivePowerAll(0.0)
        driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotDistanceToObject drives the robot to the set number of inches from an object
     * (usually the wall) at the given power level.
     * @param inches How many inches away to the object to go to
     * @param power Power level to set motors to
     */
    override fun driveRobotDistanceToObject(power: Double, inches: Double, smartAccel: Boolean) {
        val target = rangeSensor.getDistance(DistanceUnit.INCH).toFloat() - inches // FIXME: how accurate is sensor?
        console.display(10, "Range Sensor: ${rangeSensor.getDistance(DistanceUnit.INCH)}")
        driveRobotPosition(abs(power), target, smartAccel) // Use abs() to make sure power is positive
    }


    /**
     * DriveRobotPosition drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    override fun driveRobotPosition(power: Double, inches: Double, smartAccel: Boolean) {

        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL
        val position: Double = inches * COUNTS_PER_INCH


        if (smartAccel && power > 0.25) {
            drivePowerAll(0.25) // Use abs() to make sure power is positive
            state = 1 // ACCEL
        } else {
            drivePowerAll(abs(power)) // Use abs() to make sure power is positive
        }
        val flOrigTarget: Int = hardware.lFDrive.targetPosition
        val frOrigTarget: Int = hardware.rFDrive.targetPosition
        val blOrigTarget: Int = hardware.lBDrive.targetPosition
        val brOrigTarget: Int = hardware.rBDrive.targetPosition
        driveSetRunToPosition()
        driveAddTargetPosition(position.toInt(), position.toInt(), position.toInt(), position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                val flDrive: Int = hardware.lFDrive.currentPosition
                val frDrive: Int = hardware.rFDrive.currentPosition
                val blDrive: Int = hardware.lBDrive.currentPosition
                val brDrive: Int = hardware.rBDrive.currentPosition
                console.display(3, "Front left encoder: $flDrive")
                console.display(4, "Front right encoder: $frDrive")
                console.display(5, "Back left encoder: $blDrive")
                console.display(6, "Back right encoder $brDrive")
                console.display(7, "Front left target: ${hardware.lFDrive.targetPosition}")
                console.display(8, "Front right target: ${hardware.rFDrive.targetPosition}")
                console.display(9, "Back left target: ${hardware.lBDrive.targetPosition}")
                console.display(10, "Back right target ${hardware.rBDrive.targetPosition}")

                // State magic
                if (state == 1 &&
                        (abs(flDrive - flOrigTarget) > 2 * COUNTS_PER_INCH || abs(frDrive - frOrigTarget) > 2 * COUNTS_PER_INCH || abs(blDrive - blOrigTarget) > 2 * COUNTS_PER_INCH || abs(brDrive - brOrigTarget) > 2 * COUNTS_PER_INCH)) {
                    // We have gone 2 inches, go to full power
                    drivePowerAll(abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(frDrive - frOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(blDrive - blOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(brDrive - brOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2))) {
                    // Cut power by half to DECEL
                    drivePowerAll(abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
                console.display(7, "State: $state (0=NONE,1=ACCEL,2=DRIVING,3=DECEL")
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
        // Clear used section of dashboard
        console.display(3, "")
        console.display(4, "")
        console.display(5, "")
        console.display(6, "")
        console.display(7, "")
    }


    override fun driveRobotTurn(power: Double, degree: Double, smartAccel: Boolean) {

        val position: Double = degree * COUNTS_PER_DEGREE
        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL

        driveSetRunToPosition()
        if (smartAccel) {
            state = 1
            driveSetPower(power * 0.5, -power * 0.5, power * 0.5, -power * 0.5)
        } else {
            driveSetPower(power, -power, power, -power)
        }
        val flOrigTarget: Int = hardware.lFDrive.targetPosition
        val frOrigTarget: Int = hardware.rFDrive.targetPosition
        val blOrigTarget: Int = hardware.lBDrive.targetPosition
        val brOrigTarget: Int = hardware.rBDrive.targetPosition
        driveAddTargetPosition(position.toInt(), -position.toInt(), position.toInt(), -position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                val flDrive: Int = hardware.lFDrive.currentPosition
                val frDrive: Int = hardware.rFDrive.currentPosition
                val blDrive: Int = hardware.lBDrive.currentPosition
                val brDrive: Int = hardware.rBDrive.currentPosition
                console.display(3, "Front left encoder: $flDrive")
                console.display(4, "Front right encoder: $frDrive")
                console.display(5, "Back left encoder: $blDrive")
                console.display(6, "Back right encoder: $brDrive")

                // State magic
                if (state == 1 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(frDrive - frOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(blDrive - blOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(brDrive - brOrigTarget) > COUNTS_PER_DEGREE * 10)) {
                    // We have rotated 10 degrees, go to full power
                    drivePowerAll(abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(frDrive - frOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(blDrive - blOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(brDrive - brOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10))) {
                    // We are within 10 degrees of our destination, cut power by half to DECEL
                    drivePowerAll(abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
                console.display(7, "State: $state (0=NONE,1=ACCEL,2=DRIVING,3=DECEL")
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
        // Clear used section of dashboard
        console.display(3, "")
        console.display(4, "")
        console.display(5, "")
        console.display(6, "")
        console.display(7, "")
    }

    /**
     * DriveRobotStrafe drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    override fun driveRobotStrafe(power: Double, inches: Double, smartAccel: Boolean) {

        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL
        val position: Double = inches * COUNTS_PER_INCH

        if (smartAccel && power > 0.25) {
            drivePowerAll(0.25) // Use abs() to make sure power is positive
            state = 1 // ACCEL
        } else {
            drivePowerAll(abs(power)) // Use abs() to make sure power is positive
        }
        val flOrigTarget: Int = hardware.lFDrive.targetPosition
        val frOrigTarget: Int = hardware.rFDrive.targetPosition
        val blOrigTarget: Int = hardware.lBDrive.targetPosition
        val brOrigTarget: Int = hardware.rBDrive.targetPosition

        driveSetRunToPosition()
        driveAddTargetPosition(-position.toInt(), position.toInt(), position.toInt(), -position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                val flDrive: Int = hardware.lFDrive.currentPosition
                val frDrive: Int = hardware.rFDrive.currentPosition
                val blDrive: Int = hardware.lBDrive.currentPosition
                val brDrive: Int = hardware.rBDrive.currentPosition
                console.display(3, "Front left encoder: $flDrive")
                console.display(4, "Front right encoder: $frDrive")
                console.display(5, "Back left encoder: $blDrive")
                console.display(6, "Back right encoder $brDrive")
                console.display(7, "Front left target: ${hardware.lFDrive.targetPosition}")
                console.display(8, "Front right target: ${hardware.rFDrive.targetPosition}")
                console.display(9, "Back left target: ${hardware.lBDrive.targetPosition}")
                console.display(10, "Back right target ${hardware.rBDrive.targetPosition}")

                // State magic
                if (state == 1 &&
                        (abs(flDrive - flOrigTarget) > 2 * COUNTS_PER_INCH || abs(frDrive - frOrigTarget) > 2 * COUNTS_PER_INCH || abs(blDrive - blOrigTarget) > 2 * COUNTS_PER_INCH || abs(brDrive - brOrigTarget) > 2 * COUNTS_PER_INCH)) {
                    // We have gone 2 inches, go to full power
                    drivePowerAll(abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(frDrive - frOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(blDrive - blOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(brDrive - brOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2))) {
                    // Cut power by half to DECEL
                    drivePowerAll(abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
                console.display(7, "State: $state (0=NONE,1=ACCEL,2=DRIVING,3=DECEL")
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
        // Clear used section of dashboard
        console.display(3, "")
        console.display(4, "")
        console.display(5, "")
        console.display(6, "")
        console.display(7, "")
    }

    /**
     * DriveSidewaysTime makes the robot drive sideways for the specified time and power.
     * @param time How long to drive in seconds
     * @param power The power to use while driving,
     * positive values go right and negative values go left
     */
    override fun driveSidewaysTime(time: Double, power: Double) {
        driveSetMode(DcMotor.RunMode.RUN_USING_ENCODER)
        driveSetPower(-power, power, power, -power)

        // Continue driving for the specified amount of time, then stop
        val ms = time * 1000
        Thread.sleep(ms.toLong())
        drivePowerAll(0.0)
        driveSetRunToPosition()
        driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotHug is used to make the robot drive hugging a wall.
     * The robot will move mostly straight and slightly to the side,
     * so it will stay against the wall.
     * @param power Power to use while driving
     * @param inches How many inches to drive
     * @param hugLeft Whether to hug left or right
     */
    override fun driveRobotHug(power: Double, inches: Int, hugLeft: Boolean) {

        val position: Double = inches * COUNTS_PER_INCH

        driveSetRunToPosition()
        if (!hugLeft && inches > 0 || hugLeft && inches < 0) {
            driveSetPower(power * .5, power, power, power * .5)
            driveAddTargetPosition(position.toInt() / 2, position.toInt(), position.toInt(), position.toInt() / 2)
        } else if (!hugLeft && inches < 0 || hugLeft && inches > 0) {
            driveSetPower(power, power * .5, power * .5, power)
            driveAddTargetPosition(position.toInt(), position.toInt() / 2, position.toInt() / 2, position.toInt())
        }

        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                console.display(3, "Left front encoder: ${hardware.lFDrive.currentPosition}")
                console.display(4, "Right front encoder: ${hardware.rFDrive.currentPosition}")
                console.display(5, "Left back encoder: ${hardware.lBDrive.currentPosition}")
                console.display(6, "Right back encoder: ${hardware.rBDrive.currentPosition}")
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
    }

    override fun driveRobotArc(power: Double, inches: Double, difference: Double) {

        var difference = difference
        val position: Double = inches * COUNTS_PER_INCH

        difference = Range.clip(difference, -1.0, 1.0)
        //power 1, inches -48, difference -.5
        driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        if (difference > 0) {
            hardware.rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.lFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.lBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
        } else {
            hardware.rFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.rBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.lFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        if (difference > 0 && inches > 0)
            driveSetPower(abs(power), abs(power * difference), abs(power), abs(power * difference))
        else if (difference > 0 && inches < 0)
            driveSetPower(abs(power), -abs(power * difference), abs(power), -abs(power * difference))
        else if (difference < 0 && inches > 0)
            driveSetPower(abs(power * difference), abs(power), abs(power * difference), abs(power))
        else if (difference < 0 && inches < 0)
            driveSetPower(-abs(power * difference), abs(power), -abs(power * difference), abs(power))

        if (difference > 0) {
            hardware.lFDrive.targetPosition = position.toInt()
            hardware.lBDrive.targetPosition = position.toInt()
        } else {
            hardware.rFDrive.targetPosition = position.toInt()
            hardware.rBDrive.targetPosition = position.toInt()
        }
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            if (difference > 0) {
                while (hardware.lFDrive.isBusy && hardware.lBDrive.isBusy) {
                    val flDrive: Int = hardware.lFDrive.currentPosition
                    val blDrive: Int = hardware.lBDrive.currentPosition
                    console.display(3, "Front left encoder: $flDrive")
                    console.display(4, "Back left encoder: $blDrive")
                }
            } else {
                while (hardware.rFDrive.isBusy && hardware.rBDrive.isBusy) {
                    val frDrive: Int = hardware.rFDrive.currentPosition
                    val brDrive: Int = hardware.rBDrive.currentPosition
                    console.display(3, "Front left encoder: $frDrive")
                    console.display(4, "Back left encoder: $brDrive")
                }
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
    }

    override fun driveRobotArcStrafe(power: Double, inches: Double, difference: Double) {

        var difference = difference
        val position: Double = inches * COUNTS_PER_INCH

        difference = Range.clip(difference, -1.0, 1.0)
        //power 1, inches -48, difference -.5
        driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        if (difference > 0) {
            hardware.rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.lFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.lBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
        } else {
            hardware.rFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.rBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            hardware.lFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            hardware.lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        if (difference > 0 && inches > 0)
            driveSetPower(-abs(power), abs(power * difference), abs(power), -abs(power * difference))
        else if (difference > 0 && inches < 0)
            driveSetPower(-abs(power), -abs(power * difference), abs(power), abs(power * difference))
        else if (difference < 0 && inches > 0)
            driveSetPower(-abs(power * difference), abs(power), abs(power * difference), -abs(power))
        else if (difference < 0 && inches < 0)
            driveSetPower(abs(power * difference), abs(power), -abs(power * difference), -abs(power))

        if (difference > 0) {
            hardware.lFDrive.targetPosition = -position.toInt()
            hardware.lBDrive.targetPosition = position.toInt()
        } else {
            hardware.rFDrive.targetPosition = position.toInt()
            hardware.rBDrive.targetPosition = -position.toInt()
        }

        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            if (difference > 0) {
                while (hardware.lFDrive.isBusy && hardware.lBDrive.isBusy) {
                    val flDrive: Int = hardware.lFDrive.currentPosition
                    val blDrive: Int = hardware.lBDrive.currentPosition
                    console.display(3, "Front left encoder: $flDrive")
                    console.display(4, "Back left encoder: $blDrive")
                }
            } else {
                while (hardware.rFDrive.isBusy && hardware.rBDrive.isBusy) {
                    val frDrive: Int = hardware.rFDrive.currentPosition
                    val brDrive: Int = hardware.rBDrive.currentPosition
                    console.display(3, "Front left encoder: $frDrive")
                    console.display(4, "Back left encoder: $brDrive")
                }
            }
            Thread.sleep(10)
        }
        drivePowerAll(0.0)
    }

    override fun doGoToPosition(target: Coordinate, maxPower: Double, distancePIDX: PID, distancePIDY: PID, anglePID: PID, distanceMin: Double, angleDegMin: Double, reset: Boolean, opmode: LinearOpMode) {
        TODO("Not yet implemented")
    }

    private var robotCoordinate = GlobalRobot()

    override fun straightGoToPosition(target: Coordinate, maxPower: Double, distanceMin: Double, opmodeisactive: LinearOpMode) {

        driveRobotPosition((robotCoordinate.y - target.y), maxPower, true)
    }

    override fun turnGoToPosition(target: Coordinate, maxPower: Double, angleDegMin: Double, opmodeisactive: LinearOpMode) {
        TODO("Not yet implemented")
    }

}