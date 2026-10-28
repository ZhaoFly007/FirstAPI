package com.example.firstapi.service;

import com.example.firstapi.entity.User;
import com.example.firstapi.mapper.UserMapper;
import com.example.firstapi.security.JwtUtil;
import com.example.firstapi.security.PasswordStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service // 告诉 Spring：这是大堂经理，归你管
public class UserService {

    // 把需要的工具（库管员、加密机、发证机）都拿过来
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordStrategy passwordStrategy;
    @Autowired private JwtUtil jwtUtil;

    // 1. 注册逻辑
    public void register(User user) {
        // 第一步：去冷库查查，这个名字是不是已经有人用了？
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在，换一个吧！");
        }

        // 第二步：把明文密码（如123456）放进加密机，变成乱码
        String encryptedPassword = passwordStrategy.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 第三步：设置默认角色为普通用户
        user.setRole("USER");

        // 第四步：让库管员把数据存进数据库
        userMapper.insert(user);
    }

    // 2. 登录逻辑
    public Map<String, String> login(String username, String rawPassword) {
        // 第一步：去冷库把这个用户找出来
        User user = userMapper.findByUsername(username);

        // 第二步：如果找不到人，或者加密机比对密码不对，直接打回
        if (user == null || !passwordStrategy.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 第三步：密码对了！用 JWT 工具生成一张防伪通行证（Token）
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 第四步：把 Token 装进一个盒子返回给前台
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("message", "登录成功");
        return result;
    }

    // 3. 查询用户信息（供前端展示）
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }
}