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
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Makroefi
 */
public class Cell {

    Graphics2D g;
    Set<Cell> set;
    int w;
    int x, y;
    int gridX, gridY;
    //                left, right, top, bottom
    public Wall[] wall = {new Wall(true, this), 
        new Wall(true, this),
        new Wall(true, this),
        new Wall(true, this)};
    
    public boolean isPath = false;
    public boolean genVisited = false;
    public boolean searchVisited = false;
    public boolean current = false;
    public boolean goal = false;

    public Cell(Graphics2D graphic, int x, int y, int w) {
        this.g = graphic;
        this.w = w;
        this.x = 2 + x * w;
        this.y = 2 + y * w;
        this.gridX = x;
        this.gridY = y;
    }

    public void ClearCell() {
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
    
    public void ResetCell() {
        isPath = false;
        searchVisited = false;
        current = false;
    }

    public void SetLeftWall(boolean flag) {
        wall[0].SetWall(flag);
    }

    public void SetRightWall(boolean flag) {
        wall[1].SetWall(flag);
    }

    public void SetTopWall(boolean flag) {
        wall[2].SetWall(flag);
    }

    public void SetBottomWall(boolean flag) {
        wall[3].SetWall(flag);
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
        }else if (searchVisited){
            g.setColor(new Color(10,50,80));
        }else if (genVisited) {
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
        if (wall[0].GetWall()) {
            g.drawLine(x, y, x, y + w);
        }
        // right
        if (wall[1].GetWall()) {
            g.drawLine(x + w, y, x + w, y + w);
        }
        // top
        if (wall[2].GetWall()) {
            g.drawLine(x, y, x + w, y);
        }
        // bottom
        if (wall[3].GetWall()) {
            g.drawLine(x, y + w, x + w, y + w);
        }
    }

}
