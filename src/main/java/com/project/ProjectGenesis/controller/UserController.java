package com.project.ProjectGenesis.controller;

import com.project.ProjectGenesis.model.User;
import com.project.ProjectGenesis.model.DetailedUser;
import com.project.ProjectGenesis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public void createUser(@RequestBody DetailedUser detailedUser) throws Exception {
        userService.createUser(detailedUser);
    }

    @GetMapping("/user/{id}")
    public User getUserDetail(@PathVariable("id") long id, @QueryParam("detail") boolean detail){
        return userService.getUser(id, detail);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(@QueryParam("detail") boolean detail){
        return userService.getAllUsers(detail);
    }

    @PutMapping("/user")
    public void editUser(@RequestBody User user){
        userService.editUser(user);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
    }
}
