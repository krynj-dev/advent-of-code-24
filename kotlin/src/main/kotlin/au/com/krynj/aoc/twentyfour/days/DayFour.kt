package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCAlgorithmUtil.findAround
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sign

class DayFour : AoCDay<List<String>>, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()

    override fun getDay(): Int {
        return 4
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        var result = 0
        inputLines.forEachIndexed { y, str ->
            str.forEachIndexed { x, c ->
                if (c == 'X') {
                    val mLocs = findAround(y, x, inputLines, 'M', 1)
                    val aLocs = mLocs.flatMap {
                        findAround(it.first, it.second, inputLines, 'A', 1)
                            .filter { that ->
                                Pair(
                                    (y - it.first).sign,
                                    (x - it.second).sign
                                ) == Pair((it.first - that.first).sign, (it.second - that.second).sign)
                            }
                    }
                    val sLocs = aLocs.flatMap {
                        findAround(it.first, it.second, inputLines, 'S', 1)
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
                    val mLocs = findAround(y, x, inputLines, 'M', 1)
                    val sLocs = findAround(y, x, inputLines, 'S', 1)
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
        println("Part 1: " + addColour("%d", AoCConsoleColours.GREEN)
            .format(partOne(AoCUtil.readResourceFile("dayfour/input.txt"))))
        println("Part 2: " + addColour("%d", AoCConsoleColours.GREEN)
            .format(partTwo(AoCUtil.readResourceFile("dayfour/input.txt"))))
    }

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach { it.notify(partialResult) }
    }
}