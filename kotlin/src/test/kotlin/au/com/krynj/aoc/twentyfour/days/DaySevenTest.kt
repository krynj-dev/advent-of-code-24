package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class DaySevenTest {

    @Test
    fun testPartOne() {
        val daySeven = DaySeven()
        val result = daySeven.partOne(AoCUtil.readResourceFile("dayseven/example.txt"))
        assertEquals(3749.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val daySeven = DaySeven()
        val result = daySeven.partTwo(AoCUtil.readResourceFile("dayseven/example.txt"))
        assertEquals(11387.toBigInteger(), result)
    }

    @Test
    fun testConcat() {
        val daySeven = DaySeven()
        val result = daySeven.isConcatEq(15.toBigInteger(), listOf(6.toBigInteger()), 156.toBigInteger(), "")
        assertEquals(true, result)
    }
}