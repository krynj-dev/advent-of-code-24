package au.com.krynj.aoc.twentyfour

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import au.com.krynj.aoc.cmp.AoCComposeAppLanding
import au.com.krynj.aoc.framework.AoCApplication

fun main() = application {
    val app = AoCApplication("au.com.krynj.aoc")
    Window(onCloseRequest = ::exitApplication, title = "Advent of Code 2024") {
        AoCComposeAppLanding(app.getDayClasses())
    }
}
