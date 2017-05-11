package io.renren.service.impl;

import io.renren.dao.UserBehaviorDao;
import io.renren.entity.UserBehaviorEntity;
import io.renren.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public  void insert(UserBehaviorEntity logUserBehavior) {userBehaviorDao.insert(logUserBehavior);
    }


}
