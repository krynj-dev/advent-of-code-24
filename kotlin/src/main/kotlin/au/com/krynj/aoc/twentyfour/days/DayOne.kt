package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class DayOne : AoCDay, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()

    override fun run() {
        println("Part 1: " + partOne(AoCUtil.readResourceFile("dayone/partone.txt")))
        println("Part 2: " + partTwo(AoCUtil.readResourceFile("dayone/partone.txt")))
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
            broadcast(diff)
        }
        return distanceTotal
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        // Convert to int list pair
        val counts: Pair<MutableMap<BigInteger, BigInteger>, MutableMap<BigInteger, BigInteger>> =
            Pair(LinkedHashMap(), LinkedHashMap())
        inputLines.forEach() { line ->
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
            broadcast(distanceTotal)
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

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach {
            it.notify(partialResult)
        }
    }

}
