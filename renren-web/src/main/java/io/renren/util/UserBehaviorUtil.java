package io.renren.util;

import io.renren.entity.UserBehaviorEntity;
import io.renren.service.DailyService;
import io.renren.service.UserBehaviorService;
import io.renren.service.impl.UserBehaviorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

@RestController
public class UserBehaviorUtil {

    public UserBehaviorService userBehaviorService;

    public UserBehaviorUtil(UserBehaviorService userBehaviorService){
        this.userBehaviorService = userBehaviorService;
    }
    public void insert(UserBehaviorEntity logUserBehavior) {
        userBehaviorService.insert(logUserBehavior);
    }
}
