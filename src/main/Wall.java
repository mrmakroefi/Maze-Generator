/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Makroefi
 */
public class Wall {
    private boolean on;
    private final Cell current;
    private Cell next = null;
    
    public Wall(boolean on, Cell current){
        this.on = on;
        this.current = current;
    }
    
    public void SetNextCell(Cell next) {
        this.next = next;
    }
    
    public Cell GetCurrentWallCell() {
        return current;
    }
    
    public Cell GetNextWallCell() {
        return next;
    }
    
    public void SetWall(boolean flag){
        this.on = flag;
    }
    
    public boolean GetWall(){
        return on;
    }
}
