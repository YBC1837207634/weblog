package com.gong.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author asus
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-10-20 10:03:17
* @Entity generator.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




