package io.renren.service.shichang.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.dao.shichang.ChannelHeadManagerDao;
import io.renren.entity.SysUserEntity;
import io.renren.entity.shichang.ChannelHeadManagerEntity;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.system.jdbc.JdbcHelper;

import static io.renren.utils.ShiroUtils.getUserEntity;



@Service("channelHeadManagerService")
public class ChannelHeadManagerServiceImpl implements ChannelHeadManagerService {
	@Autowired
	private ChannelHeadManagerDao channelHeadManagerDao;
	@Autowired
	DruidDataSource dataSource;
	
	@Override
	public List<Map<String,Object>> queryList(Map<String, Object> map){
		return channelHeadManagerDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return channelHeadManagerDao.queryTotal(map);
	}
	
	@Override
	public void save(ChannelHeadManagerEntity entity){
		channelHeadManagerDao.save(entity);
	}
	
	@Override
	public void update(ChannelHeadManagerEntity entity){
		channelHeadManagerDao.update(entity);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		channelHeadManagerDao.delete(map);
	}
	
	@Override
	public void deleteBatch(String[] channelLabels){
		channelHeadManagerDao.deleteBatch(channelLabels);
	}

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		channelHeadManagerDao.insert(map);
	}

	@Override
	public ChannelHeadManagerEntity queryObject(String channelLabel) {
		// TODO Auto-generated method stub
		return channelHeadManagerDao.queryObject(channelLabel);
	}
	
	@Override
	public ChannelHeadManagerEntity queryByChannelHead(ChannelHeadManagerEntity entity) {
		// TODO Auto-generated method stub
		return channelHeadManagerDao.queryByChannelHead(entity);
	}
	
	private List<String> getChannelHeadByUserName(String userName) {
		// TODO Auto-generated method stub
		List<String> channelHeadList = new ArrayList<String>();
		List<Map<String, Object>> list = channelHeadManagerDao.queryListMap(null);
		Map<String,String> channelHeadMap = new HashMap<String, String>();
		String currChannelHead = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String sysAccount = map.get("sys_account") + "";
			String channel_head = map.get("channel_head") + "";
			String parent_channel_head = map.get("parent_channel_head") + "";
			if(channelHeadMap.containsKey(parent_channel_head)){
				channelHeadMap.put(parent_channel_head, channelHeadMap.get(parent_channel_head) + "^" + channel_head);
			}else{
				channelHeadMap.put(parent_channel_head, channel_head);
			}
			
			if(userName.equals(sysAccount)){//自身所负责渠道
				currChannelHead = channel_head;
				channelHeadList.add(channel_head);
			}
		}
		//组员所负责渠道
		getAll(channelHeadList, channelHeadMap, currChannelHead);
//		if(channelHeadMap.containsKey(currChannelHead)){//父节点是当前节点的
//			String[] headArr = channelHeadMap.get(currChannelHead).split("//^");
//			for (int i = 0; i < headArr.length; i++) {
//				currChannelHead = headArr[i];
//				channelHeadList.add(currChannelHead);
//				getAll(channelHeadList, channelHeadMap, currChannelHead);
//			}
//		}
		System.err.println("++++++++++++++" + channelHeadList);
		return channelHeadList;
	}

	private void getAll(List<String> channelHeadList, Map<String, String> channelHeadMap, String currChannelHead) {
		//组员所负责渠道
		if(channelHeadMap.containsKey(currChannelHead)){//父节点是当前节点的
			String[] headArr = channelHeadMap.get(currChannelHead).split("//^");
			for (int i = 0; i < headArr.length; i++) {
				currChannelHead = headArr[i];
				channelHeadList.add(currChannelHead);
				getAll(channelHeadList, channelHeadMap, currChannelHead);
			}
		}
	}

	@Override
	public List<String> queryAuthByChannelHead() {
		// TODO Auto-generated method stub
		SysUserEntity userEntity = getUserEntity();
		List<String> channelHeadList = getChannelHeadByUserName(userEntity.getUsername());
		return channelHeadList;
	}

	@Override
	public boolean isMarketDirector() {
		// TODO Auto-generated method stub
		SysUserEntity userEntity = getUserEntity();
		List<Map<String, Object>> list = channelHeadManagerDao.queryListMap(null);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String sysAccount = map.get("sys_account") + "";
			String parent_channel_head = map.get("parent_channel_head") + "";
			if(userEntity.getUsername().equals(sysAccount) && "-1".equals(parent_channel_head)){
				return true;
			}
		}
		return false;
	}

	
	@Override
	public List<String> queryChannelAuthByChannelHead(String key) {
		List<String> labelList = new ArrayList<>();
		List<String> headList = queryAuthByChannelHead();
		String sql = "select * from dim_channel d where CHANNEL_HEAD in (${headString})";
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			String headString = "";
			for (int i = 0; i < headList.size(); i++) {
				String head = headList.get(i);
				if(i == headList.size() - 1){
					headString += "'" + head + "'";
				}else{
					headString += "'" + head + "',";
				}
			}
			if(headList.size() > 0){
				List<Map<String, Object>> data = jdbcHelper.query(sql.replace("${headString}", headString));
				for (int i = 0; i < data.size(); i++) {
					String label = data.get(i).get(key.toUpperCase()) + "";
					labelList.add(label );
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return labelList;
	}
	
}
