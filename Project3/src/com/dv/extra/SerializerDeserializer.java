package com.dv.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerDeserializer {

	public static byte [] serialize(DVData segment){
		
		byte [] serializedData = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(segment);
			serializedData = bos.toByteArray();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serializedData;
	}
	
	public static DVData deserialize(byte [] serializedData){
		
		DVData segmentClass = null;
		
		ObjectInputStream iStream;
		try {
			iStream = new ObjectInputStream(new ByteArrayInputStream(serializedData));
			segmentClass = (DVData) iStream.readObject();
			iStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return segmentClass;
	}
	
	public static void main(String args[]){
		
	
	}
}
