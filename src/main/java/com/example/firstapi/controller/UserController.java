package com.example.firstapi.controller;

import com.example.firstapi.entity.User;
import com.example.firstapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 注册接口（POST）
    @PostMapping("/auth/register")
    public String register(@RequestBody User user) {
        userService.register(user);
        return "注册成功！";
    }

    // 2. 登录接口（POST）
    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody User user) {
        // 把前台收到的账号密码，交给经理处理
        return userService.login(user.getUsername(), user.getPassword());
    }

    // 3. 获取用户信息接口（GET）
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id, HttpServletRequest request) {
        // 别忘了，我们的 JwtInterceptor 门卫大爷已经把身份信息塞进 request 里了
        Long currentUserId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        // 核心权限控制：如果角色不是 ADMIN，而且查的又不是自己的ID，直接拒绝！
        if (!"ADMIN".equals(role) && !currentUserId.equals(id)) {
            throw new RuntimeException("403 无权限：普通用户只能查看自己的信息！");
        }

        // 放行，去数据库捞数据
        return userService.getUserById(id);
    }
}