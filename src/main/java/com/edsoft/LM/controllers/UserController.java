package com.edsoft.LM.controllers;

import com.edsoft.LM.exception.*;
import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import com.edsoft.LM.service.UserService;
import com.edsoft.LM.validation.UserValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
        try {
            if (!userValidation.existsUser(user)) {
                throw new UserValidationException("There is a user with the same id: " + user.getId() +
                        " or username: " + user.getName());
            }
        } catch (UserValidationException e) {
            log.error("User validation error: {}", e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during user validation: {}", e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }

        return ResponseEntity.ok(userService.singUp(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String password) {
        return ResponseEntity.ok(userService.getAll(name,password));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable Long userId) {
        try {
            if (!userValidation.existsUserById(userId)) {
                throw new UserNotFoundException(userId);
            }
        } catch (UserValidationException e) {
            log.error("User validation error: {}", e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during user validation: {}", e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }

        return ResponseEntity.ok(userService.delete(userId));
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody UserPasswordChangePojo userPasswordChangePojo) {
        try {
            if (!userValidation.existsUserByName(userPasswordChangePojo.name)) {
                throw new UserNotFoundException(userPasswordChangePojo.name);
            }
        } catch (UserNotFoundException e) {
            log.error("User not found error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        try {
            if (!userValidation.oldPasswordCheck(userPasswordChangePojo.name, userPasswordChangePojo.oldPassword)) {
                throw new InvalidOldPasswordException();
            }
        } catch (InvalidOldPasswordException e) {
            log.error("Invalid old password error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        try {
            if (!userValidation.newPasswordCheck(userPasswordChangePojo)) {
                throw new InvalidNewPasswordException();
            }
        } catch (InvalidNewPasswordException e) {
            log.error("Invalid new password error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }


        return ResponseEntity.ok(userService.changePassword(userPasswordChangePojo));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        try {
            if (!userValidation.existsUserByName(user.getName())) {
                throw new UserNotFoundException(user.getName());
            }
        } catch (UserNotFoundException e) {
            log.error("User not found error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        try {
            if (!userValidation.checkLogin(user)) {
                throw new UserLoginCheckException("Login failed");
            }
        } catch (UserLoginCheckException e) {
            log.error("User did not log in error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Wrong password or username please check out again: \n" +
                            "User Name: " + user.getName() + " Password : " + user.getPassword());
        }

        return ResponseEntity.ok(userService.login(user));
    }
}
