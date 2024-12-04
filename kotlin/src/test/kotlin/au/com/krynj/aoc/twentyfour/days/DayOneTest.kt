package au.com.krynj.aoc.twentyfour.days

import au.com.krnyj.aoc.framework.TestAoCObserver
import au.com.krynj.aoc.util.AoCUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.time.measureTime

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
    fun testPartOneConcise() {
        val dayOne = DayOne()
        val result: BigInteger = dayOne.partOneConcise(AoCUtil.readResourceFile("dayone/example-1-1.txt"))

        assertEquals(11.toBigInteger(), result)
    }

    @Test
    fun testPartOneConciseSameResult() {
        val dayOne = DayOne()
        val partOnePath = "dayone/input.txt"
        // Ensure Same result
        assertEquals(
            dayOne.partOne(AoCUtil.readResourceFile(partOnePath)),
            dayOne.partOneConcise(AoCUtil.readResourceFile(partOnePath))
        )
    }

    @Test
    fun testPartOneTimings() {
        val dayOne = DayOne()
        val partOnePath = "dayone/input.txt"
        val timesOG = ArrayList<Long>()
        val timesNew = ArrayList<Long>()
        repeat(500) {
            val timeOriginal = measureTime {
                dayOne.partOne(AoCUtil.readResourceFile(partOnePath))
            }
            val timeConcise = measureTime {
                dayOne.partOneConcise(AoCUtil.readResourceFile(partOnePath))
            }
            timesOG.add(timeOriginal.inWholeMicroseconds)
            timesNew.add(timeConcise.inWholeMicroseconds)
        }
        println(
            "Time taken average (original, concise):\t%f\t%f".format(
                timesOG.average(), timesNew.average()
            )
        )
    }

    @Test
    fun testPartTwoExampleInput() {
        val dayOne = DayOne()
        dayOne.addObserver(
            TestAoCObserver(
                mutableListOf(
                    27.toBigInteger(),
                    31.toBigInteger(),
                    31.toBigInteger(),
                    31.toBigInteger(),
                )
            )
        )

        val result: BigInteger = dayOne.partTwo(AoCUtil.readResourceFile("dayone/example-1-1.txt"))

        assertEquals(31.toBigInteger(), result)
    }
}