package me.Alientation.AlienPlugin.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;

import com.google.common.io.Files;

import me.Alientation.AlienPlugin.Main;

public class GameFileHandler {
	public static JSONObject dataFile = null;
	
	public static void init(JSONObject df) {
		dataFile = df;
	}
	
	@SuppressWarnings("unchecked")
	public static void save(String attributeName, Object obj) {
		if (dataFile.containsKey(attributeName)) {
			dataFile.replace(attributeName, obj);
		} else {
			dataFile.put(attributeName, obj);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Object load(String attributeName, Object defaultValue) {
		return dataFile.getOrDefault(attributeName, defaultValue);
	}
	
	public static void writeToFile() {
		FileWriter fw = null;
		try {
			File f = new File("C:\\Users\\mcpir\\Desktop\\Spigot1.16.5\\AlienWorldData\\data.json");
			String back = "C:\\Users\\mcpir\\Desktop\\Spigot1.16.5\\AlienWorldData\\dataBackUp_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".json";
			File backup = new File(back);
			if (backup.exists()) {
				backup.delete();
			}
			Files.copy(f, backup);
			if (f.exists()) {
				f.delete();
			}
			fw = new FileWriter("C:\\Users\\mcpir\\Desktop\\Spigot1.16.5\\AlienWorldData\\data.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fw == null) {
			System.out.println("Something went wrong whilst attempting to save game");
		} else {
			try {
				fw.write(Main.data.toJSONString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
