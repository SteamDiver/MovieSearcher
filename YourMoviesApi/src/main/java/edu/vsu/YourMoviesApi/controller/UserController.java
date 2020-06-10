package edu.vsu.YourMoviesApi.controller;

import edu.vsu.YourMoviesApi.domain.dto.JwtRequest;
import edu.vsu.YourMoviesApi.domain.dto.JwtResponse;
import edu.vsu.YourMoviesApi.domain.dto.UserDTO;
import edu.vsu.YourMoviesApi.service.AuthenticationService;
import edu.vsu.YourMoviesApi.service.MovieUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationService authenticationService;

    private final MovieUserService movieUserService;

    public UserController(AuthenticationService authenticationService, MovieUserService movieUserService) {
        this.authenticationService = authenticationService;
        this.movieUserService = movieUserService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        final String token;
        try {
            token = authenticationService.generateToken(authenticationRequest);
            return ResponseEntity.ok().body(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        try {
            return movieUserService.save(user) != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
