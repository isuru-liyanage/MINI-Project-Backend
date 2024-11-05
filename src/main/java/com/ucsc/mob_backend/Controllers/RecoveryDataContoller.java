package com.ucsc.mob_backend.Controllers;

import com.ucsc.mob_backend.dto.PrivateInformationDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.service.RecoveryDataService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping ("/recovery")
public class RecoveryDataContoller {

    private final RecoveryDataService recoveryDataService;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping ("/save")
    public ResponseEntity<SingleLineResponceDTO> saveRecoveryData(@RequestBody @Valid PrivateInformationDTO privateInformationDTO,@AuthenticationPrincipal UserData userData) {
        return recoveryDataService.saveRecoveryData(privateInformationDTO,userData);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping ("/get")
    public ResponseEntity<PrivateInformationDTO> getRecoveryData(@AuthenticationPrincipal UserData userData) {
        return recoveryDataService.getRecoveryData(userData);
    }

}
