package io.renren.service.main;

import java.util.Map;

/**
 * 
 * 
 */
public interface MainService {


	Map<String, Object> queryUserWait(String oldUserId,String cgUserId);
	
	
}
