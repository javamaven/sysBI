package io.renren.service.impl;

import io.renren.dao.UserBehaviorDao;
import io.renren.entity.UserBehaviorEntity;
import io.renren.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */


@Service("userBehaviorService")
    public class UserBehaviorServiceImpl implements UserBehaviorService {
        @Autowired
        private  UserBehaviorDao userBehaviorDao;

    @Override
    public List<UserBehaviorEntity> queryList(Map<String, Object> map){return userBehaviorDao.queryList(map);
    }

//    @Override
//    public List<UserBehaviorEntity> queryList(Map<String, Object> map){
//
//        List<UserBehaviorEntity> list = userBehaviorDao.queryList(map);
//        for (int i = 0; i < list.size(); i++) {
//            UserBehaviorEntity entity = list.get(i);
//            String statData = entity.getStatData();
//            if(statData.length() == 8){
//                String year = statData.substring(0, 4);
//                String month = statData.substring(4, 6);
//                String day = statData.substring(6, 8);
//                entity.setStatData(year + "-" + month + "-" + day);
//            }
//        }
//        return list;
//    }




    @Override
    public  void insert(Map<String, Object> map ) {userBehaviorDao.insert(map);
    }
    @Override
    public List<UserBehaviorEntity> queryExport() { return userBehaviorDao.queryExport();//导出EX
    }
    @Override
    public String querySysUsers(Long id) {
        return userBehaviorDao.querySysUsers(id);
    }
    @Override
    public Map<String, String> getExcelFields() {
        Map<String, String> headMap = new LinkedHashMap<String, String>();
        headMap.put("userName", "用户名");
        headMap.put("statDate", "访问时间");
        headMap.put("reportType", "访问报表");
        headMap.put("seeTimes", "查看次数");
        headMap.put("exportTimes", "导出次数");
        headMap.put("editTimes", "修改次数");
        headMap.put("deleteTimes", "删除次数");

        return headMap;
    }

}
