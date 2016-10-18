package test;

import java.io.File;
import java.io.IOException;

public class FileTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File a = new File("e:\\test\\abc\\a.txt");
		try {
			a.getParentFile().mkdirs();
			a.createNewFile();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
