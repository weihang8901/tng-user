package com.central.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.constant.CommonConstant;
import com.central.common.lock.DistributedLock;
import com.central.common.model.*;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.user.utils.AESOperator;
import com.central.user.utils.SnowFlake;
import com.central.user.mapper.UserInfoMapper;
import com.central.user.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.io.*;
import java.util.*;


/*
 * 用户基础信息表
 *
 * @author wangweijun
 * @date 2020-04-09 14:21:39
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends SuperServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {


    private final static String LOCK_KEY_USERNAME = CommonConstant.LOCK_KEY_PREFIX + "username:";

    @Autowired
    private RedisRepository redisRepository;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private DistributedLock lock;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginAppUser findByUsername(String username) {
        UserInfo userInfo = this.byUsername(username);
        return getLoginAppUser(userInfo);
    }

    private UserInfo getUser(List<UserInfo> users) {
        UserInfo user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    @Override
    public LoginAppUser findByOpenId(String openId) {
        List<UserInfo> users = baseMapper.selectList(
                new QueryWrapper<UserInfo>().eq("open_id", openId)
        );
        return getLoginAppUser(getUser(users));
    }

    @Override
    public LoginAppUser findByMobile(String tngUserPhone) {
        List<UserInfo> users = baseMapper.selectList(
                new QueryWrapper<UserInfo>().eq("tng_user_phone", tngUserPhone)
        );
        return getLoginAppUser(getUser(users));
    }

    @Override
    public LoginAppUser getLoginAppUser(UserInfo userinfo) {
        if (userinfo != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(userinfo, loginAppUser);
            return loginAppUser;
        }
        return null;
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override
    public PageResult<UserInfo> findList(Map<String, Object> params) {
        Page<UserInfo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<UserInfo> list = baseMapper.findList(page, params);
        return PageResult.<UserInfo>builder().data(list).code(0).count(page.getTotal()).build();
    }

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     */
    @Transactional
    @Override
    public Result saveOrUpdateUser(UserInfo userInfo) {
        //        long start, end;
        //        start = System.currentTimeMillis();
        //        log.info("Method@@@@开始时间-------" + start);
        Long user_id = userInfo.getId();
        if (user_id == null) {
            log.info("修改操作失败：用户唯一标识ID不能为空");
            return Result.failed("修改操作失败：用户唯一标识ID不能为空");
        }
//        boolean is_id_falg = user_id == null;
        String accountName = userInfo.getUsername();
//        String password = userInfo.getPassword();
//        String sex = userInfo.getTngUserSex();
//        if (is_id_falg) {
//            if (StringUtils.isEmpty(accountName)) {
//                return Result.failed("用户名不能为空");
//            }
//            if (StringUtils.isEmpty(password)) {
//                return Result.failed("密码不能为空");
//            }
//            userInfo.setPassword(passwordEncoder.encode(password));
//            if (StringUtils.isEmpty(sex)) {
//                return Result.failed("性别不能为空");
//            }
//            long id = SnowFlake.nextId();
//            userInfo.setId(id);
        String phone = userInfo.getTngUserPhone();
        if (StringUtils.isNotEmpty(phone)) {
            userInfo.setTngUserPhone(AESOperator.getInstance().encrypt(phone));
        }
        String idcard = userInfo.getTngUserCredentials();
        if (StringUtils.isNotEmpty(idcard)) {
            userInfo.setTngUserCredentials(AESOperator.getInstance().encrypt(idcard));
        }
//        }
/*        int count = 0;
        if (is_id_falg) {//新增
            //按照身份证划分存储key //分布式ID生成
//            long start1 = 0,end1 =0;
//            start = System.currentTimeMillis();
//            log.info("开始时间ID-------"+start1);
            long id = SnowFlake.nextId();
//            log.info("开始时间ID-------"+end1);
//            log.info("Run Time:ID ------->" + (end1 - start1) + "(ms)");
            userInfo.setId(id);
            count = userInfoMapper.insert(userInfo);
        } else {
            count = userInfoMapper.updateById(userInfo);
        }
        boolean insert_update_flag = count > 0;*/
        boolean insert_update_flag;
        try {
            insert_update_flag = super.saveOrUpdateIdempotency(userInfo, lock, LOCK_KEY_USERNAME + accountName, new QueryWrapper<UserInfo>().eq("tng_account_name", accountName), "该用户名已存在");
            HashMap<String, Object> map = formateData(userInfo);
//            if (insert_update_flag) {
            //新增成功写入缓存
//            long start1 = 0,end1 =0;
//            log.info("开始时间ID-------"+start1);
//            redisRepository.setExpire("user_" + userInfo.getTngAccountName(), userInfo, 90);
//            log.info("开始时间ID-------"+end1);
//            log.info("Run Time: Redis------->" + (end1 - start1) + "(ms)");
//                HashOperations<String, String, Object> hashOperations = redisRepository.getRedisTemplate().opsForHash();
//                hashOperations.putAll("user_" + userInfo.getUsername(), map);
//            } else {
            //删除缓存
//            redisRepository.getRedisTemplate().delete("user_"+userInfo.getTngUserName());
            //hash存储
            redisRepository.getRedisTemplate().opsForHash().delete("user_" + userInfo.getTngUserName(), map);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户信息修改失败：" + e.getMessage());
            return Result.failed("服务器异常，修改操作失败");
        }
//        end = System.currentTimeMillis();
//        log.info("Method@@@@结束时间---------" + end);
//        log.info("Method@@@@ Run Time:" + userInfo.getTngAccountName() + "------->" + (end - start) + "(ms)");
        return insert_update_flag ? Result.succeed("修改操作成功") : Result.failed("修改操作失败");
    }

    @Transactional
    @Override
    public Result register(UserInfo userInfo) {
        String accountName = userInfo.getUsername();
        if (StringUtils.isEmpty(accountName)) {
            return Result.failed("用户名不能为空");
        }
        String password = userInfo.getPassword();
        if (StringUtils.isEmpty(password)) {
            return Result.failed("密码不能为空");
        }
        userInfo.setPassword(
                passwordEncoder.encode(password));
        String sex = userInfo.getTngUserSex();
        if (StringUtils.isEmpty(sex)) {
            return Result.failed("性别不能为空");
        }
        long id = SnowFlake.nextId();
        userInfo.setId(id);
        String phone = userInfo.getTngUserPhone();
        if (StringUtils.isNotEmpty(phone)) {
            userInfo.setTngUserPhone(AESOperator.getInstance().encrypt(phone));
        }
        String idcard = userInfo.getTngUserCredentials();
        if (StringUtils.isNotEmpty(idcard)) {
            userInfo.setTngUserCredentials(AESOperator.getInstance().encrypt(idcard));
        }
        boolean insert_update_flag;
        try {
            insert_update_flag = super.saveOrUpdateIdempotency(userInfo, lock, LOCK_KEY_USERNAME + accountName, new QueryWrapper<UserInfo>().eq("username", accountName), "该用户名已存在");
            HashMap<String, Object> map = formateData(userInfo);
            if (insert_update_flag) {
                HashOperations<String, String, Object> hashOperations = redisRepository.getRedisTemplate().opsForHash();
                hashOperations.putAll("user_" + userInfo.getUsername(), map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户信息注册失败：" + e.getMessage());
            return Result.failed("服务器异常，注册操作失败");
        }
        return insert_update_flag ? Result.succeed("注册操作成功") : Result.failed("注册操作失败");
    }

    @Override
    public UserInfo getUserById(Long id) {
        UserInfo info = new UserInfo();
        //从缓存中获取
        boolean flag = false;
        Map<Object, Object> map = redisRepository.getRedisTemplate().opsForHash().entries("user_" + id.toString());
        flag = map.isEmpty();
        if (!flag) {
            log.info("通过缓存获取信息");
//            Map<Object,Object> map =redisRepository.getRedisTemplate().opsForHash().entries("user_"+id.toString());
//            Map<String,Object> map = redisRepository.getHashValue("shanxi");
//            Object o = redisRepository.get(id.toString());
            info = JSONObject.parseObject(JSONObject.toJSONString(map), UserInfo.class);
//            BeanUtils.copyProperties(o,info);
        } else {
            log.info("从数据库获取信息");
//            List<UserInfo> infos = userInfoMapper.selectList(new QueryWrapper<UserInfo>().eq("username",username));
            info = userInfoMapper.selectById(id);
//            if(infos != null && infos.size()>0){
//                info = infos.get(0);
//            }
            if (info == null) {
                return null;
            }
            //写入redis
            HashMap<String, Object> hamap = formateData(info);
            HashOperations<String, String, Object> hashOperations = redisRepository.getRedisTemplate().opsForHash();
            hashOperations.putAll("user_" + info.getId().toString(), hamap);
        }
        return info;
    }

    @Override
    public Result selectByUsername(String username) {
        UserInfo info = new UserInfo();
        //从缓存中获取
        boolean flag = false;
        //防止缓存击穿
        synchronized (this) {
//             flag = redisRepository.getRedisTemplate().hasKey("user_"+username);
            Map<Object, Object> map = redisRepository.getRedisTemplate().opsForHash().entries("user_" + username);
            flag = map.isEmpty();
            if (!flag) {
                log.info("通过缓存获取信息");
                info = JSONObject.parseObject(JSONObject.toJSONString(map), UserInfo.class);
            } else {
                log.info("从数据库获取信息");
                List<UserInfo> infos = userInfoMapper.selectList(new QueryWrapper<UserInfo>().eq("username", username));
                if (infos != null && infos.size() > 0) {
                    info = infos.get(0);
                    String phone = info.getTngUserPhone();
                    if (StringUtils.isNotEmpty(phone)) {
                        info.setTngUserPhone(AESOperator.getInstance().decrypt(phone));
                    }
                    String idCard = info.getTngUserCredentials();
                    if (StringUtils.isNotEmpty(idCard)) {
                        info.setTngUserCredentials(AESOperator.getInstance().decrypt(idCard));
                    }
                }
                if (info == null) {
                    log.error("用户名获取用户信息：该用户不存在");
                    return Result.failed(info, "该用户不存在");
                }
//                redisRepository.setExpire("user_"+username,info,90);
                HashMap<String, Object> hamap = formateData(info);
                HashOperations<String, String, Object> hashOperations = redisRepository.getRedisTemplate().opsForHash();
                hashOperations.putAll("user_" + info.getUsername(), hamap);
            }
        }
        return Result.succeed(info, "获取用户信息成功");
    }

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    public UserInfo byUsername(String username) {
        UserInfo info = new UserInfo();
        //从缓存中获取
        boolean flag = false;
        //防止缓存击穿
        synchronized (this) {
//             flag = redisRepository.getRedisTemplate().hasKey("user_"+username);
            Map<Object, Object> map = redisRepository.getRedisTemplate().opsForHash().entries("user_" + username);
            flag = map.isEmpty();
            if (!flag) {
                log.info("通过缓存获取信息");
                info = JSONObject.parseObject(JSONObject.toJSONString(map), UserInfo.class);
            } else {
                log.info("从数据库获取信息");
                List<UserInfo> infos = userInfoMapper.selectList(new QueryWrapper<UserInfo>().eq("username", username));
                if (infos != null && infos.size() > 0) {
                    info = infos.get(0);
                    String phone = info.getTngUserPhone();
                    if (StringUtils.isNotEmpty(phone)) {
                        info.setTngUserPhone(AESOperator.getInstance().decrypt(phone));
                    }
                    String idCard = info.getTngUserCredentials();
                    if (StringUtils.isNotEmpty(idCard)) {
                        info.setTngUserCredentials(AESOperator.getInstance().decrypt(idCard));
                    }
                }
//                redisRepository.setExpire("user_"+username,info,90);
                HashMap<String, Object> hamap = formateData(info);
                HashOperations<String, String, Object> hashOperations = redisRepository.getRedisTemplate().opsForHash();
                hashOperations.putAll("user_" + info.getUsername(), hamap);
            }
        }
        return info;
    }

    @Override
    public Result passwordReset(String username, String oldPasswrd, String newPassword) {
        UserInfo user = byUsername(username);
        if (user != null) {
            if (StringUtils.isNotEmpty(oldPasswrd)) {
                if (!passwordEncoder.matches(oldPasswrd, user.getPassword())) {
                    return Result.failed("旧密码错误");
                }
            }
            if (StrUtil.isBlank(newPassword)) {
                newPassword = CommonConstant.DEF_USER_PASSWORD;
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userInfoMapper.updateById(user);
            redisRepository.del("user_" + username);
        }
        return Result.succeed("修改成功");
    }

    @Override
    public Result saveList(List<UserInfo> userInfo) {
        List<UserInfo> users = new ArrayList<UserInfo>();
        StringBuffer msg = new StringBuffer();
        for (UserInfo user : userInfo) {
            if (byUsername(user.getUsername()).getId() != null) {
                msg.append(user.getUsername() + "用户已存在，添加失败。");
            } else {
                user.setId(SnowFlake.nextId());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                users.add(user);
            }
        }
        if (users.size() > 0) {
            userInfoMapper.saveList(users);
        }
        if (StringUtils.isEmpty(msg.toString())) {
            msg.append("添加成功");
            return Result.succeed(msg.toString());
        }
        return Result.failed(msg.toString());
    }

    @Override
    public Result lockUser(UserInfo userInfo) {
        Long user_id = userInfo.getId();
        if (user_id == null) {
            log.info("修改操作失败：用户唯一标识ID不能为空");
            return Result.failed("修改操作失败：用户唯一标识ID不能为空");
        }
        try {
            userInfoMapper.updateById(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("锁定/解锁用户失败" + e.getMessage());
            return Result.failed("锁定/解锁用户失败");
        }
        return Result.succeed("锁定/解锁用户成功");
    }

    public HashMap<String, Object> formateData(UserInfo info) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", info.getId());
        map.put("username", info.getUsername());
        map.put("password", info.getPassword());
        map.put("enabled", info.getEnabled());
        map.put("tngAccountFlag", info.getEnabled());
        map.put("tngUserName", info.getTngUserName());
//        map.put("tngIdType", info.getTngIdType());
        map.put("tngUserSex", info.getTngUserSex());
        String phone = info.getTngUserPhone();
        if (StringUtils.isNotEmpty(phone)) {
            map.put("tngUserPhone", AESOperator.getInstance().decrypt(phone));
        }
        String idCard = info.getTngUserCredentials();
        if (StringUtils.isNotEmpty(idCard)) {
            map.put("tngUserCredentials", AESOperator.getInstance().decrypt(idCard));
        }
//        map.put("tngUserType", info.getTngUserType());
//        map.put("tngUserRemark", info.getTngUserRemark());
        return map;
    }



    //获取json
    public static void getJson() {
        try {
            Set<String>s= new HashSet<>();

            InputStream input = new FileInputStream("D:/json.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                   // 打印当前行字符串
                if(line.contains("uid")){
                 String str=line.substring(line.indexOf("uid")+5,line.indexOf("uid")+36);
                    System.out.println(str);
                 s.add(str);
                }

            }
            System.out.println(s.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
