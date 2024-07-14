# Technical specification

In the technical specification, we expand on the description of the program flow presented in the [detailed specification](https://github.com/szczepaf/csfd_network_analysis/blob/main/detailed_specification.md) and provide more insight into technical solutions, individual classes and important functions.


### Data download and Parsing
The results from the detailed query at ČSFD contain pages with film links (HTML content with tags holding the names of the films). We represent a page of the results by a Page class holding the links of media present at that given result page. The links can be hold in an ArrayList, which provides useful list functions. We use a `PageDownloader` class that iterates over the pages of results by increasing the page index in the URL and downloading the HTML code. We access the results by the hash of the URL with the results, as specified in the User guide.

We then use the `PageParser` class to parse the HTML contents into individual films ~ instances of the Film Class. We select the relevant tags by calling functions from the **jsoup** library.

We iterate using a similar approach towards downloading the film data - we download the HTML contents from all film URLs and parse the relevant tags into a `Film` class. We use the classes `FilmDownloader` and `FilmParser` for iterating over lists of links. The user specifies the target file for the data to be stored in. The download function should store progress continuously so that if interrupted, it is not lost.

The `Parser` and `Downloader` classes implement corresponding interfaces. The reasons for interfaces is extensibility (e.g., so that downloading actor data will be an easily implementable feature). We use the class `DownloadController` as a controller and wrapper function for working with files, orchestrating the export, and parsing. It composes the downloader and parser functions into more concise functions.

The `Film` class has to hold all relevant data about a film. In the current implementation, that is duration, name, date created, list of directors, list of actors, and rating. For the rating, we use a `Rating` class containing the total number of ratings of a given film and its average value. 
For representing actors and directors, we use the classes `Actor` and `Director`, which currently only hold the person's name. They are implementations of a `Person` interface with an overridden `Equals` method (that checks the equality of names), which will be important in constructing the network.

When working with the downloads, it is desirable to set some timeout between individual HTTP calls.

### Network construction
Currently, the program works with networks of Films. This should be easily extensible to networks of another kind (e.g. Actors). Therefore, the class `FilmNetwork` is an implementation of the interface `INetwork` containing these important methods:

- `loadNodes`: based on some filtering conditions for nodes (Films in our case), all nodes satisfying that condition will be loaded into the network
- `createEdges`: based on some binary predicate for nodes (e.g., the Films share a common actor), edges will be placed between nodes satisfying that predicate
- `export`: export the current network to a specified file. In the current implementation, we use the GraphML format, which is a common format to work with graphs and easily loadable by other software.
- `visualize`: Draw the graph. In the current implementation, we use functions from the library **GraphStream**. This method might need some work in the future so that the plots are nicer.

To decide whether a node should or an potential edge should be included in a network, we use two **functional interfaces**: `NodeCondition` and `EdgeCondition`. To work with these functional interfaces, we use the **Factory** design pattern in the class `ConditionFactory.` It parses the JSON from the user on the input and transforms it into corresponding conditions, which are then taken by the `Network` class and the `loadNodes` and `createEdges` functions. These functions iterate over all nodes and pairs of nodes and add them to a newly created network only if they satisfy the edge/node condition. 

### Main
The main class provides the user with an interface to control the program. It is a Console-like interface that, once launched, presents the user with options to run the program (see the user guide for more details). The implementation is quite straightforward: check the input, carry out the action, if possible (if not, catch the exception and notify the user), and repeat. Amongst the actions are fetching links from a detailed ČSFD query, downloading the data from these links, loading nodes, creating edges, network visualization, network export, getting help, and exiting the program. 

### Tests
Unit tests for important functions were written in the JUnit 5 framework. These are the test files:

- `ConditionFactoryTest`: tests whether the nodes are filtered correctly based on a user-specified input. It also checks the Factory pattern execution and does the same for edges.
- `FilmNetworkTest`: checks whether the mechanisms loading nodes into the network and creating edges work properly.
- `FilmTest`: Tests the Film Parsing mechanisms and Film Download Functions. When checking the correctness of downloaded data, we have to consider the important fact that the data on the portal changes constantly. We only have to check fields that remain the same in the Download structure.
- `PageTest`: Tests the mechanism of downloading data from the ČSFD portal. Tests the Page parsing mechanism. The note from the previous column also applies in this class. 
