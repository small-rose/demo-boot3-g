package com.small.rose.demo.modules.knife4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project: demo-boot3-g
 * @Author: 张小菜
 * @Description: [ UserController ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/12/8 周一 22:13
 * @Version: v1.0
 */

@RestController
@RequestMapping("/admin/api")
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {


    @Operation(summary = "获取用户信息")
    @GetMapping("/user/{id}")
    public String getUser(
            @Parameter(description = "用户ID")
            @PathVariable Long id) {
        return "User ID: " + id;
    }

    @Operation(summary = "创建用户")
    @PostMapping("/user")
    public String createUser(
            @Parameter(description = "用户名")
            @RequestParam String username,
            @Parameter(description = "密码")
            @RequestParam String password) {
        return "Create user: " + username;
    }
}
