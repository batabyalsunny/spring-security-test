/**
 * 
 */
package ml.bootcode.springsecuritytest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom filter for intercepting JWT token.
 * 
 * Extends OncePerRequestFilter as this includes DB communication for
 * authenticating user and more than one DB communication is not needed.
 * 
 * @author sunnyb
 *
 */
public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;

	/**
	 * @param jwtTokenProvider
	 */
	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Get the token from request header.
		String token = jwtTokenProvider.extractBearerTokenFromRequestHeader(request);

		// Try authentication.
		try {
			if (null != token && jwtTokenProvider.validateToken(token)) {
				// Get authentication object.
				Authentication auth = jwtTokenProvider.getAuthentication(token);

				// Set auth object to security context.
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (AuthenticationException ex) {
			// Clear the security context as user is not authenticated at all.
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
			return;
		}

		// Continue executing next filters.
		filterChain.doFilter(request, response);
	}
}
