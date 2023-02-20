package edu.pdx.cs410J.leolu;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlParser implements AirlineParser<Airline> {
    private Airline airline;
    final String DTD = "";
    private String filepath ="";
    private StringBuilder err = new StringBuilder();
    private Document xml;

    public XmlParser(String filepath){
        this.filepath=filepath;
    }

    public Airline parse() throws ParserException{
        try {
            if(filepath.isEmpty()) throw new ParserException("File path is empty");
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = buildDoc(factory);
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);
            //xml = builder.parse(this.getClass().getResourceAsStream(filepath));
            xml = builder.parse(new File(filepath));
        } catch (SAXException | IOException e) {
            throw new ParserException("Please enter a valid XML file path.",e);
        } catch(ParserException e){
            err.append(e.getMessage());
            /**KEEP?*/
            throw new ParserException("Please enter a valid XML file path.",e);
        }

        Element root = xml.getDocumentElement();
        String airlineName = root.getElementsByTagName("name").item(0).getTextContent();
        this.airline = new Airline(airlineName);

        NodeList innerFlights = root.getElementsByTagName("flight");
        for(int i=0; i<innerFlights.getLength(); i++){
            Flight fl = buildFlight(innerFlights.item(i));
            if(fl.getError().length()!=0){
                System.out.println("Flight information for the number " + i
                        + " of XML file is formatted incorrectly, please review and correct the file.");
                return null;
            }
            this.airline.addFlight(fl);
        }

        return airline;
    }

    private DocumentBuilder buildDoc(DocumentBuilderFactory factory){
        DocumentBuilder builder =
                null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return builder;
    }

    private String createDate(Element date){
        String day = date.getAttribute("day");
        String month = date.getAttribute("month");
        String year = date.getAttribute("year");
        StringBuilder sb = new StringBuilder(month + "/" + day + "/" + year);
        return sb.toString();
    }

    private String createTime(Element time){
        String hour = time.getAttribute("hour");
        int minute = Integer.parseInt(time.getAttribute("minute"));
        StringBuilder sb = new StringBuilder(hour+":");
        if(minute<10) sb.append("0");
        sb.append(Integer.toString(minute));
        return sb.toString();
    }

    private String[] datetime(Element flElement, String type){
        Node n = flElement.getElementsByTagName(type).item(0);
        Element datetime = (Element)n;
        Node dateNode = datetime.getElementsByTagName("date").item(0);
        Element dateElement = (Element) dateNode;
        Node timeNode = datetime.getElementsByTagName("time").item(0);
        Element timeElement = (Element) timeNode;
        String date = createDate(dateElement);
        String time = createTime(timeElement);
        return new String[]{date,time};
    }
    private Flight buildFlight(Node fl){
        if(fl==null)return null;
        Element flElement = (Element)fl;
        String number = flElement.getElementsByTagName("number").item(0).getTextContent();
        String src = flElement.getElementsByTagName("src").item(0).getTextContent();
        String dest = flElement.getElementsByTagName("dest").item(0).getTextContent();

        String[] depart = datetime(flElement,"depart");
        String[] arrive = datetime(flElement, "arrive");

        return new Flight(number,src,depart[0],depart[1],dest,arrive[0],arrive[1],true);
    }

}
