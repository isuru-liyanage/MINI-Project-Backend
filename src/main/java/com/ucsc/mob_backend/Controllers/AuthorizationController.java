package com.ucsc.mob_backend.Controllers;



import com.ucsc.mob_backend.dto.*;
import com.ucsc.mob_backend.entity.RecoveryData;
import com.ucsc.mob_backend.entity.UserData;
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
    @PostMapping("/changePassword")
    public ResponseEntity<SingleLineResponceDTO> changePassword(@RequestBody ChangePwDTO request ,@AuthenticationPrincipal UserData userData) {
        return authenticationService.changepassword(userData,request);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<SingleLineResponceDTO> resetPassword(@RequestBody ResetPwDTO request ) {
        return authenticationService.resetpassword(request);
    }




    @GetMapping("/mydetails")
    public Map<String,String> getUserDetails(@AuthenticationPrincipal UserData userDetails) {
        if (userDetails == null) {
            return Map.of("message", "No user authenticated");
        }
        return Map.of("email", userDetails.getUsername());
    }
    @GetMapping("/myrole")
    public ResponseEntity<Map<String,String>> getUserRole(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No user authenticated"));
        }
        return ResponseEntity.ok(Map.of("role", userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/recover_account")
    public ResponseEntity<SingleLineResponceDTO> recoverAccount(@RequestBody @Valid RecoveryData recoverAccountDTO) {
        return authenticationService.recoverAcc(recoverAccountDTO);
    }

}
