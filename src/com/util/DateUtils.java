package com.util;

public class DateUtils {
	
	
	public static String millisToHMS(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		
		String time = String.format("%02d時%02d分%02d秒", hour, minute, second);
		return time;
	}

}
