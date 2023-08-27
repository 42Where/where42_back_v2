package kr.where.backend.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class TokenController {

    @GetMapping("/token")
    public String homePage(@RequestParam("token") String token) {
        log.info(token);
        return "token"; // Thymeleaf
    }
}
