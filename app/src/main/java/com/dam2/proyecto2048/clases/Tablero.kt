package com.dam2.proyecto2048.clases

import kotlin.random.Random

class Tablero {

    companion object {
        private var INSTANCE: Tablero? = null
        fun get(): Tablero {
            if (INSTANCE == null) INSTANCE = Tablero()
            return INSTANCE!!
        }
    }

    var board = MutableList(4) { MutableList(4) { 0 } }
    var score = 0
    var dificultad = 0

    fun reset() {
        board = MutableList(4) { MutableList(4) { 0 } }
        score = 0
    }

    enum class Direction { UP, DOWN, LEFT, RIGHT }

    fun move(direction: Direction) {
        val rotatedBoard = when (direction) {
            Direction.RIGHT -> board
            Direction.LEFT -> rotate180(board)
            Direction.UP -> rotateRight(board)
            Direction.DOWN -> rotateLeft(board)
        }.map { it.toMutableList() }.toMutableList()

        mergeRight(rotatedBoard)

        val result = when (direction) {
            Direction.RIGHT -> rotatedBoard
            Direction.LEFT -> rotate180(rotatedBoard)
            Direction.UP -> rotateLeft(rotatedBoard)
            Direction.DOWN -> rotateRight(rotatedBoard)
        }

        board = result

        spawnNewCells()
    }

    fun rotateRight(board: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val size = board.size
        val rotated = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size)
            for (j in 0 until size)
                rotated[j][size - 1 - i] = board[i][j]
        return rotated
    }

    fun rotate180(board: MutableList<MutableList<Int>>) = rotateRight(rotateRight(board))

    fun rotateLeft(board: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val size = board.size
        val rotated = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size)
            for (j in 0 until size)
                rotated[size - 1 - j][i] = board[i][j]
        return rotated
    }

    fun mergeRight(board: MutableList<MutableList<Int>>) {
        for (row in 0 until 4) {
            val filtered = board[row].filter { it != 0 }.toMutableList()
            while (filtered.size < 4) filtered.add(0, 0)

            for (col in 3 downTo 1) {
                if (filtered[col] != 0 && filtered[col] == filtered[col - 1]) {
                    filtered[col] *= 2
                    score += filtered[col]
                    filtered[col - 1] = 0
                }
            }

            val compacted = filtered.filter { it != 0 }.toMutableList()
            while (compacted.size < 4) compacted.add(0, 0)

            for (col in 0 until 4) board[row][col] = compacted[col]
        }
    }

    fun spawnNewCells() {
        val prob4 = when (dificultad) {
            0 -> 0.15
            1 -> 0.30
            2 -> 0.50
            else -> 0.30
        }

        val cantidadSpawn = when (dificultad) {
            0 -> 1
            1 -> 1
            2 -> 2
            else -> 1
        }

        val cells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..3)
            for (j in 0..3)
                if (board[i][j] == 0) cells.add(Pair(i, j))

        if (cells.isEmpty()) return

        repeat(cantidadSpawn) {
            if (cells.isEmpty()) return
            val pos = cells.removeAt(Random.nextInt(cells.size))
            board[pos.first][pos.second] =
                if (Random.nextDouble() < prob4) 4 else 2
        }
    }

    fun isGameOver(): Boolean {
        for (i in 0 until 4) for (j in 0 until 4) if (board[i][j] == 0) return false
        for (i in 0 until 4) for (j in 0 until 3)
            if (board[i][j] == board[i][j + 1] || board[j][i] == board[j + 1][i]) return false
        return true
    }

    fun hasWon(): Boolean {
        for (i in 0 until 4) for (j in 0 until 4)
            if (board[i][j] == 2048) return true
        return false
    }
}
