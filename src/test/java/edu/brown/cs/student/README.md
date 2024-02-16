# Project Name:

This Sprint 1 CSV project is created by Priyam Parekh (CS
login:
paparekh).

# Project Details:

The CSV parser allows the user to use the command-line
program
and search a CSV file. In addition, the parser is able to parse CSV data from
any Reader object.
Furthermore, a user is able to implement an interface to dictate which type of
object the CSV parser will convert each row into, along with how to do that
conversion.

# Hours Worked:

It took approximately 7-8 hours to complete the project.

# Repo Link:

https://github.com/cs0320-s24/csv-priyam-parekh

# Design Choices:

In order to parse the command line arguments, I used the
Apache Commons CLI library (Can be found here: https://commons.apache
.org/proper/commons-cli/). Essentially, it allowed me to efficiently parse the
user's command line messages while enabling me to print help messages that
detailed the options available to the user.
In a high level overview, the CSV project is organized into 4 key folders: csv,
datamodel, parser, and search—and Main.
The csv folder contains the CsvDataManager class whose main purpose is to search
within the parse data; more specifically, it parses the CSV rows and allows for
searching functionality for text in the parsed data.
The datamodel folder contains the Header and StringListCreator class. The Header
contains the header information such as retrieving column indices and the total
number of columns.
The StringListCreator class creates an ArrayList<String> based on the
List<String> row input parameter.
The parser folder contains the CreatorFromRow and DataParser interface along
with the FactoryFailureException.
The search folder contains the Searcher interface along with the SearchOptions
and SearchResult class and SearchType enum. The SearchOptions class handles the
enum functionality by handling the different types of search options. The
SearchResult class processes the result of a search option; for example, the
number of matches for a word and the row/column indices where a word is found.
As mentioned earlier, I used the Apache Commons CLI library which allowed me to
handle various options for the user. For example, the user can input -h or
--help (for help), -i or --ignore-case (to perform case-insensitive search -
default: case-sensitive),
-nh or --no-headers (when file does not contain headers - default: file
contains headers), and lastly, -s or --match-substring (for match substrings -
default: exact-match). Ultimately, this gives the user more control over the
specific functionality required when using the CSV parser.

# Relationship Between Classes/Interfaces:

My implementation consists of 3 interfaces: CreatorFromRow, DataParser, and
Searcher.
The CreatorFromRow interface is implemented by the StringListCreator where an
ArrayList<String> is created from a List<String> Row.
The DataParser interface is implemented by the CsvDataManager class. The
interface contains the processData() method which processes data from a given
Reader by parsing CSV rows and creating objects based on the specified type T.
Finally, the Searcher interface is also implemented by the CsvDataManager class
. More specifically, the search() method form the Searcher interface is
utilized to perform a search on the data for a word based on the search
options.

# Specific Data Structures Used:

The main data structure that I utilized to hold the row and column indices for a
particular word was a HashMap. A HashMap
mapping an Integer to an ArrayList<Integer> allowed me to organize the row,
column locations.
I also used an enum to allow the user to efficiently operate the command line
interface. The enum represented the various search types: CASE_SENSITIVE,
CASE_INSENSITIVE, EXACT, and SUB_STRING.

# Errors/Bugs:

After thoroughly testing my CSV program, I have not found any bugs.

# Tests:

To test the program, 3 test suites were created: CsvDataManagerTest,
SearchOptionsTest, and SearchResultTest. It contained comprehensive tests
for: CSV data with and without column headers; CSV data in different
Reader types (e.g., StringReader and FileReader); CSV data with inconsistent
column count;
CSV data that lies outside the protected directory;
Searching for values that are, and aren’t, present in the CSV;
Searching for values that are present, but are in the wrong column;
Searching for values by index, by column name, and without a column identifier;
and
using multiple CreatorFromRow classes to extract CSV data in different formats.

# How to Run the tests you wrote/were provided:

Create a JUnit run configuration in the IntelliJ IDE.

# How to Build and run your program:

(1) In the terminal, type mvn clean package
(2) Then, type ./run [options (-i, -nh, -s)] <filename/path> <text-to-search>
[column-name|column-index]
An example is ./run -i data/stars/ten-star.csv Sol 1







