package jamesTelemetryMenu

//import com.qualcomm.robotcore.hardware.Gamepad
//import bstormz.hal.sim.IGamePad
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryMenu(telemetry: Telemetry): ChoiceManager() {
    /*What this menu system asserts:
        The cursor starts on line 1
        */

//    Declare variables
    private var cursorLine= 4
    private var lastUserLine= 0
    private var telemetry: Telemetry = telemetry
    private var queue: MutableList<String> = mutableListOf()
    private var menuDone= false
    private var keyDown= true


//    Choice related-------------------------------------

    private fun find(options:List<option>, valueToFind: String) = options.filter{option-> option.choice == valueToFind}

    private fun getMenu(link: List<link>, choice: String) = link.filter{option -> option.choice == choice}

    private fun addChoiceToQueue() {

        var hi = getMenu(links, currentChoice)
        replaceLine( lastUserLine + 2, "$hi:")


        var currentOption = find(options, currentChoice)
        for (i in (currentOption.indices))
            replaceLine(i + lastUserLine + 3, "${currentOption.elementAt(i).option}")
    }


//    Input & Cursor related--------------------------------------

    private fun updateCursorLine(gamepad: Gamepad) {
//        Pushes the cursor beyond the user lines
        while (cursorLine <= lastUserLine + 1)
            cursorLine += 1

//        Changes the line based on user input
        when {
            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorLine > lastUserLine + 1) cursorLine -= 1;}
            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorLine < queue.size - 1) cursorLine += 1;}
            gamepad.dpad_right && !keyDown -> {keyDown = true; cursorContent = queue[cursorLine];}
            gamepad.dpad_left && !keyDown -> {keyDown = true; menuDone = true;}
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
            else -> keyDown = true
        }
    }

//    Telemetry/Queue related----------------------------

//    User function
    fun display(line: Int, text: String) {
        replaceLine(line, text)
        if (line > lastUserLine)
            lastUserLine = line
        queueToTelemetry()
    }

    private fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (queue.size <= line)
            queue.add(" ")

//        Adds or subtracts cursor
        if (queue[line].startsWith("-"))
            queue[line] = queue[line].replaceFirst("-", " ")
        if (line == cursorLine)
            queue[line] = "-$text"
        else
            queue[line] = text
    }

    private fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until queue.size)) {
            telemetry.addLine(queue[i])
        }
        telemetry.update()
    }

//    Do menus-------------------------------------------

//    User function
    fun doMenus(gamepad: Gamepad) {
        while (!menuDone) {

//            Cursor&dpad
            updateCursorLine(gamepad)

//            Update queue
            addChoiceToQueue()

//            Telemetry
            queueToTelemetry()
        }
        display(1, "Menu done.")
    }
}