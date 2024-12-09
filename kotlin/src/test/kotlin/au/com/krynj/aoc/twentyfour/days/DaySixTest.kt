package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DaySixTest {

    @Test
    fun testPartOne() {
        val daySix = DaySix()
        val result = daySix.partOne(AoCUtil.readResourceFile("example-6-1.txt"))
        assertEquals(41.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val daySix = DaySix()
        val result = daySix.partTwo(AoCUtil.readResourceFile("example-6-1.txt"))
        assertEquals(6.toBigInteger(), result)
    }

    @Test
    fun testPartTwoAlt() {
        val daySix = DaySix()
        val result = daySix.partTwo(AoCUtil.readResourceFile("example-6-2.txt"))
        assertEquals(2.toBigInteger(), result)
    }

    @Test
    fun testRotate() {
        var dir = Pair(1, 0) // Down
        val expectedResults = listOf(
            Pair(0, -1), // Left
            Pair(-1, 0), // Up
            Pair(0, 1), // Right
            Pair(1, 0), // Down
        )
        for (i in 0..3) {
            dir = Pair(dir.second, -dir.first)
            assertEquals(expectedResults[i], dir)
        }
    }
}