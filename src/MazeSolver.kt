fun main() {
    println("Welcome to MazeSolver!")
    println("----------------------")
    println()

    while (true) {
        print("Please insert the file path (Just type [ENTER] to use the default file inside 'files' folder): ")
        var filePath = readLine()
        println()

        try {
            if(filePath.isNullOrBlank())
                filePath = "src/files/maze.txt"

            val s = Solver(filePath)

            print("Do you wish to show the solved maze? [Y/N]: ")
            val wish = readLine()!!

            if (wish.equals("N", ignoreCase = true))
                s.initAndSolve(false)
            else
                s.initAndSolve()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        println("------------------------------------------")
        print("\nDo you want to solve another maze? [Y/N]: ")
        val answer = readLine()!!

        if (answer.equals("N", ignoreCase = true)){
            println("Exiting ...")
            break
        }
    }
}