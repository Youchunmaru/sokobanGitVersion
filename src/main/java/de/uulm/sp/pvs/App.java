package de.uulm.sp.pvs;

import de.uulm.sp.pvs.util.Sokoban;
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
    public static void main( String[] args ) throws IOException {
        //field generation:
        char[][] sokoban = Sokoban.gen7x7Field();
        boolean playing = true;

        while(playing){
            System.out.println(Sokoban.sokobanToString(sokoban));
            System.out.println("Where do you want to go? (N/E/S/W or X to exit)");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            switch (in.readLine().toUpperCase()){
                case "N":
                    Sokoban.move(sokoban, Sokoban.Direction.NORTH);
                    break;
                case "E":
                    Sokoban.move(sokoban, Sokoban.Direction.EAST);
                    break;
                case "S":
                    Sokoban.move(sokoban, Sokoban.Direction.SOUTH);
                    break;
                case "W":
                    Sokoban.move(sokoban, Sokoban.Direction.WEST);
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
