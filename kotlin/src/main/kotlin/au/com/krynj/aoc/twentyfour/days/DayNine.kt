package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.math.min
import kotlin.time.measureTime

class DayNine : AoCDay<String, BigInteger>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Nine", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-9.txt").first())
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-9.txt").first())
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 9
    }

    override fun partOne(inputLines: String): BigInteger {
        val idString = getIdString(inputLines)
        return calcChecksum(compactFiles(idString))
    }

    override fun partTwo(inputLines: String): BigInteger {
        val files: MutableList<Pair<Int, Int>> = mutableListOf()
        val spaces: MutableList<Int> = mutableListOf()
        inputLines.indices.forEach { if (it % 2 == 0) files.add(Pair(it / 2, inputLines[it].digitToInt())) else spaces.add(inputLines[it].digitToInt()) }
        val compactedFiles = compactFilesWhole(files, spaces)
        return calcChecksum(compactedFiles)
    }

    override fun addObserver(observer: AoCObserver<SimpleObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: SimpleObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

    fun compactFiles(idString: List<String>): List<String> {
        var lPos = 0
        var rPos = idString.size - 1
        val dotCount = idString.count { it == "." }
        var allocated = 0
        val x: MutableList<String> = mutableListOf()
        while (allocated < dotCount) {
            while (idString[lPos] != "." && lPos <= rPos) {
                x.add(lPos, idString[lPos])
                lPos++
            }
            while (idString[rPos] == "." && lPos <= rPos) {
                rPos--
            }
            if (lPos >= rPos) break
            x.add(lPos, idString[rPos])
            lPos++
            rPos--
            allocated++
        }
        return x
    }

    fun compactFilesWhole(files: MutableList<Pair<Int, Int>>, spaces: MutableList<Int>): List<String> {
        val filesCopy: MutableList<Pair<Int, Int>> = mutableListOf()
        filesCopy.addAll(files)
        val spacesCopy: MutableList<Int> = mutableListOf()
        spacesCopy.addAll(spaces)
        val visited: MutableSet<Int> = mutableSetOf()
        var rPos = filesCopy.size-1
        while (visited.size < files.size) {
            // Move left until next unchecked file
            while (filesCopy[rPos].first in visited) {
                rPos--
            }
            // find slot to move file to
            visited.add(filesCopy[rPos].first)
            val openSlotInd = spacesCopy.indexOfFirst { it >= filesCopy[rPos].second }
            if (openSlotInd in 0..<rPos) {
                val toMove = filesCopy[rPos]
                filesCopy.removeAt(rPos)
                filesCopy.add(openSlotInd+1, toMove)
                spacesCopy[openSlotInd] -= toMove.second
                spacesCopy.add(openSlotInd, 0)
                spacesCopy[rPos] += toMove.second
                if (rPos + 1 < spacesCopy.size) {
                    spacesCopy[rPos] += spacesCopy[rPos+1]
                    spacesCopy.removeAt(rPos+1)
                }
            }
        }
        return filesCopy.indices.flatMap { i ->
            (0..<filesCopy[i].second).map { filesCopy[i].first.toString() } +
                    if (i in spacesCopy.indices) (0..<spacesCopy[i]).map { "." } else listOf()
        }
    }

    fun getIdString(zippedString: String): List<String> {
        return zippedString.flatMapIndexed { index, c ->
            if (index % 2 == 0) {
                List(c.digitToInt()) {"${index / 2}"}

            } else {
                List(c.digitToInt()) {"."}
            }
        }
    }

    fun calcChecksum(string: List<String>): BigInteger {
        var checksum = BigInteger.ZERO
        string.forEachIndexed { index, c ->
            if (string[index] != ".") checksum += BigInteger(c).multiply(index.toBigInteger())
        }
        return checksum
    }
}
