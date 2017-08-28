package io.renren.util;

import java.util.Map;

public class MapUtil {

	
	public static void main(String[] args) {
		
	}
	
	public static String getValue(Map<String,Object> map, String key){
		Object object = map.get(key);
		if(object == null){
			return "";
		}
		return object.toString().trim();
	}
}
