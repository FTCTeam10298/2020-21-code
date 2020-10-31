//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM (Branch Redacted)
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS


package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import jamesTelemetryMenu.TelemetryMenu

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val menu = TelemetryMenu(telemetry)
    val robot= AimBotMovement()

    fun abscondTime(Interval:Int, milis:Int) {

        for (i in (0 .. Interval)) {

            var ramp: Double = 1.1

            robot.lBDrive.power = ramp
            robot.lFDrive.power = ramp
            robot.rFDrive.power = ramp
            robot.rBDrive.power = ramp

            if (2 % ramp == 0.0) {
                ramp /= 2
            }

            sleep(milis.toLong())
            menu.display(1, "Are you still there?")
        }

    }

    fun tiptoeMotor(motorUsed:DcMotor, ticks:Int) {

        motorUsed.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed.mode = DcMotor.RunMode.RUN_TO_POSITION
        telemetry.addLine("] ${motorUsed} JOINS THE FIGHT!")
        telemetry.update()

        motorUsed.targetPosition = ticks
        while (motorUsed.isBusy) {
            motorUsed.power = 1.0
        }
        telemetry.addLine("] ${motorUsed} uses Spin sucessfully!")
        telemetry.update()
        motorUsed.power = 0.0



    }

    fun abscondCautiously(motorUsed1:DcMotor, motorUsed2:DcMotor, motorUsed3:DcMotor, motorUsed4:DcMotor,  ticks:Int) {

        motorUsed1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed1.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed2.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed3.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed3.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed4.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed4.mode = DcMotor.RunMode.RUN_TO_POSITION
        telemetry.addLine("] ${motorUsed1} JOINS THE FIGHT!")
        telemetry.update()

        motorUsed1.targetPosition = ticks
        motorUsed2.targetPosition = ticks
        motorUsed3.targetPosition = ticks
        motorUsed4.targetPosition = ticks

        while (motorUsed1.isBusy && motorUsed2.isBusy && motorUsed3.isBusy && motorUsed4.isBusy) {
            motorUsed1.power = 1.0
            motorUsed2.power = 1.0
            motorUsed3.power = 1.0
            motorUsed4.power = 1.0
        }
        telemetry.addLine("] ${motorUsed1} uses creepBeast sucessfully!")
        telemetry.update()
        motorUsed1.power = 0.0
        motorUsed2.power = 0.0
        motorUsed3.power = 0.0
        motorUsed4.power = 0.0



    }



    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.
//        abscondTime(50,60)

        abscondCautiously(robot.lFDrive, robot.rFDrive, robot.lBDrive, robot.rBDrive, 9000)

    }
}


