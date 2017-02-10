/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Makroefi
 */
public class Maze {

    Graphics2D g;
    ArrayList<IMaze> listeners = new ArrayList<>();
    int sleepTime = 10;
    
    int cols, rows;
    int w;
    ArrayList<Cell> grid = new ArrayList<>();
    Cell current, prev;
    Stack<Cell> stack = new Stack<>();

    List<Set<Cell>> cellSet = new ArrayList<>();
    List<Wall> wallSet = new ArrayList<>();

    public Maze(Graphics2D g, int cols, int rows, int w) {
        this.g = g;
        this.cols = cols;
        this.rows = rows;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                Cell newCell = new Cell(g, x, y, w);
                grid.add(newCell);
            }
        }

        // initialize wall
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                Cell cell = grid.get(getIndex(x, y));

                // bottom right
                if (x == rows - 1 && y == cols - 1) {
                    // do nothing
                } else if (y == cols - 1) {
                    // right
                    cell.wall[1].SetNextCell(grid.get(getIndex(x + 1, y)));
                } else if (x == rows - 1) {
                    // bottom
                    cell.wall[3].SetNextCell(grid.get(getIndex(x, y + 1)));
                } else {
                    // right
                    cell.wall[1].SetNextCell(grid.get(getIndex(x + 1, y)));
                    // bottom
                    cell.wall[3].SetNextCell(grid.get(getIndex(x, y + 1)));
                }
            }
        }

        current = grid.get(0);
        current.genVisited = true;
        current.current = true;
        prev = current;
    }

    // initialize wall set & cell set
    public void KruskalInitialization() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                // get current cell
                Cell cell = grid.get(getIndex(x, y));
                // create hashmap represent one set
                HashSet<Cell> cellList = new HashSet();
                // address the cell's set
                cell.set = cellList;
                // add current cell to this set
                cellList.add(cell);
                // add that set to cellSet list
                cellSet.add(cellList);

                // bottom right
                if (x == rows - 1 && y == cols - 1) {
                    // do nothing
                } // most bottom of grid
                else if (y == cols - 1) {
                    // right
                    wallSet.add(cell.wall[1]);
                } // most right of grid
                else if (x == rows - 1) {
                    // bottom
                    wallSet.add(cell.wall[3]);
                } else {
                    // right
                    wallSet.add(cell.wall[1]);
                    // bottom
                    wallSet.add(cell.wall[3]);
                }
            }
        }
    }
    
    public void ClearMaze() {
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).ClearCell();
        }
        current = grid.get(0);
        current.current = true;
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
            @Override
            public void run() {
                try {
                    while (true) {
                        current.genVisited = true;
                        current.current = true;

                        //UpdateGraphics();
                        UpdateCurrentCellGraphic();

                        Cell next = genCheckNeighbors(current);
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

                        Thread.sleep(sleepTime);
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

    public void KruskalGenerate() throws InterruptedException {

        for (IMaze maze : listeners) {
            maze.OnGenerateStart();
        }

        Thread kruskalGenerate = new Thread() {
            @Override
            public void run() {
                try {
                    // this can be more optimized by reset the cell set only, redefining wall set can be avoided
                    KruskalInitialization();
                    
                    while (wallSet.size() > 1) {
                        int r = (int) (Math.random() * wallSet.size() - 1);

                        Wall wall = wallSet.get(r);
                        wallSet.remove(r);

                        // set goal cell
                        if (wall.GetNextWallCell().gridX == cols-1 && wall.GetNextWallCell().gridY == rows-1){
                            wall.GetNextWallCell().goal = true;
                        }
                        
                        // if two cells in different set devided by wall THEN merge the set and remove the wall
                        if (IsCellDevided(wall.GetCurrentWallCell(), wall.GetNextWallCell())
                                && !IsOneSet(wall.GetCurrentWallCell(), wall.GetNextWallCell())) {
                            // remove wall between two cells
                            removeWalls(wall.GetCurrentWallCell(), wall.GetNextWallCell());
                            
                            // merge the set
                            wall.GetCurrentWallCell().set.addAll(wall.GetNextWallCell().set);
                            
                            // tell all merged cell that they have new cell set
                            for (Cell cell : wall.GetNextWallCell().set) {
                                cell.set = wall.GetCurrentWallCell().set;
                            }
                            // delete unused set
                            cellSet.remove(wall.GetNextWallCell().set);

                            // mark two cells was visisted
                            wall.GetCurrentWallCell().genVisited = true;
                            wall.GetNextWallCell().genVisited = true;
                            
                            // update cells graphic
                            wall.GetCurrentWallCell().UpdateGraphics();
                            wall.GetNextWallCell().UpdateGraphics();
                            
                            Thread.sleep(sleepTime);
                        }
                    }
                    
                    UpdateCurrentCellGraphic();

                    for (IMaze maze : listeners) {
                        maze.OnGenerateEnd();
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        // start generate maze
        kruskalGenerate.start();
    }

    //===============================================================
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

                        Cell next = searchCheckNeighbors(current);
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

                        Thread.sleep(sleepTime);
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

                        Cell next = aStarSearchCheckNeighbors(current);
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

                        Thread.sleep(sleepTime);
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

    public Cell genCheckNeighbors(Cell currentCell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int leftIndex = getIndex(currentCell.gridX - 1, currentCell.gridY);
        Cell left = leftIndex > -1 ? grid.get(leftIndex) : null;

        int rightIndex = getIndex(currentCell.gridX + 1, currentCell.gridY);
        Cell right = rightIndex > -1 ? grid.get(rightIndex) : null;

        int topIndex = getIndex(currentCell.gridX, currentCell.gridY - 1);
        Cell top = topIndex > -1 ? grid.get(topIndex) : null;

        int bottomIndex = getIndex(currentCell.gridX, currentCell.gridY + 1);
        Cell bottom = bottomIndex > -1 ? grid.get(bottomIndex) : null;

        if (left != null && !left.genVisited) {
            neighbors.add(left);
        }
        if (right != null && !right.genVisited) {
            neighbors.add(right);
        }
        if (top != null && !top.genVisited) {
            neighbors.add(top);
        }
        if (bottom != null && !bottom.genVisited) {
            neighbors.add(bottom);
        }

        if (neighbors.size() > 0) {
            int r = (int) (Math.random() * neighbors.size());
            return neighbors.get(r);
        } else {
            return null;
        }
    }

    public Cell searchCheckNeighbors(Cell currentCell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int leftIndex = getIndex(currentCell.gridX - 1, currentCell.gridY);
        Cell left = leftIndex > -1 ? grid.get(leftIndex) : null;

        int rightIndex = getIndex(currentCell.gridX + 1, currentCell.gridY);
        Cell right = rightIndex > -1 ? grid.get(rightIndex) : null;

        int topIndex = getIndex(currentCell.gridX, currentCell.gridY - 1);
        Cell top = topIndex > -1 ? grid.get(topIndex) : null;

        int bottomIndex = getIndex(currentCell.gridX, currentCell.gridY + 1);
        Cell bottom = bottomIndex > -1 ? grid.get(bottomIndex) : null;

        if (left != null && !left.searchVisited && !IsCellDevided(currentCell, left)) {
            neighbors.add(left);
        }
        if (right != null && !right.searchVisited && !IsCellDevided(currentCell, right)) {
            neighbors.add(right);
        }
        if (top != null && !top.searchVisited && !IsCellDevided(currentCell, top)) {
            neighbors.add(top);
        }
        if (bottom != null && !bottom.searchVisited && !IsCellDevided(currentCell, bottom)) {
            neighbors.add(bottom);
        }

        if (neighbors.size() > 0) {
            int r = (int) (Math.random() * neighbors.size());
            return neighbors.get(r);
        } else {
            return null;
        }
    }

    public Cell aStarSearchCheckNeighbors(Cell currentCell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int leftIndex = getIndex(currentCell.gridX - 1, currentCell.gridY);
        Cell left = leftIndex > -1 ? grid.get(leftIndex) : null;

        int rightIndex = getIndex(currentCell.gridX + 1, currentCell.gridY);
        Cell right = rightIndex > -1 ? grid.get(rightIndex) : null;

        int topIndex = getIndex(currentCell.gridX, currentCell.gridY - 1);
        Cell top = topIndex > -1 ? grid.get(topIndex) : null;

        int bottomIndex = getIndex(currentCell.gridX, currentCell.gridY + 1);
        Cell bottom = bottomIndex > -1 ? grid.get(bottomIndex) : null;

        if (bottom != null && !bottom.searchVisited && !IsCellDevided(currentCell, bottom)) {
            neighbors.add(bottom);
        }
        if (right != null && !right.searchVisited && !IsCellDevided(currentCell, right)) {
            neighbors.add(right);
        }
        if (left != null && !left.searchVisited && !IsCellDevided(currentCell, left)) {
            neighbors.add(left);
        }
        if (top != null && !top.searchVisited && !IsCellDevided(currentCell, top)) {
            neighbors.add(top);
        }

        if (neighbors.size() > 0) {
            Cell next;

            next = neighbors.get(0);

            if (neighbors.size() > 1) {
                for (int i = 0; i < neighbors.size() - 1; i++) {
                    int currentDeltaX = cols - 1 - neighbors.get(i).gridX;
                    int currentDeltaY = rows - 1 - neighbors.get(i).gridY;
                    double currentTotalDelta = Math.sqrt((currentDeltaX*currentDeltaX) + (currentDeltaY*currentDeltaY));

                    int nextDeltaX = cols - 1 - neighbors.get(i + 1).gridX;
                    int nextDeltaY = rows - 1 - neighbors.get(i + 1).gridY;
                    double nextTotalDelta = Math.sqrt((nextDeltaX*nextDeltaX) + (nextDeltaY*nextDeltaY));

                    if (currentTotalDelta > nextTotalDelta) {
                        next = neighbors.get(i + 1);
                    }
                }
            }
            return next;
        } else {
            return null;
        }
    }

    boolean IsCellDevided(Cell current, Cell next) {
        int x = current.gridX - next.gridX;
        int y = current.gridY - next.gridY;

        // next is on the left side
        if (x == 1) {
            return current.wall[0].GetWall() && next.wall[1].GetWall();
        } else if (x == -1) {
            return current.wall[1].GetWall() && next.wall[0].GetWall();
        }

        // next is on top
        if (y == 1) {
            return current.wall[2].GetWall() && next.wall[3].GetWall();
        } else if (y == -1) {
            return current.wall[3].GetWall() && next.wall[2].GetWall();
        }

        return false;
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

    boolean IsOneSet(Cell current, Cell next) {
        return current.set.contains(next);
    }

    final int getIndex(int x, int y) {
        if (x < 0 || y < 0 || x > cols - 1 || y > rows - 1) {
            return -1;
        }

        return y + (x * cols);
    }

    public void addListener(IMaze maze) {
        listeners.add(maze);
    }

}
