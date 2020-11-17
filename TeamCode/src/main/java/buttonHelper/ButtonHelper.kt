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

    fun toggle(button: Boolean): Boolean {
        return stateChanged(button) && button
    }
}


