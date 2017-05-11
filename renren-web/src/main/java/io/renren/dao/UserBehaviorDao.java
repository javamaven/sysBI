package io.renren.dao;

import io.renren.entity.UserBehaviorEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public  interface  UserBehaviorDao extends BaseDao<UserBehaviorEntity> {
    void  insert(UserBehaviorEntity logUserBehavior);

}
