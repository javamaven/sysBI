package io.renren.util;

import io.renren.service.UserBehaviorService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

@RestController
public class UserBehaviorUtil {

    public UserBehaviorService userBehaviorService;

    public UserBehaviorUtil(UserBehaviorService userBehaviorService) {
        this.userBehaviorService = userBehaviorService;
    }

    public void insert(Long ID,Date createTime,String TYPE,String reportType,String EXECSQL) {

        String userID=ID+"";
        Map map = new HashMap();
        map.put("userID",userID);
        map.put("createTime",createTime);
        map.put("TYPE",TYPE);
        map.put("reportType",reportType);
        map.put("EXECSQL",EXECSQL);

        userBehaviorService.insert(map);
    }

}