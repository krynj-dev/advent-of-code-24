package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class DayTwelveTest {

    @Test
    fun testPartOne() {
        val dayTwelve = DayTwelve()
        val result = dayTwelve.partOne(AoCUtil.readResourceFile("example-12-1.txt"))
        assertEquals(1930.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayTwelve = DayTwelve()
        val result = dayTwelve.partTwo(AoCUtil.readResourceFile("example-12-1.txt"))
        assertEquals(1206.toBigInteger(), result)
    }

}