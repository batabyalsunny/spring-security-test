/**
 * 
 */
package ml.bootcode.springsecuritytest.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ml.bootcode.springsecuritytest.services.CustomUserDetailsService;

/**
 * The JWT token provider
 * 
 * Verify the access token’s signature Extract identity and authorization claims
 * from Access token and use them to create UserContext If Access token is
 * malformed, expired or simply if token is not signed with the appropriate
 * signing key Authentication exception will be thrown
 * 
 * @author sunnyb
 *
 */
@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1Hour

	private CustomUserDetailsService userDetailsService;

	/**
	 * @param userDetailsService
	 */
	public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	protected void init() {
		// Encode the secret key to base64 string.
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	/**
	 * Creates the JWT token.
	 * 
	 * @param username
	 * @param roles
	 * @return
	 */
	public String createToken(String username, List<String> roles) {
		// Set JWT token data parts.
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	/**
	 * Gets the authentication object by using UsernamePasswordAuthenticationToken.
	 * 
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token) {
		// Get user details by username.
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));

		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
	}

	/**
	 * Gets username from JWT token.
	 * 
	 * @param token
	 * @return
	 */
	public String getUsername(String token) {
		// Parse the token body and get the subject(username).
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Extract bearer token from request header.
	 * 
	 * @param request
	 * @return
	 */
	public String extractBearerTokenFromRequestHeader(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (null != token && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}

	/**
	 * Validates the token signature.
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	public boolean validateToken(String token) throws AuthenticationException {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			throw new AuthenticationException(ex.getMessage());
		}
	}
}
