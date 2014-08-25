keywords.csv contains the keywords for each xml file.
keywords_small.csv is a small sample of keywords.csv; it contains keywords for just 24 files.
uris.csv contains the ontologies (URIs) for each xml file
uris_small.csv is a small sample of uris.csv
filter.txt contains the words to be filtered

Database tables should be created before running GenerateOntologies2.java.
CREATE table Keywords2 (id int not null auto_increment, file_name varchar(80) not null, keyword varchar(180), primary key (id));
CREATE table Ontologies2 (id int not null auto_increment, file_name varchar(80) not null, ontology varchar(180), primary key (id));


Java files contain final variables that should be changed on other computers.
The latest .java files for use:

GenerateOntologies.java - Parses xml files for keywords, filters them, and then generates a list of ontologies in the form of URIs for each xml file based on those keywords. Writes keywords to keywords.csv and ontologies to uris.csv. Path names for the xml files’ location and keywords.csv location are specific to Grace’s computer.

GenerateOntologies2.java - Does the same as GeneratesOntologies.java, but writes to a MySQL database instead to .csv files. Can write EITHER keywords OR ontologies table; haven’t yet figured out a good way to specify writing to one, both, or neither. Path names for the xml files’ location, and the mysql database credentials and table name, are specific to Grace’s computer.



Important variables:
XML_FILE_DIRECTORY: path to the directory containing xml files to be parsed
CSV_FILE: path of the resulting .csv file of keywords
CSV_FILE_URIs: path of the resulting .csv file of ontologies
FILTER: path to the text documents containing words to be filtered
recursive: if true, parses all xml files in subdirectories as well; otherwise, only parses those in directory specified by XML_FILE_DIRECTORY
keyword_csv: if true, generates a csv file for all the keywords
ontology_csv: if true, generates a csv file for all the ontologies
table_is_ontologies: if true, writes ontologies table; if false, writes keywords table