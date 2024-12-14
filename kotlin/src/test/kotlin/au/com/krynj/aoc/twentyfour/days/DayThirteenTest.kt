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

class DayThirteenTest {

    @Test
    fun testPartOne() {
        val dayThirteen = DayThirteen()
        val result = dayThirteen.partOne(AoCUtil.readResourceFile("example-13-1.txt", ""))
        assertEquals(480.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayThirteen = DayThirteen()
        val result = dayThirteen.partTwo(AoCUtil.readResourceFile("example-13-1.txt", ""))
        assertEquals(1206.toBigInteger(), result)
    }

}