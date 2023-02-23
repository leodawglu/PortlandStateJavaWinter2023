# Airline Project3
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
Converter Usage: java edu.pdx.cs410J.leolu.Converter textFile xmlFile

General Usage:
java -jar target/airline-2023.0.0.jar [options] <args>

args are (in this order):
airline          The name of the airline
flightNumber     The flight number (Positive Numbers only)
src              Three-letter code of departure airport (Alphabets Only)
depart           Departure date and time (mm/dd/yyyy hh:mm am/pm 12hr format only)
dest             Three-letter code of departure airport (Alphabets Only)
arrive           Arrival date and time (mm/dd/yyyy hh:mm am/pm 12hr format only)

Options:
    -pretty file     Pretty print the airline’s flights to
                     a text file or standard out (file -)
	-textFile file   Where to read/write the airline info
	-xmlFile file   Where to read/write the airline info
	-print           Prints a description of the new flight
	-README          Prints a README for this project and exits

Note -textFile and -xmlFile CANNOT be specified together
Note that time must be specified in 12-hr format HH:MM AM/PM e.g.: 11:30 am
Note that multi-word arguments must be delimited with double quotes. 
For Example, airline: "Hello Airways"  

Examples:

-xmlFile someRandomFile.xml -print "EVA Air" 25 SEA 1/26/2023 0:10 am TPE 1/27/2023 5:30 pm
-textFile someRandomFile.txt -print "EVA Air" 25 SEA 1/26/2023 0:10 am TPE 1/27/2023 5:30 pm
-textFile someRandomFile.txt "EVA Air" 25 SEA 1/26/2023 0:10 am TPE 1/27/2023 5:30 pm
-print "EVA Air" 25 SEA 1/26/2023 10:10 am TPE 1/27/2023 5:30 pm
-print -textFile someFile.txt -pretty - "EVA Air" 52 TPE 1/26/2023 12:10 am IAH 1/26/2023 12:40 pm
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

