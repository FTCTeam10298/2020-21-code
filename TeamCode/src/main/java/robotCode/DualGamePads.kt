package robotCode

import com.qualcomm.robotcore.hardware.Gamepad

open class DualGamePads(private val gamepad1: Gamepad, private val gamepad2: Gamepad) {

    enum class Mode {
        ReturnHigher,
        ReturnLower,
        ReturnAvg,
    }

    var mode: Mode = Mode.ReturnHigher

    val x: Boolean
        get() {
            return when (mode) {
                Mode.ReturnHigher -> returnHigher(gamepad1.x, gamepad2.x)
                Mode.ReturnLower -> returnLower(gamepad1.x, gamepad2.x)
                Mode.ReturnAvg -> returnAvg(gamepad1.x, gamepad2.x)
            }
        }

    val left_stick_button: Boolean
        get() {
            return when (mode) {
                Mode.ReturnHigher -> returnHigher(gamepad1.left_stick_button, gamepad2.left_stick_button)
                Mode.ReturnLower -> returnLower(gamepad1.left_stick_button, gamepad2.left_stick_button)
                Mode.ReturnAvg -> returnAvg(gamepad1.left_stick_button, gamepad2.left_stick_button)
            }
        }


//    Boolean returns
    fun returnHigher(gamepad1: Boolean, gamepad2: Boolean): Boolean {
        return if (gamepad1 != gamepad2)
            return true
        else gamepad1
    }

    fun returnLower(gamepad1: Boolean, gamepad2: Boolean): Boolean {
        return if (gamepad1 != gamepad2)
            return false
        else gamepad1
    }

    fun returnAvg(gamepad1: Boolean, gamepad2: Boolean): Boolean = returnHigher(gamepad1, gamepad2)


//    Double returns
    fun returnHigher(gamepad1: Double, gamepad2: Double): Double {
        return when {
            gamepad1 > gamepad2 -> gamepad1
            gamepad2 > gamepad1 -> gamepad2
            else -> gamepad1
        }
    }

    fun returnLower(gamepad1: Double, gamepad2: Double): Double {
        return when {
            gamepad1 < gamepad2 -> gamepad1
            gamepad2 < gamepad1 -> gamepad2
            else -> gamepad1
        }
    }

    fun returnAvg(gamepad1: Double, gamepad2: Double): Double = (gamepad1 / gamepad2) * 2

//    Int returns
//    fun returnHigher(gamepad1: Int, gamepad2: Int): Int {
//
//    }
}