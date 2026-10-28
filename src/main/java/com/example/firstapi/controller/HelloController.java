package com.example.firstapi.controller;

import com.example.firstapi.NameRequest;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    // ================= 任务一 =================

    // GET 请求：返回纯文本
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World";
    }

    // POST 请求：返回纯文本
    @PostMapping("/hello")
    public String postHello() {
        return "Hello POST World";
    }

    // GET 请求：返回 JSON 格式
    @GetMapping("/hello/json")
    public Map<String, Object> getHelloJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("getMessage", "Hello World");
        response.put("postMessage", "Hello POST World");
        return response;
    }


    // ================= 任务二 =================

    // GET 传参：URL 参数
    @GetMapping("/greet")
    public String greetGet(@RequestParam String name) {
        return "Hello, " + name;
    }

    // POST 传参：表单参数 (Content-Type: application/x-www-form-urlencoded)
    @PostMapping("/greet/form")
    public String greetPostForm(@RequestParam String name) {
        return name + "，nice to meet you!";
    }

    // POST 传参：JSON 格式 (Content-Type: application/json)
    @PostMapping("/greet/json")
    public String greetPostJson(@RequestBody NameRequest request) {
        return request.getName() + "，nice to meet you! (来自JSON)";
    }
}
