package io.github.orionlibs.user.authentication;

import io.github.orionlibs.core.jwt.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter
{
    private final JWTService jwtService;
    private final UserDetailsService userService;


    public JWTFilter(JWTService jwtService, UserDetailsService userService)
    {
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException
    {
        String authHeader = req.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if(jwtService.validateToken(token, userDetails))
                {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(req, res);
    }
}
