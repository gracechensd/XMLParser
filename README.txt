results.csv contains the keywords for each xml file.
results_small.csv is a small sample of results.csv; it contains keywords for just 24 files.
uris.csv contains the ontologies (URIs) for each xml file
uris_small.csv is a small sample of uris.csv
filter.txt contains the words to be filtered



Java files contain final variables that should be changed on other computers.
The latest .java files for use:

GenerateOntologies.java - Parses xml files for keywords, filters them, and writes to a csv file called results.csv. Then generates a list of ontologies in the form of URIs for each xml file based on those keywords, writing them to a csv file called uris.csv. Path names for the xml files’ location and results.csv location are specific to Grace’s computer.

GenerateCsvFile5.java - Same as GenerateOntologies but doesn’t do the ontology parts, so it just writes keywords to results.csv.

DatabaseInsert5.java - Parses xml files for keywords, filters them, and writes to a mysql table. Path names for the xml files’ location, and the mysql database credentials and table name, are specific to Grace’s computer.



Important variables:
XML_FILE_DIRECTORY: path to the directory containing xml files to be parsed
CSV_FILE: path of the resulting .csv file of keywords
CSV_FILE_URIs: path of the resulting .csv file of ontologies
FILTER: path to the text documents containing words to be filtered
recursive: if true, parses all xml files in subdirectories as well; otherwise, only parses those in directory specified by XML_FILE_DIRECTORY