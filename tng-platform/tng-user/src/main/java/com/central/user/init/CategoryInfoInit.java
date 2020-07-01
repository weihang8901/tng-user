package com.central.user.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.user.model.CategoryInfo;
import com.central.user.mapper.CategoryInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;




/**
 * @program: tng
 * @description: 初始化加载用户类别
 * @author: Jue
 * @create: 2020-04-24 09:15
 **/
@Component
@Slf4j
public class CategoryInfoInit {
    //获取类别
    @Autowired
    private CategoryInfoMapper categoryInfoMapper;

    public static Map<Long,String> categoryMap = new ConcurrentHashMap<Long,String>();

    @PostConstruct
    public void init() {
        try{
//            CategoryInfo categoryInfo = new CategoryInfo();
            List<CategoryInfo> list = categoryInfoMapper.selectList(new QueryWrapper<CategoryInfo>().eq("tng_category_flag",true));
            categoryMap = list.stream().collect(Collectors.toMap(CategoryInfo::getId,CategoryInfo::getTngCategoryName));
            log.info("init category:"+ categoryMap.toString());
        }catch (Exception e){
            log.error("init category is failure");
        }

    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

//    @Scheduled(cron = "0 0 0/2 * * ?")
    public void testOne() {
        //每2小时执行一次缓存
        init();
    }

  /*  @Override
    public void run(String... args) throws Exception {
        try{
//            CategoryInfo categoryInfo = new CategoryInfo();
            List<CategoryInfo> list = categoryInfoMapper.selectList(new QueryWrapper<CategoryInfo>().eq("tng_category_flag",true));
            categoryMap = list.stream().collect(Collectors.toMap(CategoryInfo::getId,CategoryInfo::getTngCategoryName));
            log.info("init category:"+ categoryMap.toString());
        }catch (Exception e){
            log.error("init category is faile");
        }

    }*/
}
