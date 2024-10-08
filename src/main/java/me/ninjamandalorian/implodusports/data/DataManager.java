package me.ninjamandalorian.implodusports.data;

import me.ninjamandalorian.implodusports.ImplodusPorts;
import me.ninjamandalorian.implodusports.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataManager {

	@SuppressWarnings("unused")
	private static ImplodusPorts plugin;

	private static String dataFolder = "plugins" + File.separator + "ImplodusPorts" + File.separator + "data";
	private static DumperOptions options;

	public static void init() {
		plugin = ImplodusPorts.getInstance();

		createFolders();
	}

	/**
	 * Saves data into a .yml format
	 *
	 * @param filePath - File's path after ImplodusPorts/Data/
	 * @param map      - Map to be saved
	 */
	public static void saveYmlData(String filePath, HashMap<String, Object> map) {
		File file = getFile(filePath);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			Logger.log("Encountered error when creating PrintWriter for " + filePath);
		}

		if (options == null) {
			options = new DumperOptions();
			options.setIndent(2);
			options.setPrettyFlow(true);
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		}

		Yaml yaml = new Yaml(options);
		yaml.dump(map, writer);
		writer.close();
	}

	public static void saveRawData(String filePath, String rawData) {
		File file = getFile(filePath);
		try {
			PrintWriter out = new PrintWriter(file.getPath(), "UTF-8");
			out.write(rawData);
			out.close();
		} catch (UnsupportedEncodingException e) {
			Logger.log("Error: unsupported encoding");
		} catch (FileNotFoundException e) {
			Logger.log("Error: file not found");
		}
	}

	/**
	 * Gets the data back as a HashMap
	 *
	 * @param filePath - FilePath of data
	 * @return HashMap of data or null
	 */
	public static HashMap<String, Object> getYmlData(String filePath) {

		File file = getFile(filePath);
		if (!file.exists()) {
			Logger.log("Attempted to retrieve non-existant file: " + filePath);
			return null;
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.warn("[ImplodusPorts] Encountered error when creating FileInputStream for " + filePath);
			return null;
		}
		Yaml yaml = new Yaml();
		HashMap<String, Object> data = yaml.load(inputStream);
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.log(e.getMessage());
		}

		return data;
	}

	/**
	 * Retrieves raw data of file (UTF-8)
	 *
	 * @param pathString - File path string of raw data
	 * @return String of file path
	 */
	public static String getRawData(String pathString) {
		Path path = getFile(pathString).toPath();

		try {
			return Files.readString(path);
		} catch (IOException e) {
			Logger.log("Error: " + e.getMessage());
			return "";
		}
	}

	/**
	 * Deletes a file in data
	 *
	 * @param filePath - File path
	 * @return Boolean of success
	 */
	public static boolean deleteFile(String filePath) {
		File file = getFile(filePath);

		if (!file.exists()) {
			Logger.log("Attempted to retrieve non-existant file: " + filePath + ".yml");
			return false;
		}

		try {
			return Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Initial creation of all needed folders
	 */
	private static void createFolders() {
		List<String> paths = Arrays.asList(
			"plugins" + File.separator + "ImplodusPorts" + File.separator + "data"
		);

		for (String path : paths) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/**
	 * Helper to get file (creates if null)
	 *
	 * @param path - Path to file
	 * @return File
	 */
	private static File getFile(String path) {
		path = dataFolder + File.separator + path;

		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.log("Encountered error when creating file - " + path + " :: " + e.getMessage());
				return null;
			}
		}
		return file;
	}

}
