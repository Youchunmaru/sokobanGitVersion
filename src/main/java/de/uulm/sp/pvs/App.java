package de.uulm.sp.pvs;

import de.uulm.sp.pvs.exceptions.SokobanException;
import de.uulm.sp.pvs.sokoban.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Samuel Gröner
 *
 * 
 */
public class App {
    /**
     * start the game sokoban and handles I/O
     * */
    public static void main(String[] args ) throws SokobanException, IOException {
        //level generation per input: test_level.xml
        System.out.println("Filename of SokobanLevel:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = in.readLine();

        if (checkLevel(name)) {
            String path = "resources/" + name;
            SokobanLevel sokobanLevel = new SokobanLevel(path);
            boolean playing = true;
            //Loop to get a input every round
            while (playing) {
                System.out.println(sokobanLevel.sokobanToString());
                System.out.println("Where do you want to go? (N/E/S/W or X to exit)");
                playing = Sokoban.command(in.readLine().toUpperCase(), sokobanLevel);
                //ends the game if won
                if (sokobanLevel.checkWinCondition()) {
                    System.out.println(sokobanLevel.sokobanToString());
                    System.out.println("You win!");
                    playing = false;
                }
            }
        } else {
            System.out.println("couldn't find " + name);
        }
        in.close();

    }
    /**
     * checks if the specific level exists
     * @param name is the Filename of the level
     * @return true if the level file exists
     * */
    public static boolean checkLevel(String name){

        File directory = new File("resources");
        String[] content = directory.list();
        if (content != null) {
            for (String value:content) {
                if (value.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
