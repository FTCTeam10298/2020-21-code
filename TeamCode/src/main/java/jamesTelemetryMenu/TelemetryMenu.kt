package jamesTelemetryMenu

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.hardware.Gamepad

open class TelemetryMenu(private val console:TelemetryConsole)  {

    private var cursorLine = 0
    private var menuDone = false
    private var menuIterator = 0

    private var firstOption = ""


    private fun isNotEmpty(it: String): Boolean = it !== " " || it !== " "

    /*private*/ var currentOption: String? = ""
        get()  = console.linesDisplayed.filter{ it ->  menuSystem.options.listOfOptions.any{ menuSystem.options.listOfOptions.contains(it) } && isNotEmpty(it) }.joinToString(" ")



//    Input & Cursor--------------------------------------

    private fun getCursorContent(): String = console.linesDisplayed[cursorLine].replaceFirst("-", "")


    private var keyDown = true

    private fun updateCursorLine(gamepad: Gamepad) {

//        Pushes the cursor beyond the user lines
        cursorLine = console.lastUserLine + 1
        firstOption.contains("hi")

//        Changes the line based on user input
        when {
            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorLine > console.lastUserLine + 1) cursorLine -= 1;}
            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorLine < console.linesDisplayed.size - 1) cursorLine += 1;}
            gamepad.dpad_right && !keyDown -> {keyDown = true; getCursorContent(); chosen.add(getCursorContent());}
            gamepad.dpad_left && !keyDown -> {keyDown = true; menuDone = true;}
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
            else -> keyDown = true
        }

//        Adds cursor

        if (console.linesDisplayed.size >= cursorLine) {
            removeCursor()
            addCursor()
        }
    }

    private fun removeCursor() {
        if (console.linesDisplayed[cursorLine].startsWith("-"))
            console.replaceLine(cursorLine, console.linesDisplayed[cursorLine].replaceFirst("-", ""))
    }

    private fun addCursor() {
        if (!console.linesDisplayed[cursorLine].contains("-"))
            console.replaceLine(cursorLine, "-${console.linesDisplayed[cursorLine]}")
    }

//    Options

    /*private*/ val menuSystem = MenuSystem()
    private val chosen: MutableList<String> = mutableListOf("")

    private fun addToChosen(item: String) {
        chosen.add(item)
    }

//    OPTION INTERACTION

    fun addOption(option: String,  item: String) {
        menuSystem.addChoiceItem(option, item)
    }

    fun wasItemChosen(item: String): Boolean = chosen.contains(item)

    fun linkOption(item: String,  option: String) {
        menuSystem.addLink(option, item)
    }

    fun firstOption(option: String) {
        firstOption = option
    }

//    FIND AND REPLACE?

    private fun addChoiceToQueue() {

        lateinit var option: String
        lateinit var item: List<String>

        if (currentOption == "[]") {
            option = firstOption
//            item = menu.getItems(firstOption)
            item = menuSystem.getItems(currentOption)
        } else {
            option = currentOption
            item = menuSystem.getItems(currentOption)
        }

        console.replaceLine(console.lastUserLine + 2, "$option:")

//        problematic
        for (i in (item.indices))
            console.replaceLine(i + console.lastUserLine + 3, item.elementAt(i).toString())
    }

//    DO MENUS

    fun doMenus(gamepad: Gamepad) {
        while (!menuDone) {
            menuIterator ++

//            Cursor&dpad
            updateCursorLine(gamepad)

//            Update queue
            addChoiceToQueue()

//            Telemetry
            console.queueToTelemetry()

//            test
            sleep(1000)
        }
        console.display(1, "Menu done.")
    }
}
