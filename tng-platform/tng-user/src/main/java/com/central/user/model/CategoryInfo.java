package com.central.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;

/**
 * @program: tng
 * @description: 用户类别信息
 * @author: Jue
 * @create: 2020-04-22 16:37
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("tng_category_info")
public class CategoryInfo extends Model<CategoryInfo> {

    private long id ;
    private String tngCategoryName;
    private String tngCategoryFlag;
    private Date createTime;
}
