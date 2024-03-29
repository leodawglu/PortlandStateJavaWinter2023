/**
 * The {@code Project2Test} class contains unit tests for the {@link edu.pdx.cs410J.leolu.Project4} class
 *
 *
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A unit test for code in the <code>Project2</code> class.  This is different
 * from <code>Project2IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 *
 * How to write tests for Project2
 * Think of Project2 as Student class
 * Write tests of various arg inputs/scenarios
 * So class can be tested independently
 *
 */
class Project4Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project4.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Airline"));
    }
  }

  @Test
  void sampleTestPrintInput() throws IOException {
    Project4.main(new String[] {"-print", "EVA Airways", "26", "TPE", "05/19/2023", "23:40", "SEA", "05/19/2023", "18:40"});
  }

  @Test
  void sampleREADMETestInput() throws IOException {
    Project4.main(new String[] {"-README","-print", "Java Airlines", "12345", "PDX", "05/19/2023", "11:53", "SEA", "05/21/2023", "17:33"});
  }

  @Test
  void sample12hrTestPrintInput() throws IOException {
    Project4.main(new String[] {"-print", "EVA Airways", "26", "TPE", "05/19/2023", "3:40","pm", "SEA", "05/19/2023", "8:40","AM"});
  }

  @Test
  void testPrintFlightToStdOut() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project4 proj = new Project4();
    proj.main(new String[]{"-print", "EVA Air", "12345", "SEA", "05/19/2023", "11:03","am", "LAX" ,"05/19/2023", "11:53","pm"});
    String output = baos.toString();
    assertThat(output,containsString("EVA Air 12345\n" +
            "Departing From: SEA\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 11:03 AM\n" +
            "Bound For     : LAX\n" +
            "Arrival Date  : 05/19/2023\n" +
            "Arrival Time  : 11:53 PM"));
    System.setOut(System.out);
  }

  @Test
  void toggleMin12hrChangeDefaultValueFrom2To0(){
    Project4 proj = new Project4();
    proj.toggleMin12hr();
    assertThat(proj.min12hr,equalTo(0));
    proj.toggleMin12hr();
    assertThat(proj.min12hr,equalTo(2));
  }

  @Test
  void missingArgsPrintlnShallPrint24Hr(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project4 proj = new Project4();
    proj.missingArgsPrintln(0,proj);
    String output = baos.toString();
    assertThat(output,containsString("Arrival Time AM/PM"));
    System.setOut(System.out);
  }

  @Test
  void noMissingArgsPrintlnShallPrintNothing(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project4 proj = new Project4();
    proj.missingArgsPrintln(10,proj);
    String output = baos.toString();
    assertThat(output,equalTo("The following arguments are missing: \nPlease review usage and try again.\n"));
    System.setOut(System.out);
  }
  @Test
  void missingArgsPrintlnShallPrintsOnly24hrMsg(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project4 proj = new Project4();
    proj.toggleMin12hr();
    proj.missingArgsPrintln(0,proj);
    String output = baos.toString();
    assertThat(output,containsString("Arrival Time"));
    System.setOut(System.out);
  }

  @Test
  void invokePrettyOptionUsingOptionChecker(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();

    proj.optionChecker("-pretty",proj);
    assertEquals(proj.prettyStatus,1);
    proj.optionChecker("-",proj);
    assertEquals(proj.prettyStatus,-2);
    proj.optionChecker("-pretty",proj);
    String output = baos.toString();
    assertThat(output,containsString("can only be called once."));
    System.setErr(System.err);
  }

  @Test
  void prettyOptionNotFollowedByDashOrFilename(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();

    proj.optionChecker("-pretty",proj);
    assertEquals(proj.prettyStatus,1);
    proj.optionChecker("-pretty",proj);
    String output = baos.toString();
    assertThat(output,containsString("should be followed by a file name"));
    System.setErr(System.err);
  }

  @Test
  void prettyPrintMethodInvokedWithWrongStatus(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();
    proj.prettyPrint(3,proj);
    String output = baos.toString();
    assertThat(output,containsString("Incorrect Status code!"));
    System.setErr(System.err);
  }

  @Test
  void prettyPrintMethodPrintToStdOut() throws IOException {
    String[] args = new String[]{"-pretty", "-","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project4 proj = new Project4();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("Flight Number : 25\n" +
            "Departing From: TPE\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 12:40 AM\n" +
            "Bound For     : SEA\n" +
            "Arrival Date  : 05/20/2023\n" +
            "Arrival Time  : 12:10 PM\n" +
            "Duration HH|MM: 35hrs 30mins\n" +
            "Duration(mins): 2130"));
    System.setOut(System.out);
  }

  @Test
  void prettyPrintMethodPrintToFile() throws IOException {
    String path=getClass().getResource("prettywriting.txt").getPath();
    String[] args = new String[]{"-pretty",path,"-print","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};

    Project4 proj = new Project4();
    proj.subMainForTesting(args);
  }
  @Test
  void prettyPrintPrintToFile(@TempDir File tempDir) throws IOException{
    File file = new File(tempDir, "test.txt");
    FileWriter writer = new FileWriter(file);
    PrettyPrinter printer = new PrettyPrinter(writer);
    Flight fl = new Flight("25", "TPE",
            "05/19/2023", "12:40 am", "SEA", "05/20/2023", "12:10 pm");
    Airline air = new Airline("EVA Air");
    air.addFlight(fl);
    printer.dump(air);
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String contents="";
    for(String line : reader.lines().toArray(String[]::new)){
      contents += line;
      contents += "\n";
    }
    reader.close();
    assertThat(contents, containsString(air.getName()));
    assertThat(contents, containsString("Flight Number : "));
    assertThat(contents, containsString("Duration HH|MM: "));

  }

  @Test
  void prettyPrintAnAirlineToStdOut() throws IOException {
    String airpath=getClass().getResource("evaair.txt").getPath();
    String[] args = new String[]{"-textFile",airpath,"-pretty","-","-print","EVA Air", "52", "TPE",
            "05/19/2023", "12:20","am", "IAH", "05/19/2023", "2:00","pm"};

    Project4 proj = new Project4();
    proj.subMainForTesting(args);
  }

  @Test
  void prettyPrintAnAirlineToFile() throws IOException {
    String path=getClass().getResource("prettywriting.txt").getPath();
    String airpath=getClass().getResource("evaair.txt").getPath();
    String[] args = new String[]{"-textFile",airpath,"-pretty",path,"-print","EVA Air", "52", "TPE",
            "05/19/2023", "12:20","am", "IAH", "05/19/2023", "2:00","pm"};

    Project4 proj = new Project4();
    proj.subMainForTesting(args);
  }

  @Test
  void xmlFileFollowedByDashPrintsErr() throws IOException {
    String[] args = new String[]{"-xmlFile", "-","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("The -xmlFile option should be followed by a xml file name."));
    System.setErr(System.err);
  }

  @Test
  void xmlFileCalledAfterTextFilePrintsErr() throws IOException {
    String[] args = new String[]{"-textFile","sometext.txt","-xmlFile", "somexml.xml","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("The -textFile option was called before -xmlFile option."));
    System.setErr(System.err);
  }

  @Test
  void textFileCalledAfterXmlFilePrintsErr() throws IOException {
    String[] args = new String[]{"-xmlFile", "somexml.xml","-textFile","sometext.txt","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("The -xmlFile option was called before -textFile option."));
    System.setErr(System.err);
  }
/*
  @Test
  void successfullyAddFlightToXmlFile() throws IOException {
    String[] args = new String[]{"-xmlFile", "src/test/resources/edu/pdx/cs410J/leolu/evaair.xml","EVA Air", "52", "TPE",
            "05/19/2023", "10:10","pm", "IAH", "05/20/2023", "9:40","pm"};
    Project4 proj = new Project4();
    proj.subMainForTesting(args);
  }*/

  @Test
  void successfullyCreatesNewXmlFileForAirline() throws IOException {
    String path="src/test/resources/edu/pdx/cs410J/leolu/taiwanair.xml";
    String[] args = new String[]{"-xmlFile",path,"Taiwan Air", "52", "TPE",
            "05/19/2023", "10:10","pm", "IAH", "05/20/2023", "9:40","pm"};
    Project4 proj = new Project4();
    proj.subMainForTesting(args);
    File temp = new File(path);
    temp.delete();
  }

  @Test
  void differentAirlineNameBetweenXMLFileAndArgsPrintErr() throws IOException {
    String[] args = new String[]{"-xmlFile", "src/test/resources/edu/pdx/cs410J/leolu/evaair.xml","EVAV Air", "52", "TPE",
            "05/19/2023", "10:10","pm", "IAH", "05/20/2023", "9:40","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project4 proj = new Project4();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("does not match"));
    System.setErr(System.err);
  }
}
