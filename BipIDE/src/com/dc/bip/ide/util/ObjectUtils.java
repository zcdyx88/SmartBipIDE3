package com.dc.bip.ide.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectUtils {

	public static Object getObject(File file) {
		Object obj = null;

		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(new FileInputStream(file));
			obj = objIn.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != objIn) {
				try {
					objIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

}
