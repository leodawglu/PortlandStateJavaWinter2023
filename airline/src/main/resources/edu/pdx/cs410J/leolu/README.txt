#Project2
Is an Airline Flight Management System - A Java Command Line Program

A simple command line program that creates an airline and a flight with a user argument. It can also read/write the airline and flight to a text file.

## Getting Started

These instructions will help you run the program on your local machine for testing and development purposes.

### Prerequisites

- Java 11 or later
- Maven 3.8 or later
- A Java development environment (e.g., Eclipse, IntelliJ)

### Installing

- Clone the repository to your local machine
- Import the project into your Java development environment
- Run the program using the `java` command in the terminal/command prompt

## Usage
java -jar target/airline-2023.0.0.jar [options] <args>

args are (in this order):
airline          The name of the airline
flightNumber     The flight number (Positive Numbers only)
src              Three-letter code of departure airport (Alphabets Only)
depart           Departure date and time (mm/dd/yyyy hh:mm format only) 
dest             Three-letter code of departure airport (Alphabets Only)
arrive           Arrival date and time (mm/dd/yyyy hh:mm format only)  

Options:
	-textFile file   Where to read/write the airline info
	-print           Prints a description of the new flight
	-README          Prints a README for this project and exits


Note that multi-word arguments must be delimited with double quotes. 
For Example, airline: "Hello Airways"  

Examples:

-textFile someRandomFile.txt -print "EVA Air" 25 SEA 1/26/2023 0:10 TPE 1/27/2023 5:30
-textFile someRandomFile.txt "EVA Air" 25 SEA 1/26/2023 0:10 TPE 1/27/2023 5:30
-print "EVA Air" 25 SEA 1/26/2023 10:10 TPE 1/27/2023 15:30
-README

## Built With

Java 17
Maven 3.9

## Contributing
Professor David Whitlock for the main source files:
https://github.com/DavidWhitlock/PortlandStateJavaGettingStarted

##Authors
Professor David Whitlock, Portland State University
Ming-Hsuan Lu, Student

