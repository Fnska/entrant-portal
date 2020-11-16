package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.domain.enums.Role;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;
import ru.ncedu.frolov.entrantportal.security.jwt.JwtTokenProvider;
import ru.ncedu.frolov.entrantportal.web.rest.v1.dto.AuthRequestDTO;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(), authRequestDTO.getPassword()));

            User user = userRepository.findByLogin(authRequestDTO.getLogin()).orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
            String token = jwtTokenProvider.createToken(user.getLogin(), user.getRole());

            HashMap<Object, Object> body = new HashMap<>();
            body.put("id", user.getId());
            body.put("login", user.getLogin());
            body.put("token", token);
            body.put("role", user.getRole());

            return ResponseEntity.ok(body);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO authRequestDTO) {
        if (userRepository.existsByLogin(authRequestDTO.getLogin())) {
            return ResponseEntity.badRequest().body("Login is already in use");
        }
        User user = new User();
        user.setLogin(authRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        user.setRole(Role.ENTRANT);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
