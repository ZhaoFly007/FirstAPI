package com.example.firstapi.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 如果是浏览器的跨域预检请求（OPTIONS），直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. 从请求头（Header）里找名为 "Authorization" 的字段
        String token = request.getHeader("Authorization");

        // 3. 如果没找到，或者格式不对（必须是 Bearer 开头），直接打回
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401); // 401 代表未授权
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"未登录或凭证缺失，请先登录\"}");
            return false; // false 表示拦截，不往下走了
        }

        try {
            // 4. 去掉 "Bearer " 这 7 个字符，拿到真正的 token 字符串并解析
            String realToken = token.substring(7);
            Claims claims = jwtUtil.parseToken(realToken);

            // 5. 解析成功！把用户 ID 和角色存到 request 域里，后面的 Controller 随时能取
            request.setAttribute("userId", claims.get("userId", Long.class));
            request.setAttribute("role", claims.get("role", String.class));

            return true; // true 表示放行，让请求继续去找 Controller

        } catch (Exception e) {
            // 如果解析报错（比如 token 被篡改了，或者过期了），就打回
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"凭证无效或已过期，请重新登录\"}");
            return false;
        }
    }
}
