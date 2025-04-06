package com.DailycodeBuffer.spring_security_6.Config;

import com.DailycodeBuffer.spring_security_6.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public String checkJwtInCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    System.out.println("Cookie found!: " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String temp = checkJwtInCookies(request);
        if(temp.isEmpty()) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            temp = authHeader.substring(7);
        }
        final String jwt = temp;

        final String username = jwtService.extractUserName(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(username != null && authentication == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else{ //go to login page
                response.sendRedirect("/login");
            }
        }
        else{ //go to login page
            response.sendRedirect("/login");
        }


        filterChain.doFilter(request,response);
    }
}
