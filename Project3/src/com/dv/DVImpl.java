package com.dv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DVImpl {
	
	private int initialNode;
	private String fileName;
	private int node1;
	private int node2;
	//private DVTable [] dvTables;
	private Node [] nodes;
	private int [][] adjacencyMatrix;
	private float [][] costMatrix;
	private boolean costUpdated = false;
	private int noOfNodes;
	public DVImpl(int initialNode, String fileName, int node1, int node2){
		
		this.initialNode = initialNode;
		this.fileName = fileName;
		this.node1 = node1;
		this.node2 = node2;
		
		readFile();
		//dvTables[initialNode - 1].setTableUpdated(true);
	}
	
	
	private boolean isNeighbour(int sourceIndex, int destinationIndex){
		
		boolean isNeighbour = false;
		if(adjacencyMatrix[sourceIndex] [destinationIndex] == 1)
			isNeighbour = true;
		return isNeighbour;
	}
	
	private float getCost(int sourceIndex, int destinationIndex){
		return costMatrix[sourceIndex] [destinationIndex];
	}
	private void updateCostMatrix(int source , int destination, float cost){
		costMatrix[source][destination] = cost;
		costUpdated = true;
	}
	private boolean readFile(){
		boolean success = false;
		File file = new File(this.fileName);
		String nextLineReadInput;
		String splitStrings[];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int noOfNodes = Integer.parseInt(reader.readLine());
			this.noOfNodes = noOfNodes;
			nodes = new Node[noOfNodes];
			for(int i=0; i < noOfNodes; i++){
				nodes[i] = new Node(noOfNodes);
			}
			
		
			while((nextLineReadInput=reader.readLine())!=null){
				
				splitStrings = nextLineReadInput.split("\\s"); 
				int source = Integer.parseInt(splitStrings[0]);
				int destination = Integer.parseInt(splitStrings[1]);
				float cost = Float.parseFloat(splitStrings[2]);
				
				nodes[source -1].addEntryToAdjacencyMatrix(destination - 1, 1);
				nodes[destination -1].addEntryToAdjacencyMatrix(source -1, 1);
				
				
				//adjacencyMatrix[source -1] [destination -1] = 1;
				//adjacencyMatrix [destination -1] [source -1] = 1;
				
				nodes[source -1].addEntryToCostMatrix(source -1, destination -1, cost);
				nodes[destination -1].addEntryToCostMatrix(destination -1 , source -1, cost);
				
				nodes[source -1].addEntryToCostMatrix(source - 1, source - 1, 0);
				nodes[destination -1].addEntryToCostMatrix(destination - 1, destination - 1, 0);
				
				//updateCostMatrix(source-1, destination-1, cost);
				//updateCostMatrix(destination-1, source-1, cost);
				//updateCostMatrix(source-1, source-1, 0);
				//updateCostMatrix(destination-1, destination-1, 0);
				
			}
			/*for(int i=0; i < noOfNodes; i ++){
				System.out.println("============");
				printRoutingTable(i);
				System.out.println("============");
			}*/
			reader.close();
			success = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
/*	private int hasUpdates(){
		
		int hasUpdates = -1;
		for(int i=0; i < this.dvTables.length; i++){
			if(dvTables[i].isTableUpdated() == true){
				hasUpdates = i;
				break;
			}
		}
		return hasUpdates;
	}*/
	public void simulate()
	{
		int index;
		float minCost = Float.MAX_VALUE;
		//while(costUpdated == true){
			costUpdated = false;
			for(int i = 0; i < adjacencyMatrix.length; i++){
				for(int j = 0; j < adjacencyMatrix.length; j++){
					if( i != j)
					{
						for(int k = 0; k < adjacencyMatrix.length; k++){
							if(isNeighbour(i, k)){
								float newCost = getCost(i, k) + getCost(k, j);
								if(newCost < minCost){
									minCost = newCost;
								}
							}
						}
						if(minCost != Float.MAX_VALUE)
							updateCostMatrix(i, j, minCost);
						minCost = Float.MAX_VALUE;
					}
				}
			}
		//}
		printCostMatrix();
		
	}
	
	private int getNextNodeIndex(){
		int nodeNumber = -1;
		for(int i = 0 ; i < noOfNodes; i ++){
			if(nodes[i].isCostChanged() == true){
				nodeNumber = i;
				//nodes[i].setCostChanged(false);
				break;
			}
		}
		return nodeNumber;
	}
	
	private void copyCostMatrices(int fromIndex, int toIndex){
		
		float [] [] fromCostMatrix = nodes[fromIndex].getCostMatrix();
		float [] [] toCostMatrix = nodes[toIndex].getCostMatrix();
		for(int i = 0; i < noOfNodes; i++){
			//for(int j = 0; j < noOfNodes; j ++){
				if(toCostMatrix[fromIndex][i] > fromCostMatrix[fromIndex][i]){
					toCostMatrix[fromIndex][i] = fromCostMatrix[fromIndex][i];
				}
			//}
		}
		
		nodes[toIndex].setCostMatrix(toCostMatrix);
	}
	
	public void simulate_new(){
		
		int currentNodeIndex = initialNode - 1;
		boolean minCostChanged = false;
		float minCost = Float.MAX_VALUE;
		
		for(int i = 0; i < noOfNodes; i ++){
			if (nodes[currentNodeIndex].isNeighbour(i)){
				nodes[i].setCostChanged(true);
				//copyCostMatrices(currentNodeIndex, i);
			}
		}
		
		while(currentNodeIndex != -1){
			for(int i = 0; i < noOfNodes; i++  ){
				if( currentNodeIndex != i){
					for(int j =0; j < noOfNodes; j ++){
						
							if(nodes[currentNodeIndex].isNeighbour(j)){
								float newCost = nodes[currentNodeIndex].getCost(currentNodeIndex, j) + nodes[j].getCost(j, i);
								if(newCost < minCost){
									minCost = newCost;
								}
							}
						
					}
					if(minCost != Float.MAX_VALUE){
						if(minCost < nodes[currentNodeIndex].getCost(currentNodeIndex, i)){
							nodes[currentNodeIndex ].addEntryToCostMatrix(currentNodeIndex, i, minCost);
							minCostChanged = true;							
						}
						minCost = Float.MAX_VALUE;
					}
				}
			}
			if (minCostChanged == true){
				for(int i = 0; i < noOfNodes; i ++){
					if (nodes[currentNodeIndex].isNeighbour(i)){
						//System.out.println("Setting cost changed for " + i + " by " + currentNodeIndex);
						nodes[i].setCostChanged(true);
						nodes[i].incrementCostChanges();
						nodes[currentNodeIndex].setCostChanged(true);
						nodes[currentNodeIndex].incrementCostChanges();
						copyCostMatrices(currentNodeIndex, i);
					}
				}
			}else{
				nodes[currentNodeIndex].setCostChanged(false);
			}
			
			currentNodeIndex = getNextNodeIndex();
			minCostChanged = false;
		}
		
	}
	
	public void printRoutingTable(int nodeIndex){
		float [][] costMatrix = nodes[nodeIndex].getCostMatrix();
		System.out.println("--------------------------------------------------------------------------------------------------------");
		System.out.println("Routing Table for Node " + (nodeIndex +1));
		for(int i=0; i < noOfNodes; i ++){			
			System.out.print("Node " + (i+1) + " :");
			for(int j = 0; j < noOfNodes; j ++){
				if(Float.MAX_VALUE != costMatrix[i][j]){
					System.out.print(costMatrix[i][j] + "\t");
				}
			
			}
			System.out.println();
			
		}
		System.out.println("--------------------------------------------------------------------------------------------------------");
	}
	
	public void printAllRoutingTables(){
		for(int i = 0; i < this.noOfNodes; i++){
			printRoutingTable(i);
		}
	}
	private void printCostMatrix(){
		for(int i=0; i < adjacencyMatrix.length; i ++){
			for(int j=0;j<adjacencyMatrix.length;j++){
				System.out.print(costMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printCostOfLeastPath(int nodeIndex1, int nodeIndex2){
		
		float [][]costMatrix = nodes[nodeIndex1].getCostMatrix();
		float leastCost = costMatrix[nodeIndex1][nodeIndex2];
		System.out.println("Least cost from " + (nodeIndex1 +1) +" to " + (nodeIndex2+1) + ": " +leastCost);
		
	}

public int getInitialNode() {
		return initialNode;
	}



	public void setInitialNode(int initialNode) {
		this.initialNode = initialNode;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public int getNode1() {
		return node1;
	}



	public void setNode1(int node1) {
		this.node1 = node1;
	}



	public int getNode2() {
		return node2;
	}



	public void setNode2(int node2) {
		this.node2 = node2;
	}


	public int maxIterations(){
		int maxValue = Integer.MIN_VALUE;
		for(int i=0; i <noOfNodes; i++){
			if(nodes[i].getNumberOfCostChanges() > maxValue){
				maxValue = nodes[i].getNumberOfCostChanges();
			}
		}
		return maxValue;
	}

public static void main(String args[]){
	
	if (args.length == 4){
		int initialNode = Integer.parseInt(args[0]);
		String fileName = args[1];
		int node1 = Integer.parseInt(args[2]);
		int node2 = Integer.parseInt(args[3]);
		
		DVImpl dvImpl = new DVImpl(initialNode, fileName, node1, node2);
		dvImpl.simulate_new();
		//dvImpl.printAllRoutingTables();
		dvImpl.printRoutingTable(node1-1);
		dvImpl.printRoutingTable(node2-1);
		dvImpl.printCostOfLeastPath(node1 - 1, node2 - 1);
		
		//System.out.println("Max iterations: " +dvImpl.maxIterations());
		
	}else{
		System.out.println("Insufficient parameters");
	}
}

}