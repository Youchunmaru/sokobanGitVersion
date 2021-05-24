package de.uulm.sp.pvs.util;

/**
 * @author Samuel Gröner
 * implements the direction parameters for all possible movements
 *
 * */
public enum Direction {NORTH(-1,0),
    SOUTH(1,0),
    WEST(0,-1),
    EAST(0,1);
    public final int first;
    public final int second;

    Direction(int first, int second){
        this.first = first;
        this.second = second;
    }
}
