|*****************************--README.txt--*****************************|
|*************************--Project1 | Airline--*************************|
| Project1 is a simple command line application that creates an airline  |
| and a flight with a user argument.                                     |
|                                                                        |
|*****************************----USAGE-----*****************************|
|                                                                        |
| To print the README.txt file, type "-README" at the beginning of       |
| argument and press enter. Note that when "-README" is included,        |
| the rest of the argument would be ignored.                             |
|------------------------------------------------------------------------|
| To print out the airline and flight created, type "-print" at the      |
| beginning of the argument and press enter. Note that "-print" will     |
| only execute when the airline and flight arguments are valid.          |
|                                                                        |
| To create an airline and a flight, enter each detail in this order,    |
| with each detail separated by a space:                                 |
|    airline - The name of the airline                                   |
|    flightNumber - The flight number (Positive Numbers only)            |
|    src - Three-letter code of departure airport (Alphabets only)       |
|    depart - Departure date and time (mm/dd/yyyy hh:mm format only)     |
|    dest - Three-letter code of arrival airport (Alphabets only)        |
|    arrive - Arrival date and time (mm/dd/yyyy hh:mm format only)       |
|                                                                        |
| Note that multi-word arguments must be delimited with double quotes.   |
|    For example                                                         |
|    -   airline: "Hello Airways"                                        |
|                                                                        |
| Example Airline and Flight argument:                                   |
|    "Java Airlines" 12345 PDX 05/19/2023 11:53 SEA 05/21/2023 17:33     |
|                                                                        |
| To run the program, enter:                                             |
|    java -jar target/airline-2023.0.0.jar                               |
| Followed by the arguments.                                             |
| This project is based on:                                              |
| https://github.com/DavidWhitlock/PortlandStateJavaWinter2023           |
| If you have any questions or feedback, please contact leolu@pdx.edu    |
|*****************************--------------*****************************|