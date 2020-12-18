package jamesTelemetryMenu

import com.qualcomm.robotcore.hardware.Gamepad

class TelemetryWizard(private val console: TelemetryConsole) {

    private val startLine = 2
    private var endLine = 0

//    Menu organization
    private var menuList: List<Menu> = listOf()
    private var chosenItems: List<String> = listOf()

    data class Menu(val name: String, val caption: String, val items: List<String>, val nextMenu: Menu? = null, val firstMenu: Boolean = false)

    fun newMenu(name: String, caption: String, items: List<String>, nextMenu: Menu? = null, firstMenu: Boolean = false) {
//        val nextMenu = previousMenu!!.nextMenu
        menuList += Menu(name, caption, items, nextMenu, firstMenu)
    }

    fun getMenu(name: String): Menu = menuList.first{ it.name == name }

    fun wasItemChosen(item: String): Boolean = chosenItems.contains(item)

    private fun formatMenu(menu: Menu): List<String> {
        var formattedMenu = listOf(menu.caption + ":\n")
        menu.items.forEachIndexed { index, action ->
            formattedMenu += placeCursor(index) + action
        }
        return formattedMenu
    }

    private fun displayMenu(formattedMenu: List<String>) {
        formattedMenu.forEachIndexed{ index, action ->
            console.replaceLine(index + startLine, action)
        }
        endLine = formattedMenu.size + startLine
        console.queueToTelemetry()
    }

    private fun eraseLastMenu() {
        console.clearAll()
//        for (i in (startLine .. endLine))
//            console.eraseLine(i)
//        console.queueToTelemetry()
    }

//    Gamepad input handler
    private var cursorLine = 0
    private var menuDone = false
    private var keyDown = false

    private fun changeCursorBasedOnDPad(gamepad: Gamepad, currentMenu: Menu) {
        val cursorMax = currentMenu.items.size - 1

        when {
            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorLine > 0) cursorLine -= 1} //moves cursor up
            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorLine < cursorMax) cursorLine += 1}  //moves cursor down
            gamepad.dpad_right && !keyDown -> {keyDown = true; chosenItems += currentMenu.items[cursorLine]; menuDone = true;} //selects option
//                gamepad.dpad_left && !keyDown -> //Stops wizard or menu (haven't decided) and sets answers to default
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
        }
        if (cursorLine >= cursorMax)
            cursorLine = cursorMax
    }

    private fun placeCursor(option: Int): String {
        return if (option == cursorLine) {
            "-"
        } else {
            " "
        }
    }

    fun summonWizard(gamepad: Gamepad) {
        val firstMenu = menuList.first { it.firstMenu }
        var lastMenu = Menu("", "", listOf(), firstMenu)

        menuList.forEachIndexed { index, action ->
            val thisMenu = lastMenu.nextMenu
            menuDone = thisMenu === null

            while (!menuDone) {
                changeCursorBasedOnDPad(gamepad, thisMenu!!)

                displayMenu(formatMenu(thisMenu))
            }
            lastMenu = thisMenu!!
            eraseLastMenu()
        }

        console.display(startLine, "Wizard Complete!")
    }
}
