package com.dam2.proyecto2048.clases

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

class Tablero() {

    // Singleton real
    companion object {
        private var INSTANCE: Tablero? = null
        fun get(): Tablero {
            if (INSTANCE == null) {
                INSTANCE = Tablero()
            }
            return INSTANCE!!
        }
    }

    var board = MutableList(4){ MutableList(4){0} }

    fun reset(){
        board = MutableList(4){ MutableList(4){0} }
    }

    enum class Direction { UP, DOWN, LEFT, RIGHT }

    fun move(direction: Direction){

        val rotatedBoard = when (direction) {
            Direction.RIGHT -> board
            Direction.LEFT  -> rotate180(board)        // girar 180°
            Direction.UP    -> rotateRight(board)      // girar 90° a derecha
            Direction.DOWN  -> rotateLeft(board)       // girar 90° a izquierda
        }.map { it.toMutableList() }.toMutableList()

        // Ejecutar el movimiento siempre como RIGHT
        mergeRight(rotatedBoard)

        // Rotar nuevamente a la postura original
        val result = when (direction) {
            Direction.RIGHT -> rotatedBoard
            Direction.LEFT  -> rotate180(rotatedBoard)
            Direction.UP    -> rotateLeft(rotatedBoard)     // inversa: left
            Direction.DOWN  -> rotateRight(rotatedBoard)    // inversa: right
        }

        board = result

        Log.i("GAME", "TABLERO MERGEADO")

        spawnNewCells()
    }

    fun rotateRight(board: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val size = board.size
        val rotated = MutableList(size) { MutableList(size) { 0 } }

        for (i in 0 until size) {
            for (j in 0 until size) {
                rotated[j][size - 1 - i] = board[i][j]
            }
        }

        return rotated
    }

    fun rotate180(board: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        return rotateRight(rotateRight(board))
    }


    fun rotateLeft(board: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val size = board.size
        val rotated = MutableList(size) { MutableList(size) { 0 } }

        for (i in 0 until size) {
            for (j in 0 until size) {
                rotated[size - 1 - j][i] = board[i][j]
            }
        }

        return rotated
    }

    fun mergeRight(board: MutableList<MutableList<Int>>) {

        for (row in 0 until 4) {

            // 1. Filtrar ceros (compactar hacia la derecha)
            val filtered = board[row].filter { it != 0 }.toMutableList()

            // 2. Rellenar con ceros a la izquierda para mantener tamaño 4
            while (filtered.size < 4) {
                filtered.add(0, 0)
            }

            // 3. Combinar desde la derecha hacia la izquierda
            for (col in 3 downTo 1) {
                if (filtered[col] != 0 && filtered[col] == filtered[col - 1]) {
                    filtered[col] *= 2
                    filtered[col - 1] = 0
                }
            }

            // 4. Compactar otra vez (quitar los ceros del medio)
            val compacted = filtered.filter { it != 0 }.toMutableList()
            while (compacted.size < 4) {
                compacted.add(0, 0)
            }

            // 5. Volcar la fila resultante en el tablero original
            for (col in 0 until 4) {
                board[row][col] = compacted[col]
            }
        }
    }

    fun spawnNewCells(){
        val gen2Change = 0.5

        var cells = mutableListOf<Pair<Int, Int>>()

        for (i in 0..board.size-1){
            for (j in 0..board[i].size-1){
                if (board[i][j] == 0){
                    cells.add(Pair(i,j))
                }
            }
        }

        if (cells.size < 2){
            return
        }

        for (i in 0..1){
            var cell = cells.get(Random.nextInt(cells.size))
            cells.removeAt(Random.nextInt(cells.size))

            if (Random.nextDouble(1.0) < 0.5){
                board[cell.first][cell.second] = 2
            }else board[cell.first][cell.second] = 4
        }
    }
}