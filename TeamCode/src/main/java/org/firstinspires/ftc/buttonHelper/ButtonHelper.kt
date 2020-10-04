package buttonHelper

class ButtonHelper() {

//    val gamepad: Gamepad = Gamepad()

    var buttonPreviousValue = false
    fun isStateChanged(button: Boolean): Boolean {

        if (buttonPreviousValue != button) {
            /*state has changed*/
            buttonPreviousValue = button
            return true
        }
        return false
    }

    var buttonDown = false
    fun loopButton(button: Boolean): Boolean {

        if (isStateChanged(button)) /* button state has changed */ {
            if (button) /* only fire event on button press, not release */
                buttonDown = !buttonDown /*invert*/
        }

        return buttonDown
    }

    var toggleState: Boolean = false
    fun toggleButton(button: Boolean): Boolean {


        if (isStateChanged(button)) { // button state has changed
            if (button) // only fire event on button down
                toggleState = !toggleState //invert
        }

        return toggleState
    }

//    fun hi() {
//        when (toggleButton(gamepad.x)) {
//            true -> println("button toggle 1")
//            false -> println("button toggle 2")
//        }
//    }

}


