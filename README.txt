results.csv contains the keywords for each xml file.
results_small.csv is a small sample of results.csv; it contains keywords for just 24 files.
filter.txt contains the words to be filtered



Java files contain final variables that should be changed on other computers. The latest .java files for use:

GenerateCsvFile5.java - Parses xml files for keywords, filters them, and writes to a csv file called results.csv. Path names for the xml files’ location and results.csv location are specific to Grace’s computer.

DatabaseInsert5.java - Parses xml files for keywords, filters them, and writes to a mysql table. Path names for the xml files’ location, and the mysql database credentials and table name, are specific to Grace’s computer.



Important variables:
XML_FILE_DIRECTORY: path to the directory containing xml files to be parsed
CSV_FILE: path of the resulting .csv file
FILTER: path to the text documents containing words to be filtered
recursive: if true, parses all xml files in subdirectories as well; otherwise, only parses those in directory specified by XML_FILE_DIRECTORY