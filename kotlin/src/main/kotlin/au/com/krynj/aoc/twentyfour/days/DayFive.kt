package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.DirectedGraph
import java.math.BigInteger

class DayFive: AoCDay<List<List<String>>>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Five", CYAN))
        println("Part 1: " + addColour("%d", GREEN)
            .format(partOne(AoCUtil.readResourceFile("dayfive/input.txt", ""))))
        println("Part 2: " + addColour("%d", GREEN)
            .format(partTwo(AoCUtil.readResourceFile("dayfive/input.txt", ""))))
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
                val sorted = graph.topologicalSort()!!
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