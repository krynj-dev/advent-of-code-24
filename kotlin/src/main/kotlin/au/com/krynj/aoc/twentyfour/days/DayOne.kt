package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.time.measureTime

class DayOne : AoCDay<List<String>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day One", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-1.txt"))
        }
        println("Part 1: ${addColour("$result1", AoCConsoleColours.GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-1.txt"))
        }
        println("Part 2: ${addColour("$result2", AoCConsoleColours.GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 1
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        // Create a set of parallel prio queues for each column in the file (sorted first by value, then by location)
        val lists: Pair<Queue<BigInteger>, Queue<BigInteger>> = Pair(
            PriorityQueue(), PriorityQueue()
        )
        inputLines.forEach {
            val lineInts: List<BigInteger> = AoCUtil.readLineAsBigInt(it, "   ")
            assert(lineInts.size == 2)
            // Add entries
            lists.first.add(lineInts[0])
            lists.second.add(lineInts[1])
        }
        var distanceTotal: BigInteger = BigInteger.ZERO
        while (lists.first.isNotEmpty() && lists.second.isNotEmpty()) {
            val diff = lists.first.poll().subtract(lists.second.poll()).abs()
            distanceTotal = distanceTotal.add(diff)
            broadcast(SimpleObserverContext(diff))
        }
        return distanceTotal
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        // Convert to int list pair
        val counts: Pair<MutableMap<BigInteger, BigInteger>, MutableMap<BigInteger, BigInteger>> =
            Pair(LinkedHashMap(), LinkedHashMap())
        inputLines.forEach { line ->
            val lineInts: List<BigInteger> = AoCUtil.readLineAsBigInt(line, "   ")
            assert(lineInts.size == 2)
            // Add entries
            val num0 = lineInts[0]
            val num1 = lineInts[1]
            if (num0 !in counts.first.keys) {
                counts.first[num0] = BigInteger.ONE
            } else {
                counts.first[num0] = counts.first[num0]!!.plus(BigInteger.ONE)
            }
            if (num1 !in counts.second.keys) {
                counts.second[num1] = BigInteger.ONE
            } else {
                counts.second[num1] = counts.second[num1]!!.plus(BigInteger.ONE)
            }
        }

        var distanceTotal: BigInteger = BigInteger.ZERO

        counts.first.keys.forEach { leftNum ->
            if (leftNum in counts.second.keys) distanceTotal =
                distanceTotal.plus(leftNum.multiply(counts.first[leftNum]!!.multiply(counts.second[leftNum])))
            broadcast(SimpleObserverContext(distanceTotal))
        }

        return distanceTotal
    }

    fun partOneConcise(inputLines: List<String>): BigInteger {
        val queues = inputLines.map { AoCUtil.readLineAsBigInt(it, "   ") }
            .fold(mutableListOf(PriorityQueue<BigInteger>(), PriorityQueue<BigInteger>())) { acc, bigIntegers ->
                acc[0].add(bigIntegers[0])
                acc[1].add(bigIntegers[1])
                acc
            }
        var result = BigInteger.ZERO
        while (queues.first().isNotEmpty()) {
            result = result.add(queues[0].poll().minus(queues[1].poll()).abs())
        }
        return result
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
