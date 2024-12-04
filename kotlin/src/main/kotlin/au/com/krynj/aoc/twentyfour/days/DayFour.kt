package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class DayFour : AoCDay, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()

    override fun getDay(): Int {
        return 4
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        var result = 0
        inputLines.forEachIndexed { y, str ->
            str.forEachIndexed { x, c ->
                if (c == 'X') {
                    val mLocs = findAround(y, x, inputLines, 'M')
                    val aLocs = mLocs.flatMap {
                        findAround(it.first, it.second, inputLines, 'A')
                            .filter { that ->
                                Pair(
                                    (y - it.first).sign,
                                    (x - it.second).sign
                                ) == Pair((it.first - that.first).sign, (it.second - that.second).sign)
                            }
                    }
                    val sLocs = aLocs.flatMap {
                        findAround(it.first, it.second, inputLines, 'S')
                            .filter { that ->
                                Pair(
                                    (y - it.first).sign,
                                    (x - it.second).sign
                                ) == Pair((it.first - that.first).sign, (it.second - that.second).sign)
                            }
                    }
                    result += sLocs.size
                }
            }
        }
        return result.toBigInteger()
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        var result = 0
        inputLines.forEachIndexed { y, str ->
            str.forEachIndexed { x, c ->
                if (c == 'A') {
                    val mLocs = findAround(y, x, inputLines, 'M')
                    val sLocs = findAround(y, x, inputLines, 'S')
                    val crosses =
                        mLocs.filter { sLocs.any { that -> abs(it.first - that.first) == 2 && abs(it.second - that.second) == 2 } }
                    if (crosses.size == 2) result += 1
                }
            }
        }
        return result.toBigInteger()
    }

    fun findAround(y: Int, x: Int, puzzle: List<String>, target: Char): List<Pair<Int, Int>> {
        val locs: MutableList<Pair<Int, Int>> = mutableListOf()
        for (m in max(0, y - 1)..min(y + 1, puzzle.size - 1)) {
            for (l in max(0, x - 1)..min(x + 1, puzzle.first().length - 1)) {
                if (puzzle[m][l] == target) locs.add(Pair(m, l))
            }
        }
        return locs
    }

    override fun run() {
        println("Part 1: " + partOne(AoCUtil.readResourceFile("dayfour/input.txt")))
        println("Part 2: " + partTwo(AoCUtil.readResourceFile("dayfour/input.txt")))
    }

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach { it.notify(partialResult) }
    }
}