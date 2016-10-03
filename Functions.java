
import java.util.*;
/**
 * Functions class is used to define user functions
 * @author ziyuhuang
 *
 */
public class Functions {
	private HashMap<String, String> funcMap;
	
	private String foundKey = "";
	public Functions(){
		funcMap = new HashMap<String, String>();
	}
	
	/**
	 * check if the function exist or not
	 * if not exist, separate the value 
	 * for example: input string s = add(x y z)=x+y+z
	 * then key = add(x y z), value = x+y+z
	 * @param s
	 */
	public void checkFunction(String s){
		if(!funcMap.containsKey(s)){
			StringBuilder str = new StringBuilder();
			String value = "";
			
			for(int i = 0; i < s.length(); i++){
				char ch = s.charAt(i);
				if(ch == '='){
					value = s.substring(i+1);
					break;
				}
				str.append(ch);
			}
			String key = str.toString();
			funcMap.put(key, value);
		}
	}
	
	public void printAllFunctions(){
		for(String x: funcMap.keySet()){
			System.out.println(x);
			System.out.println(funcMap.get(x));
		}
			
	}
	
	/**
	 * precondition: the function prototype has been found in the funcMap
	 * based on the values that the user input, find the key of the function
	 * and perform operations by substitute the values into the evaluation function
	 * for example, in funcMap contains <"add(x y z)", "x + y + z">
	 * user input: add(1 2 3)
	 * the precondition is add(1 2 3) is shared with the same regular expression 
	 * with a key in the map. In this case which is add(x y z). 
	 * This method will get the value "1" "2" "3" and map it to x + y + z and it becomes
	 * "1 + 2 + 3" and return it.
	 * @param evlString evlString user input
	 * @return the substituted evaluation function string
	 */
	public String constructEval(String evlString){
		HashMap<String, String> map = new HashMap<String, String>();
		String operation = "";
		operation = funcMap.get(foundKey);
		int index1, index2, endIndex1, endIndex2;
		
		//get rid of '(' and ')'. e.g: turn (x + y + z) to x + y + z
		index1 = foundKey.indexOf('(');			
		index2 = evlString.indexOf('(');		
		endIndex1 = foundKey.length() - 1;
		endIndex2 = evlString.length() - 1;
		String content1 = foundKey.substring(index1 + 1, endIndex1).trim();	
		String content2 = evlString.substring(index2 + 1, endIndex2).trim();
		
		Scanner input1 = new Scanner(content1);
		Scanner input2 = new Scanner(content2);
		while(input1.hasNext()){
			map.put(input1.next(), input2.next());
		}
		input1.close();
		input2.close();
		
		//put values into the function for evaluation 
		//e.g x + y + z = value1 + value2 + value3
		String operationCopy = operation;
		for(String x: map.keySet()){
			operationCopy = operationCopy.replace(x, map.get(x));
		}
		
		return operationCopy;
	}
	
	/**
	 * check if the key has a particular regular expression is found in the funcMap
	 * @param regex regular expression
	 * @return true if the funcMap contains the key that share the same regex, false otherwise
	 */
	public boolean checkMatch(String regex){
		for(String x : funcMap.keySet()){
			if(x.matches(regex)){
				foundKey = x;
				return true;
			}
		}
		return false;
	}
	
}
