package com.wjx.web.controller;

import com.wjx.config.annotation.RedisLock;
import com.wjx.config.common.Result;
import com.wjx.web.pojo.User;
import com.wjx.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: dingguo
 * @Date: 2019/8/27 下午3:45
 */
@RestController
public class LockController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity query(@RequestParam String token) {
        Result result = new Result();
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @RedisLock(name = "add_user")
    public ResponseEntity add(@RequestBody User user) {
        userService.addUser();
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
