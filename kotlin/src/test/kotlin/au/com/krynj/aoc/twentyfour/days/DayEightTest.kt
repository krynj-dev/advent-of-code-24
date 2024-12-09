package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class DayEightTest {

    @Test
    fun testPartOne() {
        val dayEight = DayEight()
        val result = dayEight.partOne(AoCUtil.readResourceFile("example-8-1.txt"))
        assertEquals(14.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayEight = DayEight()
        val result = dayEight.partTwo(AoCUtil.readResourceFile("example-8-1.txt"))
        assertEquals(34.toBigInteger(), result)
    }

    @Test
    fun testGetAntennae() {
        val dayEight = DayEight()
        val antennae = dayEight.getAntennaeLocations(AoCUtil.readResourceFile("example-8-1.txt"))
        return
    }
}