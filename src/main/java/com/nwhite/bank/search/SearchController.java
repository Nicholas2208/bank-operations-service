package com.nwhite.bank.search;

import com.nwhite.bank.common.PageResponse;
import com.nwhite.bank.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final UserService userService;

    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserResponse> findByPhoneNumber(
            @PathVariable("phone") String phoneNumber
    ) {
        Optional<UserResponse> userResponseOptional = userService.findByPhoneNumber(phoneNumber);

        return userResponseOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(
            @PathVariable("email") String email
    ) {
        Optional<UserResponse> userResponseOptional = userService.findByEmail(email);

        return userResponseOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/dob/{birthDate}")
    public ResponseEntity<List<UserResponse>> findByBirthDate(
            @PathVariable("birthDate") LocalDate birthDate
    ) {
        return ResponseEntity.ok(userService.findByBirthDate(birthDate));
    }

    @GetMapping("/fio/{fio}")
    public ResponseEntity<PageResponse<UserResponse>> findByFio(
            @PathVariable("fio") String fio,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok( userService.findByFio(page, size, fio));
    }

}
