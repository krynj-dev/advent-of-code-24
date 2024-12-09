package au.com.krynj.aoc.twentyfour.days

import au.com.krnyj.aoc.framework.TestAoCObserver
import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class DayThreeTest {

    @Test
    fun testPartOneExampleInput() {
        val dayThree = DayThree()
        dayThree.addObserver(
            TestAoCObserver(
                mutableListOf(
                    8.toBigInteger(),
                    33.toBigInteger(),
                    121.toBigInteger(),
                    161.toBigInteger()
                )
            )
        )

        val result: BigInteger = dayThree.partOne(AoCUtil.readResourceFile("example-3-1.txt"))

        assertEquals(161.toBigInteger(), result)
    }

    @Test
    fun testPartTwoExampleInput() {
        val dayThree = DayThree()
        dayThree.addObserver(
            TestAoCObserver(
                mutableListOf(
                    8.toBigInteger(),
                    48.toBigInteger()
                )
            )
        )

        val result: BigInteger = dayThree.partTwo(AoCUtil.readResourceFile("example-3-2.txt"))

        assertEquals(48.toBigInteger(), result)
    }

    @Test
    fun testPartTwoEdgeCases() {
        val dayThree = DayThree()
        dayThree.addObserver(
            TestAoCObserver(
                mutableListOf(
                    8.toBigInteger(),
                    17.toBigInteger(),
                    20.toBigInteger(),
                )
            )
        )
        val testData = listOf(
            "mul(2,4)do()mul(3,3)don't()mul(4,5)",
            "don't()mul(3,3)don't()mul(4,5)do()mul(1,3)",
        )

        val result: BigInteger = dayThree.partTwo(testData)

        assertEquals(20.toBigInteger(), result)
    }
}