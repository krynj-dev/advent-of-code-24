package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DaySeventeenTest {

    @Test
    fun testPartOne() {
        val daySeventeen = DaySeventeen()
        val result = daySeventeen.partOne(AoCUtil.readResourceFile("example-17-1.txt", ""))
        assertEquals("4,6,3,5,6,3,5,2,1,0", result)
    }

    @Test
    fun testPartTwo() {
        val daySeventeen = DaySeventeen()
        val result = daySeventeen.partTwo(AoCUtil.readResourceFile("example-17-1.txt", ""))
        assertEquals(45.toBigInteger(), result)
    }
}