package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayTenTest {

    @Test
    fun testPartOne() {
        val dayTen = DayTen()
        val result =
            dayTen.partOne(AoCUtil.readResourceFile("example-10-1.txt").map { AoCUtil.readLineAsInt(it, true) })
        assertEquals(36.toBigInteger(), result)
    }

    @Test
    fun testPartOneE2() {
        val dayTen = DayTen()
        val result =
            dayTen.partOne(AoCUtil.readResourceFile("example-10-2.txt").map { AoCUtil.readLineAsInt(it, true) })
        assertEquals(2.toBigInteger(), result)
    }

    @Test
    fun testPartOneE3() {
        val dayTen = DayTen()
        val result =
            dayTen.partOne(AoCUtil.readResourceFile("example-10-3.txt").map { AoCUtil.readLineAsInt(it, true) })
        assertEquals(4.toBigInteger(), result)
    }

    @Test
    fun testPartOneE4() {
        val dayTen = DayTen()
        val result =
            dayTen.partOne(AoCUtil.readResourceFile("example-10-4.txt").map { AoCUtil.readLineAsInt(it, true) })
        assertEquals(3.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayTen = DayTen()
        val result =
            dayTen.partTwo(AoCUtil.readResourceFile("example-10-1.txt").map { AoCUtil.readLineAsInt(it, true) })
        assertEquals(81.toBigInteger(), result)
    }

}