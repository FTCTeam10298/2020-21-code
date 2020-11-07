package jamesTelemetryMenu

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryMenu(telemetry: Telemetry, gamepad1: Gamepad): TelemetryConsole(telemetry) {

    private val gamepad = gamepad1
    private var cursorLine= 4
    private var menuDone= false
    private var keyDown= true

    val firstOption= "First Option"
    var currentChoice= "Choices"
    var cursorContent= "Options"

//    Input & Cursor--------------------------------------

    private fun updateCursorLine() {
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

//        Adds line
        addcursor(cursorLine)
    }

    private fun addcursor(line: Int) {

        if (queue[line].startsWith("-"))
            queue[line] = queue[line].replaceFirst("-", " ")

        if (line == cursorLine)
            queue[line] = "-${queue[line]}"
        else
            queue[line] = queue[line]
    }


//    CHOICES

    data class option(var choice: String, var option: String)
    var options: MutableList<option> = mutableListOf(option("The Choicest", "Of Memes"))

    data class link(var option: String, var choice: String)
    var links: MutableList<link> = mutableListOf(link("The Choicest", "Of Memes"))

//    OPTION INTERACTION

    fun addOption(choice: String,  option: String) {
        options.add(option(choice, option))
    }

    fun optionChosen(option: String): Boolean {
        return cursorContent == option
    }

    fun linkOption(option: String, choice: String) {
//        links.add(link(option, choice))
    }

//    FIND AND REPLACE?
    private fun find(options:List<option>, valueToFind: String) = options.filter{option-> option.choice == valueToFind}

    private fun getMenu(link: List<link>, choice: String) = link.filter{option -> option.choice == choice}

    private fun addChoiceToQueue() {

        var hi = getMenu(links, currentChoice)
        replaceLine( lastUserLine + 2, "$hi:")

        var currentOption = find(options, currentChoice)
        for (i in (currentOption.indices))
            replaceLine(i + lastUserLine + 3, "${currentOption.elementAt(i).option}")
    }


//    DO MENUS

    fun doMenus() {
        while (!menuDone) {

//            Cursor&dpad
            updateCursorLine()

//            Update queue
            addChoiceToQueue()

//            Telemetry
            queueToTelemetry()
        }
        display(1, "Menu done.")
    }
}