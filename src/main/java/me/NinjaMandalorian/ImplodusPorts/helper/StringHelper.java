package me.NinjaMandalorian.ImplodusPorts.helper;

import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;

public class StringHelper {

	/**
	 * Removes the first element from an array of strings.
	 * @param strings - Array of strings
	 * @return Array without the first element
	 */
	public static String[] removeFirstElement(String[] strings) {
		if (strings == null || strings.length < 2) return new String[0];
		
		return Arrays.stream(strings, 1, strings.length).toArray(String[]::new);
	}

	/**
	 * Capitalizes the first letter of each word in a string.
	 * @param string - String to capitalize
	 * @return Capitalized string
	 */
	public static String capitalize(String string) {
		if (string == null || string.isEmpty()) return string;
		
		StringBuilder returnString = new StringBuilder();
		char[] characters = string.toCharArray();
		returnString.append(Character.toUpperCase(characters[0]));
		
		for (int i = 1; i < characters.length; i++) {
			if (!Character.isAlphabetic(characters[i - 1])) {
		            returnString.append(Character.toUpperCase(characters[i]));
			} else {
		            returnString.append(Character.toLowerCase(characters[i]));
			}
		}

		    return returnString.toString();
	}

	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
