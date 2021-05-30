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
 * creates a Sokoban play field,
 * It manipulates the playfield for every move to match the game status.
 *
 * */
public class SokobanLevel {

    /** a list of authors of a SokobanLevel
     * */
    public final List<String> authors;
    /** the name of a SokobanLevel
     * */
    public final String levelName;
    /** the difficulty of a SokobanLevel
     * */
    public final Difficulty difficulty;
    /** the SokobanLevel as a char matrix
     * */
    public final char[][] level;
    /** the width of a SokobanLevel
     * */
    public final int width;
    /** the height of a SokobanLevel
     * */
    public final int height;

    /**
     * creates a SokobanLevel using a xml
     * @param path location of the level
     * */
    public SokobanLevel(String path) throws SokobanException {

        //Loads the schema
         Schema schema;
         try {
             schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).
                     newSchema(new File("resources/sokoban.xsd"));
         }catch(SAXException saxException){

             throw new SokobanException("Error reading schema",saxException);
         }
        //creates the document builder
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         documentBuilderFactory.setSchema(schema);
         documentBuilderFactory.setNamespaceAware(true);

        DocumentBuilder documentBuilder;
        try{
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }catch (ParserConfigurationException parserConfigurationException){
            throw new SokobanException("Error in parser configuration",parserConfigurationException);
        }
        //reads the document
        Document document;
        try{
            document = documentBuilder.parse(path);
        }catch (SAXException | IOException exception){
            throw new SokobanException("Error while parsing file",exception);
        }
        //gets the Author
        NodeList nodeListAuthors = document.getElementsByTagName("Author");
        authors = new LinkedList<>();
        for (int i = 0; i < nodeListAuthors.getLength(); i++) {
            authors.add(nodeListAuthors.item(i).getTextContent());
        }
        //gets the levelName
        levelName = document.getElementsByTagName("LevelName").item(0).getTextContent();
        //gets the Difficulty
        NodeList nodeListDifficulty = document.getElementsByTagName("Difficulty");
        if (nodeListDifficulty.getLength() == 0) {
            difficulty = Difficulty.NONE;
        }else{
            difficulty = Difficulty.valueOf(nodeListDifficulty.item(0).getTextContent());
        }
        //gets the LevelData
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
     * @return the position of the player as a {@link Pair}
     * */
    public Pair<Integer,Integer> findPlayer(){

        int first = -1;
        int second = -1;

        for (char[] value:this.level) {
            first++;
            for (char obj:value) {
                second++;
                if (obj == '@' || obj == '+') {
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
     * @return the Sokoban play field as a String
     * */
    public String sokobanToString(){
        return Arrays.stream(this.level).map((char[] value)-> Arrays.toString(value).
                replace(", ","")).collect(Collectors.joining("\n"));
    }
    /**
     * Moves the player in the particular {@link Direction}
     * @param direction the direction parameters
     * @return if player was able to move or not as boolean
     * */
    public boolean move(Direction direction){
        //player cords:
        Pair<Integer,Integer> pair = findPlayer();
        int first = pair.getFirst();
        int second = pair.getSecond();
        boolean normal = false;
        boolean push = false;

        if (this.level[first + direction.first][second + direction.second] == '$') {
            push = pushMove(first, second, direction);
        }else if(this.level[first + direction.first][second + direction.second] == '*'){
            push = pushOnGoalMove(first, second, direction);
        }else{
            normal = normalMove(first,second,direction);
        }
        if (push==normal) {
            System.err.println("moving failed");
            return false;
        }
        return true;
    }
    /**
     * checks if all boxes($) are on their final position('.')
     * @return whether the game is won or not
     * */
    public boolean checkWinCondition(){

        for (char[] value:this.level) {
            for (char obj:value) {
                if (obj == '.' || obj == '+') {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * manipulates the level to match the status after a pushing move
     * @param first the y coordinate of the player
     * @param second the x coordinate of the player
     * @param direction the direction the player moves
     * @return if the player could move or not
     * */
    public boolean pushMove(int first,int second, Direction direction){
            if (this.level[first + (direction.first*2)][second + (direction.second*2)] == ' ') {

                changeHomeField(first, second);
                this.level[first + direction.first][second + direction.second] = '@';
                this.level[first + (direction.first*2)][second + (direction.second*2)] = '$';
                return true;
            }
            if (this.level[first + (direction.first*2)][second + (direction.second*2)] == '.') {

                changeHomeField(first, second);
                this.level[first + direction.first][second + direction.second] = '@';
                this.level[first + (direction.first*2)][second + (direction.second*2)] = '*';
                return true;
            }
        return false;
    }
    /**
     * manipulates the level to match the status after a pushing move on a goal field
     * @param first the y coordinate of the player
     * @param second the x coordinate of the player
     * @param direction the direction the player moves
     * @return if the player could move or not
     * */
    public boolean pushOnGoalMove(int first,int second, Direction direction){
        if (this.level[first + (direction.first*2)][second + (direction.second*2)] == ' ') {

            changeHomeField(first, second);
            this.level[first + direction.first][second + direction.second] = '+';
            this.level[first + (direction.first*2)][second + (direction.second*2)] = '$';
            return true;
        }
        if (this.level[first + (direction.first*2)][second + (direction.second*2)] == '.') {

            changeHomeField(first, second);
            this.level[first + direction.first][second + direction.second] = '+';
            this.level[first + (direction.first*2)][second + (direction.second*2)] = '*';
            return true;
        }
        return false;
    }
    /**
     * manipulates the level to match the status after a normal move
     * @param first the y coordinate of the player
     * @param second the x coordinate of the player
     * @param direction the direction the player moves
     * @return if the player could move or not
     * */
    public boolean normalMove(int first, int second, Direction direction){
        if (this.level[first + direction.first][second + direction.second] == ' ') {

            changeHomeField(first, second);
            this.level[first + direction.first][second + direction.second] = '@';
            return true;
        }
        if (this.level[first + direction.first][second + direction.second] == '.') {

            changeHomeField(first, second);
            this.level[first + direction.first][second + direction.second] = '+';
            return true;
        }
        return false;
    }
    /**
     * manipulates the level after a move of the player
     * @param first the y coordinate of the player
     * @param second the x coordinate of the player
     * */
    public void changeHomeField(int first, int second){
        if (this.level[first][second] == '+') {
            this.level[first][second] = '.';
        }else{
            this.level[first][second] = ' ';
        }
    }
}
