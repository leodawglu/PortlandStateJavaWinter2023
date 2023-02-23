package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConverterTest {

    @Test
    void argsNullPrintsErr() throws ParserException {
        String[] args = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Converter.main(args);
        String errOutput = baos.toString();
        assertThat(errOutput,containsString("usage: java edu.pdx.cs410J.leolu.Converter txtFile xmlFile."));
        System.setErr(System.err);
    }

    @Test
    void argsMoreThan2PrintsErr() throws ParserException {
        String[] args = {"1","2","3"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Converter.main(args);
        String errOutput = baos.toString();
        assertThat(errOutput,containsString("Too many arguments."));
        System.setErr(System.err);
    }

    @Test
    void malFormattedTxtFilePrintsCouldNotBeParsedErr() throws ParserException {
        String malformed = "src/test/resources/edu/pdx/cs410J/leolu/malformed-flight.txt";
        String[] args = {malformed,"someplace.xml"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Converter.main(args);
        String errOutput = baos.toString();
        assertThat(errOutput,containsString("Provided txt file is malformed."));
        System.setErr(System.err);
    }

    @Test
    void txtFileNotFoundPrintsFileNotFoundErr() throws ParserException {
        String malformed = "src/test/resources/edu/pdx/cs410J/lzxcasdeolu/malformed-flight.txt";
        String[] args = {malformed,"someplace.xml"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Converter.main(args);
        String errOutput = baos.toString();
        assertThat(errOutput,containsString("Provided text file path does not have an existing txt file. Please use a valid file path."));
        System.setErr(System.err);
    }

    @Test
    void canParseTxtFileAndConvertToXmlFile() throws ParserException {
        String txtFile ="src/test/resources/edu/pdx/cs410J/leolu/evaair.txt";
        String xmlFile ="src/test/resources/edu/pdx/cs410J/leolu/evaair.xml";
        String[] args = {txtFile,xmlFile};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Converter.main(args);
        String output = baos.toString();
        assertThat(output,containsString("created successfully."));
        System.setOut(System.out);
    }
}
