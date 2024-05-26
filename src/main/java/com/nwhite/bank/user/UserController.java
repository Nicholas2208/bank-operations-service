package com.nwhite.bank.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/email/add/{email}")
    public ResponseEntity<User> addEmail(@PathVariable("email") String email,
                                      Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.addEmail(email, authenticatedUser));
    }

    @PatchMapping("/email/remove/{index}")
    public ResponseEntity<User> removeEmail(@PathVariable("index") int index,
                                         Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.removeEmail(index, authenticatedUser));
    }

    @PatchMapping("/email/edit/{email}/{index}")
    public ResponseEntity<User> editEmail(@PathVariable("email") String email,
                                         @PathVariable("index") int index,
                                         Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.updateEmail(email, index, authenticatedUser));
    }

    @PatchMapping("/phone/add/{phone}")
    public ResponseEntity<User> addPhoneNumber(@PathVariable("phone") String phone,
                                         Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.addPhoneNumber(phone, authenticatedUser));
    }

    @PatchMapping("/phone/remove/{index}")
    public ResponseEntity<User> removePhone(@PathVariable("index") int index,
                                            Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.removePhoneNumber(index, authenticatedUser));
    }

    @PatchMapping("/phone/edit/{phone}/{index}")
    public ResponseEntity<User> editPhone(@PathVariable("phone") String phone,
                                          @PathVariable("index") int index,
                                          Authentication authenticatedUser) {
        return ResponseEntity.ok(userService.updatePhoneNumber(phone, index, authenticatedUser));
    }

}
