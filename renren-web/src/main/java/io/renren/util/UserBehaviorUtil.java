package io.renren.util;

import io.renren.dao.UserBehaviorDao;
import io.renren.entity.UserBehaviorEntity;
import io.renren.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/5/5 0005.
 */


public class UserBehaviorUtil {
    @Autowired
    public static UserBehaviorService userBehaviorService;

    public static void insert(UserBehaviorEntity logUserBehavior) {
        System.out.println(logUserBehavior);
        userBehaviorService.insert(logUserBehavior);
    }
}
