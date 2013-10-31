package org.tempuri;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

	public static void serializeObject(Object obj) {

		String fileName = obj.getClass().getName() + ".txt";
		try {
			FileOutputStream fos = new FileOutputStream(fileName);

			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(obj);

			oos.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static Object deserializeObject(Object obj) {

		String fileName = obj.getClass().getName() + ".txt";
		Object p = null;
		try {
			FileInputStream fos = new FileInputStream(fileName);

			ObjectInputStream ois = new ObjectInputStream(fos);

			p = ois.readObject();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

}
