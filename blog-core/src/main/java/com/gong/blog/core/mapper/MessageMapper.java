package com.gong.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.core.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
* @author asus
* @description 针对表【message】的数据库操作Mapper
* @createDate 2023-11-30 19:00:47
* @Entity generator.entity.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




