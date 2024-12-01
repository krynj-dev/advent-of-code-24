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
        // Convert to int list pair
        val lists: Pair<Queue<Pair<BigInteger, Int>>, Queue<Pair<BigInteger, Int>>> =
            Pair(
                PriorityQueue(compareBy<Pair<BigInteger, Int>?> { it?.first }.thenBy { it?.second }),
                PriorityQueue(compareBy<Pair<BigInteger, Int>?> { it?.first }.thenBy { it?.second })
            )
        inputLines.forEachIndexed { index, line ->
            val lineInts: List<BigInteger> = AoCUtil.readLineAsInt(line, "   ")
            assert(lineInts.size == 2)
            // Add entries
            lists.first.add(Pair(lineInts[0], index))
            lists.second.add(Pair(lineInts[1], index))
        }

        var distanceTotal: BigInteger = BigInteger.ZERO

        while (lists.first.isNotEmpty() && lists.second.isNotEmpty()) {
            val a = lists.first.poll()
            val b = lists.second.poll()
            val diff = a.first.subtract(b.first).abs()
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
            val lineInts: List<BigInteger> = AoCUtil.readLineAsInt(line, "   ")
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

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach {
            it.notify(partialResult)
        }
    }

}
