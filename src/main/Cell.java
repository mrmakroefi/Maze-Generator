/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Makroefi
 */
public class Cell {

    Graphics2D g;
    int w;
    int x, y;
    int gridX, gridY;
    //                left, right, top, bottom
    public boolean[] wall = {true, true, true, true};
    Maze maze;

    public boolean isPath = false;
    public boolean genVisited = false;
    public boolean searchVisited = false;
    public boolean current = false;
    public boolean goal = false;

    public Cell(Graphics2D graphic, Maze maze, int x, int y, int w) {
        this.g = graphic;
        this.maze = maze;
        this.w = w;
        this.x = 2 + x * w;
        this.gridX = x;
        this.y = 2 + y * w;
        this.gridY = y;
    }

    public void ResetCell() {
        isPath = false;
        genVisited = false;
        searchVisited = false;
        current = false;
        goal = false;
        SetLeftWall(true);
        SetRightWall(true);
        SetTopWall(true);
        SetBottomWall(true);
    }

    public void SetLeftWall(boolean flag) {
        wall[0] = flag;
    }

    public void SetRightWall(boolean flag) {
        wall[1] = flag;
    }

    public void SetTopWall(boolean flag) {
        wall[2] = flag;
    }

    public void SetBottomWall(boolean flag) {
        wall[3] = flag;
    }

    public Cell genCheckNeighbors() {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int leftIndex = getIndex(gridX - 1, gridY);
        Cell left = leftIndex > -1 ? maze.grid.get(leftIndex) : null;

        int rightIndex = getIndex(gridX + 1, gridY);
        Cell right = rightIndex > -1 ? maze.grid.get(rightIndex) : null;

        int topIndex = getIndex(gridX, gridY - 1);
        Cell top = topIndex > -1 ? maze.grid.get(topIndex) : null;

        int bottomIndex = getIndex(gridX, gridY + 1);
        Cell bottom = bottomIndex > -1 ? maze.grid.get(bottomIndex) : null;

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

    public Cell searchCheckNeighbors() {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int leftIndex = getIndex(gridX - 1, gridY);
        Cell left = leftIndex > -1 ? maze.grid.get(leftIndex) : null;

        int rightIndex = getIndex(gridX + 1, gridY);
        Cell right = rightIndex > -1 ? maze.grid.get(rightIndex) : null;

        int topIndex = getIndex(gridX, gridY - 1);
        Cell top = topIndex > -1 ? maze.grid.get(topIndex) : null;

        int bottomIndex = getIndex(gridX, gridY + 1);
        Cell bottom = bottomIndex > -1 ? maze.grid.get(bottomIndex) : null;

        if (left != null && !left.searchVisited && !wall[0] && !left.wall[1]) {
            neighbors.add(left);
        }
        if (right != null && !right.searchVisited && !wall[1] && !right.wall[0]) {
            neighbors.add(right);
        }
        if (top != null && !top.searchVisited && !wall[2] && !top.wall[3]) {
            neighbors.add(top);
        }
        if (bottom != null && !bottom.searchVisited && !wall[3] && !bottom.wall[2]) {
            neighbors.add(bottom);
        }

        if (neighbors.size() > 0) {
            int r = (int) (Math.random() * neighbors.size());
            return neighbors.get(r);
        } else {
            return null;
        }
    }

    public void UpdateGraphics() {

        if (current) {
            // green
            g.setColor(new Color(19, 245, 64));
        } else if(isPath) {
            g.setColor(new Color(213,0,128));
        }else if (goal) {
            // red
            g.setColor(Color.red);
        } else if (genVisited) {
            // blue
            g.setColor(new Color(40, 80, 110));
        } else {
            // black
            g.setColor(Color.black);
        }
        Rectangle rect = new Rectangle(x + 1, y + 1, w - 1, w - 1);
        g.fill(rect);
        g.draw(rect);

        g.setColor(Color.white);

        // left
        if (wall[0]) {
            g.drawLine(x, y, x, y + w);
        }
        // right
        if (wall[1]) {
            g.drawLine(x + w, y, x + w, y + w);
        }
        // top
        if (wall[2]) {
            g.drawLine(x, y, x + w, y);
        }
        // bottom
        if (wall[3]) {
            g.drawLine(x, y + w, x + w, y + w);
        }
    }

    int getIndex(int x, int y) {
        if (x < 0 || y < 0 || x > maze.cols - 1 || y > maze.rows - 1) {
            return -1;
        }

        return y + (x * maze.cols);
    }
}
