package it.epicode.Circle.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        System.out.println("JwtRequestFilter is executing...");
        final String requestTokenHeader = request.getHeader("Authorization");

        if(shouldNotFilter(request)) {
            chain.doFilter(request, response);
            return;
        }
        String username = null;
        String jwtToken = null;

        // Estrae il token JWT dal header Authorization
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            System.out.println("JWT Token: " + jwtToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                System.out.println("Username from token: " + username);
            } catch (IllegalArgumentException e) {
                request.setAttribute("javax.servlet.error.exception", new AccessDeniedException("Impossibile ottenere il token JWT"));
                request.getRequestDispatcher("/error").forward(request, response);
            } catch (ExpiredJwtException e) {
                request.setAttribute("javax.servlet.error.exception", new AccessDeniedException("Il token JWT è scaduto"));
                request.getRequestDispatcher("/error").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("javax.servlet.error.exception", new AccessDeniedException("Impossibile ottenere il token JWT"));
                request.getRequestDispatcher("/error").forward(request, response);
            }

            } else {
                request.setAttribute("javax.servlet.error.exception", new JwtTokenMissingException("JWT Token is missing"));
                request.getRequestDispatcher("/error").forward(request, response);
                return;
        }

        // Valida il token e configura l'autenticazione nel contesto di sicurezza
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            System.out.println("Authorities from DB: " + userDetails.getAuthorities());

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                boolean valid = jwtTokenUtil.validateToken(jwtToken, userDetails);
                System.out.println("Is token valid? " + valid);

                System.out.println("AUTHORITIES FROM USER: " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                System.out.println("Setting authentication for user: " + userDetails.getUsername());

                System.out.println("Security context set with: " +
                        SecurityContextHolder.getContext().getAuthentication());
            } else {
                System.out.println("INVALID TOKEN: Token validation failed");
            }
        }
        chain.doFilter(request, response);
    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean exclude = EXCLUDED_URLS.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
        System.out.println("Request to '" + path + "' excluded from filter? " + exclude);

        return EXCLUDED_URLS.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/api/public",
            "/api/auth/login",
            "/api/auth/register",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error",
            "/sw.js"
    );


}
