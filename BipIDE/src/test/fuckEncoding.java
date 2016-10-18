package test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by vincentfxz on 16/5/23.
 */
public class fuckEncoding {

//    private static final String targetPath = "/Users/vincentfxz/Desktop/fanfan521/BipIDE";
    private static final String srcPath = "E:\\workspace\\BipIDE\\src";

    public static void main(String[] args) {
        String path = "";
        File dir = new File(srcPath);
        scanAndConvert(dir);
    }

    public static void scanAndConvert(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    scanAndConvert(subFile);
                } else {
                    try {
                        if(subFile.getName().endsWith("java")){
                            String temp = FileUtils.readFileToString(subFile, "GBK");
                            FileUtils.write(subFile, temp, "UTF-8");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
