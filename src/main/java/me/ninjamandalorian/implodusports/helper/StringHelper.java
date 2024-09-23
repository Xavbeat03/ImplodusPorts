package me.ninjamandalorian.implodusports.helper;

import net.md_5.bungee.api.ChatColor;

public class StringHelper {

	public static String[] remFirst(String[] strings) {
		if (strings.length < 2) return new String[0];
		String[] returnArr = new String[strings.length - 1];
		for (int i = 1; i < strings.length; i++) {
			returnArr[i - 1] = strings[i];
		}
		return returnArr;
	}

	public static String capitalize(String string) {
		String returnString = "";

		char[] characters = string.toCharArray();
		returnString += Character.toUpperCase(characters[0]);
		for (int i = 1; i < characters.length; i++) {
			if (!Character.isAlphabetic(characters[i - 1])) {
				returnString += Character.toUpperCase(characters[i]);
			} else {
				returnString += Character.toLowerCase(characters[i]);
			}
		}

		return returnString;
	}

	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
