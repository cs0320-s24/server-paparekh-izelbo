> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
Sprint 2 involved creating a server application that provides a web API for 
both data retrieval and search. Ultimately, teh server would be able to use 
2 data sources. One of them being a CSV file(s) and the United States Census 
API. The project involved the following learning goals: setting up a web API 
server that provides access to multiple data sources, using proxy/other 
patters to mediate access to the data, both serializing and deserializing 
JSONs, incorporating integration testing, utilizing mock data, and fuzz 
testing the server program with random inputs.

Team members: Priyam Parekh (paparekh) and Ian Zelbo (izelbo)

Total estimated time: 12 hours

Repo link: https://github.com/cs0320-s24/server-paparekh-izelbo

# Design Choices
To develop the server functionality, we built on top of both our CSV project 
code and the server gearup code. We also talked with a fellow classmate 
(Matt Yoon - myoon15) to gain a conceptual understanding.

The CSV parser and search functionality is handled by the Parser and Search 
classes. The Parser class contains the parsing functionality which 
essentially converts each row into an instance of T. As the name suggests, 
the Search class searches for a specific key word within a particular CSV 
file. It can perform searches across all columns or within the column 
specific by the user. 

With regard to the server functionality, the following classes were created: 
Server, ViewHandler, SearchHandler, LoadHandler, Serialization, 
BroadbandHandler, and Cache. Also, A Broadbands interface was created which 
was implemented by the BroadbandHandler class.

Delving deeper, the Server class starts the server and uses Spark to created 
the different handlers: LoadHandler, ViewHandler, SearchHandler, and lastly, 
BroadbandHandler. LoadHandler implements the Route interface (a part of the 
spark interface), which contains the handle() method. The main purpose of 
the LoadHandler method is to handle the functionality to load CSV files and 
parsing that data. It uses a Map, mapping a String and Object to keep track 
of the results from the file path. The ViewHandler class allows for the 
functionality to view the loaded data for incoming requests. Similar to the 
LoadHandler class, it implements the Route interface. This class contains 
serialization which serializes the responses into JSON strings. As a result, 
the Serialization class contains methods to convert JSON->Array and 
Array->JSON. Similar to the other handlers, the SearchHandler class 
implements the Route interface and is the primary handler for performing 
search operations on the data. It uses a hashmap to store key-value pairs 
between Strings and Objects. In addition, it uses the converts the array 
into a JSON string using Serialization method. Lastly, the BroadbandHandler 
class implements both the Route and Broadbands interface. It handles 
requests concerning the broadband data. It uses a hashmap to store the state 
data which maps from String to String. This is where the Cache class is 
instantiated. Furthermore, the BroadbandHandler class also contains the API 
request to the census URI. The Cache class utilizes Google's Guava cache 
library. This allows for the usage of LoadingCache to create a CacheBuilder and
avoid sending excessive network requests.


# Errors/Bugs
After extensively testing the server program, we have not found any bugs.
There are no checkstyle errors.

# Tests
We were able to extensively test the CSV parser functionality. 
However, we were not able to sufficiently test the Server program beyond manual checking.

# How to
Run the tests: you create a JUnit run configuration in the IntelliJ IDE.

Run the program:
(1) In the terminal, type mvn clean package
(2) Type ./run
(3) Use the server program: http://localhost:3232/[insert type of handler]?[filepath=][CSV data]
    Example: http://localhost:3232/loadcsv?filepath=data/census/dol_ri_earnings_disparity.csv

