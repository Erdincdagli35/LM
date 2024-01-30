package com.edsoft.LM.controllers;

import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import com.edsoft.LM.service.UserService;
import com.edsoft.LM.validation.UserValidation;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@EnableAutoConfiguration
@CrossOrigin
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;

    @PostMapping("/singUp")
    public ResponseEntity singUp(@RequestBody User user) {
        if (!userValidation.existsUser(user)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is a user with same id : " + user.getId() + " or username: " + user.getName());
        }

        return ResponseEntity.ok(userService.singUp(user));
    }

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable Long userId) {
        if (!userValidation.existsUserById(userId)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is no user : userId : " + userId);
        }

        return ResponseEntity.ok(userService.delete(userId));
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody UserPasswordChangePojo userPasswordChangePojo) {
        if (!userValidation.existsUserByName(userPasswordChangePojo.name)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is no a user  : username: " + userPasswordChangePojo.name);
        }
        if (!userValidation.oldPasswordCheck(userPasswordChangePojo.name, userPasswordChangePojo.oldPassword)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("The old password did not match by entered.");
        }
        if (!userValidation.newPasswordCheck(userPasswordChangePojo)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("The old password did not match by entered.");
        }


        return ResponseEntity.ok(userService.changePassword(userPasswordChangePojo));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if (!userValidation.existsUserByName(user.getName())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There is no a user  : username: " + user.getName());
        }
        if (!userValidation.checkLogin(user)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wrong password or username please check out again: \n" +
                            "User Name: " + user.getName() + " Password : " + user.getPassword());
        }

        return ResponseEntity.ok(userService.login(user));
    }
}
