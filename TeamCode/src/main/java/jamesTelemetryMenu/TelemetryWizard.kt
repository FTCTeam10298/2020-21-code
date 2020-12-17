package jamesTelemetryMenu

import com.qualcomm.robotcore.hardware.Gamepad

class TelemetryWizard(private val console: TelemetryConsole) {

    private var menuList: List<Menu> = listOf()

    data class Menu(val name: Any, val caption: String, val items: List<Any>, val nextMenu: Any, val firstMenu: Boolean = false)

//    fun newMenu(option: Any, caption: String, items: List<Any>, nextMenu: Any, firstMenu: Boolean = false) {
//        menuList += Menu(option, caption, items, nextMenu, firstMenu)
//    }

    fun newMenu(menu: Menu) {
        menuList += menu
    }

    private fun formatMenu(menu: Menu): String/*List<String>*/ {
//        var format = listOf(menu.caption, " sadf ")
        var format = menu.caption

//        for (i in (1..menu.items.size))
//            format += menu.items[i].toString()
        return format
    }

    private fun displayMenu(formattedMenu: /*List<String>*/String) {
//        for (i in (1..formattedMenu.size)) {
//            console.replaceLine(i, formattedMenu[i])
//        }
        console.replaceLine(1, formattedMenu)
    }

    fun summonWizard(gamepad: Gamepad) {
        displayMenu(formatMenu(/*menuList.first { it.firstMenu }*/Menu("hi", "Hi", listOf(), "iH", true)))
    }

}