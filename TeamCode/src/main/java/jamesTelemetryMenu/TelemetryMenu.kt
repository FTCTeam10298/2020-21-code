//package jamesTelemetryMenu
//
//import com.qualcomm.robotcore.hardware.Gamepad
//import org.firstinspires.ftc.robotcore.external.Telemetry
//import java.lang.Thread.sleep
//
//open class TelemetryMenu(telemetry: Telemetry): TelemetryConsole(telemetry) {
//
//    private var cursorLine= 4
//    private var menuDone= false
//    private var keyDown= true
//
//    val firstOption= "First Option"
//    var currentChoice= "Choices"
//    var cursorContent= "Options"
//
////    Input & Cursor--------------------------------------
//
//    private fun updateCursorLine(gamepad: Gamepad) {
//
////        Pushes the cursor beyond the user lines
////        while (cursorLine <= lastUserLine + 1)
//        cursorLine = lastUserLine + 1
//
////        Changes the line based on user input
//        when {
//            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorLine > lastUserLine + 1) cursorLine -= 1;}
//            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorLine < queue.size - 1) cursorLine += 1;}
//            gamepad.dpad_right && !keyDown -> {keyDown = true; cursorContent = queue[cursorLine];}
//            gamepad.dpad_left && !keyDown -> {keyDown = true; menuDone = true;}
//            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
//            else -> keyDown = true
//        }
//
////        Adds line
//        addCursor(cursorLine)
//    }
//
//    private fun addCursor(line: Int) {
//
//        if (queue[line].startsWith("-"))
//            queue[line] = queue[line].replaceFirst("-", " ")
//
//        if (line == cursorLine)
//            queue[line] = "-${queue[line]}"
////        else
////            queue[line] = queue[line]
//    }
//
//
////    CHOICES
//
//    private val options = LinkSystem()
//
////    OPTION INTERACTION
//
////    fun addOption(choice: String,  option: String) {
////        options.add(option(choice, option))
////    }
//
//    fun optionChosen(option: String): Boolean {
//        return cursorContent == option
//    }
//
////    fun linkOption(option: String, choice: String) {
//////        links.add(link(option, choice))
////    }
//
////    FIND AND REPLACE?
//
//    private fun addChoiceToQueue() {
//
//        var hi = options.(currentChoice)
//        replaceLine( lastUserLine + 2, "$hi:")
//
//        var currentOption = options.getMatch(currentChoice)
//        for (i in (currentOption.indices))
//            replaceLine(i + lastUserLine + 3, currentOption.elementAt(i).option)
//    }
//
//
////    DO MENUS
//
//    fun doMenus(gamepad: Gamepad) {
//        while (!menuDone) {
//            display(1, "still works")
//            sleep(1000)
//
////            Cursor&dpad
//            updateCursorLine(gamepad)
//
////            Update queue
//            addChoiceToQueue()
//
////            Telemetry
////            queueToTelemetry()
//        }
//        display(1, "Menu done.")
//    }
//}