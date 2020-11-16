package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean isOwner(String login, Long id) {
        Optional<User> user = userRepository.findByLogin(login);
        return user
                .map(usr -> usr.getId().equals(id))
                .orElse(false);
    }
}
