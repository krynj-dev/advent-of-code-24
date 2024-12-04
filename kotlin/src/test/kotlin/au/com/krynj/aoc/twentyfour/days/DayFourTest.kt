package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DayFourTest {

    @Test
    fun testPartOne() {
        val dayFour = DayFour()
        val result = dayFour.partOne(AoCUtil.readResourceFile("dayfour/example.txt"))
        assertEquals(18.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayFour = DayFour()
        val result = dayFour.partTwo(AoCUtil.readResourceFile("dayfour/example.txt"))
        assertEquals(9.toBigInteger(), result)
    }

}