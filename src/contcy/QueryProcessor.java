package contcy;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.ResultPageInfo;
import db.Database_Manager;

//TODO sort
public class QueryProcessor
{
	private String query;
	private Database_Manager DBM;

	// lists
	private Vector<String> wordsList;
	private Vector<String> phrasesList;
	public Vector<ResultPageInfo> results;

	public QueryProcessor()
	{
		DBM = new Database_Manager();
		DBM.Openconn("jdbc:mysql://localhost:3306/Crawler_Database", "root","m123456789");
		wordsList = new Vector<String>();
		phrasesList = new Vector<String>();
		results = new Vector<ResultPageInfo>();
	}

	public void processQuery(String query)
	{
		//clean
		wordsList.clear();
		phrasesList.clear();
		results.clear();
		
		this.query = query;
		extractPhrases();
		extractWords();
		search();
	}

	private void extractPhrases()
	{
		Pattern pattern = Pattern.compile("\"(.*?)\"");
		Matcher matcher = pattern.matcher(query);
		while (matcher.find())
		{
			String str = matcher.group();
			str = str.substring(1, str.length() - 1).trim().toLowerCase();
			str = str.replaceAll("[^a-zA-Z ]", "");
			phrasesList.add(str);
		}
	}

	private void extractWords()
	{
		//query = query.replaceAll("\"(.*?)\"", " ");
		String[] templist;
		query = query.replaceAll("[^a-zA-Z ]", " ");
		templist = query.split(" ");
		for (String str : templist)
		{
			str = str.trim().toLowerCase();
			wordsList.add(str);
		}
	}
	
	private void sortResults()
	{
		//TODO
	}
	private void search()
	{
		Vector<String> tmplist = new Vector<String>();
		for(int i=0;i<wordsList.size();i++)
		{
			//search by words
			System.out.println(wordsList.elementAt(i));
			tmplist = DBM.Search(wordsList.elementAt(i));
			for(int k=0;k<tmplist.size();k++)
			{
				addResultByWordsRep(tmplist.elementAt(k), DBM.list_of_Rep_W.elementAt(k));
			}
			
			//update phrases count
			updatePhrasesCount();
		}
	}
	
	private void addResultByWordsRep(String url, int wordrep)
	{
		int indx = isResultExist(url);
		if(indx == -1)
		{
			//create new ResultPageInfo object
			ResultPageInfo newResult = new ResultPageInfo(url, DBM.GetBody(url), wordrep, 0);
			results.add(newResult);
		}else
		{
			//increment the importance by word number of occurrence
			results.elementAt(indx).importance += wordrep;
		}
	}
	
	private void updatePhrasesCount()
	{
		for(int i=0;i<results.size();i++)
		{
			for(int k=0;k<phrasesList.size();k++)
			{
				if(DBM.Get_Statement(results.elementAt(i).url, phrasesList.elementAt(k)))
					results.elementAt(i).phrasesCount++;
			}
		}
	}
	
	private int isResultExist(String url)
	{
		for(int i=0;i<results.size();i++)
		{
			if(results.elementAt(i).url.equals(url))
				return i;
		}
		
		return -1;
	}
}
