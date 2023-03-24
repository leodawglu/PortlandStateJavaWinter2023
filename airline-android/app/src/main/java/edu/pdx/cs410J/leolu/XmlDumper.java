/**
 * The {code XmlDumper} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import edu.pdx.cs410J.AirlineDumper;

/**
 * <code>XmlDumper</code> class for Project 4.
 * Produces valid XML file according to provided DTD:
 * http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd
 */
public class XmlDumper implements AirlineDumper<Airline> {

    private String filepath;
    private File airlineFile;
    private Airline airways;
    private StringBuilder err = new StringBuilder();
    public XmlDumper(String filepath) {
        this.filepath = filepath;
    }
    public XmlDumper(File airlineFile){this.airlineFile = airlineFile;}

    /**
     * @param airways Accepts an airline object
     * dumps the airline and its flight information into an XML file
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
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder =
                    factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);

            Document doc = builder.newDocument();
            Element airline = doc.createElement("airline");
            Element name = doc.createElement("name");
            name.setTextContent(this.airways.getName());
            airline.appendChild(name);

            for(Flight fl:this.airways.getFlights()){
                airline.appendChild(createFlightElements(doc,fl));
            }
            doc.appendChild(airline);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "us-ascii");

            DOMSource source = new DOMSource(doc);

            //StreamResult result = new StreamResult(new File(filepath));
            StreamResult result = new StreamResult(airlineFile);

            t.transform(source,result);
            System.out.println("XML file for " + this.airways.getName() + " created successfully.");

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

    /**
     * @param doc Accepts document object created by Document Builder
     * @param fl Accepts a flight object
     * createFlight method creates the XML elements according to details within a flight object
     * @return Element object which stores the flight elements created
     * */
    private Element createFlightElements(Document doc, Flight fl){
        Element flight = doc.createElement("flight");
        Element number = doc.createElement("number");
        number.setTextContent(Integer.toString(fl.getNumber()));
        flight.appendChild(number);
        /*Departure*/
        Element src = doc.createElement("src");
        src.setTextContent(fl.getSource());
        flight.appendChild(src);
        Element depart = createDatetimeElements("depart",doc, fl);
        flight.appendChild(depart);

        /*Arrival*/
        Element dest = doc.createElement("dest");
        dest.setTextContent(fl.getDestination());
        flight.appendChild(dest);
        Element arrive = createDatetimeElements("arrive",doc, fl);
        flight.appendChild(arrive);

        return flight;
    }

    /**
     * @param fl Accepts flight object
     * @param type String declares whether the datetime elements are created for depart or arrive
     * @param doc Accepts Document object created by DocumentBuilder
     * createDateTime method creates date and time elements for either the depart or arrive info of flight
     * @return an Element object which stores the date and time elements created
     * */
    private Element createDatetimeElements(String type, Document doc, Flight fl){
        String[] hoursMins;
        String[] monthDayYear;
        Element direction = doc.createElement(type);
        if(type.equalsIgnoreCase("depart")){
            hoursMins = fl.getDepTime24().split(":");
            monthDayYear = fl.getDepDate().split("/");
        } else { // "arrive"
            hoursMins = fl.getArrTime24().split(":");
            monthDayYear = fl.getArrDate().split("/");
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

    /**
     * Deletes the XML file using the filepath associated with the XmlDumper object
     * */
    public void deleteXmlFile(){
        File temp = new File(filepath);
        temp.delete();
        System.out.println("Deleted XML file for " + airways.getName() +" successfully.");
    }

    /**
     * @return String of error message stored within XmlDumper object
     * */
    public String getErrorMsg(){
        return err.toString();
    }
}
