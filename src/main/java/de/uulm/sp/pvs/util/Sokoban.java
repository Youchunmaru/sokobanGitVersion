package de.uulm.sp.pvs.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Samuel Gröner
 * implements the game sokoban
 * @version 21.05.20
 *          optimized all functions
 * */
public class Sokoban {

    /**
     * holds the direction parameters for every move
     * */
    public enum Direction{NORTH(-1,0),
        SOUTH(1,0),
        WEST(0,-1),
        EAST(0,1);
        private final int first;
        private final int second;

        Direction(int first, int second){
            this.first = first;
            this.second = second;
        }
    }
    /**
     * generates a 7x7 play field
     * @return the play field as char[][]
     * */
    public static char [][] gen7x7Field(){
        char [][] sokoban = new char [7][];
        sokoban[0] = "#######".toCharArray();
        sokoban[1] = "#.....#".toCharArray();
        sokoban[2] = "#..$..#".toCharArray();
        sokoban[3] = "#.$@$.#".toCharArray();
        sokoban[4] = "#..$..#".toCharArray();
        sokoban[5] = "#.....#".toCharArray();
        sokoban[6] = "#######".toCharArray();
        return sokoban;
    }
    /**
     * Finds the player in a sokoban play field
     * @param array the play field
     * @return the position of the player as a {@link Pair}
     * */
    public static Pair<Integer,Integer> findPlayer(char[][] array){

        int first = -1;
        int second = -1;

        for (char[] value:array) {
            first++;
            for (char obj:value) {
                second++;
                if (obj == '@') {
                    return new Pair<>(first,second);
                }
            }
            second = -1;
        }
        return new Pair<>(first,second);
    }
    /**
     * Turns the play field into a String for output.
     * @implNote replaces ", " with "" because Arrays.toString(char[] a)
     *              puts them with ", " together
     * @param array the play field
     * @return the Sokoban play field as a String
     * */
    public static String sokobanToString(char[][] array){
        return Arrays.stream(array).map((char[] value)-> Arrays.toString(value).replace(", ","")).
                collect(Collectors.joining("\n"));
    }
    /**
     * Moves the player in the particular {@link Direction}
     * @param array is the play field
     * @param direction the direction parameters
     * @return if player was able to move or not as boolean
     * */
    public static boolean move(char[][] array, Direction direction){
        //player cords:
        Pair<Integer,Integer> pair = findPlayer(array);
        int first = pair.getFirst();
        int second = pair.getSecond();
        //normal move
        if (array[first + direction.first][second + direction.second] == '.') {

            array[first][second] = '.';
            array[first + direction.first][second + direction.second] = '@';
            return true;
        }
        //move with push
        if (array[first + direction.first][second + direction.second] == '$') {
            if (array[first + (direction.first*2)][second + (direction.second*2)] == '.') {

                array[first][second] = '.';
                array[first + direction.first][second + direction.second] = '@';
                array[first + (direction.first*2)][second + (direction.second*2)] = '$';
                return true;
            }
        }
        System.err.println("moving failed");
        return false;
    }
}
