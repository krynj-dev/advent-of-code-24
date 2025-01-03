package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.DirectedGraph
import java.math.BigInteger
import kotlin.time.measureTime

class DayFive: AoCDay<List<List<String>>, BigInteger>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Five", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-5.txt", ""))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-5.txt", ""))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 5
    }

    override fun partOne(inputLines: List<List<String>>): BigInteger {
        val rules = inputLines[0].map { AoCUtil.readLineAsInt(it, '|') }
        val updates = inputLines[1].map { AoCUtil.readLineAsInt(it, ',') }

        var result = 0
        updates.forEach {
            val locations = locationMap(it)
            if (isValid(locations, rules)) result += it[it.size / 2]
        }
        return result.toBigInteger()
    }

    fun locationMap(intList: List<Int>): HashMap<Int, Int> {
        val theMap: HashMap<Int, Int> = HashMap()
        intList.forEachIndexed { index, i -> theMap[i] = index }
        return theMap
    }

    fun isValid(locations: HashMap<Int, Int>, rules: List<List<Int>>): Boolean {
        return rules.map { rule -> !(locations.containsKey(rule[0]) && locations.containsKey(rule[1]))
                || (locations[rule[0]]!! < locations[rule[1]]!!)}.all { it }
    }

    override fun partTwo(inputLines: List<List<String>>): BigInteger {
        val rules = inputLines[0].map { AoCUtil.readLineAsInt(it, '|') }
        val updates = inputLines[1].map { AoCUtil.readLineAsInt(it, ',') }
        var result = 0
        updates.forEach{
            val applicableRules = rules.filter { r -> r.first() in it && r.last() in it }
            if (!isValid(locationMap(it), applicableRules)) {
                val nodeMap: MutableMap<Int, DirectedGraph.GraphNode<Int>> = mutableMapOf()
                val graph = DirectedGraph<Int>()
                applicableRules.forEach { r ->
                    if (r.first() !in nodeMap.keys) nodeMap[r.first()] = graph.addNode(r.first())
                    if (r.last() !in nodeMap.keys) nodeMap[r.last()] = graph.addNode(r.last())
                    graph.addEdge(nodeMap[r.first()]!!, nodeMap[r.last()]!!)
                }
                val sorted = graph.topologicalSort { a, b -> a.value.compareTo(b.value) }!!
                result += sorted[sorted.size / 2].value
            }
        }

        return result.toBigInteger()
    }

    override fun addObserver(observer: AoCObserver<SimpleObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: SimpleObserverContext) {
        observers.forEach { it.notify(context) }
    }


}