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

class DayFourteenTest {

    @Test
    fun testPartOne() {
        val dayFourteen = DayFourteen()
        dayFourteen.setSize(Pair(7, 11))
        val result = dayFourteen.partOne(AoCUtil.readResourceFile("example-14-1.txt"))
        assertEquals(12.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayFourteen = DayFourteen()
        dayFourteen.setSize(Pair(7, 11))
        val result = dayFourteen.partTwo(AoCUtil.readResourceFile("example-14-1.txt"))
        assertEquals(1206.toBigInteger(), result)
    }

    @Test
    fun testExample() {
        val dayFourteen = DayFourteen()
        assertEquals(Pair(102, 100), dayFourteen.wrapNegative(Pair(-1, -1)))
        assertEquals(Pair(103-47, 101-20), dayFourteen.positionAfter(Pair(-356, -323), Pair(0, 0), 1))

    }

}