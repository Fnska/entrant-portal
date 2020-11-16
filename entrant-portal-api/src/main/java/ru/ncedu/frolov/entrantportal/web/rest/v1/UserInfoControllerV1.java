package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.ncedu.frolov.entrantportal.domain.Education;
import ru.ncedu.frolov.entrantportal.domain.PassportData;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.domain.UserData;
import ru.ncedu.frolov.entrantportal.domain.enums.Grade;
import ru.ncedu.frolov.entrantportal.repository.EducationRepository;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;
import ru.ncedu.frolov.entrantportal.service.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/{id}")
@PreAuthorize("hasRole('ROLE_ADMIN') or @authenticationService.isOwner(principal.username, #id)")
@AllArgsConstructor
public class UserInfoControllerV1 {

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final StorageService storageService;
    private static final String ROOT_LOCATION = "./src/files/";

    @GetMapping("/")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @GetMapping("/data")
    public ResponseEntity<UserData> getUserDataById(@PathVariable(name = "id") Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById
                .map(user -> new ResponseEntity<>(user.getUserData(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @PostMapping("/data")
    public ResponseEntity<User> saveUserDataById(@PathVariable(name = "id") Long id,
                                                 @RequestBody UserData userData) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            userData.setId(user.getId());
            userData.setUser(user);
            user.setUserData(userData);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/passport")
    public ResponseEntity<PassportData> getPassportDataById(@PathVariable(name = "id") Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById
                .map(user -> new ResponseEntity<>(user.getPassportData(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @PostMapping("/passport")
    public ResponseEntity<User> savePassportDataById(@PathVariable(name = "id") Long id,
                                                     @RequestBody PassportData passportData) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            passportData.setId(user.getId());
            passportData.setUser(user);
            user.setPassportData(passportData);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/files")
    public ResponseEntity<Set<Education>> listUploadedFiles(@PathVariable(name = "id") Long id) throws IOException {
        Optional<User> userById = userRepository.findOneWithEducationsById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            Set<Education> educations = user.getEducations();
            educations.forEach(edu -> edu.setDocumentPath(MvcUriComponentsBuilder
                            .fromMethodName(UserInfoControllerV1.class, "getUserUploadedFile", id, Paths.get(edu.getDocumentPath()).getFileName().toString())
                            .build().toUri().toString()));
            return new ResponseEntity<>(educations, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getUserUploadedFile(@PathVariable(name = "id") Long id,
                                                        @PathVariable String filename) {
        String pathname = ROOT_LOCATION + id + "/" + filename;
        Resource file = storageService.loadAsResource(pathname);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

    @PostMapping("/files/upload")
    public ResponseEntity<?> saveUserUploadedFileById(@PathVariable(name = "id") Long id,
                                                      @RequestParam("image") MultipartFile multipartFile,
                                                      @RequestParam("grade") Grade grade){
        String pathname = ROOT_LOCATION + id + "/" + multipartFile.getOriginalFilename();
        File file = new File(pathname);
        try {
            storageService.store(multipartFile, file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> userById = userRepository.findOneWithEducationsById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            Education education = new Education();
            education.setDocumentPath(pathname);
            education.setGrade(grade);
            user.addEducation(education);
            educationRepository.save(education);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
