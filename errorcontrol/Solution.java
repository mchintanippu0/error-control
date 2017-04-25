import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="photo.jpg, Warsaw, 2013-09-05 14:08:15\njohn.png, London, 2015-06-20 15:13:22\nmyFriends.png, Warsaw, 2013-09-05 14:07:13\nEiffel.jpg, Paris, 2015-07-23 08:03:02\npisatower.jpg, Paris, 2015-07-22 23:59:59\nBOB.jpg, London, 2015-08-05 00:02:03\nnotredame.png, Paris, 2015-09-01 12:00:00\nme.jpg, Warsaw, 2013-09-06 15:40:22\na.png, Warsaw, 2016-02-13 13:33:50\nb.jpg, Warsaw, 2016-01-02 15:12:22\nc.jpg, Warsaw, 2016-01-02 14:34:30\nd.jpg, Warsaw, 2016-01-02 15:15:01\ne.png, Warsaw, 2016-01-02 09:49:09\nf.png, Warsaw, 2016-01-02 10:55:32\ng.jpg, Warsaw, 2016-02-29 22:13:11";
		System.out.println(solution(s));
		
	}
	public static String solution(String S) {
		
        // write your code in Java SE 8
		StringTokenizer st=new StringTokenizer(S, "\n");
		System.out.println("No of Tokens: "+st.countTokens());
		HashMap<String,Integer> indexHashMap=new HashMap<>();
		HashMap<String,HashMap<String,Long>> cityHashMap=new HashMap<>();
		HashMap<String,String> result= new HashMap<>();
		int indexForEachPhoto=0;
		String output="";
		while(st.hasMoreTokens())
		{
			String eachToken=st.nextToken();
			indexForEachPhoto+=1;
			//System.out.println("--  "+eachToken);
			StringTokenizer st1=new StringTokenizer(eachToken, ",");
			int countVariable=1;
			String keyForTheEntry=null;
			String cityEntry=null;
			long dateTime=0;
			while(st1.hasMoreTokens())
			{
				String eachItemInName=st1.nextToken().trim();
				switch(countVariable)
				{
				case 1: // this is the name of the photo
						indexHashMap.put(eachItemInName,indexForEachPhoto);
						keyForTheEntry=eachItemInName;
						break;
				case 2: // this is the city entry
						cityEntry=eachItemInName;
						break;
				case 3: // this is the date and time
						SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date=null;
						try {
							date=sdf.parse(eachItemInName);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								}
						//System.out.println(""+date.getTime());
						dateTime=date.getTime();
						break;
				}
				countVariable+=1;
			}
			//System.out.println("Each Row ="+keyForTheEntry+" "+cityEntry+" "+dateTime+"======");
			if(cityHashMap.containsKey(cityEntry)){
				cityHashMap.get(cityEntry).put(keyForTheEntry, dateTime);
			}
			else // create new Hashmap of the city and save it in the hashmap
			{
				HashMap<String,Long> currentCityHashMap=new HashMap<>();
				currentCityHashMap.put(keyForTheEntry, dateTime);
				cityHashMap.put(cityEntry, currentCityHashMap);
			}
		}
		for (String k:cityHashMap.keySet())
		{
			//System.out.println(" Key = "+k);
			 Map<String, Long> map = sortByValues(cityHashMap.get(k)); 
			 int digitsNeeded=Integer.toString(map.size()).length();
			 int valueCount=1;
			 Set set2 = map.entrySet();
		      Iterator iterator2 = set2.iterator();
		      while(iterator2.hasNext()) {
		           Map.Entry me2 = (Map.Entry)iterator2.next();
		           String name=String.format("%0"+digitsNeeded+"d",valueCount);
		           StringTokenizer stk=new StringTokenizer(me2.getKey().toString(), ".");
		           String extension=null;
		           while(stk.hasMoreTokens())
		           {
		        	   extension=stk.nextToken();
		           }
		           //String[] extension=me2.getKey().toString().split(".");
		           result.put(me2.getKey().toString(),k+name+"."+extension);
		          // System.out.println(k+name+"."+extension);
		           valueCount+=1;  
		      }
		}
		      Map<Integer,String> reverse=new HashMap<>();
		      for (Map.Entry<String, Integer> entry: indexHashMap.entrySet())
		      {
		    	  reverse.put(entry.getValue(), entry.getKey());
		      }
		      
		      //System.out.println("=="+ reverse.size()+"=="+indexHashMap.size());
		      for (Integer iter:reverse.keySet())
		      {
		    	  String d= result.get(reverse.get(iter));
		    	  output=output+d+"\n";
		    	  //System.out.println(d+"==");
		      }
		  //System.out.println(output);
		return output;
    }
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
}
