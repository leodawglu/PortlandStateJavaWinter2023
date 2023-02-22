/**
 * The {code XmlDumper} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * A skeletal implementation of the <code>XmlDumper</code> class for Project 2.
 */
public class XmlDumper implements AirlineDumper<Airline> {

    private String filepath;
    private Airline airways;
    private StringBuilder err = new StringBuilder();
    public XmlDumper(String filepath) {
        this.filepath = filepath;
    }

    /**
     * @param airways Accepts an airline object and dumps the airline and its flight information into file
     * */
    @Override
    public void dump(Airline airways) {
        if(airways==null){
            err.append("XmlDumper dump method does not accept null for airline object!");
            System.err.println(err);
            return;
        }
        this.airways = airways;
        try {
            if(filepath.isEmpty()) throw new IOException("File path is empty");
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder =
                    factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);

            Document doc = builder.newDocument();
            Element airline = doc.createElement("airline");
            Element name = doc.createElement("name");
            name.setTextContent(this.airways.getName());
            for(Flight fl:this.airways.getFlights()){
                airline.appendChild(createFlight(doc,fl));
            }
            doc.appendChild(airline);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "us-ascii");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(filepath));

            t.transform(source,result);
            System.out.println("XML file for " + this.airways.getName() + " created successfully.");

        } catch (IOException e) {
            err.append(e.getMessage());
            //throw new ParserException("Please enter a valid XML file path.",e);
        } catch (ParserConfigurationException e) {
            err.append("DocumentBuilderFactory could not produce a builder.");
            System.err.println(err);
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            err.append("TransformerFactory could not produce a transformer.");
            System.err.println(err);
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            err.append("Transformer failed to transform document object to xml file.");
            System.err.println(err);
            throw new RuntimeException(e);
        }

    }

    private Element createFlight(Document doc, Flight fl){
        if(fl==null){
            err.append("Flight object cannot be null for createFlight method! Please amend code construction.");
            System.err.println(err);
            return null;
        }
        if(doc==null){
            err.append("Document object cannot be null for createFlight method! Please amend code construction.");
            System.err.println(err);
            return null;
        }
        Element flight = doc.createElement("flight");
        Element number = doc.createElement("number");
        number.setTextContent(Integer.toString(fl.getNumber()));
        flight.appendChild(number);
        /*Departure*/
        Element src = doc.createElement("src");
        src.setTextContent(fl.getSource());
        flight.appendChild(src);
        Element depart = createDatetime("depart",doc, fl);
        flight.appendChild(depart);

        /*Arrival*/
        Element dest = doc.createElement("dest");
        dest.setTextContent(fl.getDestination());
        flight.appendChild(dest);
        Element arrive = createDatetime("arrive",doc, fl);
        flight.appendChild(arrive);

        return flight;
    }

    private Element createDatetime(String type, Document doc, Flight fl){
        if(fl==null){
            err.append("Flight object provided for XmlDumper datetime method cannot be null!");
            System.err.println(err);
            return null;
        }
        if(doc == null){
            err.append("Document object provided for XmlDumper datetime method cannot be null!");
            System.err.println(err);
            return null;
        }
        String[] hoursMins;
        String[] monthDayYear;
        Element direction = doc.createElement(type);
        if(type.equalsIgnoreCase("depart")){
            hoursMins = fl.getDepTime24().split(":");
            monthDayYear = fl.getDepDate().split("/");
        } else if (type.equalsIgnoreCase("arrive")) {
            hoursMins = fl.getArrTime24().split(":");
            monthDayYear = fl.getArrDate().split("/");
        } else {
            err.append("Wrong input type for XmlDumper datetime method. Must be depart or arrive!");
            System.err.println(err);
            return null;
        }
        Element date = doc.createElement("date");
        date.setAttribute("day",monthDayYear[1]);
        date.setAttribute("month",monthDayYear[0]);
        date.setAttribute("year",monthDayYear[2]);
        Element time = doc.createElement("time");
        time.setAttribute("hour",hoursMins[0]);
        time.setAttribute("minute",hoursMins[1]);

        direction.appendChild(date);
        direction.appendChild(time);
        return direction;
    }

}
