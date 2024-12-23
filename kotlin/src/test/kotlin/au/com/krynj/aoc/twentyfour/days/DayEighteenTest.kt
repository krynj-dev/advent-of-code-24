package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayEighteenTest {

    @Test
    fun testPartOne() {
        val dayEighteen = DayEighteen()
        dayEighteen.size = 7 to 7
        dayEighteen.simulations = 12
        val result = dayEighteen.partOne(AoCUtil.readResourceFile("example-18-1.txt"))
        assertEquals("22", result)
    }

    @Test
    fun testPartTwo() {
        val dayEighteen = DayEighteen()
        dayEighteen.size = 7 to 7
        dayEighteen.simulations = 12
        val result = dayEighteen.partTwo(AoCUtil.readResourceFile("example-18-1.txt"))
        assertEquals("6,1", result)
    }
}