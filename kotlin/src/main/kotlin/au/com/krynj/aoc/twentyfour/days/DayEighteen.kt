package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.AoCObserverContext
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import kotlin.time.measureTime

class DayEighteen : AoCDay<List<String>, String>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    var size: Pair<Int, Int> = 71 to 71
    var simulations = 1024

    override fun run() {
        println(addColour("Day Eighteen", CYAN))
        val result1: String
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-18.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: String
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-18.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 18
    }

    override fun partOne(inputLines: List<String>): String {
        val bytes = readBytes(inputLines)
        val placeholderMap = (0..size.first).map { ".".repeat(size.second+1).toCharArray().toList() }
        val res = findPath(placeholderMap, bytes.subList(0, simulations), Pair(0, 0), Pair(size.first-1, size.second-1))
        return (res.size-1).toString()
    }

    override fun partTwo(inputLines: List<String>): String {
        val placeholderMap = (0..size.first).map { ".".repeat(size.second+1).toCharArray().toList() }
        var idx = simulations+1
        val bytes = readBytes(inputLines)
        var res = findPath(placeholderMap, bytes.subList(0, simulations), Pair(0, 0), Pair(size.first-1, size.second-1))
        while (res.isNotEmpty() && idx < bytes.size) {
            while (idx < bytes.size && bytes[idx] !in res) {
                idx++
            }
            res = findPath(placeholderMap, bytes.subList(0, idx+1), Pair(0, 0), Pair(size.first-1, size.second-1))
        }
        return "${bytes[idx].second},${bytes[idx].first}"
    }

    fun printMap(inputLines: List<String>) {
    }

    fun readBytes(inputLines: List<String>): List<Pair<Int, Int>> {
        return inputLines.map {
            val vs = """(\d+),(\d+)""".toRegex().find(it)!!.groupValues
            Pair(vs[2].toInt(), vs[1].toInt())
        }
    }

    fun findPath(map: List<List<Char>>, bytes: List<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {
        return AoCAlgorithmUtil.shortestPath(start, end,
            map, heur, getDist(bytes.toSet()))
    }

    val heur: (pos: Pair<Int, Int>, target: Pair<Int, Int>, map: List<List<Char>>) -> Int = { pos, target, _ ->
        target.first-pos.first + target.second-pos.second
    }

    fun getDist(obstacles: Set<Pair<Int, Int>>): (from: Pair<Int, Int>, to: Pair<Int, Int>, map: List<List<Char>>) -> Int {
        return {
               _, to, _ -> if (to in obstacles || to.first !in 0..<size.first || to.second !in 0..<size.second) Int.MAX_VALUE else 1
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
