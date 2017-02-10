/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Makroefi
 */
public class Maze {

    Graphics2D g;
    ArrayList<IMaze> listeners = new ArrayList<>();

    int cols, rows;
    int w;
    ArrayList<Cell> grid = new ArrayList<>();
    Cell current, prev;
    Stack<Cell> stack = new Stack<>();

    public Maze(Graphics2D g, int cols, int rows, int w) {
        this.g = g;
        this.cols = cols;
        this.rows = rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell newCell = new Cell(g, this, i, j, w);
                grid.add(newCell);
            }
        }

        current = grid.get(0);
        current.genVisited = true;
        current.current = true;
        prev = current;
    }

    public void ClearMaze() {
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).ClearCell();
        }
        current = grid.get(0);
        prev = current;
        UpdateGraphics();
    }

    public void ResetMaze() {
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).ResetCell();
        }
        current = grid.get(0);
        current.current = true;
        prev = current;
        UpdateGraphics();
        for (IMaze maze : listeners) {
            maze.OnResetEnd();
        }
    }

    public void UpdateGraphics() {
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).UpdateGraphics();
        }
    }

    public void UpdateCurrentCellGraphic() {
        prev.UpdateGraphics();
        current.UpdateGraphics();
    }

    public void Generate() throws InterruptedException {

        for (IMaze maze : listeners) {
            maze.OnGenerateStart();
        }

        Thread generate = new Thread() {
            public void run() {
                try {
                    while (true) {
                        current.genVisited = true;
                        current.current = true;

                        //UpdateGraphics();
                        UpdateCurrentCellGraphic();

                        Cell next = current.genCheckNeighbors();
                        if (next != null) {
                            removeWalls(current, next);

                            stack.push(current);

                            current.current = false;

                            prev = current;
                            current = next;
                        } else if (!stack.isEmpty()) {
                            current.current = false;

                            prev = current;
                            current = stack.pop();
                        } else {
                            break;
                        }
                        if (current.gridX == cols - 1 && current.gridY == rows - 1) {
                            current.goal = true;
                        }

                        Thread.sleep(10);
                    }

                    for (IMaze maze : listeners) {
                        maze.OnGenerateEnd();
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        // start generate maze
        generate.start();
    }

    public void Search() throws InterruptedException {

        for (IMaze maze : listeners) {
            maze.OnSearchStart();
        }

        Thread search = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        current.searchVisited = true;
                        current.current = true;

                        //UpdateGraphics();
                        UpdateCurrentCellGraphic();

                        Cell next = current.searchCheckNeighbors();
                        if (next != null) {
                            stack.push(current);
                            current.current = false;

                            prev = current;
                            current = next;
                        } else if (!stack.isEmpty()) {
                            current.current = false;

                            prev = current;
                            current = stack.pop();
                        } else {
                            break;
                        }

                        if (current.goal == true) {
                            current.current = true;
                            while (!stack.isEmpty()) {
                                stack.pop().isPath = true;
                            }
                            UpdateGraphics();
                            break;
                        }

                        Thread.sleep(10);
                    }

                    for (IMaze maze : listeners) {
                        maze.OnSearchEnd();
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };

        // start search for solution
        search.start();
    }

    public void AStarSearch() throws InterruptedException {
        for (IMaze maze : listeners) {
            maze.OnSearchStart();
        }

        Thread aStarSearch = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        current.searchVisited = true;
                        current.current = true;

                        //UpdateGraphics();
                        UpdateCurrentCellGraphic();

                        Cell next = current.aStarSearchCheckNeighbors();
                        if (next != null) {
                            stack.push(current);
                            current.current = false;

                            prev = current;
                            current = next;
                        } else if (!stack.isEmpty()) {
                            current.current = false;

                            prev = current;
                            current = stack.pop();
                        } else {
                            break;
                        }

                        if (current.goal == true) {
                            current.current = true;
                            while (!stack.isEmpty()) {
                                stack.pop().isPath = true;
                            }
                            UpdateGraphics();
                            break;
                        }

                        Thread.sleep(10);
                    }

                    for (IMaze maze : listeners) {
                        maze.OnSearchEnd();
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };

        // start search
        aStarSearch.start();
    }

    void removeWalls(Cell current, Cell next) {
        int x = current.gridX - next.gridX;
        int y = current.gridY - next.gridY;

        // next is on the left side
        if (x == 1) {
            current.SetLeftWall(false);
            next.SetRightWall(false);
        } else if (x == -1) {
            current.SetRightWall(false);
            next.SetLeftWall(false);
        }

        // next is on top
        if (y == 1) {
            current.SetTopWall(false);
            next.SetBottomWall(false);
        } else if (y == -1) {
            current.SetBottomWall(false);
            next.SetTopWall(false);
        }
    }

    public void addListener(IMaze maze) {
        listeners.add(maze);
    }

}
