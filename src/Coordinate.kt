data class Coordinate (var x: Int, var y: Int)
{
    fun setNewCoordenate(newCoordinate: Coordinate){
        this.x = newCoordinate.x
        this.y = newCoordinate.y
    }

    fun goToTheLeft() {
        x--
    }

    fun goToTheRight() {
        x++
    }

    fun goUp() {
        y--
    }

    fun goDown() {
        y++
    }

    fun left()  = Coordinate(x - 1, y)

    fun right() = Coordinate(x + 1, y)

    fun up()    = Coordinate(x, y - 1)

    fun down()  = Coordinate(x, y + 1)
}