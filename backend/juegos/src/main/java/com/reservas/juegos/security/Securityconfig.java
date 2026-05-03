package com.reservas.juegos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración central de Spring Security.
 *
 * Rutas públicas:
 *   POST /api/auth/registro  → cualquiera puede registrarse
 *   POST /api/auth/login     → cualquiera puede hacer login
 *   GET  /api/productos/**   → catálogo público
 *   GET  /api/categorias/**  → categorías públicas
 *
 * Solo ADMIN:
 *   POST/PUT/DELETE /api/productos/**
 *   POST/PUT/DELETE /api/categorias/**
 *   POST/PUT/DELETE /api/caracteristicas/**
 *   /usuarios/**
 *
 * Usuario autenticado (USER o ADMIN):
 *   /api/reservas/**
 *   /api/favoritos/**
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Públicas ────────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/auth/verificar-admin").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/caracteristicas/**").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/busqueda/**").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/disponibilidad/**").permitAll()

                        // ── Solo ADMIN ──────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST,   "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/caracteristicas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/caracteristicas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/caracteristicas/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // ── Autenticado ─────────────────────────────────────────
                        .requestMatchers("/api/reservas/**").authenticated()
                        .requestMatchers("/api/favoritos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** BCrypt con factor de trabajo 12. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /** CORS: en producción reemplaza el "*" por tu dominio real. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}