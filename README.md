# Maze-depthfirst-java
Generating maze using depth-first search algorithm, implemented in Java.

This is a pretty simple implementation of generating maze with depth-first search algorithm using backtracking method.

Pseudocode (adopted from good old Wikipedia):

    1. Choose the initial cell, mark it as visited and push it to the stack
    2. While the stack is not empty
        1. Pop a cell from the stack and make it a current cell
        2. If the current cell has any neighbours which have not been visited
            1. Push the current cell to the stack
            2. Choose one of the unvisited neighbours
            3. Remove the wall between the current cell and the chosen cell
            4. Mark the chosen cell as visited and push it to the stack
