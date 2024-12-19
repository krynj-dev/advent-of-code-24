package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayFifteenTest {

    @Test
    fun testPartOne() {
        val dayFifteen = DayFifteen()
        val result = dayFifteen.partOne(AoCUtil.readResourceFile("example-15-1.txt", ""))
        assertEquals(2028.toBigInteger(), result)
    }

    @Test
    fun testPartOneVariantTwo() {
        val dayFifteen = DayFifteen()
        val result = dayFifteen.partOne(AoCUtil.readResourceFile("example-15-2.txt", ""))
        assertEquals(10092.toBigInteger(), result)
    }

    @Test
    fun testPartTwo() {
        val dayFifteen = DayFifteen()
        val result = dayFifteen.partTwo(AoCUtil.readResourceFile("example-15-1.txt", ""))
        assertEquals(1206.toBigInteger(), result)
    }

}