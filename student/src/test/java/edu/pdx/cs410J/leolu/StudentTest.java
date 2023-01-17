package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  void studentNamedPatIsNamedPat() {
    String name = "Pat";
    var pat = new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
    assertThat(pat.getName(), equalTo(name));
  }
  @Test
  void studentDatIsDat(){
    double gpa = 0.0;
    String dName = "Dat",gender = "non-binary";
    var dat = new Student(dName, new ArrayList<>(),gpa,gender);
    assertThat(dat.getGender(),equalTo(gender));
    assertThat(dat.getGPA(),equalTo(gpa));
    assertThat(dat.getName(),equalTo(dName));
  }

}
