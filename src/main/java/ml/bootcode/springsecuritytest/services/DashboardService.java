/**
 * 
 */
package ml.bootcode.springsecuritytest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import ml.bootcode.springsecuritytest.repositories.UserRepository;
import ml.bootcode.springsecuritytest.security.JwtTokenProvider;

/**
 * @author sunnyb
 *
 */
@Service
public class DashboardService {

	private UserRepository userRepository;

	private JwtTokenProvider jwtTokenProvider;

	private AuthenticationManager authenticationManager;

	/**
	 * @param userRepository
	 * @param jwtTokenProvider
	 * @param authenticationManager
	 */
	public DashboardService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
	}

	public String signIn(String username, String password) {
		try {

			// Try to authenticate.
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			// Get the roles.
			List<String> roles = userRepository.findByEmail(username).getRoles().stream().map(role -> role.getName())
					.collect(Collectors.toList());

			return jwtTokenProvider.createToken(username, roles);
		} catch (AuthenticationException ex) {
			throw ex;
		}
	}
}
