package com.dv.extra;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		if(args.length == 2){
			int portNo = Integer.parseInt(args[1]);
			Node.initNode(args[0], portNo);
			Receiver receiver = new Receiver(portNo);
			receiver.start();
			//Node.printMatrices();
			Scanner scanner = new Scanner(System.in);
			int a = scanner.nextInt();
			if(a==1){
			Node.setUpdateReceived(true);
			Node.sendInitialDV();
			
			}
			a = scanner.nextInt();
			if(a==2){
				Node.simulateDV();
			}
		}

	}

}
