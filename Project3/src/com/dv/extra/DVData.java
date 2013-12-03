package com.dv.extra;

import java.io.Serializable;

public class DVData implements Serializable{

	private int node;
	private float [] costMatrix;
	
	public DVData(int noOfNodes, int fromNode){
		costMatrix = new float[noOfNodes];
		this.node = fromNode;
	}
	public int getNode() {
		return node;
	}
	public void setNode(int node) {
		this.node = node;
	}
	public float[] getCostMatrix() {
		return costMatrix;
	}
	public void setCostMatrix(float[] costMatrix) {
		this.costMatrix = costMatrix;
	}
}
