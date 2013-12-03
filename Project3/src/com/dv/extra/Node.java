package com.dv.extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;



public class Node {

	//private DVTable dvtable;
	private static int [][] adjacencyMatrix;
	private static float [] [] costMatrix;
	private static HashMap<Integer, ConnectionDetail> connxDetailMap;
	private boolean costChanged = false;
	private int numberOfCostChanges = 0;
	private static String myIP;
	private static int myPort;
	private static int noOfNodes;
	private static int myid = -1;
	private static Node _instance = null;
	private static boolean updateReceived = false;
	public int getNumberOfCostChanges() {
		return numberOfCostChanges;
	}

	public void setNumberOfCostChanges(int numberOfCostChanges) {
		this.numberOfCostChanges = numberOfCostChanges;
	}
	
	public void incrementCostChanges(){
		this.numberOfCostChanges++;
	}
	private Node(int noOfNodes){
		
		try{
			adjacencyMatrix = new int[noOfNodes] [noOfNodes];
			costMatrix = new float[noOfNodes] [noOfNodes];
			this.myIP = Inet4Address.getLocalHost().getHostAddress();
			
			connxDetailMap = new HashMap<Integer, ConnectionDetail>();
			for(int i=0; i < noOfNodes; i++){
				for(int j=0; j < noOfNodes; j++){
					costMatrix[i][j] = Float.MAX_VALUE;
				}
			}
		}catch(UnknownHostException ex){
			ex.printStackTrace();
		}
	}
	
	public static Node getInstance(){
		if(_instance != null){			
			return _instance;
		}else{
			_instance = new Node(noOfNodes);
			return _instance;
		}
	}
	
	public static void printMatrices(){
		System.out.println("Adjacency matrix at node " + myid);
		for(int i=0; i < noOfNodes; i++){
			for(int j=0; j < noOfNodes; j++){
				System.out.print(adjacencyMatrix[i][j] + "\t");
			}
			System.out.println();
		}
		
		System.out.println("Cost matrix at node " + myid);
		for(int i=0; i < noOfNodes; i++){
			for(int j=0; j < noOfNodes; j++){
				System.out.print(costMatrix[i][j] + "\t");
			}
			System.out.println();
		}
	}
	public static void simulateDV(){
		
		int index = myid -1;
		while(true){
			printMatrices();
			System.out.println("simulateDV: Before update received");
			while(isUpdateReceived() == false);
			System.out.println("simulateDV: Update received");
			float minCost = Float.MAX_VALUE;
			boolean minCostChanged = false;
			for(int i = 0; i < noOfNodes; i++  ){
				if( index != i){
					for(int j =0; j < noOfNodes; j ++){
						
							if(isNeighbour(index,j)){
								float newCost = getCost(index, j) + getCost(j, i);
								if(newCost < minCost){
									minCost = newCost;
								}
							}
						
					}
					if(minCost != Float.MAX_VALUE){
						if(minCost < getCost(index, i)){
							addEntryToCostMatrix(index, i, minCost);
							minCostChanged = true;							
						}
						minCost = Float.MAX_VALUE;
					}
				}
			}
			if (minCostChanged == true){
				for(int i = 0; i < noOfNodes; i ++){
					if (isNeighbour(index,i)){
						//System.out.println("Setting cost changed for " + i + " by " + currentNodeIndex);
						DVData dvData = new DVData(noOfNodes,index+1);
						float [] costDV = dvData.getCostMatrix();
						for(int j=0; j < noOfNodes; j++){
							costDV[j] = costMatrix[index][j];
						}
						ConnectionDetail connectionDetail = (ConnectionDetail) connxDetailMap.get(Integer.valueOf(i+1));
						String ip = connectionDetail.getIp();
						int port = connectionDetail.getPort();
						Sender sender = new Sender(ip, port, dvData);
						sender.start();
						/*nodes[i].setCostChanged(true);
						nodes[i].incrementCostChanges();
						nodes[currentNodeIndex].setCostChanged(true);
						nodes[currentNodeIndex].incrementCostChanges();
						copyCostMatrices(currentNodeIndex, i);*/
					}
				}
			}else{
				setUpdateReceived(false);
			}
		}
	}
	
	public static boolean initNode(String fileName, int portno){
		boolean success = false;
		File file = new File(fileName);
		String nextLineReadInput;
		String splitStrings[];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int no = Integer.parseInt(reader.readLine());
			noOfNodes = no;
			Node node = getInstance();
			
		
			while((nextLineReadInput=reader.readLine())!=null){
				
				splitStrings = nextLineReadInput.split("\\s"); 
				int source = Integer.parseInt(splitStrings[0]);
				int destination = Integer.parseInt(splitStrings[1]);
				float cost = Float.parseFloat(splitStrings[2]);
				
				nextLineReadInput = reader.readLine();
				splitStrings = nextLineReadInput.split("\\s");
				
				ConnectionDetail srcConnectionDetail = new ConnectionDetail();
				srcConnectionDetail.setIp(splitStrings[0]);
				srcConnectionDetail.setPort(Integer.parseInt(splitStrings[1]));
				
				ConnectionDetail dstConnectionDetail = new ConnectionDetail();
				dstConnectionDetail.setIp(splitStrings[2]);
				dstConnectionDetail.setPort(Integer.parseInt(splitStrings[3]));
				
				//set nodes ID if not set
				if(node.getMyid() == -1){
					if(srcConnectionDetail.getIp().equals(node.getMyIP()) && srcConnectionDetail.getPort() == portno){
						node.setMyid(source);
						node.setMyPort(srcConnectionDetail.getPort());
					}else if (dstConnectionDetail.getIp().equals(node.getMyIP()) && dstConnectionDetail.getPort() == portno){
						node.setMyid(destination);
						node.setMyPort(dstConnectionDetail.getPort());
					}
				}
				
				//add connection details
				node.addEntryToConnectionDetailMap(Integer.valueOf(source), srcConnectionDetail);
				node.addEntryToConnectionDetailMap(Integer.valueOf(destination), dstConnectionDetail);
				
				if(node.getMyid() == source){
					node.addEntryToAdjacencyMatrix(source -1, destination -1, 1);
					node.addEntryToCostMatrix(source -1, destination -1, cost);
					node.addEntryToCostMatrix(source - 1, source - 1, 0);
				}else if (node.getMyid() == destination){
					node.addEntryToAdjacencyMatrix(destination - 1, source - 1, 1);
					node.addEntryToCostMatrix(destination -1, source -1, cost);
					node.addEntryToCostMatrix(destination - 1, destination - 1, 0);
				}					
				
				
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
	
	public static void sendInitialDV(){
		
		int index = myid -1;
		for(int i = 0; i < noOfNodes; i ++){
			if (isNeighbour(index,i)){
				//System.out.println("Setting cost changed for " + i + " by " + currentNodeIndex);
				DVData dvData = new DVData(noOfNodes,myid);
				float [] costDV = dvData.getCostMatrix();
				for(int j=0; j < noOfNodes; j++){
					costDV[j] = costMatrix[index][j];
				}
				ConnectionDetail connectionDetail = (ConnectionDetail) connxDetailMap.get(Integer.valueOf(i+1));
				String ip = connectionDetail.getIp();
				int port = connectionDetail.getPort();
				Sender sender = new Sender(ip, port, dvData);
				sender.start();
			}
		}
	}
	
	public static void addEntryToConnectionDetailMap(Integer key, ConnectionDetail value){
		connxDetailMap.put(key, value);
	}
	public ConnectionDetail getConnectionDetail(Integer key){
		ConnectionDetail connectionDetail = this.connxDetailMap.get(key);
		return connectionDetail;
	}
	public static void addEntryToAdjacencyMatrix(int sourceIndex, int destinationIndex, int connected){
		adjacencyMatrix [sourceIndex][destinationIndex] = connected;
		
	}
	
	public static void addEntryToCostMatrix(int sourceIndex, int destinationIndex, float cost){
		costMatrix[sourceIndex] [destinationIndex] = cost;
		//this.costMatrix[destination -1] [source -1] = cost;
	}
	
	public static synchronized void updateDV(DVData dvData){
		
		int index = dvData.getNode() - 1;
		float [] fromCostMatrix = dvData.getCostMatrix();
		for(int i=0; i < noOfNodes; i++){
			if(costMatrix[index][i] > fromCostMatrix[i]){
				costMatrix[index][i] = fromCostMatrix[i];
			}
		}
		setUpdateReceived(true);
	}
	
	public static boolean isNeighbour(int sourceIndex, int destinationIndex){
		if(adjacencyMatrix[sourceIndex][destinationIndex] == 1)
			return true;
		return false;
	}
	public static float getCost(int source, int destination){
		return costMatrix[source] [destination];
	}

	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(int[] [] adjacencyMatrix) {
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

	public HashMap<Integer, ConnectionDetail> getConnxDetailMap() {
		return connxDetailMap;
	}

	public void setConnxDetailMap(HashMap<Integer, ConnectionDetail> connxDetailMap) {
		this.connxDetailMap = connxDetailMap;
	}

	public String getMyIP() {
		return myIP;
	}

	public void setMyIP(String myIP) {
		this.myIP = myIP;
	}

	public int getMyPort() {
		return myPort;
	}

	public void setMyPort(int myPortarg) {
		myPort = myPortarg;
	}

	public int getMyid() {
		return myid;
	}

	public void setMyid(int myid) {
		this.myid = myid;
	}

	public synchronized static boolean isUpdateReceived() {
		return updateReceived;
	}

	public synchronized static void setUpdateReceived(boolean updateReceived) {
		Node.updateReceived = updateReceived;
	}
}
