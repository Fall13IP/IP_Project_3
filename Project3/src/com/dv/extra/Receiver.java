package com.dv.extra;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver extends Thread{

	private Node node;
	private int portNo;
	public Receiver(int portNo){
		node = Node.getInstance();
		this.portNo = portNo;
	}

	@Override
	public void run() {
		DatagramSocket socket;
		DatagramPacket packet;
		
		try {
			System.out.println("opened receiving socket on " + portNo);
			socket = new DatagramSocket(portNo);
			while(true){
				byte [] buf = new byte[1024];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				
				DVData dvData = SerializerDeserializer.deserialize(packet.getData());
				System.out.println("received from " +dvData.getNode());
				Node.updateDV(dvData);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
