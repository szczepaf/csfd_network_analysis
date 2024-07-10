### Title:
ČSFD Network Analysis

### Description:
ČSFD Network Analysis is a Java project written to create, visualize, and export film networks using data from the ČSFD portal. The program constructs a network of films, adds nodes and edges based on specific conditions, visualizes the network, and exports it. The underlying motivation for the program is to explore approximate symmetries in the film network (see the theoretical introduction in the user guide).

### Parts of the program
The program can be decomposed into the following parts:
- Data download
- Data parsing
- Network creation
- Network visualization
- Network export
- Console interface

The implementation logic of these parts is described below:

### Data download
The first step in the process of network creation is retrieving the target data from ČSFD. It consists of fetching the movie links from a detailed query directly in the portal and then downloading the data corresponding to these links. 

A detailed query can be constructed directly in the ČSFD [https://www.csfd.cz/podrobne-vyhledavani/](website). We select the selected filters and retrieve the films. The URL of the resulting query has a hashed suffixed corresponding to that query. We use this suffix to refer to the results of the query, and using pagination and a suitable Java library for HTML handling (e.g., the library ``jsoup``), we parse the links of the films in the results.

The second part is downloading the film data itself. Once we have the film links, we iterate over them and download the contents of the ČSFD pages of the movies. We use parser and downloader classes to parse and download the pages of results and movie data itself.

### Data parsing and important classes
Once the film data is downloaded, we parse it into instances of the class representing the movies. The class should hold the movie name, the actors and directors of the movie, the movie rating, the length, the date created, and possibly other attributes. Similarly, to represent other entities, we use classes representing a Person (and classes for Actors and Directors inheriting from this class) and a Rating of a movie (which should hold the average of all ratings and rating count).

### Network creation
Once all data is parsed into instances of various ČSFD entities, we create a film network based on some condition. The conditions are predicates for movies and films. The conditions to be satisfied are as follows: Is the rating of a movie higher than 80 %? Was it created after the year 2000? Do the two movies share a director? Only nodes satisfying the condition are included in the network. Pairs of nodes satisfying the binary predicate are connected by an edge. This way, the network is populated. For a full list of possible conditions, refer to the user guide.

The way the condition is implemented is a functional interface. Conditions are created by the class Condition Factory, which implements the Factory design pattern and produces conditions based on specifications from the user.

### Network visualization
The network should be visualized so that the user has some sense of the result. Java is not as smooth in graph visualization as, e.g., Python's NetworkX, but in the end, the library GraphStream proved sufficient. We call suitable library functions to display the graph (excluding isolated vertices for simplicity and visual purposes).

### Network export
For further analysis of the network, it is desirable to be able to export it. Again, we use functions of the GraphStream library and offer the user the option to export the network to the GraphML format, which is one of the more common graph formats that are easily loadable by libraries like NetworkX.

### Network interface
The selected user interface is sort of a console. It queries the user about the current action and provides help if asked. In every step, the user selects which step (data download, link fetching, network export, etc.) to do, and the console either facilitates the action if the user input is correct or prints out an error message and suitable help.