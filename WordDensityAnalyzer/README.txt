/**
 * Program: Word Density Analyzer Execution: Ability to handle the following
 * Execution: java -jar Assignment.jar (e.g. java -jar Assignment.jar
 * "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/")
 * 
 * The number of relevant keywords (single, two words pairs) can be changed by setting the static variable 
 * RELEVANTWORDS_COUNT to the desired value. Default value is 10.
 * 
 * @author priyakotwal  //University of Southern California
 
 
	Program logic
	1. Validate URL
	2. establishConnection (Check for exceptions)
	3. Used Jsoup library to parse html content 
		doc.text() in Jsoup helps to extract only text without html
		Split the text string at space
	4. Calculate word density
		1. Remove special chars
		2. Skip stop words
		3. Used Hashmaps for single word and two keywords
		4. Calculated word density
		5. Printed the top keywords

 
 * 
 */
 
 
 Sample output for the URL:
 java -jar Assignment.jar "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/"
 Output:
 
Priyas-MacBook-Pro:WordDensityAnalyzer priyakotwal$ java -jar Assignment.jar "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/"
Top 10 keywords:
edward
watched
just
government
surveillance
said
us
nsa
snowden

Two Keywords Pairs Top 10 keywords:
nsa leaker
us intelligence
national security
hong kong
patriot act
must watch
just watched
videos ...
must
edward snowden
Total words: 1572
