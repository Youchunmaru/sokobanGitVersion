package de.uulm.sp.pvs;

import de.uulm.sp.pvs.sokoban.SokobanLevel;
import de.uulm.sp.pvs.util.*;
import de.uulm.sp.pvs.sokoban.Sokoban;
import org.xml.sax.SAXException;

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
    public static void main( String[] args ) throws IOException, SAXException {
        //field generation: resources/test_level.xml
        System.out.println("Path to SokobanLevel:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String path = in.readLine();
        SokobanLevel sokobanLevel = new SokobanLevel(path);
        boolean playing = true;

        while(playing){
            System.out.println(Sokoban.sokobanToString(sokobanLevel.level));
            System.out.println("Where do you want to go? (N/E/S/W or X to exit)");

            switch (in.readLine().toUpperCase()){
                case "N":
                    Sokoban.move(sokobanLevel.level, Direction.NORTH);
                    break;
                case "E":
                    Sokoban.move(sokobanLevel.level, Direction.EAST);
                    break;
                case "S":
                    Sokoban.move(sokobanLevel.level, Direction.SOUTH);
                    break;
                case "W":
                    Sokoban.move(sokobanLevel.level, Direction.WEST);
                    break;
                case "X":
                    System.out.println("Bye");
                    playing = false;
                    break;
                default:
                    System.err.println("undefined input!");
            }
        }

    }
}
