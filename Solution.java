import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Solution {
	static int d;
	static SolutionMatrix sol[][][];
	static String res = null;
	static Gates[] gateArr;
	static ArrayList<Integer> myFileLines = new ArrayList<Integer>();
	static int length;
	static int numOfGates;
	static int thresholdDelayValue;
	static int maxValue = 0;
	static int minValue = Integer.MAX_VALUE;

	// fetching values from file and assigning values to matrix and arrayList.
	public static void fetchAndAssignValues() {
		String line = null;
		BufferedReader br = null, gatesRdr = null;
		System.out.println("TXT File is in location :" + new java.io.File("").getAbsolutePath());

		try {
			gatesRdr = new BufferedReader(new FileReader("GateInput.txt"));
			
			if((line = gatesRdr.readLine()) != null)
			{
				numOfGates = Integer.parseInt(line);
			}
			
			int i = 0;
			gateArr = new Gates[numOfGates];
			
			while((line = gatesRdr.readLine()) != null)
			{
				gateArr[i] = new Gates();
				gateArr[i].gateName = line;
				if((line = gatesRdr.readLine()) == null)
					break;
				gateArr[i].cost = Integer.parseInt(line);
				
				if((line = gatesRdr.readLine()) == null)
					break;
				gateArr[i].gateDelay = Integer.parseInt(line);
				
				i++;
			}
			
			gatesRdr.close();
			br = new BufferedReader(new FileReader("InputOrder.txt"));
			length = Integer.parseInt(br.readLine());
			sol = new SolutionMatrix[length][length][numOfGates];
			
			System.out.println("Input Pin Arrival Times: ");
			
			while ((line = br.readLine()) != null) {
				myFileLines.add(Integer.parseInt(line));
				System.out.println(line);
			}
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Get minimum time required by the given order of gates.
	public static void getMinTime(ArrayList<Integer> p, int i, int j, int gateNum) {
		int k, x;
		int count;
		String tempRes1, tempRes2;
		
		if (i == j) {
			sol[i][j][gateNum].delay = myFileLines.get(i);
			sol[i][j][gateNum].expr = Integer.toString(myFileLines.get(i));
		}
		
		for (k = i; k < j; k++) {
			for(x = 0; x < numOfGates; x++)
			{	
				if (sol[i][k][x].delay == -1) {
					getMinTime(p, i, k, x);
					tempRes1 = sol[i][k][x].expr;
				} 
				else {
					tempRes1 = sol[i][k][x].expr;
				}
				
				if (sol[k + 1][j][x].delay == -1) {
					getMinTime(p, k + 1, j, x);
					tempRes2 = sol[k + 1][j][x].expr;
				} 
				else {
					tempRes2 = sol[k + 1][j][x].expr;
				}
				
				count = Math.max(sol[i][k][x].delay, sol[k + 1][j][x].delay) + gateArr[x].gateDelay;
				
				if (count <= thresholdDelayValue) {
					if(sol[i][j][x].delay == -1){
						sol[i][j][x].expr = tempRes1;
						sol[i][j][x].expr += tempRes2;
						sol[i][j][x].expr += gateArr[x].gateName;
						
						sol[i][j][x].cost = sol[i][k][x].cost + sol[k+1][j][x].cost + gateArr[x].cost;
						sol[i][j][x].delay = count;
					}
					else
					{
						int tempCost = sol[i][k][x].cost + sol[k+1][j][x].cost + gateArr[x].cost;
						
						if(sol[i][j][x].cost > tempCost || (sol[i][j][x].cost == tempCost && sol[i][j][x].delay > count))
						{
							sol[i][j][x].expr = tempRes1;
							sol[i][j][x].expr += tempRes2;
							sol[i][j][x].expr += gateArr[x].gateName;
							
							sol[i][j][x].cost = sol[i][k][x].cost + sol[k+1][j][x].cost + gateArr[x].cost;
							sol[i][j][x].delay = count;
						}
					}
				}
			}
		}
	}

	// main method
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int i = 0;
		fetchAndAssignValues();
		SolutionMatrix temp[] = new SolutionMatrix[numOfGates];
		SolutionMatrix finalSol =  new SolutionMatrix();
		
		if(length != myFileLines.size()){
			System.out.println("Length and the number of items do not match");
			System.out.println("Please enter valid length and number of items");
			System.exit(0);
		}
		
		// to set the default values
		for (i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				for(int k = 0; k < numOfGates; k++){
					sol[i][j][k] = new SolutionMatrix();
				}
			}
		}
		for(i = 0; i < length; i++)
			if(maxValue < myFileLines.get(i))
					maxValue = myFileLines.get(i);
		
		for(i = 0; i < numOfGates; i++)
			if(minValue > gateArr[i].gateDelay)
				minValue = gateArr[i].gateDelay;
		
		System.out.println("Following gates are considered to build optimal plan plan");
		
		for(i = 0; i < numOfGates; i++)
			System.out.println("Gate " + (i + 1) + " with a delay of " 
						+ gateArr[i].gateDelay + " and a cost of " + gateArr[i].cost);
		
		System.out.println("Enter the tolerance value of the output : ");
		thresholdDelayValue = Integer.parseInt(in.next());
		in.close();
		try{

		for(i = 0; i < numOfGates; i++){
			getMinTime(myFileLines, 0, length - 1, i);
		}
		
		//retrieves the final output from the list of potential outputs
		finalSol = getFinalSol(temp);
		
			
			if(finalSol.delay != -1)
			{			
				System.out.println("The minimum time required by the given order of inputs and gates = "
						+ finalSol.delay);
				System.out.println("The minimum cost required for building the solution = " + finalSol.cost);
				System.out.println("The postfix expression for the given order of inputs with its respective gates = "
						+ finalSol.expr);
			}
			else
			{
				System.out.println("There is no possible solution for the given tolerance value");
			}
		}
		catch(Exception e){
			System.out.println("No Output");
			System.exit(0);
		}
	}

	public static SolutionMatrix getFinalSol(SolutionMatrix[] temp) {
		int min = Integer.MAX_VALUE, i;
		int solPos = -1;
		
		for(i = 0; i< numOfGates; i++)
		{
			if(sol[0][length-1][i].delay != -1 && sol[0][length-1][i].delay >= maxValue)
			{
				if(sol[0][length-1][i].cost <= min)
				{
					solPos = i;
					min = sol[0][length-1][i].cost;
				}
			}
		}
		
		if(solPos == -1)
		{
			return new SolutionMatrix(); //as everything returns empty
		}
		
		return sol[0][length-1][solPos];
	}
}