package jamesTelemetryMenu

import com.qualcomm.robotcore.hardware.Gamepad

class TelemetryWizard(private val console: TelemetryConsole) {

    private val startLine = 2
    private var endLine = 0

//    Menu organization
    private var menuList: List<Menu> = listOf()
    private var chosenItems: List<String?> = listOf()

    data class Menu(val id: String, val caption: String, val items: Map<String, String?>, val firstMenu: Boolean = false)

    fun newMenu(name: String, caption: String, items: List<String>, nextMenu: String? = null, firstMenu: Boolean = false) {
        var item: Map<String, String?> = mapOf()
        items.forEach{ item += mapOf(it to nextMenu) }

        menuList += Menu(name, caption, item!!, firstMenu)
    }

    fun newMenu(name: String, caption: String, items: Map<String, String?>, firstMenu: Boolean = false) {
        menuList += Menu(name, caption, items, firstMenu)
    }

    fun getMenu(id: String?): Menu? = menuList.first{ it.id == id }

    fun wasItemChosen(item: String): Boolean = chosenItems.contains(item)

    private fun formatMenu(menu: Menu): List<String> {
        var formattedMenu = listOf(menu.caption + ":\n")
//        problematic
        for (i in menu.items) {
            formattedMenu += placeCursor(menu.items.size + 1) + i.key
            console.display(5, menu.items[i].hashCode().toString())
        }
//        console.display(5, cursorLine.toString())

        menu.items.forEach{ index, key ->
            formattedMenu += placeCursor(index.) + action
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
            gamepad.dpad_right && !keyDown -> {keyDown = true; chosenItems += currentMenu.items.keys.first{ it.hashCode() == cursorLine }; menuDone = true;} //selects option
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
        val firstMenu = menuList.first{ it.firstMenu }
        var lastMenu = Menu("", "", mapOf("hi" to firstMenu.id))
        var chosenItem: String? = "hi"

        menuList.forEachIndexed{ index, action ->
            val thisMenu = getMenu(lastMenu.items[chosenItem])
            menuDone = thisMenu === null

            while (!menuDone) {
                changeCursorBasedOnDPad(gamepad, thisMenu!!)

                displayMenu(formatMenu(thisMenu))
            }
//            chosenItem = thisMenu?.items?[chosenItem]
            lastMenu = thisMenu!!
            eraseLastMenu()
        }

        console.display(startLine, "Wizard Complete!")
    }
}
