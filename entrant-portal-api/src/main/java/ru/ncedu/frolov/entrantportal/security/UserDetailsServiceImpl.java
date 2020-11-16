package ru.ncedu.frolov.entrantportal.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists"));
        return SecurityUser.fromUser(user);
    }
}
