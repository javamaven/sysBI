package io.renren.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.LogUserBehaviorDao;
import io.renren.entity.LogUserBehaviorEntity;
import io.renren.service.LogUserBehaviorService;



@Service(" LogUserBehaviorService")
public class LogUserBehaviorServiceImpl implements LogUserBehaviorService {
	@Autowired
	private LogUserBehaviorDao logUserBehaviorDao;

	@Override
	public List<LogUserBehaviorEntity>  queryAction() {
		return logUserBehaviorDao.queryAction();
	}

	@Override
	public List<LogUserBehaviorEntity>  queryActionPlatform() {
		return logUserBehaviorDao.queryActionPlatform();
	}

	@Override
	public List<LogUserBehaviorEntity> queryList(Map<String, Object> map){
		String start_action_time = map.get("start_action_time") + "";
		if (StringUtils.isNotEmpty(start_action_time)) {
			map.put("start_action_time", start_action_time + " 00:00:00");
		}
		String end_action_time = map.get("end_action_time") + "";
		if (StringUtils.isNotEmpty(end_action_time)) {
			map.put("end_action_time", end_action_time + " 23:59:59");
		}

		return logUserBehaviorDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return logUserBehaviorDao.queryTotal(map);
	}
	@Override
	public List<LogUserBehaviorEntity> queryExport() { return logUserBehaviorDao.queryExport();//导出EX
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("userID", "用户ID");
		headMap.put("userName", "用户名");
		headMap.put("channlName", "渠道名称");
		headMap.put("channlMark", "渠道标记");
		headMap.put("actionTime", "操作时间");
		headMap.put("actionPlatform", "操作平台");
		headMap.put("action", "行为");
		headMap.put("projectType", "涉及项目类型");
		headMap.put("projectAmount", "涉及项目本金");
		return headMap;
	}
	
}
