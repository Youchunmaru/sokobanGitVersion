package de.uulm.sp.pvs.sokoban;


import de.uulm.sp.pvs.exceptions.SokobanException;
import de.uulm.sp.pvs.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Samuel Gröner
 * implements the reading
 *
 * */
public class SokobanLevel {

    public final List<String> authors;
    public final String levelName;
    public final Difficulty difficulty;
    public final char[][] level;
    public final int width;
    public final int height;

    public SokobanLevel(String path) throws SokobanException {

         Schema schema;
         try {
             schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).
                     newSchema(new File("resources/sokoban.xsd"));
         }catch(SAXException saxException){

             throw new SokobanException("Error reading schema",saxException);
         }

         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         documentBuilderFactory.setSchema(schema);
         documentBuilderFactory.setNamespaceAware(true);

        DocumentBuilder documentBuilder;
        try{
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }catch (ParserConfigurationException parserConfigurationException){
            throw new SokobanException("Error in parser configuration",parserConfigurationException);
        }

        Document document;
        try{
            document = documentBuilder.parse(path);
        }catch (SAXException | IOException exception){
            throw new SokobanException("Error while parsing file",exception);
        }

        NodeList nodeListAuthors = document.getElementsByTagName("Author");
        authors = new LinkedList<>();
        for (int i = 0; i < nodeListAuthors.getLength(); i++) {
            authors.add(nodeListAuthors.item(i).getTextContent());
        }

        levelName = document.getElementsByTagName("LevelName").item(0).getTextContent();

        NodeList nodeListDifficulty = document.getElementsByTagName("Difficulty");
        if (nodeListDifficulty.getLength() == 0) {
            difficulty = Difficulty.NONE;
        }else{
            difficulty = Difficulty.valueOf(nodeListDifficulty.item(0).getTextContent());
        }

        Node nodeListLevel = document.getElementsByTagName("LevelData").item(0);
        width = Integer.parseInt(nodeListLevel.getAttributes().getNamedItem("width").getTextContent());
        height = Integer.parseInt(nodeListLevel.getAttributes().getNamedItem("height").getTextContent());

        level = new char[height][];
        String[] levelString = nodeListLevel.getTextContent().split("\n|\r");
        if (levelString.length == height) {
            for (int i = 0; i < height; i++) {
                if (levelString[i].length() == width) {
                    level[i] = levelString[i].toCharArray();
                }else{
                    throw new SokobanException("width is " + level[i].length + " but should be " + width);
                }
            }
        }else{
            throw new SokobanException("height is " + levelString.length + " but should be " + height);
        }
    }
    /**
     * Finds the player in a sokoban play field
     * @param array the play field
     * @return the position of the player as a {@link Pair}
     * */
    public Pair<Integer,Integer> findPlayer(char[][] array){

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
    public String sokobanToString(char[][] array){
        return Arrays.stream(array).map((char[] value)-> Arrays.toString(value).replace(", ","")).
                collect(Collectors.joining("\n"));
    }
    /**
     * Moves the player in the particular {@link Direction}
     * @param array is the play field
     * @param direction the direction parameters
     * @return if player was able to move or not as boolean
     * */
    public boolean move(char[][] array, Direction direction){
        //player cords:
        Pair<Integer,Integer> pair = findPlayer(array);
        int first = pair.getFirst();
        int second = pair.getSecond();
        //normal move
        if (array[first + direction.first][second + direction.second] == ' ') {

            array[first][second] = ' ';
            array[first + direction.first][second + direction.second] = '@';
            return true;
        }
        //move with push
        if (array[first + direction.first][second + direction.second] == '$') {
            if (array[first + (direction.first*2)][second + (direction.second*2)] == ' ') {

                array[first][second] = ' ';
                array[first + direction.first][second + direction.second] = '@';
                array[first + (direction.first*2)][second + (direction.second*2)] = '$';
                return true;
            }
        }
        System.err.println("moving failed");
        return false;
    }
}
