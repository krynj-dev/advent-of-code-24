package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.AoCObserverContext
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.RED
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.DirectedGraph
import au.com.krynj.aoc.util.ds.DirectedGraph.GraphNode
import au.com.krynj.aoc.util.ds.Tuple
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.measureTime

class DaySixteen : AoCDay<List<String>, BigInteger>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Sixteen", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-16.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-16.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 16
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        val maze = parseInput(inputLines)
        var start: Pair<Int, Int>? = null
        var end: Pair<Int, Int>? = null
        inputLines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == 'S') start = Pair(y, x)
                if (c == 'E') end = Pair(y, x)
            }
        }
//        val res = shortestPath(maze, maze.getNodeByValue(start!!)!!, maze.getNodeByValue(end!!)!!)
        val res = newShortest(inputLines)
//        inputLines.forEachIndexed { y, row ->
//            println(row.mapIndexed { x, c -> if (Pair(y, x) in res.second.map { it.value }) addColour(c.toString(), RED) else c }.joinToString(""))
//        }
        return res[end]!!.first.toBigInteger()
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        // 431 too high, 412 too low
        var start: Pair<Int, Int>? = null
        var end: Pair<Int, Int>? = null
        inputLines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == 'S') start = Pair(y, x)
                if (c == 'E') end = Pair(y, x)
            }
        }
        val res = newShortest(inputLines)
        val theSet = getVisited(res, end!!, start!!)
        inputLines.forEachIndexed { y, row ->
            println(row.mapIndexed { x, c -> if (Pair(y, x) in theSet) addColour(c.toString(), RED) else c }
                .joinToString(""))
        }
        return theSet.size.toBigInteger()
    }

    fun getVisited(
        theStuff: Map<Pair<Int, Int>, Pair<Int, Set<Pair<Int, Int>>>>,
        finish: Pair<Int, Int>, start: Pair<Int, Int>
    ): Set<Pair<Int, Int>> {
        val q: Queue<Pair<Int, Int>> = LinkedList()
        val s: MutableSet<Pair<Int, Int>> = mutableSetOf()
        q.add(finish)
        while (q.isNotEmpty()) {
            val n = q.poll()
            s.add(n)
            if (n != start) {
                q.addAll(theStuff[n]!!.second)
                println(theStuff[n]!!.second.map { theStuff[it]!!.first - theStuff[n]!!.first })
            }
        }
        return s
    }

    override fun addObserver(observer: AoCObserver<AoCObserverContext>) {
        observers.add(observer)
    }

    fun parseInput(inputLines: List<String>): DirectedGraph<Pair<Int, Int>> {
        val graph: DirectedGraph<Pair<Int, Int>> = DirectedGraph()
        val q: Queue<Pair<Int, Int>> = LinkedList()
        inputLines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c in ".SE") {
                    val coord = Pair(y, x)
                    q.add(coord)
                    graph.addNode(coord)
                }
            }
        }
        val chars = inputLines.map { it.toCharArray().toList() }
        while (q.isNotEmpty()) {
            val n = q.poll()
            val vals = (AoCAlgorithmUtil.findAround(n, chars, '.', includeDiagonals = false) +
                    AoCAlgorithmUtil.findAround(n, chars, 'S', includeDiagonals = false) +
                    AoCAlgorithmUtil.findAround(n, chars, 'E', includeDiagonals = false)).filter { it != n }
            vals.forEach { graph.addEdge(graph.getNodeByValue(n)!!, graph.getNodeByValue(it)!!) }
        }
        return graph
    }

    fun newShortest(inputLines: List<String>): Map<Pair<Int, Int>, Pair<Int, Set<Pair<Int, Int>>>> {
        var start: Pair<Int, Int>? = null
        var end: Pair<Int, Int>? = null
        val locs: MutableSet<Pair<Int, Int>> = mutableSetOf()
        inputLines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c in ".SE") locs.add(Pair(y, x))
                if (c == 'S') start = Pair(y, x)
                if (c == 'E') end = Pair(y, x)
            }
        }
        val distanceMap: MutableMap<Pair<Int, Int>, Pair<Int, MutableSet<Pair<Int, Int>>>> =
            mutableMapOf()
        locs.forEach { l ->
            distanceMap[l] = if (l == start) Pair(
                0, mutableSetOf(Pair(start!!.first, start!!.second - 1))
            ) else Pair(Int.MAX_VALUE, mutableSetOf())
        }
        distanceMap[Pair(start!!.first, start!!.second+1)] = Pair(1, mutableSetOf(start!!))
        distanceMap[Pair(start!!.first+1, start!!.second)] = Pair(1001, mutableSetOf(start!!))
        distanceMap[Pair(start!!.first-1, start!!.second)] = Pair(1001, mutableSetOf(start!!))
        val unvisited: PriorityQueue<Pair<Pair<Int, Int>, Int>> =
            PriorityQueue { o1, o2 ->
                o1.second.compareTo(o2.second)
            }
        unvisited.addAll(locs.map {
            Pair(it, distanceMap[it]!!.first)
        })
        while (unvisited.peek() != null && unvisited.peek().second != Int.MAX_VALUE && unvisited.peek().first != end) {
            // remove node
            val qn = unvisited.poll()
            val node = qn.first
            // look at unvisited children
            val ggg = AoCAlgorithmUtil.findAround(node, inputLines.map { it.toCharArray().toList() }, {
                    c: Char -> c in ".SE"
            }, range=1, includeDiagonals=false)
            for (fr in ggg) {
                for (to in ggg) {
                    if (fr != to && node !in listOf(fr, to) && distanceMap[fr]!!.first != Int.MAX_VALUE) {
                        val isDiag = fr.first - to.first != 0 && fr.second - to.second != 0
                        val dist = (if (isDiag) 1001 else 1) + distanceMap[node]!!.first
                        if (dist < distanceMap[to]!!.first) {
                            // New lower, replace
                            distanceMap[to] = Pair(dist, mutableSetOf(node))
                            if (to in unvisited.map { it.first }) {
                                val r = unvisited.removeIf { it.first == to }
                                if (r) {
                                    unvisited.add(Pair(to, distanceMap[to]!!.first))
                                }
                            }
                        } else if (dist == distanceMap[to]!!.first) {
                            distanceMap[to]!!.second.add(node)
                        }
                    }
                }
            }
        }
        return distanceMap
    }

    fun shortestPath(
        graph: DirectedGraph<Pair<Int, Int>>,
        from: GraphNode<Pair<Int, Int>>,
        to: GraphNode<Pair<Int, Int>>
    ): MutableMap<GraphNode<Pair<Int, Int>>, Pair<Int, MutableSet<GraphNode<Pair<Int, Int>>>>> {
        val distanceMap: MutableMap<GraphNode<Pair<Int, Int>>, Pair<Int, MutableSet<GraphNode<Pair<Int, Int>>>>> =
            mutableMapOf()
        graph.nodes.values.forEach {
            distanceMap[it] = if (it == from) Pair(
                0, mutableSetOf(GraphNode(Pair(from.value.first, from.value.second - 1)))
            ) else Pair(Int.MAX_VALUE, mutableSetOf())
        }
        val unvisited: PriorityQueue<Pair<GraphNode<Pair<Int, Int>>, Int>> =
            PriorityQueue { o1, o2 ->
                o1.second.compareTo(o2.second)
            }
        unvisited.addAll(graph.nodes.values.map {
            Pair(it, distanceMap[it]!!.first)
        })
        while (unvisited.peek() != null && unvisited.peek().second != Int.MAX_VALUE) {
            val n = unvisited.poll()
            if (n.first.value == Pair(7, 4)) {
                println("ASD")
            }
            val prev = distanceMap[n.first]!!.second
            for (last in prev) {
                for (x in graph.nodeChildren(n.first)) {
                    if (x.value == Pair(7, 4)) {
                        println("ASD")
                    }
                    val isDiag = last.value.first - x.value.first != 0 && last.value.second - x.value.second != 0
                    val dist = (if (isDiag) 1001 else 1) + distanceMap[n.first]!!.first
                    if (dist < distanceMap[x]!!.first) {
                        distanceMap[x] = Pair(dist, mutableSetOf(n.first))
                        if (x.value in unvisited.map { it.first.value }) {
                            val r = unvisited.removeIf { it.first == x }
                            if (r) unvisited.add(Pair(x, distanceMap[x]!!.first))
                        }
                    } else if (dist == distanceMap[x]!!.first) {
                        distanceMap[x]!!.second.add(n.first)
                        if (x.value in unvisited.map { it.first.value }) {
                            val r = unvisited.removeIf { it.first == x }
                            if (r) unvisited.add(Pair(x, distanceMap[x]!!.first))
                        }
                    }
                }
            }
        }
        return distanceMap
    }

    override fun broadcast(context: AoCObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

}
