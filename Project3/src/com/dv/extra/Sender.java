package com.dv.extra;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sender extends Thread {

	private Node node;
	private String ip;
	private int port;
	private DVData dvData;
	public Sender(String ip, int port, DVData dvData){
		node = Node.getInstance();
		this.ip = ip;
		this.port = port;
		this.dvData = dvData;
	}
	
	@Override
	public void run() {
		DatagramSocket socket;
		try {
			socket = new DatagramSocket();
			byte [] buf;
			buf = SerializerDeserializer.serialize(dvData);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
			System.out.println("Sending data of " + dvData.getNode() + " to " + ip + " port " + port);
			socket.send(packet);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
