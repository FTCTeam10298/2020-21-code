package buttonHelper

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1

interface Button {
    fun isPressed():Boolean
    fun onPress(listener:()->Unit)
}

interface Analog {
    fun d(): Float
}

interface Dpad {
    fun up(): Button
    fun down(): Button
    fun left(): Button
    fun right(): Button
}

interface Stick {
    fun x(): Analog
    fun y(): Analog
    fun button(): Button
}

interface Gamepad {
    fun leftStick(): Stick
    fun rightStick(): Stick
    fun leftTrigger() : Analog
    fun rightTrigger() : Analog
    fun x(): Button
    fun y(): Button
    fun a(): Button
    fun b(): Button
}

abstract class Gamepad1(): Gamepad {

//    STICKS
    abstract class leftStick: Stick {
        class x: Analog {
            override fun d(): Float {
                TODO("Not yet implemented")
            }
        }

        class y: Analog {
            override fun d(): Float {
                TODO("Not yet implemented")
            }
        }

        class button: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }
    }

    abstract class rightStick: Stick {
        class x: Analog {
            override fun d(): Float {
                TODO("Not yet implemented")
            }
        }

        class y: Analog {
            override fun d(): Float {
                TODO("Not yet implemented")
            }
        }

        class button: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }
    }

//    DPAD
    abstract class dpad: Dpad {
        class up: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }

        class down: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }

        class left: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }

        class right: Button {
            override fun isPressed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun onPress(listener: () -> Unit) {
                TODO("Not yet implemented")
            }
        }
    }

//    TRIGGERS
    class leftTrigger: Analog {
        override fun d(): Float {
            TODO("Not yet implemented")
        }
    }

    class rightTrigger: Analog {
        override fun d(): Float {
            TODO("Not yet implemented")
        }
    }

//    BUTTONS
    class x: Button {
        override fun isPressed(): Boolean {
            TODO("Not yet implemented")
        }

        override fun onPress(listener: () -> Unit) {
            TODO("Not yet implemented")
        }
    }
}

class Toggle(private val initialActive: Boolean, private val button: Button){
    private var active = initialActive;
    init{
        button.onPress{
            this.active = !this.active
        }
    }
    fun isActive():Boolean = active
}

class IntermittentSwitch(private val button:Button){
    fun isActive():Boolean = button.isPressed()
}

class RadioButtonSet<T>(private val buttons:List<Pair<Button, T>>){
    fun currentMode():T? = null
}


enum class ShooterMode {
    AUTO_SHOOTER,
    MANUAL,
    HYBRID
}

// inside loop

val directionToggle = Toggle(initialActive = false, gamepad1.x)
val fireButton = IntermittentSwitch(initialActive = false, gamepad1.y)

val shooterMode = RadioButtonSet(listOf(Pair(gamepad1.x, ShooterMode.AUTO_SHOOTER),
        Pair(gamepad1. x, ShooterMode.AUTO_SHOOTER)))

when(shooterMode.currentMode()){
    ShooterMode.AUTO_SHOOTER ->

}