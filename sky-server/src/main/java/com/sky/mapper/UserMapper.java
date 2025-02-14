package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid=#{openid}")
    public User getUserByOpenid(String openid);

    public void insertUser(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long id);

    Integer countByMap(Map map);
}
