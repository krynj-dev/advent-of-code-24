package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCAlgorithmUtil.findAround
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sign
import kotlin.time.measureTime

class DayFour : AoCDay<List<List<Char>>>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun getDay(): Int {
        return 4
    }

    override fun partOne(inputLines: List<List<Char>>): BigInteger {
        var result = 0
        inputLines.forEachIndexed { y, str ->
            str.forEachIndexed { x, c ->
                if (c == 'X') {
                    val mLocs = findAround(Pair(y, x), inputLines, 'M', 1)
                    val aLocs = mLocs.flatMap {
                        findAround(it, inputLines, 'A', 1)
                            .filter { that ->
                                Pair(
                                    (y - it.first).sign,
                                    (x - it.second).sign
                                ) == Pair((it.first - that.first).sign, (it.second - that.second).sign)
                            }
                    }
                    val sLocs = aLocs.flatMap {
                        findAround(it, inputLines, 'S', 1)
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

    override fun partTwo(inputLines: List<List<Char>>): BigInteger {
        var result = 0
        inputLines.forEachIndexed { y, str ->
            str.forEachIndexed { x, c ->
                if (c == 'A') {
                    val mLocs = findAround(Pair(y, x), inputLines, 'M', 1)
                    val sLocs = findAround(Pair(y, x), inputLines, 'S', 1)
                    val crosses =
                        mLocs.filter { sLocs.any { that -> abs(it.first - that.first) == 2 && abs(it.second - that.second) == 2 } }
                    if (crosses.size == 2) result += 1
                }
            }
        }
        return result.toBigInteger()
    }

    override fun run() {
        println(addColour("Day Four", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-4.txt").map { it.toCharArray().toList() })
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-4.txt").map { it.toCharArray().toList() })
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun addObserver(observer: AoCObserver<SimpleObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: SimpleObserverContext) {
        observers.forEach { it.notify(context) }
    }
}