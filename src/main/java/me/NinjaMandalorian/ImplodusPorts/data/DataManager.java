package me.NinjaMandalorian.ImplodusPorts.data;

import me.NinjaMandalorian.ImplodusPorts.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

	private static final String DATA_FOLDER = "plugins" + File.separator + "ImplodusPorts" + File.separator + "data";
	public static final String ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE = "Attempted to retrieve non-existant file: ";
	private static DumperOptions options;

	public static void init() {
		createFolders();
	}

	/**
	 * Saves data into a .yml format
	 *
	 * @param filePath - File's path after ImplodusPorts/Data/
	 * @param map      - Map to be saved
	 */
	public static void saveYmlData(String filePath, Map<String, Object> map) {

		File file = getFile(filePath);
		if (file == null) return;

		try (PrintWriter writer = new PrintWriter(file)) {
			if (options == null) {
				options = new DumperOptions();
				options.setIndent(2);
				options.setPrettyFlow(true);
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			}

			Yaml yaml = new Yaml(options);
			yaml.dump(map, writer);
		} catch (FileNotFoundException e) {
			Logger.log("Encountered error when creating PrintWriter for " + filePath);
		}
	}

	public static void saveRawData(String filePath, String rawData) {
		File file = getFile(filePath);
		if (file == null) return;
		try {
			PrintWriter out = new PrintWriter(file.getPath(), StandardCharsets.UTF_8);
			out.write(rawData);
			out.close();
		} catch (UnsupportedEncodingException e) {
			Logger.log("Error: unsupported encoding");
		} catch (FileNotFoundException e) {
			Logger.log("Error: file not found");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the data back as a HashMap
	 *
	 * @param filePath - FilePath of data
	 * @return HashMap of data or null
	 */
	public static Map<String, Object> getYmlData(String filePath) {

		File file = getFile(filePath);
		if(file == null) {
			Logger.log(ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE + filePath);
			return Collections.emptyMap();
		}
		if (!file.exists()) {
			Logger.log(ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE + filePath);
			return Collections.emptyMap();
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.warn("[ImplodusPorts] Encountered error when creating FileInputStream for " + filePath);
			return Collections.emptyMap();
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
	 * Gets the raw data from a file
	 * @param pathString - Path to file
	 * @return String of data
	 */
	public static String getRawData(String pathString) {
		if(getFile(pathString) == null) {
			Logger.log(ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE + pathString);
			return "";
		}
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
		if(file == null) {
			Logger.log(ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE + filePath + ".yml");
			return false;
		}
		if (!file.exists()) {
			Logger.log(ATTEMPTED_TO_RETRIEVE_NON_EXISTANT_FILE + filePath + ".yml");
			return false;
		}

		try {
			return Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			Logger.log("Error: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Initial creation of all needed folders
	 */
	private static void createFolders() {
		List<String> paths = List.of(
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
		path = DATA_FOLDER + File.separator + path;

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
