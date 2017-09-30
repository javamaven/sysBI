package io.renren.util;

public class StringUtil {
	
	
	public static void main(String[] args) {
		
		
	}
	
	
	public static boolean isEmpty(Object obj){
		
		if(obj == null){
			return true;
		}
		if(obj.toString().trim().equals("")){
			return true;
		}
		
		return false;
	}
	
	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}
	
	
	public static double getDoubleValue(Object obj){
		if(isEmpty(obj)){
			return 0;
		}
		return Double.parseDouble(obj.toString());
	}
	
	public static int getIntegerValue(Object obj){
		if(isEmpty(obj)){
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}

}
