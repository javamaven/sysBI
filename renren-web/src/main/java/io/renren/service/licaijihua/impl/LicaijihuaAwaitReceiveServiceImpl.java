package io.renren.service.licaijihua.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.service.licaijihua.LicaijihuaAwaitReceiveService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.PageUtils;

@Service("licaijihuaAwaitReceiveService")
public class LicaijihuaAwaitReceiveServiceImpl implements LicaijihuaAwaitReceiveService{

	@Autowired
	DataSourceFactory dataSourceFactory;
	
	@Override
	public PageUtils query(Map<String, Object> params) {
		Integer page = Integer.parseInt(params.get("page").toString());
		Integer limit = Integer.parseInt(params.get("limit").toString());
		Integer start = (page - 1) * limit;
		Integer end	= start + limit;
		String statPeriod = params.get("statPeriod") + "";
		statPeriod = statPeriod.replace("-", "");
		String month = statPeriod.substring(0, 6);
		PageUtils pageUtils = null;
		try {
			String path = this.getClass().getResource("/").getPath();
			String sql = FileUtil.readAsString(new File(path + File.separator + "sql/理财计划/理财计划待收存量.txt"));
			sql = sql.replace("${day}", statPeriod);
			sql = sql.replace("${month}", month);
			List<Map<String, Object>> dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(sql);
			List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
			if(retList.size() < end){
				retList.addAll(dataList);
			}else{
				retList.addAll(dataList.subList(start, end));
			}
			pageUtils = new PageUtils(retList , dataList.size(), limit, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageUtils;
	}

}
