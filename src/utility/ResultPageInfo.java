package utility;

public class ResultPageInfo
{
	public String url;
	public String bodySnippet;
	public int importance;
	public int phrasesCount;
	
	public ResultPageInfo(String url, String body, int imp, int phr)
	{
		this.url = url;
		this.bodySnippet = body;
		this.importance = imp;
		this.phrasesCount = phr;
	}
	
	/**
	 * 
	 * @param second to cmpare
	 * @return 0 for equal, 1 greater than second, -1 less than second 
	 */
	public int compare(ResultPageInfo second)
	{
		if(this.phrasesCount > second.phrasesCount)
		{
			return 1;
		}else if(this.phrasesCount < second.phrasesCount)
		{
			return -1;
		}else if(this.importance > second.importance)
		{
			return 1;
		}else if(this.importance < second.importance)
		{
			return -1;
		}else return 0;
	}
}
