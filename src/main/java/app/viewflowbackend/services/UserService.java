package app.viewflowbackend.services;

import app.viewflowbackend.DTO.auth.LoginRequest;
import app.viewflowbackend.DTO.auth.RegisterRequest;
import app.viewflowbackend.DTO.auth.TokenResponse;
import app.viewflowbackend.enums.Role;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.repositories.UserRepository;
import app.viewflowbackend.services.security.JwtService;
import app.viewflowbackend.services.security.RefreshTokenService;
import app.viewflowbackend.services.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already exists");
        }

        Viewer viewer = Viewer
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(Role.USER)
                .build();

        userRepository.save(viewer);

        UserDetailsImpl userDetails = new UserDetailsImpl(viewer);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(viewer.getId());

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserDetails userDetails = loadUserByUsername(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetails;
        Viewer viewer = user.getViewer();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(viewer.getId());
        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }


    @Transactional
    public TokenResponse refresh(String refreshToken) {
        Long userId = refreshTokenService.exctractUserIdFromRefreshToken(refreshToken);
        refreshTokenService.verifyRefreshToken(refreshToken, userId);

        Viewer viewer = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserDetailsImpl userDetails = new UserDetailsImpl(viewer);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(userId);

        refreshTokenService.deleteRefreshToken(refreshToken);

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Viewer viewer = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(viewer);
    }

    public Viewer getCurrentUser() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetailsImpl.getViewer();
    }

}
