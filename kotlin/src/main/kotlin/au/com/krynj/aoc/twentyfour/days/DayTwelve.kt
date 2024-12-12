package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCAlgorithmUtil.findAround
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

class DayTwelve : AoCDay<List<String>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Twelve", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-12.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-12.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 12
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        return getPrice(inputLines.map { it.toList() })
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        return getPrice(inputLines.map { it.toList() }, true)
    }

    fun getPrice(plotMap: List<List<Char>>, useCorners: Boolean = false): BigInteger {
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var price = 0
        for (y in plotMap.indices) {
            for (x in plotMap[y].indices) {
                if (Pair(y, x) in visited) continue
                val (area, perimeter) = scanArea(Pair(y, x), plotMap, visited, useCorners)
                price += (area*perimeter)
            }
        }
        return price.toBigInteger()
    }

    fun scanArea(loc: Pair<Int, Int>, plotMap: List<List<Char>>, visited: MutableSet<Pair<Int, Int>>, useCorners: Boolean = false): Pair<Int, Int> {
        val queue = LinkedList<Pair<Int, Int>>()
        var area = 0
        var perimeter = 0
        var corners = 0
        queue.add(loc)
        while (queue.isNotEmpty()) {
            val plant = queue.remove()
            if (plant in visited) continue
            visited.add(plant)
            area++
            val adjacentPlants = findAround(plant, plotMap, plotMap[plant.first][plant.second], 1, false).filter { it != plant }
            perimeter += 4 - adjacentPlants.size
            corners += AoCAlgorithmUtil.countCorners(plant, plotMap)
            adjacentPlants.forEach { if (it !in visited) queue.add(it) }
        }
        return Pair(area, if (useCorners) corners else perimeter)
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
