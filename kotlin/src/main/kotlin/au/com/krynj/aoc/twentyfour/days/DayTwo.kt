package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.sign
import kotlin.time.measureTime

class DayTwo : AoCDay<List<String>, BigInteger>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Two", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-2.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-2.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 2
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        var count = BigInteger.ZERO
        diffArray(AoCUtil.readLinesAsInt(inputLines, ' ')).forEach{
            if (validityFilter(it)) count = count.add(BigInteger.ONE)
            broadcast(SimpleObserverContext(count))
        }
        return count
    }

    fun validityFilter(intList: List<Int>): Boolean {
        return (intList.all { n -> n.sign == -1 } || intList.all { n -> n.sign == 1 }) && (intList.all { n -> abs(n) in 1..3 })
    }

    fun dpDiffs(report: List<Int>): List<List<Int?>> {
        val dp: MutableList<MutableList<Int?>> = mutableListOf()
        report.forEach{ _ -> dp.add(MutableList(4) { null }) }
        for (i in report.indices) {
            if (i < report.size-1 && (dp[i][2] == null || dp[i+1][1] == null)) {
                dp[i][2] = report[i+1] - report[i]
                dp[i+1][1] = report[i] - report[i+1]
            }
            if (i < report.size-2 && (dp[i][3] == null || dp[i+2][0] == null)) {
                dp[i][3] = report[i+2] - report[i]
                dp[i+2][0] = report[i] - report[i+2]
            }
        }
        return dp
    }

    fun validityFilterTwo(report: List<Int>): Boolean {
        val diffs = dpDiffs(report)
        // Scan left to right
        var i = 0
        var sign = diffs[0][2]?.sign
        var hasSkipped = false
        var valid = true
        while(i < report.size) {
            val nextDiff = diffs[i][2]
            if (nextDiff != null && (nextDiff.sign != sign || abs(nextDiff) !in 1..3)) {
                val skipDiff = diffs[i][3]
                if (hasSkipped || (skipDiff != null && (skipDiff.sign != sign || abs(skipDiff) !in 1..3))) {
                    valid = false
                    break
                }
                hasSkipped = true
                i += 2
                continue
            }
            i += 1
        }
        if (valid) return true
        // Scan right to left
        i = report.size - 1
        sign = diffs[report.size-1][1]?.sign
        hasSkipped = false
        valid = true
        while(i >= 0) {
            val nextDiff = diffs[i][1]
            if (nextDiff != null && (nextDiff.sign != sign || abs(nextDiff) !in 1..3)) {
                val skipDiff = diffs[i][0]
                if (hasSkipped || (skipDiff != null && (skipDiff.sign != sign || abs(skipDiff) !in 1..3))) {
                    valid = false
                    break
                }
                hasSkipped = true
                i -= 2
                continue
            }
            i -= 1
        }
        return valid
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        var count = BigInteger.ZERO
        inputLines.forEach{
            if (validityFilterTwo(AoCUtil.readLineAsInt(it, ' '))) count = count.add(BigInteger.ONE)
            broadcast(SimpleObserverContext(count))
        }
        return count
    }

    fun diffArray(intArray: List<List<Int>>): List<List<Int>> {
        return intArray.map {
            (0..<it.size - 1).map { i -> it[i] - it[i + 1] }
        }
    }

    override fun addObserver(observer: AoCObserver<AoCObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: AoCObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

}
