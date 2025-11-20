package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.auth.LoginRequestDTO;
import app.viewflowbackend.DTO.auth.RefreshTokenRequestDTO;
import app.viewflowbackend.DTO.auth.RegisterRequestDTO;
import app.viewflowbackend.DTO.auth.TokenResponseDTO;
import app.viewflowbackend.services.security.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return new ResponseEntity<>(userService.register(registerRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(userService.login(loginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String refreshToken = refreshTokenRequestDTO.getRefreshToken();
        return new ResponseEntity<>(userService.refresh(refreshToken), HttpStatus.OK);
    }
}
