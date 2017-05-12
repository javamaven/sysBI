package io.renren.service;

import io.renren.entity.UserBehaviorEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public interface UserBehaviorService {
    void insert(Map<String, Object> map);
    List<UserBehaviorEntity> queryList(Map<String, Object> map);
    String querySysUsers(Long id);
}
