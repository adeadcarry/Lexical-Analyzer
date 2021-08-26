package src;/*Nathaniel Copeland
10/12/2019
CSCI 4200 Programming Languages 
Dr. Salimi
Lexical Analyzer
*/

import java.io.*;

public class LexicalAnalyzer {
	// Variables used to handle the methods and tokens
	public static String charClass;
	public static char[] lexeme = new char[100];
	public static char nextChar;
	public static int lexLen;
	public static String token;
	public static String nextToken;
	static int index = 0;
	static String line;
	// Char categories
	public static final String LETTER = "LETTER";
	public static final String DIGIT = "DIGIT";
	public static final String UNKNOWN = "UNKOWN";
	// Tokens
	public static final String INT_LIT = "INT-LIT";
	public static final String IDENT = "IDENT";
	public static final String ASSIGN_OP = "ASSIGN_OP";
	public static final String ADD_OP = "ADD_OP";
	public static final String SUB_OP = "SUB_OP";
	public static final String MULT_OP = "MULT_OP";
	public static final String DIV_OP = "DIV_OP";
	public static final String LEFT_PAREN = "LEFT_PAREN";
	public static final String RIGHT_PAREN = "RIGHT_PAREN";
	public static final String EOF = "EOF";

	static String lookup(char ch) {
		switch (ch) {
		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;
		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;
		case '+':
			addChar();
			nextToken = ADD_OP;
			break;
		case '-':
			addChar();
			nextToken = SUB_OP;
			break;
		case '*':
			addChar();
			nextToken = MULT_OP;
			break;
		case '/':
			addChar();
			nextToken = DIV_OP;
			break;
		case '=':
			addChar();
			nextToken = ASSIGN_OP;
			break;
		default:
			addChar();
			nextToken = EOF;
			break;
		}
		return nextToken;
	}

	// addChar - a function to add nextChar to lexeme
	static void addChar() {
		if (lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		} else
			System.out.println("Error - lexeme is too long \n");
	}

	/*
	 * getChar - a function to get the next character of input and determine its
	 * character class
	 */
	static void getChar() throws IOException {
		if (checkIfExist()) {
			if (Character.isLetter(nextChar))
				charClass = LETTER;
			else if (Character.isDigit(nextChar))
				charClass = DIGIT;
			else
				charClass = UNKNOWN;
		} else
			charClass = EOF;
	}

	private static boolean checkIfExist() throws java.lang.StringIndexOutOfBoundsException {
		try {// checks if there is a next char
			nextChar = line.charAt(index);
			index++;
			return true;
		} catch (java.lang.StringIndexOutOfBoundsException e) {
			return false;
		}
	}

	static void getNonBlank() throws IOException {// calls the getChar function until a non-white space is returned
		while (Character.isWhitespace(nextChar))
			getChar();
	}

	/* **************************************************** */
	// lex - a simple lexical analyzer for arithmetic
	static int lex() throws IOException {
		lexLen = 0;
		lexeme = new char[100]; // initializes fresh array
		getNonBlank();
		switch (charClass) {// switch states for which parse is being called
		case LETTER:
			addChar();
			getChar();
			while (charClass == LETTER || charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = IDENT;
			break;

		case DIGIT:
			addChar();
			getChar();
			while (charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = INT_LIT;
			break;
		/* Parentheses and operators */
		case UNKNOWN:
			lookup(nextChar);
			getChar();
			break;
		// if the next token is at the end of the file
		case EOF:
			nextToken = EOF;
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			break;
		}
		if (nextToken != EOF) {
			System.out.printf("Next token is: %-15s Next lexeme is ", nextToken);// prints out all the lexemes, but not
																					// EOF
			System.out.println(lexeme);
		}
		return 0;
	} // End of function lex

	public static void main(String[] args) throws IOException {// main method for program
		try {
			PrintStream fileStream = new PrintStream("src/lexOutput.txt");
			System.setOut(fileStream);
			System.out.println("Nathaniel Copeland Student, CSCI4200-DA, Fall 2019, Lexical Analyzer");
			System.out.println("********************************************************************************");
			BufferedReader br = new BufferedReader(new FileReader("src/lexInput.txt"));// opens the input txt file
			while ((line = br.readLine()) != null) {
				index = 0;
				System.out.println(("Input: ") + line);
				getChar();// calls the getChar method to char in the txt file
				do {
					lex();
				} while (nextToken != EOF);// while the char is not the last char the getChar method is continued to be
											// called
				System.out.println("********************************************************************************"); // 80
			}

		}

		catch (IOException e) {
			System.out.println("ERROR - cannot open file");
		}
		System.out.printf("Next token is: %-15s Next lexeme is ", nextToken);
		System.out.println(lexeme);
		System.out.println("Lexical analysis of the program is complete!");
	}

	// switch statement for operators and parentheses

}// end of class
