# 1. Project Description
This project aims to develop a comprehensive data extraction, analysis, and machine
learning model training system. Leveraging cutting-edge technologies, 
it extracts data from diverse sources and harness its potential for actionable 
insights. By integrating information from various platforms and repositories, 
the system compiles a robust dataset that encompasses a wide range of attributes. 
Employing advanced analytical techniques, we meticulously examine this data, 
identifying patterns, trends, and relationships that would otherwise remain hidden. 
Through the application of machine learning algorithms, it develops a powerful 
ML model capable of making accurate predictions and informed decisions based 
on the analyzed data.

The project is separated in 3 steps, guided by more detailed actions which
are described comprehensively in the next steps:

1. Data Management 
   * Data Extraction
   * Data Structuring
   * Data Cleaning
   * Data Storage
   * Data Analysis
2. Model Creation
   * Model Training
   * Model Testing
   * Model Validation 
3. Result Visualization

# 2. Data Management Description
Data management plays a critical role in ensuring the reliability, integrity, 
and accessibility of the extracted data. Firstly, we establish a systematic process 
for data collection from different sources, including databases, APIs, web scraping, 
and other relevant platforms. The main data source is the website https://www.lyrics.com/,
which has lyrics of all artists found on the internet. 

**Data Extraction Step** is done mainly by developing a web crawler that go through the links of artists'
pages and extract the necessary information, such as artist name, albums and unmediated, lyrics.
As mentioned above, it does not limit only to web crawler. So depending on the type of data and where 
is located, necessary steps will be fulfilled to extract them. 

**Data Structuring Step** is important to give the meaning of the data. The structure will imply
columns of the identity and origin of the data. For example the lyrics from lyrics dataset will 
have columns as:

| Artist | Album Name | Song Name | Lyrics |
|--------|------------|-----------|--------|       

**Data Cleaning Step** includes techniques to remove inconsistencies, errors, and duplicates 
from the collected data. This involves identifying and rectifying missing values, 
standardizing formats, and resolving any discrepancies that could potentially affect 
the analysis and ML model training.

**Data Storage Step** implies searching of a convenient platform to store the extracted data. Because
it extracts only textual data the list of storing options it's shrank to only services that provide 
a comfortable solution for this case. Depending on the chose architecture, it will imply the steps
of creation of an adapter to manipulate with extracted data.

**Data Analysis Step** is the final step of Data Management, where we can see some results of the 
extractions which gives answers on the business questions. It will imply the usage of different 
machine learning algorithms such as clustering or classification and visualization tools such as JTableSaw.


# 3. Model Creation Description
This step is strongly connected with the second step (Data Management), because based on the results
that were provide, it will become possible to train a Machine Learning Model for the next described 
purposes. 




















