package buttonHelper

//class GamepadManeger {
//    var gamepad1Fields: List<Map<String, Any>> = listOf(mapOf())
//    var gamepad2Fields: List<Map<String, Any>> = listOf(mapOf())
//
//    fun init(gamepad1: Gamepad, gamepad2: Gamepad) {
//        gamepad1::javaClass.get().fields.map{field ->
//            gamepad1Fields += mapOf(field.name to field.type)
//        }
//        gamepad1Fields.filter{ it[2] == Float || it[2] == Boolean}
//
//        gamepad2::javaClass.get().fields.map{field ->
//            gamepad2Fields += mapOf(field.name to field.type)
//        }
//        gamepad2Fields.filter{ it[2] == Float || it[2] == Boolean}
//    }
//
//    fun stateChanged(button: KProperty<Boolean>) {
//        val gamepad = button::javaClass.get()
//
//
//        buttonHelpers += ButtonHelper(button)
//    }
//}

class ButtonHelper {

    var buttonPreviousValue = false
    fun stateChanged(currentValue: Boolean):Boolean {
        val re: Boolean = buttonPreviousValue != currentValue
        buttonPreviousValue = currentValue
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

class JoystickHelper {

}


fun fieldLister(o:Any):Map<String, *> {
    return o::javaClass.get().fields.map{field ->
        println("val button${field.name} = ButtonHelper(gamepad.${field.name})")
        Pair(field.name, field.get(o))
    }.toMap()
}