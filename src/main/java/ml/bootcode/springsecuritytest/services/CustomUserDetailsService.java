/**
 * 
 */
package ml.bootcode.springsecuritytest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ml.bootcode.springsecuritytest.models.User;
import ml.bootcode.springsecuritytest.repositories.UserRepository;

/**
 * @author sunnyb
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);

		if (null == user) {
			throw new UsernameNotFoundException("User Not found");
		}

		return new UserDetailsImpl(user);
	}

}
