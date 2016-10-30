import java.util.*;
import java.io.*;
import java.util.Scanner;

public class RunDpWithoutMemoization {
	static int d; // delay
	static int c; // cost
	static int totalCost; // Total Cost
	static String gate = "g0";
	static String res = null;
	static String expr[][];
	static ArrayList<Integer> myFileLines = new ArrayList<Integer>();
	static int length;

	// fetching values from file and assigning values to matrix and arraylist.
	public static void fetchAndAssignValues() {
		String line = null;
		BufferedReader br = null;
		System.out.println("TXT File is in location :" + new java.io.File("").getAbsolutePath());

		try {
			//InputOrder.txt - For 6 values.
			//InputOrderForFifteenValues.txt - For 15 values.
			br = new BufferedReader(new FileReader("InputOrderForFifteenValues.txt"));
			length = Integer.parseInt(br.readLine());
			expr = new String[length][length];
			System.out.println("Elements entered are: ");
			while ((line = br.readLine()) != null) {
				myFileLines.add(Integer.parseInt(line));
				System.out.println(line);
			}
			br.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Get minimum time required by the given order of gates.
	public static int getMinTime(ArrayList<Integer> p, int i, int j) {
		int k;
		int min = Integer.MAX_VALUE;
		int count;
		String tempRes1, tempRes2, finalRes = null;
		if (i == j) {
			res = Integer.toString(p.get(i));
			return p.get(i);
		}
		for (k = i; k < j; k++) {
			int left = getMinTime(p, i, k);
			expr[i][k] = res;
			tempRes1 = res;
			int right = getMinTime(p, k + 1, j);
			expr[k + 1][j] = res;
			tempRes2 = res;
			count = Math.max(left, right) + d;
			if (count < min) {
				min = count;
				finalRes = tempRes1;
				finalRes += tempRes2;
				finalRes += gate;
				totalCost += c;
			}
		}
		res = finalRes;
		return min;
	}

	// main method
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		fetchAndAssignValues();
		if(length != myFileLines.size()){
			System.out.println("Length and the number of items do not match");
			System.out.println("Please enter valid length and number of items");
			System.exit(0);
		}
		System.out.println("Enter the delay value for each gate : ");
		d = Integer.parseInt(in.next());
		
		
		long startTime = System.currentTimeMillis();
		
		//Our main method which gets the minimum time delay
		int min = getMinTime(myFileLines, 0, length - 1);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("The total number of time taken to run the program without memoization is :" + totalTime + " milli seconds");
		
		
		System.out.println("The minimum time required by the given order of gates = " + min);
		System.out.println("The postfix expression for the given order of gates = " + res);
		in.close();
	}
}
