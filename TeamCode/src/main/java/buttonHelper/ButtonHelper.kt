package buttonHelper

class ButtonHelper() {
//var name: Int = 0
//    val gamepad: Gamepad = Gamepad()

    var buttonPreviousValue = false
    fun stateChanged(button: Boolean):Boolean {
        val re: Boolean = buttonPreviousValue != button
        buttonPreviousValue = button
        return re
    }

    var stickPreviousValue = 0.0
    fun stateChanged(button: Double): Boolean {
        return if (stickPreviousValue > 0.0 && button > 0.0) {
            false
        } else {
            val re: Boolean = stickPreviousValue != button
            stickPreviousValue = button
            re
        }

    }

    fun toggle(button: Boolean): Boolean {
        return stateChanged(button) && button
    }
}


