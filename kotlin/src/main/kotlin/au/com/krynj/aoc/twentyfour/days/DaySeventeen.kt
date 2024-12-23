package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.AoCObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.collections.ArrayList
import kotlin.time.measureTime

class DaySeventeen : AoCDay<List<List<String>>, String>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Seventeen", CYAN))
        val result1: String
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-17.txt", ""))
        }
        println("Part 1: ${addColour(result1, GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: String
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-17.txt", ""))
        }
        println("Part 2: ${addColour(result2, GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 17
    }

    override fun partOne(inputLines: List<List<String>>): String {
        val initA = """\d+""".toRegex().find(inputLines.first().first())?.value!!.toInt()
        val initB = """\d+""".toRegex().find(inputLines.first()[1])?.value!!.toInt()
        val initC = """\d+""".toRegex().find(inputLines.first()[2])?.value!!.toInt()
        val mem = SeventeenMemory(initA, initB, initC)
        runProgram(mem, inputLines.last().first().split(" ").last().split(",").map { it.toInt() })
        return mem.stdOut.joinToString(",")
    }

    override fun partTwo(inputLines: List<List<String>>): String {
        val mem = SeventeenMemory(0, 0, 0)
        runProgram(mem, inputLines.last().first().split(" ").last().split(",").map { it.toInt() })
        return mem.stdOut.joinToString(",")
    }

    class SeventeenMemory(var a: Int = 0, var b: Int = 0, var c: Int = 0) {
        var instructionPointer = 0
        var stdOut: MutableList<Int> = mutableListOf()
        val theList: MutableList<Pair<Int, Int>> = mutableListOf()
    }

    fun runProgram(mem: SeventeenMemory, program: List<Int>) {
        while (mem.instructionPointer < program.size-1) {
            val opcode = program[mem.instructionPointer]
            val operand = program[mem.instructionPointer+1]
            mem.theList.add(opcode to operand)
            when (opcode) {
                0 -> adv(mem, operand)
                1 -> bxl(mem, operand)
                2 -> bst(mem, operand)
                3 -> jnz(mem, operand)
                4 -> bxc(mem, operand)
                5 -> out(mem, operand)
                6 -> bdv(mem, operand)
                7 -> cdv(mem, operand)
            }
            if (opcode != 3 || mem.a == 0) mem.instructionPointer += 2
        }
    }

    fun combo(mem: SeventeenMemory, operand: Int): Int {
        return when (operand) {
            4 -> mem.a
            5 -> mem.b
            6 -> mem.c
            else -> operand
        }
    }

    fun adv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.a = numerator/denominator
    }

    fun bxl(mem: SeventeenMemory, operand: Int) {
        mem.b = mem.b xor operand
    }

    fun bst(mem: SeventeenMemory, operand: Int) {
        mem.b = combo(mem, operand) % 8
    }

    fun jnz(mem: SeventeenMemory, operand: Int) {
        if (mem.a != 0) mem.instructionPointer = operand
    }

    fun bxc(mem: SeventeenMemory, operand: Int) {
        mem.b = mem.b xor mem.c
    }

    fun out(mem: SeventeenMemory, operand: Int) {
        mem.stdOut.add(combo(mem, operand) % 8)
    }

    fun bdv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.b = numerator/denominator
    }

    fun cdv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.c = numerator/denominator
    }

//    fun findQuine(mem: SeventeenMemory, program: List<Int>): Int {
//        mem.instructionPointer = program.size-2
//
//    }

    fun advInv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.a = numerator/denominator
    }

    fun bxlInv(mem: SeventeenMemory, operand: Int) {
        mem.b = mem.b xor operand
    }

    fun bstInv(mem: SeventeenMemory, operand: Int) {
        mem.b = combo(mem, operand) % 8
    }

    fun jnzInv(mem: SeventeenMemory, operand: Int) {
        if (mem.a != 0) mem.instructionPointer = operand
    }

    fun bxcInv(mem: SeventeenMemory, operand: Int) {
        mem.b = mem.b xor mem.c
    }

    fun outInv(mem: SeventeenMemory, operand: Int) {
        mem.stdOut.add(combo(mem, operand) % 8)
    }

    fun bdvInv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.b = numerator/denominator
    }

    fun cdvInv(mem: SeventeenMemory, operand: Int) {
        val numerator = mem.a
        val denominator = 1.shl(combo(mem, operand))
        mem.c = numerator/denominator
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
