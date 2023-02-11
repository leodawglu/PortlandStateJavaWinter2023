package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
    }
    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments(){
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardOut(), containsString("No command line arguments"));
    }

    @Test
    void notEnoughCommandLineArguments(){
        MainMethodResult result = invokeMain(new String[]{"-print", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "LAX" ,"05/19/2023"});
        assertThat(result.getTextWrittenToStandardOut(),containsString("Arrival Time"));
    }

    @Test
    void tooManyCommandLineArguments(){
        MainMethodResult result = invokeMain(new String[]{"-print", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "LAX", "SEA" ,"05/19/2023", "11:53", "LAX", "SEA", "05/19/2023", "11:53", "LAX"});
        assertThat(result.getTextWrittenToStandardOut(),containsString("is an extraneous argument"));
    }

    @Test
    void invalidDestinationAirportWillNotPrintFlightInformation(){
        MainMethodResult result = invokeMain(new String[]{"-print", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53","am", "L1X" ,"05/19/2023", "11:53","pm"});
        assertThat(result.getTextWrittenToStandardOut(),containsString("must be a 3-letter alphabetical code"));
    }

    @Test
    void textFileOptionAlreadyCalled(){
        MainMethodResult result = invokeMain(new String[]{"-print", "-textFile","someFile", "-textFile", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "L1X" ,"05/19/2023", "11:53"});
        assertThat(result.getTextWrittenToStandardError(),containsString("can only be called once"));
    }
    @Test
    void consecutiveTextFileOptionPrintsCanOnlyCallOnceError(){
        MainMethodResult result = invokeMain(new String[]{"-textFile", "-textFile","someFile", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "L1X" ,"05/19/2023", "11:53"});
        assertThat(result.getTextWrittenToStandardError(),containsString("followed by a file name"));
    }

    @Test
    void optionInvokedAfterTextFileOptionPrintsCanOnlyCallOnceError(){
        MainMethodResult result = invokeMain(new String[]{"-textFile", "-textFile","someFile", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "L1X" ,"05/19/2023", "11:53"});
        assertThat(result.getTextWrittenToStandardError(),containsString("followed by a file name"));
    }

    @Test
    void invalidOptionPrintsNotValidOptionError(){
        MainMethodResult result = invokeMain(new String[]{"-bleh", "Java Airlines", "12345", "SEA", "05/19/2023", "11:53", "L1X" ,"05/19/2023", "11:53"});
        assertThat(result.getTextWrittenToStandardError(),containsString("not a valid option"));
    }


    @Test
    void textFileAirlineNameDoesNotMatchArgs() throws FileNotFoundException, ParserException {
        String filePath = getClass().getResource("evaair.txt").getPath();

        MainMethodResult result = invokeMain(new String[]{"-print", "-textFile", filePath,"EVAV Air", "25", "TPE", "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"});
        assertThat(result.getTextWrittenToStandardError(),containsString("does not match"));
    }


}