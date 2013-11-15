package com.example.routingAlgo;
import com.example.graphParser.*;
/*This file has some basic functions for Dijkstras Algo,
 //input: file and the node for which to compute the matrix.
  //output: Cost MAtrix of the computed graph.
   Basic Functions:
   1. ComputeFilePath: used to process the initialized cost matrix
   2. UpdateCOstMAtrix: computes the cost_Matrix and updates it with recent node traversed values
   3. minNodeSelection: selects and returns the min node in cost Matrix.
   4. printCostMatrix: prints the cost matrix, for showing result
   Functions of graphReader Class:
   1. readGraph: takes the file as input and reads it, prepares a adjacency matrix representation of graph provided.
   2. graphSize: used to take out the number of nodes of graph, uses a very naive call to file every time not suggested.
   3. printGraphMatrix: prints the Graph adjacecy MAtrix computed from the file.
   
   ISSUE: there is a issue with representation of graph, in graph edges start from 1 ,2 ... 5 and so on; but matrix starts with 0, so take into consideration that.
   2. Also, take into consideration the files which he have in his site, there are 3 files it seems. For simplicity and testing I have used a very sparse graph.
   FLOW: Call Dijkstras ->calls readGraph - > intializes cost Matrix -> call computePathFile.
   computeFilePath -> calls minNode SElection ->updateCosMatrix.
   */
public class DikstrasAlgo {
	static int nodesVisited[];
	static int noOfNodes;
	static float adjacency_matrix[][];
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Dijkstras("smallnet.txt", 0);//send to this file the basic edge number for which you have to calculate the thing from the main.
		//implement the algo of Dijkstras.
		
	}
	
	public static float[][] Dijkstras(String filename,int nodeNumber){
		String fileName = "smallnet.txt";
		noOfNodes = graphReader.graphSize(fileName);
		nodesVisited = new int[noOfNodes];
		int iteratorX=0;
		adjacency_matrix = new float[noOfNodes][noOfNodes];
		adjacency_matrix = graphReader.readGraph(fileName);
		nodesVisited[0] = nodeNumber;
		float cost_Matrix[][] = new float[noOfNodes][3];
		
		//initializing the matrix given.
		for(iteratorX=0;iteratorX<noOfNodes;iteratorX++){
			if(adjacency_matrix[nodeNumber][iteratorX]==0.0){
				cost_Matrix[iteratorX][0]=100;
				cost_Matrix[iteratorX][1]= nodeNumber;
			}else{
				cost_Matrix[iteratorX][0]=adjacency_matrix[nodeNumber][iteratorX];
				cost_Matrix[iteratorX][1]= nodeNumber;
			}
			cost_Matrix[iteratorX][2]=0;
		}
		cost_Matrix[nodeNumber][2]=1;
		//it is correct till here, need to implement the stuff back here.
		cost_Matrix = ComputePathFile(cost_Matrix, noOfNodes);
		//return the cost Matrix for each vertex.
		graphReader.printGraphMatrix();
		return cost_Matrix;
				
	}
	
	public static float[][] ComputePathFile(float[][] cost_Matrix,int noofNodes){
		//take this node, and input this in the node visited matrix.and recompute the cost matrix. add the cost of selected node and then change the matrix value if it is there.
		int nodeToInputNext[] = new int[2];
		
		
		for(int iteratorX = 0; iteratorX<(noofNodes-1);iteratorX++){
			
			nodeToInputNext = minNodeSelection(cost_Matrix, noofNodes);
			cost_Matrix[nodeToInputNext[1]][2] = 1;
			nodesVisited[iteratorX+1] = nodeToInputNext[1];
			//now once included in the nodes visited section, we recompute the cost matrix.
			cost_Matrix = updateCostMatrix(cost_Matrix,nodeToInputNext[1],nodeToInputNext[0]);
			//return the comouted cost matrix.
		
		}
		System.out.print("Node Visited");
		for(int iteratorX=0;iteratorX<nodesVisited.length;iteratorX++){
			System.out.print(nodesVisited[iteratorX]);
		}
		System.out.println("Final Cost Mat:");
		PrintCostMatrix(cost_Matrix);
		return cost_Matrix;
		
	}
	public static float[][] updateCostMatrix(float[][] cost_Matrix, int nodeToInput, int costOfNode){
		//take costMatrix as the input and compute the cost matrix and return the new one.
		for(int iteratorX=0;iteratorX<noOfNodes;iteratorX++){
			if(cost_Matrix[iteratorX][2]!=1){
				if(adjacency_matrix[nodeToInput][iteratorX]!=0.0){
				if(((adjacency_matrix[nodeToInput][iteratorX])+costOfNode-1)<cost_Matrix[iteratorX][0]){
					cost_Matrix[iteratorX][0]=(adjacency_matrix[nodeToInput][iteratorX]+costOfNode);
					cost_Matrix[iteratorX][1] = nodeToInput;
				//changes the cost matrix wrt to the adjacency matrix.			
					}
				}
			}
		}
		
		return cost_Matrix;
		
	}
	public static int[] minNodeSelection(float[][] cost_Matrix,int noOfNodes){//selects the minimum node in the cost matrix, checking if it is already a node, if it is does not selects it.
		int minimumCost = 100;
		int iterator;
		int returnMinNode[] = new int[2];
		for(iterator=0;iterator<noOfNodes;iterator++){
			
			if(minimumCost>cost_Matrix[iterator][0]){
				
				if(cost_Matrix[iterator][2]==0){
				returnMinNode[0] = (int) cost_Matrix[iterator][0];
				returnMinNode[1] = iterator;	
			}
		}
		}
		//System.out.println(" Final Cost is:"+minimumCost+"iterator"+returnMinNode);
		return returnMinNode;
	}
	

	public static void PrintCostMatrix(float[][] cost_Matrix){
		int iteratorX=0,iteratorY=0;
		int noOfNodes = cost_Matrix.length;
		System.out.println("Cost Matrix is:");
		for(iteratorX=0;iteratorX<noOfNodes;iteratorX++)//initializes the matrix with 0 distance.
		{	System.out.println("\n");
			for(iteratorY=0;iteratorY<3;iteratorY++){
				System.out.print(cost_Matrix[iteratorX][iteratorY]+"\t");
			}
		}
		
	}

}
