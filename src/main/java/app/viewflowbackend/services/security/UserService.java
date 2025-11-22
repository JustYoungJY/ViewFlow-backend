package app.viewflowbackend.services.security;

import app.viewflowbackend.DTO.auth.LoginRequestDTO;
import app.viewflowbackend.DTO.auth.RegisterRequestDTO;
import app.viewflowbackend.DTO.auth.TokenResponseDTO;
import app.viewflowbackend.DTO.user.UserProfileDTO;
import app.viewflowbackend.DTO.user.UserUpdateRequestDTO;
import app.viewflowbackend.enums.Role;
import app.viewflowbackend.exceptions.EmailAlreadyExistsException;
import app.viewflowbackend.exceptions.InvalidPasswordException;
import app.viewflowbackend.exceptions.UserNotFoundException;
import app.viewflowbackend.exceptions.UsernameAlreadyExistsException;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenService refreshTokenService,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.modelMapper = modelMapper;
    }


    public TokenResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
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

        return TokenResponseDTO
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }


    public TokenResponseDTO login(LoginRequestDTO request) {
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new InvalidPasswordException();
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetails;
        Viewer viewer = user.getViewer();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(viewer.getId());
        return TokenResponseDTO
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }


    public TokenResponseDTO refresh(String refreshToken) {
        Long userId = refreshTokenService.extractUserIdFromRefreshToken(refreshToken);
        refreshTokenService.verifyRefreshToken(refreshToken, userId);

        Viewer viewer = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDetailsImpl userDetails = new UserDetailsImpl(viewer);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(userId);

        refreshTokenService.deleteRefreshToken(refreshToken);

        return TokenResponseDTO
                .builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtService.getAccessExpiration() / 1000)
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Viewer viewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(viewer);
    }


    public void updateProfile(Viewer viewer, UserUpdateRequestDTO request) {
        Optional.ofNullable(request.getAvatarUrl()).ifPresent(viewer::setAvatarUrl);
        Optional.ofNullable(request.getBio()).ifPresent(viewer::setBio);

        userRepository.save(viewer);
    }


    public UserProfileDTO convertToUserProfileDTO(Viewer viewer) {
        return modelMapper.map(viewer, UserProfileDTO.class);
    }

//    public Viewer getCurrentUser() {
//        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal();
//        return userDetailsImpl.getViewer();
//    }

}
