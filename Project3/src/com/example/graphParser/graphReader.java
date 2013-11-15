package com.example.graphParser;
import java.awt.PrintGraphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.w3c.dom.traversal.NodeFilter;


public class graphReader {

	
	//returns the adjacency matrix by reading the input file argument.
	static float adjacency_Matrix[][];
	
	public static float[][] readGraph(String fileName){
		File file = new File(fileName);
		try {
			int iteratorX=0, iteratorY=0;
			int noOfNodes;
			String nextLineReadInput=null;
			String breakInputLineinParts[]=new String[3];
			FileReader fr = new FileReader(file);//reads the input file and and puts it in reader.
			
			BufferedReader reader = new BufferedReader(new FileReader(file));//has a buffered reader which takes file line by line.
			if(reader.ready()){
				noOfNodes = Integer.parseInt(reader.readLine()); 
				adjacency_Matrix = new float[noOfNodes][noOfNodes];
				for(iteratorX=0;iteratorX<noOfNodes;iteratorX++)//initializes the matrix with 0 distance.
				{
					for(iteratorY=0;iteratorY<noOfNodes;iteratorY++){
						adjacency_Matrix[iteratorX][iteratorY]=0;
					}
				}
				//now divide each line and input it in matrix
				while((nextLineReadInput=reader.readLine())!=null){//initiates the matrix with values as it reads the file.
					breakInputLineinParts = nextLineReadInput.split("\\s"); 
					adjacency_Matrix[Integer.parseInt(breakInputLineinParts[0])-1][Integer.parseInt(breakInputLineinParts[1])-1] = Float.parseFloat(breakInputLineinParts[2]);
					adjacency_Matrix[Integer.parseInt(breakInputLineinParts[1])-1][Integer.parseInt(breakInputLineinParts[0])-1] = Float.parseFloat(breakInputLineinParts[2]);
				}
				
						
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return adjacency_Matrix;
		}
	public static void printGraphMatrix(){
		int iteratorX=0,iteratorY=0;
		int noOfNodes = adjacency_Matrix.length;
		for(iteratorX=0;iteratorX<noOfNodes;iteratorX++)//initializes the matrix with 0 distance.
		{	System.out.println("\n");
			for(iteratorY=0;iteratorY<noOfNodes;iteratorY++){
				System.out.print(adjacency_Matrix[iteratorX][iteratorY]+"\t");
			}
		}
	}
	
	public static int graphSize(String fileName){
		File file = new File(fileName);
		int noOfNodes=0;
		try {
			FileReader fr = new FileReader(file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		noOfNodes = Integer.parseInt(reader.readLine()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noOfNodes;
		
	}
	}
