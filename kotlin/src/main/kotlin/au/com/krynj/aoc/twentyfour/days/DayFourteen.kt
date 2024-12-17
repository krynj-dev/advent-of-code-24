package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCAlgorithmUtil.findAround
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCMathsUtil
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.Tuple
import java.math.BigInteger
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.time.measureTime

class DayFourteen : AoCDay<List<String>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()
    private var size: Pair<Int, Int> = Pair(103, 101)

    override fun run() {
        println(addColour("Day Fourteen", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-14.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-14.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 14
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        val robots = parseInput(inputLines)
        val endPos = robots.map {
            positionAfter(it.first, it.second, 100)
        }
        return countQuadrants(endPos)
    }

    fun countQuadrants(locs: List<Pair<Int, Int>>): BigInteger {
        val q1 = { p: Pair<Int, Int> -> p.first < size.first / 2 && p.second < size.second / 2 }
        val q2 = { p: Pair<Int, Int> -> p.first < size.first / 2 && p.second > size.second / 2 }
        val q3 = { p: Pair<Int, Int> -> p.first > size.first / 2 && p.second < size.second / 2 }
        val q4 = { p: Pair<Int, Int> -> p.first > size.first / 2 && p.second > size.second / 2 }
        return (locs.filter(q1).size.toBigInteger() * locs.filter(q2).size.toBigInteger()
                * locs.filter(q3).size.toBigInteger() * locs.filter(q4).size.toBigInteger())
    }

    fun printGrid(locs: List<Pair<Int, Int>>) {
        (0..<size.first).forEach { i ->
            println((0..<size.second).map { j ->
                if (Pair(i, j) in locs) "1" else "."
            }.joinToString(" "))
        }
    }

    fun parseInput(inputLines: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        return inputLines.map {
            val (a, b, c, d) = """p=(-*\d+),(-*\d+)\sv=(-*\d+),(-*\d+)""".toRegex().find(it)!!.destructured
            Pair(b.toInt(), a.toInt()) to Pair(d.toInt(), c.toInt())
        }
    }

    fun positionAfter(startPos: Pair<Int, Int>, velocity: Pair<Int, Int>, cycles: Int): Pair<Int, Int> {
        return wrapNegative(Pair(
            (startPos.first + velocity.first*cycles) % size.first,
            (startPos.second + velocity.second*cycles) % size.second
        ))
    }

    fun wrapNegative(pos: Pair<Int, Int>): Pair<Int, Int> {
        val y = if (pos.first.sign < 0) size.first+pos.first else pos.first
        val x = if (pos.second.sign < 0) size.second+pos.second else pos.second
        return Pair(y, x)
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        val robots = parseInput(inputLines)
        var poss = robots.map {
            positionAfter(it.first, it.second, 0)
        }
        val percs: MutableList<Pair<Int, List<Pair<Int, Int>>>> = mutableListOf()
        repeat(size.first*size.second) {
            poss = poss.mapIndexed{ index, a ->
                positionAfter(a, robots[index].second, 1)
            }
            percs.add(Pair(testChristmasTreeness(poss), poss.toList()))
        }
        return percs.indexOf(percs.minBy { it.first }).toBigInteger().plus(BigInteger.ONE)
    }

    fun testChristmasTreeness(locations: List<Pair<Int, Int>>): Int {
        // minimise distance from centre
        return locations.map {
            abs(size.first / 2 - it.first) + abs(size.second / 2 - it.second)
        }.average().toInt()
    }

    fun setSize(newSize: Pair<Int, Int>) {
        size = Pair(newSize.first, newSize.second)
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
