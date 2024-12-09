package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DayFiveTest {

    @Test
    fun partOne() {
        val dayFive = DayFive()
        val result = dayFive.partOne(AoCUtil.readResourceFile("dayfive/example.txt", ""))
        assertEquals(143.toBigInteger(), result)
    }

    @Test
    fun partTwo() {
        val dayFive = DayFive()
        val result = dayFive.partTwo(AoCUtil.readResourceFile("dayfive/example.txt", ""))
        assertEquals(123.toBigInteger(), result)
    }
}