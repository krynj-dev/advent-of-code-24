package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class DayNineTest {

    @Test
    fun testPartOne() {
        val dayNine = DayNine()
        val result = dayNine.partOne(AoCUtil.readResourceFile("example-9-1.txt").first())
        assertEquals(1928.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayNine = DayNine()
        val result = dayNine.partTwo(AoCUtil.readResourceFile("example-9-1.txt").first())
        assertEquals(2858.toBigInteger(), result)
    }

    @Test
    fun testIdString() {
        val dayNine = DayNine()
        assertEquals("0..111....22222".toCharArray().map { it.toString() }, dayNine.getIdString("12345"))
        assertEquals("00...111...2...333.44.5555.6666.777.888899".toCharArray().map { it.toString() }, dayNine.getIdString("2333133121414131402"))
    }

    @Test
    fun testNumArray() {
        val expected = "0099811188827773336446555566".toCharArray().map { it.toString() }
        val dayNine = DayNine()
        val actual = dayNine.compactFiles(dayNine.getIdString("2333133121414131402"))
        assertEquals(expected, actual)
    }


    @Test
    fun testRun() {
        val dayNine = DayNine().run()
    }
}