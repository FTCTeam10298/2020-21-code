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

    var toggleState: Boolean = false
    fun toggleButton(button: Boolean): Boolean {

        if (button && stateChanged(button)) // only fire event on button down
            toggleState = !toggleState //invert


        return toggleState
    }

//    fun hi() {
//        when (toggleButton(gamepad.x)) {
//            true -> println("button toggle 1")
//            false -> println("button toggle 2")
//        }
//    }

}


