package au.com.krynj.aoc.twentyfour.days

import au.com.krnyj.aoc.framework.TestAoCObserver
import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.time.measureTime

class DayTwoTest {

    @Test
    fun testPartOneExampleInput() {
        val dayTwo = DayTwo()
        dayTwo.addObserver(
            TestAoCObserver(
                mutableListOf(
                    1.toBigInteger(),
                    1.toBigInteger(),
                    1.toBigInteger(),
                    1.toBigInteger(),
                    1.toBigInteger(),
                    2.toBigInteger(),
                )
            )
        )

        val result: BigInteger = dayTwo.partOne(AoCUtil.readResourceFile("example-2-1.txt"))

        assertEquals(2.toBigInteger(), result)
    }

    @Test
    fun testPartOneEdgeCases() {
        val testData = listOf(
            "1 2 3 4 5 6",
            "1 1 1 1 1 6",
            "3 6 9 12 15 18",
            "12 10 8 6 4 2",
            "40 36 32 28 24 20",
            "-3 -1 0 1 3 4",
        )
        val dayTwo = DayTwo()

        val result: BigInteger = dayTwo.partOne(testData)

        assertEquals(4.toBigInteger(), result)
    }

    @Test
    fun testValidityFilter() {
        val dayTwo = DayTwo()
        val testData = listOf(
            "1 2 3 4 5 6",
            "1 1 1 1 1 6",
            "3 6 9 12 15 18",
            "12 10 8 6 4 2",
            "40 36 32 28 24 20",
            "-3 -1 0 1 3 4",
        )
        val diffs = dayTwo.diffArray(AoCUtil.readLinesAsInt(testData, ' '))
        assertTrue(dayTwo.validityFilter(diffs[0]))
        assertFalse(dayTwo.validityFilter(diffs[1]))
        assertTrue(dayTwo.validityFilter(diffs[2]))
        assertTrue(dayTwo.validityFilter(diffs[3]))
        assertFalse(dayTwo.validityFilter(diffs[4]))
        assertTrue(dayTwo.validityFilter(diffs[5]))
    }

    @Test
    fun testValidityFilterTwo() {
        val dayTwo = DayTwo()

        assertTrue(dayTwo.validityFilterTwo(listOf(1, 2, 3, 4, 5, 6)))
        assertFalse(dayTwo.validityFilterTwo(listOf(1, 1, 1, 1, 1, 6)))
        assertTrue(dayTwo.validityFilterTwo(listOf(3, 6, 9, 12, 15, 18)))
        assertTrue(dayTwo.validityFilterTwo(listOf(12, 10, 8, 6, 4, 2)))
        assertFalse(dayTwo.validityFilterTwo(listOf(40, 36, 32, 28, 24, 20)))
        assertTrue(dayTwo.validityFilterTwo(listOf(-3, -1, 0, 1, 3, 4)))
        assertTrue(dayTwo.validityFilterTwo(listOf(3, 1, 2, 3, 4, 5)))
        assertTrue(dayTwo.validityFilterTwo(listOf(6, 5, 4, 3, 2, 3)))
        assertTrue(dayTwo.validityFilterTwo(listOf(4, 3, 6, 7, 8, 9)))
        assertTrue(dayTwo.validityFilterTwo(listOf(3, 5, 0, -1, -2, -3)))
        assertTrue(dayTwo.validityFilterTwo(listOf(1, 8, 3, 4, 5)))
        assertTrue(dayTwo.validityFilterTwo(listOf(9, 15, 6, 3, 0)))
        assertFalse(dayTwo.validityFilterTwo(listOf(1, 2, 0, 3, 0, 5)))

        assertFalse(dayTwo.validityFilterTwo(listOf(1, 8, 10, 11, 12, 19)))
    }

    @Test
    fun testPartTwoExampleInput() {
        val dayTwo = DayTwo()
        dayTwo.addObserver(
            TestAoCObserver(
                mutableListOf(
                    1.toBigInteger(),
                    1.toBigInteger(),
                    1.toBigInteger(),
                    2.toBigInteger(),
                    3.toBigInteger(),
                    4.toBigInteger(),
                )
            )
        )

        val result: BigInteger = dayTwo.partTwo(AoCUtil.readResourceFile("example-2-1.txt"))

        assertEquals(4.toBigInteger(), result)
    }

}