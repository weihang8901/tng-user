package com.central.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.db.mapper.SuperMapper;
import com.central.common.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户基础信息表
 * 
 * @author wangweijun
 * @date 2020-04-09 14:21:39
 */
@Mapper
public interface UserInfoMapper extends SuperMapper<UserInfo> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<UserInfo> findList(Page<UserInfo> page, @Param("p") Map<String, Object> params);
    void saveList(List<UserInfo> user);
}
