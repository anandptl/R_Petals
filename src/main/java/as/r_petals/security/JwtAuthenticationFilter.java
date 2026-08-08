package as.r_petals.security;

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

                mobileNumber =
                        jwtUtil.extractMobileNumber(token);

            } catch (Exception e) {

                System.out.println(
                        "Invalid JWT: " + e.getMessage()
                );
            }
        }

        // Authenticate User

        if (mobileNumber != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                // First validate token
                if (jwtUtil.validateToken(token)) {

                    UserDetails userDetails =userDetailsService.loadUserByUsername(mobileNumber);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities() );

                    authentication.setDetails( new WebAuthenticationDetailsSource() .buildDetails(request));

                    SecurityContextHolder .getContext() .setAuthentication(
                                    authentication );
                }

            } catch (Exception e) {

                System.out.println( "Authentication failed: " + e.getMessage()  );
            }
        }


        filterChain.doFilter(request, response);
    }
}