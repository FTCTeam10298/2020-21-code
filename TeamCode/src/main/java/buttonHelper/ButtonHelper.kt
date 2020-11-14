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

    fun loopButton(button: Boolean): Boolean {
        return button
    }

    fun toggleButton(button: Boolean): Boolean {
        return stateChanged(button) && button
    }

//    fun hi() {
//        when (toggleButton(gamepad.x)) {
//            true -> println("button toggle 1")
//            false -> println("button toggle 2")
//        }
//    }

}


