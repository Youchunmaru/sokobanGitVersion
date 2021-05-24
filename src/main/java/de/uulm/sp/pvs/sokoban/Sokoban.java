package de.uulm.sp.pvs.sokoban;

import de.uulm.sp.pvs.util.*;


/**
 * @author Samuel Gröner
 * implements the game sokoban
 * @version 21.05.20
 *          optimized all functions
 * */
public class Sokoban {

    public static boolean movement(String direction, SokobanLevel sokobanLevel){

        switch (direction){
            case "N":
                sokobanLevel.move(sokobanLevel.level, Direction.NORTH);
                break;
            case "E":
                sokobanLevel.move(sokobanLevel.level, Direction.EAST);
                break;
            case "S":
                sokobanLevel.move(sokobanLevel.level, Direction.SOUTH);
                break;
            case "W":
                sokobanLevel.move(sokobanLevel.level, Direction.WEST);
                break;
            case "X":
                System.out.println("Bye");
                return false;
            default:
                System.err.println("undefined input!");
        }
        return true;
    }
}
