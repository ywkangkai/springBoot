package com.example.travel_project.controller;

import com.example.travel_project.annotation.UserRight;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.user.UserCreateInputDto;
import com.example.travel_project.dto.input.user.UserUpdateputDto;
import com.example.travel_project.dto.output.user.UserOutputDto;
import com.example.travel_project.dto.output.user.UserOutputRestPassword;
import com.example.travel_project.dto.output.user.UserQueryAlloutputDto;
import com.example.travel_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@UserRight(roles = "admin") // 只有admin角色的用户才能访问
public class UserController {
    @Autowired
    UserService userService;
    //分页查询
    @GetMapping("/query")
    public ResponseData<List<UserOutputDto>> query(@RequestParam(value = "pageIndex", defaultValue = "1") Long pageIndex,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
                                                   String username,
                                                   String name,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String mobile) {

        ResponseData<List<UserOutputDto>> responseData = userService.query(pageIndex, pageSize, username, name, email, mobile);
        return responseData;
    }

    //查询所有
    @GetMapping("/queryAll")
    @UserRight(roles = {"admin", "staff"}) // 只有user和staff角色的用户才能访问
    public ResponseData<List<UserQueryAlloutputDto>> queryAll() {
        return userService.queryAll();
    }

    //@PathVariable 用于从 URL 路径中提取动态值，例如 /user/1 中的 1。
    //@RequestParam 用于提取查询参数，例如 /user?id=1。
    //根据ID查询
    @GetMapping("/{id}")
    public ResponseData<UserOutputDto> queryById(@PathVariable Integer id) {
        return userService.queryById(id);
    }

    //创建
    //@Validated会去校验UserCreateInputDto这个类中所有的字段是否符合约束条件
     @PostMapping("/create")
    public ResponseData<UserOutputDto> create(@RequestBody @Validated UserCreateInputDto inputDto) {
         return userService.create(inputDto);
     }

     //修改
    @PostMapping("/update")
    public ResponseData<UserOutputDto> update(@RequestBody UserUpdateputDto inputDto) {
        return userService.update(inputDto);
    }

    //删除
    @DeleteMapping("/delete")
    public ResponseData delete(@RequestParam Integer id) {
        return userService.delete(id);
    }

    //重置密码
    @PostMapping("/resetPassword")
    public ResponseData reset(@RequestBody @Validated UserOutputRestPassword inputDto) {
        return userService.reset(inputDto);
    }

    //查询非逻辑删除的用户
    @GetMapping("/queryAllNotDeleted")
    public ResponseData<List<UserOutputDto>> queryAllNotDeleted() {
        return userService.queryAllNotDeleted();
    }
}
