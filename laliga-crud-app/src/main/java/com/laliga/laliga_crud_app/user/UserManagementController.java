//package com.laliga.laliga_crud_app.user;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//@RestController
//@RequestMapping("management/api/v1/users")
//public class UserManagementController {
//    private static final List<User> USERS = Arrays.asList(
//            new User(1L,"James Bond"),
//            new User(2L,"Indiana Jones"),
//            new User(3L,"Anna Smith")
//    );
//
//    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
//    public List<User> getAllUsers(){
//        System.out.println("getAllUsers");
//        return USERS;
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('user:write')")
//    public void registerNewUser(@RequestBody User user) {
//        System.out.println("registerNewUser");
//        System.out.println(user);
//    }
//
//    @PutMapping(path = "{userId}")
//    @PreAuthorize("hasAuthority('user:write')")
//    public void updateUser(@PathVariable Long userId, @RequestBody User user) {
//        System.out.println("updateUser");
//        System.out.println(userId + " " + user);
//    }
//
//    @DeleteMapping(path = "{userId}")
//    @PreAuthorize("hasAuthority('user:write')")
//    public void deleteStudent(@PathVariable Long userId) {
//        System.out.println("deleteStudent");
//        System.out.println(userId);
//    }
//}
