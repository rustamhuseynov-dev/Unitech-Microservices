package com.rustam.unitech.util.jwt;

import com.rustam.unitech.dto.request.RefreshRequest;
import com.rustam.unitech.exception.custom.UserNotFoundException;
import com.rustam.unitech.model.User;
import com.rustam.unitech.repository.UserRepository;
import com.rustam.unitech.service.AuthService;
import com.rustam.unitech.service.user.UserDetailsServiceImpl;
import com.rustam.unitech.util.UtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final UtilService utilService;

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.getTokenFromRequest(request);
        String refreshToken = "";
        if (token != null){
            if (!jwtService.isValidUserIdFromToken(token)) {
               refreshToken = String.valueOf(authService.refreshToken(new RefreshRequest(token)));
            }else {
                refreshToken = token;
            }
        }

        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            String userIdAsUsername = jwtService.getUserIdAsUsernameFromToken(refreshToken);
            log.info("username {}",userIdAsUsername);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userIdAsUsername);

            if (userDetails != null/* && utilService.getCurrentUserByEmailForAllRole(email).isActivated()*/) {
                System.out.println("UserDetails of current user: " + userDetails);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
