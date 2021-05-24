package de.uulm.sp.pvs;

import de.uulm.sp.pvs.exceptions.SokobanException;
import de.uulm.sp.pvs.sokoban.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Samuel Gröner
 *
 * 
 */
public class App 
{
    public static void main( String[] args ) throws SokobanException, IOException {
        //field generation: resources/test_level.xml
        System.out.println("Path to SokobanLevel:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String path = in.readLine();
        SokobanLevel sokobanLevel = new SokobanLevel(path);
        boolean playing = true;

        while(playing){
            System.out.println(sokobanLevel.sokobanToString(sokobanLevel.level));
            System.out.println("Where do you want to go? (N/E/S/W or X to exit)");

            playing = Sokoban.movement(in.readLine().toUpperCase(),sokobanLevel);
        }

    }
}
