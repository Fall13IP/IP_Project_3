package com.dv;

public class Node {

	//private DVTable dvtable;
	private int [] adjacencyMatrix;
	private float [] [] costMatrix;
	private boolean costChanged = false;
	private int numberOfCostChanges = 0;
	public int getNumberOfCostChanges() {
		return numberOfCostChanges;
	}

	public void setNumberOfCostChanges(int numberOfCostChanges) {
		this.numberOfCostChanges = numberOfCostChanges;
	}
	
	public void incrementCostChanges(){
		this.numberOfCostChanges++;
	}
	public Node(int noOfNodes){
		
		adjacencyMatrix = new int[noOfNodes];
		costMatrix = new float[noOfNodes] [noOfNodes];
		
		for(int i=0; i < noOfNodes; i++){
			for(int j=0; j < noOfNodes; j++){
				costMatrix[i][j] = Float.MAX_VALUE;
			}
		}
	}
	
	public void addEntryToAdjacencyMatrix(int destinationIndex, int connected){
		this.adjacencyMatrix[destinationIndex] = connected;		
	}
	
	public void addEntryToCostMatrix(int sourceIndex, int destinationIndex, float cost){
		this.costMatrix[sourceIndex] [destinationIndex] = cost;
		//this.costMatrix[destination -1] [source -1] = cost;
	}
	
	public boolean isNeighbour(int destinationIndex){
		if(this.adjacencyMatrix[destinationIndex] == 1)
			return true;
		return false;
	}
	public float getCost(int source, int destination){
		return costMatrix[source] [destination];
	}

	public int[] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(int[] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public float[][] getCostMatrix() {
		return costMatrix;
	}

	public void setCostMatrix(float[][] costMatrix) {
		this.costMatrix = costMatrix;
	}

	public boolean isCostChanged() {
		return costChanged;
	}

	public void setCostChanged(boolean costChanged) {
		this.costChanged = costChanged;
	}
}
