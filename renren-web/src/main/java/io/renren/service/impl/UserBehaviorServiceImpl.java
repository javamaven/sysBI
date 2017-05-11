package io.renren.service.impl;

import io.renren.dao.UserBehaviorDao;
import io.renren.entity.UserBehaviorEntity;
import io.renren.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/5/5 0005.
 */



    public class UserBehaviorServiceImpl implements UserBehaviorService {
        @Autowired
        private UserBehaviorDao userBehaviorDao;

//        @Override
//        public void insert(UserBehaviorEntity UserBehavior) {
//            userBehaviorDao.insert(UserBehavior);
//    }
    @Override
    public void insert(UserBehaviorEntity UserBehavior) {userBehaviorDao.insert(UserBehavior);
    }


}
