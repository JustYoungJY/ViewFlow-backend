package app.viewflowbackend.services.security;

import app.viewflowbackend.exceptions.InvalidRefreshTokenException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RefreshTokenService {
    // Make it in Redis
    Map<String, Long> refreshTokens = new HashMap<>();

    public String createRefreshToken(Long userId) {
        String refreshToken = UUID.randomUUID().toString();
        refreshTokens.put(refreshToken, userId);
        return refreshToken;
    }

    public void verifyRefreshToken(String refreshToken, Long expectedUserId) {
        Long userId = refreshTokens.get(refreshToken);
        if (userId == null || !userId.equals(expectedUserId)) {
            throw new InvalidRefreshTokenException();
        }

    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokens.remove(refreshToken);
    }

    public Long extractUserIdFromRefreshToken(String refreshToken) {
        return refreshTokens.get(refreshToken);
    }


}
