package com.example.firstapi.mapper;

import com.example.firstapi.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // 1. 注册：把用户存进数据库（包含角色）
    // 注意：用 #{} 是 MyBatis 的预编译，能自动防 SQL 注入
    @Insert("INSERT INTO user(username, password, role) VALUES(#{username}, #{password}, #{role})")
    void insert(User user);

    // 2. 登录查岗：根据用户名找用户（因为登录时用户只传了用户名和密码）
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    // 3. 权限校验查岗：根据 ID 找用户（用于查某人信息）
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
}