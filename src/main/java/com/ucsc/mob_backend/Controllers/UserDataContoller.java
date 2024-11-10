package com.ucsc.mob_backend.Controllers;

import com.ucsc.mob_backend.dto.PrivateInformationDTO;
import com.ucsc.mob_backend.dto.PrivateKeyDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.dto.UserDetailsDTO;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.entity.Userdetails;
import com.ucsc.mob_backend.service.RecoveryDataService;
import com.ucsc.mob_backend.service.UserDetailsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping ("/userdetails")
public class UserDataContoller {

    private final UserDetailsService userDetailsService;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping ("/save")
    public ResponseEntity<SingleLineResponceDTO> saveRecoveryData(@RequestBody @Valid UserDetailsDTO userDetailsDTO,@AuthenticationPrincipal UserData userData) {
        return userDetailsService.saveDetails(userDetailsDTO,userData);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping ("/getMySubscribedData")
    public ResponseEntity<List<Userdetails>> getRecoveryData(@Valid @RequestBody PrivateKeyDTO privateKeyDTO, @AuthenticationPrincipal UserData userData) {
        return userDetailsService.getMyDetails(privateKeyDTO,userData);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping ("/get_using_id")
    public ResponseEntity<Userdetails> getRecoveryData(@RequestParam String id) {
        return userDetailsService.getDetailsbyid(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','DEVELOPER')")
    @GetMapping ("/get")
    public ResponseEntity<List<Userdetails>> getRecoveryData() {
        return userDetailsService.getAllData();
    }


}
