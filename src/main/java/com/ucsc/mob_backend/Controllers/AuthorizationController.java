package com.ucsc.mob_backend.Controllers;



import com.ucsc.mob_backend.dto.AuthResponceDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.dto.loginDTO;
import com.ucsc.mob_backend.dto.signupDTO;
import com.ucsc.mob_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {


    private final AuthenticationService authenticationService;

    @PostMapping ("/register")
    public ResponseEntity<AuthResponceDTO> Signup(@RequestBody @Valid signupDTO request ) {

        return authenticationService.signup(request);
    }


    @PostMapping ("/login")
    public ResponseEntity<AuthResponceDTO> Login(@RequestBody loginDTO request) {
        return authenticationService.login(request);
    }



    @GetMapping("/mydetails")
    public String getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "No user authenticated";
        }
        return "Authenticated user: " + userDetails.getUsername();
    }
    @GetMapping("/myrole")
    public ResponseEntity<Map<String,String>> getUserRole(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No user authenticated"));
        }
        return ResponseEntity.ok(Map.of("role", userDetails.getAuthorities().iterator().next().getAuthority()));
    }

}
