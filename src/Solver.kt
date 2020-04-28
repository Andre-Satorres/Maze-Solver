import java.io.File
import java.lang.Exception
import java.lang.StringBuilder

data class Solver(var filename: String) {
    companion object{
        const val ENTERING_CHAR = 'I'
        const val EXITING_CHAR  = 'O'
        const val WALL_CHAR = '#'
        const val PATH_CHAR = '*'
        const val WRONG_PATH = '!'
        val SPECIAL_CHARS = arrayOf(WALL_CHAR, WRONG_PATH)
    }

    private var entering_coord = Coordinate(0, 0)
    private var exit_coord = Coordinate(0, 0)
    private var matrix = ArrayList<CharArray>()
    private var visitedCoordinates = arrayListOf<Coordinate>()

    private fun readMatrixFromFile() {
        val file = File(filename)
        file.forEachLine { matrix.add(it.toCharArray()) }
    }

    private fun Char.isOnFirstRow(): Boolean = matrix[0].contains(this)
    private fun Char.isOnLastRow(): Boolean = matrix[matrix.lastIndex].contains(this)
    private fun Char.isOnFirstLine(): Boolean = matrix.all { it[0] == this }
    private fun Char.isOnLastLine(): Boolean = matrix.all { it[it.lastIndex] == this }

    private fun Char.isInAValidEnteringPosition() = (this.isOnFirstLine() || this.isOnFirstRow() || this.isOnLastLine() || this.isOnLastRow())
    private fun Char.isInAValidExitPosition() = (this.isOnFirstLine() || this.isOnFirstRow() || this.isOnLastLine() || this.isOnLastRow())

    private fun getEnteringCoordinate(): Coordinate? {
        if (ENTERING_CHAR.isInAValidEnteringPosition()){
            matrix.forEach {
                if (it.contains(ENTERING_CHAR)) {
                    return Coordinate(it.indexOf(ENTERING_CHAR), matrix.indexOf(it)) // x and y
                }
            }
        }
        else{
            throw Exception("There's no entering in the borders of this maze. Please, insert one!")
        }

        return null
    }
    private fun getExitCoordinate(): Coordinate? {
        if (EXITING_CHAR.isInAValidExitPosition()){
            matrix.forEach {
                if (it.contains(EXITING_CHAR)) {
                    return Coordinate(it.indexOf(EXITING_CHAR), matrix.indexOf(it)) // x and y
                }
            }
        }
        else{
            throw Exception("There's no exit in the borders of this maze. Please, insert one!")
        }

        return null
    }

    private fun ArrayList<CharArray>.height() = this.size - 1
    private fun ArrayList<CharArray>.width() = this[0].size - 1

    private fun Coordinate.notMatchesAnySpecialChar(): Boolean{
        for (i in SPECIAL_CHARS){
            if(i == matrix[this.y][this.x])
                return false
        }

        return true
    }

    private fun Coordinate.isNotVisited() = !visitedCoordinates.contains(this)

    private fun Coordinate.canGoLeft()  = this.x > 0 && this.left().notMatchesAnySpecialChar() && this.left().isNotVisited()
    private fun Coordinate.canGoRight() = this.x < matrix.width() && this.right().notMatchesAnySpecialChar()  && this.right().isNotVisited()// "width"
    private fun Coordinate.canGoUp()    = this.y > 0 && this.up().notMatchesAnySpecialChar() && this.up().isNotVisited()
    private fun Coordinate.canGoDown()  = this.y < matrix.height() && this.down().notMatchesAnySpecialChar() && this.down().isNotVisited()// "height"

    private fun Coordinate.isValid() = (x >= 0) && (x <= matrix.width()) && (y >= 0) && (y <= matrix.height())

    private fun ArrayList<CharArray>.getElement(c: Coordinate): Char {
        if (c.isValid()) {
            return this[c.y][c.x]
        }

        throw Exception("Tried to access invalid index!! -> $c")
    }

    private fun ArrayList<CharArray>.setElement(c: Coordinate, value: Char) {
        this[c.y][c.x] = value
    }

    private fun ArrayList<CharArray>.isNotEnterOrExit(c: Coordinate) = this.getElement(c) != ENTERING_CHAR && this.getElement(c) != EXITING_CHAR

    private fun Coordinate.addToVisited() {
        if (!visitedCoordinates.contains(this))
            visitedCoordinates.add(this)

        if(matrix.isNotEnterOrExit(this))
            matrix.setElement(this, PATH_CHAR)
    }
    private fun Coordinate.removeFromVisited(){
        visitedCoordinates.remove(this)
        matrix.setElement(this, '!')
    }

    private fun Coordinate.goBack(){
        this.removeFromVisited()
        this.setNewCoordenate(visitedCoordinates[visitedCoordinates.lastIndex])
    }

    fun initAndSolve(){
        this.readMatrixFromFile()
        this.entering_coord = getEnteringCoordinate()!! // if there's no Entering or Exit, it will had thrown an exception
        this.exit_coord = getExitCoordinate()!!

        this.solve(entering_coord)

        print("Path: $visitedCoordinates\n")
    }

    private fun solve(coordinate: Coordinate) {

        if (matrix.getElement(coordinate) == EXITING_CHAR) {
            coordinate.addToVisited()
            return
        }

        when {
            coordinate.canGoLeft() -> {
                coordinate.addToVisited()
                solve(coordinate.left())
            }
            coordinate.canGoRight() -> {
                coordinate.addToVisited()
                solve(coordinate.right())
            }
            coordinate.canGoUp() -> {
                coordinate.addToVisited()
                solve(coordinate.up())
            }
            coordinate.canGoDown() -> {
                coordinate.addToVisited()
                solve(coordinate.down())
            }
            else -> {
                // Can't go anywhere, got stuck!
                // Remove from visited and go back!
                coordinate.goBack()
                solve(coordinate)
            }
        }
    }

    fun printMaze() {
        matrix.forEach { println(it) }
    }
}