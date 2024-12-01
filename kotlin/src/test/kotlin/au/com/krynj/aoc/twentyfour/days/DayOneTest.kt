package au.com.krynj.aoc.twentyfour.days

import au.com.krnyj.aoc.framework.TestAoCObserver
import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class DayOneTest {

    @Test
    fun testPartOneExampleInput() {
        val dayOne = DayOne()
        dayOne.addObserver(
            TestAoCObserver(
                mutableListOf(
                    2.toBigInteger(),
                    1.toBigInteger(),
                    0.toBigInteger(),
                    1.toBigInteger(),
                    2.toBigInteger(),
                    5.toBigInteger(),
                )
            )
        )

        val result: BigInteger = dayOne.partOne(AoCUtil.readResourceFile("dayone/example-1-1.txt"))

        assertEquals(11.toBigInteger(), result)
    }

    @Test
    fun testPartTwoExampleInput() {
        val dayOne = DayOne()
        dayOne.addObserver(TestAoCObserver(mutableListOf(
            27.toBigInteger(),
            31.toBigInteger(),
            31.toBigInteger(),
            31.toBigInteger(),
        )))

        val result: BigInteger = dayOne.partTwo(AoCUtil.readResourceFile("dayone/example-1-1.txt"))

        assertEquals(31.toBigInteger(), result)
    }
}