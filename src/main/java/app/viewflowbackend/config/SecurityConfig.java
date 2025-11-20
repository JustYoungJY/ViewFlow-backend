package app.viewflowbackend.config;

import app.viewflowbackend.filters.JwtAuthenticationFilter;
import app.viewflowbackend.handlers.CustomAccessDeniedHandler;
import app.viewflowbackend.handlers.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UnauthorizedEntryPoint unauthorizedHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          UnauthorizedEntryPoint unauthorizedHandler,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http.exceptionHandling(handling -> handling
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/profile/**").authenticated()

                .requestMatchers(HttpMethod.POST, "/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/**").authenticated()

                .anyRequest().permitAll()
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


