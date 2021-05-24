package de.uulm.sp.pvs.sokoban;


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
import java.util.LinkedList;
import java.util.List;

public class SokobanLevel {

    public final List<String> authors;
    public final String levelName;
    public final Difficulty difficulty;
    public final char[][] level;
    public final int width;
    public final int height;

    public SokobanLevel(String path) throws SAXException {

         Schema schema;
         try {
             schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).
                     newSchema(new File("resources/sokoban.xsd"));
         }catch(SAXException saxException){

             throw new SAXException("Error reading schema");
         }

         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         documentBuilderFactory.setSchema(schema);
         documentBuilderFactory.setNamespaceAware(true);

        DocumentBuilder documentBuilder;
        try{
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }catch (ParserConfigurationException parserConfigurationException){
            throw new SAXException("Error in parser configuration");
        }

        Document document;
        try{
            document = documentBuilder.parse(path);
        }catch (IOException ioException){
            throw new SAXException("Error while parsing file");
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
                    throw new SAXException("width is " + level[i].length + " but should be " + width);
                }
            }
        }else{
            throw new SAXException("height is " + levelString.length + " but should be " + height);
        }
    }
}
