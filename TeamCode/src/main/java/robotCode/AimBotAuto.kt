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

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotMovement()

    fun abscondTime(Interval:Int, milis:Int) {

        for (i in (0 .. Interval)) {

            var ramp: Double = 1.1

            robot.lBDrive?.power = ramp
            robot.lFDrive?.power = ramp
            robot.rFDrive?.power = ramp
            robot.rBDrive?.power = ramp

            if (2 % ramp == 0.0) {
                ramp /= 2
            }

            sleep(milis.toLong())
            telemetry.addLine("Are you still there?")
        }

    }

    fun tiptoeMotor(motorUsed:DcMotor?, ticks:Int) {

        motorUsed?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed?.mode = DcMotor.RunMode.RUN_USING_ENCODER
        telemetry.addLine("] ${motorUsed} JOINS THE FIGHT!")
        telemetry.update()
        while (motorUsed!!.currentPosition < ticks) {
            motorUsed?.power = 1.0
        }
        telemetry.addLine("] ${motorUsed} uses Spin sucessfully!")
        telemetry.update()
        motorUsed?.power = 0.0



    }

    fun abscondCautiously(Interval:Int, milis:Int) {
        tiptoeMotor(robot.lFDrive, 2000)
        tiptoeMotor(robot.lBDrive, 2000)
        tiptoeMotor(robot.rFDrive, 2000)
        tiptoeMotor(robot.rBDrive, 2000)
    }


    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.
//        abscondTime(50,60)

        abscondCautiously(10298,10298)

    }
}
