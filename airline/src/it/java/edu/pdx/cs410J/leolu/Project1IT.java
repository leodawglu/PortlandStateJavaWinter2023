package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }
    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments(){
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
    }

    @Test
    void notEnoughCommandLineArguments(){
        MainMethodResult result = invokeMain("\"-print\", \"Java Airlines\", \"12345\", \"SEA\", \"05/19/2023 11:53\", \"LAX\"");
        assertThat(result.getTextWrittenToStandardError(),containsString("Missing inputs"));
    }

}