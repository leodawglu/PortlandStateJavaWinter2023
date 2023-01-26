/**
 * The {@code Project1Test} class contains unit tests for the {@link edu.pdx.cs410J.leolu.Project1} class
 *
 *
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 *
 * How to write tests for Project1
 * Think of Project1 as Student class
 * Write tests of various arg inputs/scenarios
 * So class can be tested independently
 *
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("README"));
    }
  }

  @Test
  void sampleTestPrintInput(){
    Project1.main(new String[] {"-print", "EVA Airways", "26", "TPE", "05/19/2023", "23:40", "SEA", "05/19/2023", "18:40"});
  }

  @Test
  void sampleREADMETestInput(){
    Project1.main(new String[] {"-README","-print", "Java Airlines", "12345", "PDX", "05/19/2023", "11:53", "SEA", "05/21/2023", "17:33"});
  }


}
