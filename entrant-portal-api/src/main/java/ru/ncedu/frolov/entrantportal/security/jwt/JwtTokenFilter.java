package ru.ncedu.frolov.entrantportal.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        String jwt = null;
        if (token != null && token.startsWith("Bearer ")) {
            jwt = token.substring(7);
        }
        try {
            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                Authentication auth = jwtTokenProvider.getAuth(jwt);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtAuthException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(e.getHttpStatus().value()); // 401, 403
            //throw new JwtAuthException("JWT token is invalid");
        }
        chain.doFilter(request, response);
    }
}
