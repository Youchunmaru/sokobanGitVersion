package de.uulm.sp.pvs.sokoban;

import de.uulm.sp.pvs.util.*;


/**
 * @author Samuel Gröner
 *
 * */
public class Sokoban {

    /**
     * i only put the movement function here so this class isn't useless.
     * handles movement according to given command input or ends the game
     * if the player used the according input
     * @param command the command input of the player
     * @param sokobanLevel the level to move in
     * @return true if player doesnt want to end the game
     * */
    public static boolean command(String command, SokobanLevel sokobanLevel){

        switch (command){
            case "N":
                sokobanLevel.move(Direction.NORTH);
                break;
            case "E":
                sokobanLevel.move(Direction.EAST);
                break;
            case "S":
                sokobanLevel.move(Direction.SOUTH);
                break;
            case "W":
                sokobanLevel.move(Direction.WEST);
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
