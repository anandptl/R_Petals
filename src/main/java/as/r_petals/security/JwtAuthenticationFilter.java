package as.r_petals.security;

import as.r_petals.repository.RevokedTokenRepository;
import as.r_petals.service.CustomUserDetailsService;
import as.r_petals.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        String token = null;
        String mobileNumber = null;

        // Extract JWT
        if (header != null && header.startsWith("Bearer ")) {

            token = header.substring(7);

            try {
                mobileNumber = jwtUtil.extractMobileNumber(token);
            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        // Authenticate User

        if (mobileNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                // JWT validation
                if (!jwtUtil.validateToken(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String jti = jwtUtil.extractJti(token);

                if (revokedTokenRepository.existsById(jti)) {

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("""
                            {
                                "success": false,
                                "message": "Token has been logged out"
                            }
                            """);

                    return;
                }

                // Load User

                UserDetails userDetails = userDetailsService.loadUserByUsername(mobileNumber);

                // Authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {

                System.out.println("Authentication failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}