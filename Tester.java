import java.util.*;
public class Tester {

	public static void main(String[] args) {
		Parse test = new Parse();
		Functions f = new Functions();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to Ziyu's promopt, enter 'quit' or 'exit' to terminate the program.");
		String input = "";
		System.out.print("==> ");
		input = in.nextLine().trim();
		while(input.isEmpty()){
			System.out.print("==> ");
			input = in.nextLine().trim();
		}
		do{
			if(input.contains("func ") && isFunctionSyntax(input)){
				System.out.println("You entered a new function");
				String s = input.substring(5).trim(); //eg: input: func add(a b c)=a+b+c then s: add(a b c)=a+b+c
				f.checkFunction(s);
				f.printAllFunctions();
				System.out.print("==> ");
				input = in.nextLine().trim();
				
				while(input.isEmpty()){
					System.out.print("==> ");
					input = in.nextLine().trim();
				}
			}else{
				if(isFunctionFormat(input)){
					String regexForInput = generateRegex(input);
					System.out.println(regexForInput);
					if(f.checkMatch(regexForInput)){
						String preEvaluate = f.constructEval(input);
						if(preEvaluate.contains(".")){
							test.checkVar(preEvaluate, 2);
							test.cleanStack();
							test.cleanCopyString();
						}
						else{
							test.checkVar(preEvaluate, 1);
							test.cleanStack();
							test.cleanCopyString();
						}
						System.out.println();
						System.out.print("==> ");
						input = in.nextLine().trim();
						while(input.isEmpty()){
							System.out.print("==> ");
							input = in.nextLine().trim();
						}
					}else{
						System.out.println("ERROR! Function for " + input + " has not been defined yet.");
						System.out.println();
						System.out.print("==> ");
						input = in.nextLine().trim();
						while(input.isEmpty()){
							System.out.print("==> ");
							input = in.nextLine().trim();
						}
					}
					
				}
				else{
					System.out.println("No function involved");

					if(input.contains(".")){
						test.checkVar(input, 2);
						test.cleanStack();
						test.cleanCopyString();
					}
					else{
						test.checkVar(input, 1);
						test.cleanStack();
						test.cleanCopyString();
					}
					System.out.println();
					System.out.print("==> ");
					input = in.nextLine().trim();
					while(input.isEmpty()){
						System.out.print("==> ");
						input = in.nextLine().trim();
					}
				}
			}
		}while(!input.equals("quit") && !input.equals("exit"));
		
		System.out.println("Bye~");
	}
	
	/**
	 * generate a regex for a giving string
	 * @param s string to generate regex
	 * @return a regular expression 
	 */
	public static String generateRegex(String s){
		String regex;
		StringBuilder pattern = new StringBuilder();
		String expr = "";
		for(int i = 0; i < s.length(); i ++)
		{
			char ch = s.charAt(i);
			if(Character.isLetter(ch)){
				pattern.append(ch);
			}
			if(ch == '('){
				expr = s.substring(i);
				break;
			}
		}
		for(int i = 0; i < expr.length(); i++){
			char ch = expr.charAt(i);
			if(ch == '('){
				pattern.append("[(]");
				pattern.append("\\s*");
			}
			else if(Character.isLetterOrDigit(ch)){
				while(Character.isLetterOrDigit(expr.charAt(i + 1)) || expr.charAt(i + 1) == '.'){
					i++;
				}
				pattern.append("[a-zA-Z_0-9]");

			}
			else if(ch == ' '){
				pattern.append("\\s*");
				while(expr.charAt(i + 1) == ' '){
					i = i + 1;
				}
			}else if(ch ==')'){
				pattern.append("\\s*");
				pattern.append("[)]");
			}
		}
		regex = pattern.toString();
		return regex;
		
	}
	
	public static boolean isFunctionFormat(String input){
		boolean containsLetter = false;
		boolean containsLeftPar = false;
		boolean containsOperator = false;
		boolean containsRightPar = false;

		
		char ch;
		for(int i = 0; i < input.length(); i++){
			ch = input.charAt(i);
			if(Character.isLetter(ch))
				containsLetter = true;
			else if(ch == '(')
				containsLeftPar = true;
			else if(ch == ')')
				containsRightPar = true;
			else if(isOperator(ch)){
				containsOperator = true;
			}
		}
		
		if(containsLetter && containsLeftPar && containsRightPar && !containsOperator)
			return true;
		return false;
	}
	
	public static boolean isOperator(char c){
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
	}
	
	public static boolean isFunctionSyntax(String input){
		
		boolean containsLetter = false;
		boolean containsLeftPar = false;
		boolean containsRightPar = false;
		boolean containsOperator = false;
		boolean containsEqual = false;
		
		char ch;
		for(int i = 0; i < input.length(); i++){
			ch = input.charAt(i);
			if(Character.isLetter(ch))
				containsLetter = true;
			else if(ch == '(')
				containsLeftPar = true;
			else if(ch == ')')
				containsRightPar = true;
			else if(isOperator(ch)){
				containsOperator = true;
			}else if(ch == '='){
				containsEqual = true;
			}
		}
		
		if(containsLetter && containsLeftPar && containsOperator 
				&& containsRightPar && containsEqual)
			return true;
		return false;
	}
}
