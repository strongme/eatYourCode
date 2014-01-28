package cn.mm2.evernote.ui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;



public class TokenStoreUtil {
	
	private final static String TokenFileName = "token.in";
	
	public static void saveToken(final String token){
		FileWriter fw = null;
		try {
			fw = new FileWriter(getTokenFileLocation());
			fw.write(token);
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getToken(){
		String token = "";
        File file = new File(getTokenFileLocation());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            // 一次读入一行，直到读入null为文件结束
            token = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return token;
	}
	
	private static String getTokenFileLocation(){
		Location pathLocation = Platform.getInstallLocation();
		String path = pathLocation.getURL().getPath();
		String filePath = new StringBuilder().append(path).append("\\").append(TokenFileName).toString();
		return filePath;
	}

}
