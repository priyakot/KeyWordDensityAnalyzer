package com.word.density;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Program: Word Density Analyzer Execution: Ability to handle the following
 * Execution: java -jar Assignment.jar (e.g. java -jar Assignment.jar
 * "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/")
 * 
 * The number of relevant keywords (single, two words pairs) can be changed by setting the static variable 
 * RELEVANTWORDS_COUNT to the desired value. Default value is 10.
 * 
 * @author priyakotwal
 * 
 */

public class WordDensityAnalyzer {

	// List of stopwords frequently ocuring in English language. This array can
	// be extended to include more stopwords in the future
	static String[] stopwords = {
		"a","about","above","after","again","against",
		"all","am","an","and","any","are","aren't","as","at","be",
		"because","been","before","being","below","between","both",
		"but","by","can't","cannot","could","couldn't","did","didn't",
		"do","does","doesn't","doing","don't","down","during","each","few",
		"for","from","further","had","hadn't","has","hasn't","have","haven't",
		"having","he","he'd","he'll","he's","her","here","here's","hers","herself",
		"him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in",
		"into","is","isn't","it","it's","its","itself","let's","me","more","most",
		"mustn't","my","myself","no","nor","not","of","off","on","once","only","or",
		"other","ought","our","ours","ourselves","out","over","own","same","shan't",
		"she","she'd","she'll","she's","should","shouldn't","so","some","such","than",
		"that","that's","the","their","theirs","them","themselves","then","there","there's",
		"these","they","they'd","they'll","they're","they've","this","those","through","to",
		"too","under","until","up","very","was","wasn't","we","we'd","we'll","we're","we've",
		"were","weren't","what","what's","when","when's","where","where's","which","while",
		"who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd",
		"you'll","you're","you've","your","yours","yourself","yourselves"	
	};
	
	public static int RELEVANTWORDS_COUNT = 10;

	public static void main(String[] args) {
		// Take url from user
		//String url = "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile";
		String url = args[0];
		Document doc = null;
		try {
			if (url == null)
				return;
			UrlValidator urlValidator = new UrlValidator();
			if (urlValidator.isValid(url) == false)
				return;
			
			doc = establishConnection(url);	
			// doc.text() in Jsoup helps to extract only text without html
			// Splitting the text string at space
			String[] words = doc.text().split("\\s+");
			if (words.length == 0)
				return ;
			calcWordDensity(words);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to establish connection 
	 * @param url
	 * @return
	 */
	private static Document establishConnection(String url) {
		Document doc = null;
		Connection.Response response = null;
		try {
			response = Jsoup.connect(url)
			        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
			        .timeout(10000)
			        .execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		int statusCode = response.statusCode();
		if(statusCode == 200) {
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (doc != null) {
				return doc;
			}
		}
		System.out.println("URL Not Available");
		return null;
	}

	/**
	 * calcWordDensity calculates the density of single keywords and two
	 * keywords. Approach: Remove the special characters using a regex. Remove
	 * the stopwords. Add the words to the dictionary
	 * 
	 * @param words
	 */
	private static void calcWordDensity(String[] words) {
		HashMap dictionary = new HashMap();
		HashMap twoKeyWords = new HashMap();
		for (int i = 0; i < words.length; i++) {
			// Regex to remove special characters
			words[i] = words[i].replaceAll("[()?:!.,;\\\"]+", "");
			if(isStopWord(words[i])){
				continue;
			}
			if (dictionary.containsKey(words[i].toLowerCase())) {
				Integer count = (Integer) dictionary.get(words[i].toLowerCase());
				dictionary.put(words[i].toLowerCase(), count + 1);
			} else
				dictionary.put(words[i].toLowerCase(), 1);
			if (i != words.length - 1) {
				if(!isStopWord(words[i+1])){
					if (twoKeyWords.containsKey(words[i].toLowerCase() + " " + words[i + 1].toLowerCase())) {
						Integer count = (Integer) twoKeyWords.get(words[i].toLowerCase() + " "
								+ words[i + 1].toLowerCase());
						twoKeyWords.put(words[i].toLowerCase() + " " + words[i + 1].toLowerCase(), count + 1);
					} else
						twoKeyWords.put(words[i].toLowerCase() + " " + words[i + 1].toLowerCase(), 1);					
				}
			}
			// Density of Three key words can also be calculated using the above approach
		}

		printMaps(dictionary, words.length);
		System.out.print("Two Keywords Pairs ");
		printMaps(twoKeyWords, words.length);
		System.out.println("Total words: "+ words.length);

	}

	private static boolean isStopWord(String s) {
		// TODO Auto-generated method stub
		for (String stopWord : stopwords) {
			if (s.equalsIgnoreCase(stopWord) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to print the dictionary and find the top keywords
	 * @param dictionary
	 * @param totalWords
	 */
	private static void printMaps(HashMap dictionary, int totalWords) {
		Iterator<Entry> it = dictionary.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			int count = Integer.parseInt(pair.getValue().toString());
			float density = (float) count / totalWords * 100;
			pair.setValue(density);
			//System.out.println(pair.getKey() + " " + density + " " + count);
		}
		Iterator<Entry> it2 = dictionary.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pair = (Map.Entry) it2.next();
			//System.out.println(pair.getKey() + " " + pair.getValue());
		}

        List<Entry<String, Integer>> greatest = findGreatest(dictionary, RELEVANTWORDS_COUNT);
        System.out.println("Top "+RELEVANTWORDS_COUNT+" keywords:");
        for (Entry<String, Integer> entry : greatest)
        {
            System.out.println(entry.getKey());
        }

	}

	
	/**
	 * Method to find the highest
	 * @param map
	 * @param n
	 * @return
	 */
	 private static <K, V extends Comparable<? super V>> List<Entry<K, V>> 
     findGreatest(Map<K, V> map, int n)
 {
     Comparator<? super Entry<K, V>> comparator = 
         new Comparator<Entry<K, V>>()
     {
         @Override
         public int compare(Entry<K, V> e0, Entry<K, V> e1)
         {
             V v0 = e0.getValue();
             V v1 = e1.getValue();
             return v0.compareTo(v1);
         }
     };
     PriorityQueue<Entry<K, V>> highest = 
         new PriorityQueue<Entry<K,V>>(n, comparator);
     for (Entry<K, V> entry : map.entrySet())
     {
         highest.offer(entry);
         while (highest.size() > n)
         {
             highest.poll();
         }
     }

     List<Entry<K, V>> result = new ArrayList<Map.Entry<K,V>>();
     while (highest.size() > 0)
     {
         result.add(highest.poll());
     }
     return result;
 }
	
	/**
	 * In order to to crawl the webpage, this method can be used to extract urls
	 * 
	 * @param doc
	 * @return
	 */
	private static ArrayList<String> getLinks(Document doc) {
		Elements links = doc.select("a[href]");
		//System.out.println(links.html().toString());
		ArrayList<String> urlList = new ArrayList<String>();
		HashSet urlSet = new HashSet<>();
		for (Element link : links) {
			if (!urlSet.contains(link.attr("href")))
				urlSet.add(link.attr("href"));
			urlList.add(link.attr("href"));
		//	System.out.println(link.attr("href") + "  " + link.text());
		}
		return urlList;

	}
}
