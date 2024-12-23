package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DaySixteenTest {

    @Test
    fun testPartOne() {
        val daySixteen = DaySixteen()
        val result = daySixteen.partOne(AoCUtil.readResourceFile("example-16-1.txt"))
        assertEquals(7036.toBigInteger(), result)
    }

    @Test
    fun testPartAlgo() {
        val daySixteen = DaySixteen()
        val result = daySixteen.newShortest(AoCUtil.readResourceFile("example-16-1.txt"))

        assertEquals(0, result[Pair(13,1)]?.first)
        assertEquals(1001, result[Pair(12,1)]?.first)
        assertEquals(1002, result[Pair(11,1)]?.first)
        assertEquals(2003, result[Pair(11,2)]?.first)
        assertEquals(2004, result[Pair(11,3)]?.first)
        assertEquals(3005, result[Pair(10,3)]?.first)
        assertEquals(3006, result[Pair(9,3)]?.first)
        assertEquals(3007, result[Pair(8,3)]?.first)
        assertEquals(3008, result[Pair(7,3)]?.first)
        assertEquals(4009, result[Pair(7,4)]?.first)
        assertEquals(4010, result[Pair(7,5)]?.first)
        assertEquals(4011, result[Pair(7,6)]?.first)
        assertEquals(4012, result[Pair(7,7)]?.first)
        assertEquals(4013, result[Pair(7,8)]?.first)
        assertEquals(4014, result[Pair(7,9)]?.first)
        assertEquals(4015, result[Pair(7,10)]?.first)
    }

    @Test
    fun testPartTwo() {
        val daySixteen = DaySixteen()
        val result = daySixteen.partTwo(AoCUtil.readResourceFile("example-16-1.txt"))
        assertEquals(45.toBigInteger(), result)
    }

    @Test
    fun testPartTwoAlt() {
        val daySixteen = DaySixteen()
        val result = daySixteen.partTwo(AoCUtil.readResourceFile("example-16-2.txt"))
        assertEquals(64.toBigInteger(), result)
    }
}