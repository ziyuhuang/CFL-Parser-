import java.util.*;
public class Parse{
	
//	store the output of a expression
	private int output1;	
	private double output2;
	
	//flag to check if an int expression has turned into double expression
	//because of the variable issue
//	private boolean flag = true;  //false means not turn and true means turned
	
	Stack<Character> stack = new Stack<Character>(); //stack to perform infix to postfix operations
	StringBuilder copy = new StringBuilder(); //build the postfix string
	
	
	//HashMap to store each variable's value
	HashMap<String, Number> map = new HashMap<String, Number>(); 
	
	/**
	 * Public constructor 
	 * @param input string input from user to parse
	 */
	public Parse(){
	}
	
	/**
	 * check if the input is an assign expression or not (check for character '==')
	 * if true store the variable into the HashMap
	 * @param ainput input to check
	 * @param type check if the expression is double or int type
	 * @return true if the input contains '=' false if not
	 */
	public void checkVar(String input, int type){
		
		StringBuilder str = new StringBuilder();
		int len = input.length();
		String s;
		for(int i = 0; i < len; i++){
			char ch = input.charAt(i);
			if(ch != '='){
				str.append(ch);
			}
			else{
				String expr = input.substring(i+1);
				s = str.toString();
				s = s.replaceAll("\\s+", "");
				if(type == 1){
					evalIntExpr(expr);
					map.put(s, output1);
					return;
				}
				else{
					evalDoubleExpr(expr);
					map.put(s, output2);
					return;
				}
			}
		}
		
		s = str.toString();
		if(type == 1){
			evalIntExpr(s);
		}
		else{
			evalDoubleExpr(s);
		}
	}
	
	/**
	 * Turn the input from infix to post fix
	 * using a stack
	 * e.g 
	 * input: a * (b + c)
	 * output: a b c + *
	 * @param input to be parsed
	 */
	public void parseInput(String ainput){
		
		int inputLength = ainput.length();
		
		for(int i = 0; i < inputLength; i++){
			char ch = ainput.charAt(i);
			switch(ch){
				case '(':
					stack.push(ch);
					break;
				case '+':
				case '-':
					infixToPostfix(ch, 1);
					break;
				case '*':
				case '/':
				case '^':
					infixToPostfix(ch, 2);
					break;
				case ')':
					getLeftParen();
					break;
				default:
					copy.append(ch);
					break;
			}
		}
		
		while(!stack.isEmpty()){
			copy.append(stack.pop() + " ");
		}		
	}

	/**
	 * Check if character is a operator
	 * @param c character to check
	 * @return true if it is a operator
	 */
	public boolean isOperator(char c){
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^'
	            || c == '(' || c == ')';
	}
	
	/**
	 * check if a string is a operator
	 * use for postfix
	 * @param c
	 * @return true if it is a operator
	 */
	public boolean isOperator(String c){
		return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^");
	}

	/**
	 * append the value from the stack to a string
	 * until the '(' is met
	 */
	public void getLeftParen(){
		while(!stack.isEmpty()){
			char ch = stack.pop();
			if(ch == '(')
				break;
			else
				copy.append(ch);
		}
	}
	
	/**
	 * method to help convert the infix to postfix
	 * mainly take care of the priority issues
	 * @param op
	 * @param priority
	 */
	public void infixToPostfix(char op, int priority){
		
		//if stack is empty, push the operator into stack
		if(stack.isEmpty()){
			stack.push(op);
			return;
		}
	
		//get the top element of the stack
		char top = stack.peek();
		int prior;
		if(top == '+' || top == '-'){
			prior = 1;
		}else{
			prior = 2;
		}
		if(prior < priority){
			stack.push(op);
		}
		else{
			if(top == '(')
			{
				stack.push(op);
			}
			else{
				top = stack.pop();
				stack.push(op);
				copy.append(top);
			}
		}		
	}	
	
	/**
	 * Check if a string is numeric
	 * @param s string to check
	 * @return true if the string is numeric
	 */
	public boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	
	/**
	 * Formalize the user input
	 * each operand and operator should 
	 * separate with a space
	 * e.g
	 * input: a+b*      c
	 * output: a + b * c
	 * @param input to process
	 * @return a formatted string
	 */
	public String formalize(String ainput){
		StringBuilder str = new StringBuilder();
		
		int len = ainput.length();
		for(int i = 0; i < len; i++){
			char temp = ainput.charAt(i);
			if(!isOperator(temp)){
				str.append(temp);
			}
			else{
				str.append(" " + temp + " ");
			}
		}
		str.append(" ");
		
		ainput = str.toString();
		return ainput;
	}
	
	/**
	 * print string
	 */
	public void print(String s){
		System.out.println(s);
	}
	
	/**
	 * get rid of the spaces and store the words
	 * into a string array
	 * @return the list after splitting
	 */
	public String[] getStringNode(){
		String[] list = copy.toString().split("\\s+");
		
		return list;
	}
	
	/**
	 * method to evaluate the expression that only all integers in numerical 
	 * characters. It may pass to another function if any variable is found to be 
	 * a double type.
	 * output will be stored in instance variable
	 * if the output is an int, it will be stored in output1
	 * if the output is a double, it will be stored in output2
	 * @param ainput input to be analyzed
	 */
	public void evalIntExpr(String ainput){
		
		ainput = formalize(ainput);
		parseInput(ainput);
		String[] list = getStringNode();
		Stack<String> temp = new Stack<String>();
		for(String x: list){
			
			if(!isOperator(x)){
				temp.push(x);
			}else{
				String second = temp.pop();
				String first = temp.pop();
						
				int fir, sec;
				if(isNumeric(first)){
					fir = Integer.parseInt(first);
				}
				else{	
					if(!map.containsKey(first)){
						System.out.println("Error, " + first + " is not a local variable, please make sure the input is correct.");
						return;
					}else{
						if(map.get(first) instanceof Double){
							evalDoubleExpr(ainput);
							return;
						}
						fir = (Integer) map.get(first);
					}
				}
				if(isNumeric(second)){
					sec = Integer.parseInt(second);
				}
				else{
					if(!map.containsKey(second)){
						System.out.println("Error, " + second + " is not a local variable, please make sure the input is correct.");
						return;
					}else{
						if(map.get(second) instanceof Double){
							evalDoubleExpr(ainput);
							return;
						}
						sec = (Integer) map.get(second);
					}
				}
				
				if(x.equals("+")) temp.push(String.valueOf(fir + sec));
				else if(x.equals("-")) temp.push(String.valueOf(fir - sec));
				else if(x.equals("*")) temp.push(String.valueOf(fir * sec));
				else if(x.equals("/")) temp.push(String.valueOf(fir / sec));
				else if(x.equals("^")) temp.push(String.valueOf((int) Math.pow(fir, sec)));
			}
		}
		//get the result from the stack
		String s = temp.pop();
		if(!map.containsKey(s)){
			{
				if(isNumeric(s)){
					output1 = Integer.valueOf(s);
					System.out.println(output1);
				}else{
					System.out.println("Error, " + s + " is not a local variable, please make sure the input is correct.");
					return;
				}
			}
		}
		else{
			if(map.get(s) instanceof Integer){
				output1 = (Integer) map.get(s);
				System.out.println(output1);
			}
			else{
				output2 = (Double) map.get(s);
				System.out.println(output2);
			}
		}
		
	}
	
	/**
	 * evaluate the expression that contain the double type
	 * @param ainput
	 */
	public void evalDoubleExpr(String ainput){
		
		ainput = formalize(ainput);
		parseInput(ainput);
		String[] list = getStringNode();
		
		Stack<String> temp = new Stack<String>();
		for(String x: list){
			if(!isOperator(x)){
				temp.push(x);
			}else{
				String second = temp.pop();
				String first = temp.pop();
						
				double fir, sec;
				if(isNumeric(first)){
					fir = Double.parseDouble(first);
				}
				else{	
					if(!map.containsKey(first)){
						System.out.println("Error, " + first + " is not a local variable, please make sure the input is correct.");
						return;
					}else{
						if(map.get(first) instanceof Integer){
							String s = String.valueOf(map.get(first));
							fir = Double.parseDouble(s);
						}
						else{
							fir = (Double) map.get(first);
						}
					}
				}
				if(isNumeric(second)){
					sec = Double.parseDouble(second);
				}
				else{
					if(!map.containsKey(second)){
						System.out.println("Error, " + second + " is not a local variable, please make sure the input is correct.");
						return;
					}else{
						if(map.get(second) instanceof Integer){
							String s = String.valueOf(map.get(second));
							sec = Double.parseDouble(s);
							return;
						}else{
							sec = (Double)map.get(second);
						}
					}
						
				}
				
				if(x.equals("+")) temp.push(String.valueOf((int)(fir + sec)));
				else if(x.equals("-")) temp.push(String.valueOf(fir - sec));
				else if(x.equals("*")) temp.push(String.valueOf(fir * sec));
				else if(x.equals("/")) temp.push(String.valueOf(fir / sec));
				else if(x.equals("^")) temp.push(String.valueOf((int) Math.pow(fir, sec)));
			}
		}
		//get the result from the stack
		String s = temp.pop();
		if(!map.containsKey(s))
			output2 = Double.valueOf(s);
		else{
			output2 = (Double) map.get(s);
		}
		System.out.println(output2);
	}
	
	
	/**
	 * get the output
	 * @return output
	 */
	public int getOutput(){
		return output1;
	}
	
	/**
	 * Safety method to clean the stack
	 */
	public void cleanStack(){
		while(!stack.isEmpty()){
			stack.pop();
		}
	}
	
	/**
	 * Safety method to empty the string builder 
	 * after each evaluation
	 */
	public void cleanCopyString(){
		copy.setLength(0);
	}
}
