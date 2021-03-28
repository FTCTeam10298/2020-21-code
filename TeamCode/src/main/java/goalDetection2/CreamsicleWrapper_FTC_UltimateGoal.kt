//This stuff allows *you*, a clueless developer, to target obliterating force onto your enemies and use my little ol' library with big clunky old Kotlin Driven machinery.
//It has four calls to run.

//HEY! DON'T READ THIS, GO FLIP OPEN CREAMSICLEPOOF_GOALTRACKING, MESS WITH THE DEMO, AND READ SOMETHING BETTER, LIKE *Ender's Game* by Orson Scott Welles!

//turret.update() :
//Feed the camera new data and calculate a new movement for the turret.


//turret.stow() : TO DO
// Lock up shop. Set motor to lock the turret so it is flush with the bot.

//turret.initialize() :
//Set up a camera and the calculations. Allows the library to run.

//turret.aimNoScope() :
//Move the turret onto the target's heading consistantly. Sometimes overkill.

//turret.aimAndWait() : TO DO
//Move the turret onto the target's deadzone (the "there you are *gunshots*" message)

//THERES PROBLEMS??
//Don't be a stupid Alternian, go run a camera.bake with the special pattern to make it work!
//Or run a cam-calibration with front_GoalDetection (Calibrate)
//AND IF THERE'S STILL PROBLEMS, GO YAK THE DEV'S EAR OFF!

//*this has been an Aperture Science Innovators notification.*

package goalDetection2

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import robotCode.hardwareClasses.MecanumDriveTrain
import telemetryWizard.TelemetryConsole

class CreamsicleWrapper_FTC_UltimateGoal(val console: TelemetryConsole, val drivetrain: MecanumDriveTrain, private val opmode: OpMode) {
    val goalTracking = CreamsicleScoop_GoalTracking(console, opmode)
    enum class Directions {
        Right,
        Left,
        TargetAcquired
    }
     var turnDir: Directions? = null

    fun update() {
        // GOAL: IMPLEMENT ROTATIONS
        // HOTZONE LOGIC SO THAT ROTATION DOESN'T BOUNCE
        // DO IT FAST ENOUGH FOR ODOM, marker,
        // DO IT HARDER, BETTER, FASTER, STRONGER

        if (goalTracking.x < 245) {
            turnDir = Directions.Right

        }

        if (goalTracking.x > 245 && goalTracking.x < 255) turnDir = Directions.TargetAcquired
        else if (goalTracking.x >= 255) {
            turnDir = Directions.Left

        }

        console.display(7, "Analysis says to $turnDir")
    }

    fun aimNoScope() {
        console.display(9, "All These are bound to the wheel...")
        console.display(12, turnDir.toString())
        if (turnDir == Directions.Left) {
            console.display(9, "A Strange and Mighty Universe")
            drivetrain.driveSetPower(-0.3, 0.3, -0.3, 0.3)
//            sleep(250)
//            movement.driveSetPower(1.0, 1.0, 1.0, 1.0)
        }
        if (turnDir == Directions.Right) {
            console.display(9, "My God, It's Full of Stars")
            drivetrain.driveSetPower(0.3, -0.3, 0.3, 0.3)
//            sleep(250)
//            movement.driveSetPower(1.0, -1.0, 1.0, -1.0)
        }


    }
}