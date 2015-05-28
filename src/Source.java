import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.ResultPageInfo;
import contcy.QueryProcessor;

public class Source
{
	public static void main(String[] args)
	{
		QueryProcessor QP = new QueryProcessor();
		QP.processQuery("foundation \"latest news\" page");
		
		for(ResultPageInfo RP : QP.results)
		{
			System.out.println(RP.url);
			//System.out.println(RP.bodySnippet);
		}
	}

}