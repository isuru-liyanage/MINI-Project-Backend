package com.ucsc.mob_backend.service;

import com.ucsc.mob_backend.dto.PrivateInformationDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.entity.RecoveryData;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.repository.RecoveryDataRepository;
import com.ucsc.mob_backend.repository.UserRepository;
import com.ucsc.mob_backend.util.EncripterDecripter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


@Service
public class RecoveryDataService {
    @Autowired
    private EncripterDecripter encripterDecripter;
    @Autowired
    private RecoveryDataRepository recoveryDataRepository;

    @Value("${app.secret.key}")
    private String secretKeyString;

    public ResponseEntity<SingleLineResponceDTO> saveRecoveryData(PrivateInformationDTO privateInformationDTO ,UserData userData){
        RecoveryData recoveryData = new RecoveryData ();
        SecretKey secretKey =encripterDecripter.generateKey(secretKeyString);
        recoveryData.setFullName(encripterDecripter.encrypt(privateInformationDTO.getFullName(),secretKey));
        recoveryData.setDateOfBirth(encripterDecripter.encrypt(privateInformationDTO.getDateOfBirth(),secretKey));
        recoveryData.setMothersMaidenName(encripterDecripter.encrypt(privateInformationDTO.getMothersMaidenName(),secretKey));
        recoveryData.setChildhoodBestFriendName(encripterDecripter.encrypt(privateInformationDTO.getChildhoodBestFriendName(),secretKey));
        recoveryData.setChildhoodPetName(encripterDecripter.encrypt(privateInformationDTO.getChildhoodPetName(),secretKey));
        recoveryData.setOwnQuestion(encripterDecripter.encrypt(privateInformationDTO.getOwnQuestion(),secretKey));
        recoveryData.setOwnAnswer(encripterDecripter.encrypt(privateInformationDTO.getOwnAnswer(),secretKey));
        recoveryData.setUserID(encripterDecripter.encrypt(String.valueOf(userData.getId()),secretKey));
        recoveryDataRepository.save(recoveryData);

        return ResponseEntity.ok(new SingleLineResponceDTO("Recovery data saved successfully"));
    }

    public ResponseEntity<PrivateInformationDTO> getRecoveryData(UserData userData){
        RecoveryData recoveryData = recoveryDataRepository.findByUserID(encripterDecripter.encrypt(String.valueOf(userData.getId()),encripterDecripter.generateKey(secretKeyString)));
        SecretKey secretKey =encripterDecripter.generateKey(secretKeyString);
        PrivateInformationDTO privateInformationDTO = new PrivateInformationDTO();
        privateInformationDTO.setFullName(encripterDecripter.decrypt(recoveryData.getFullName(),secretKey));
        privateInformationDTO.setDateOfBirth(encripterDecripter.decrypt(recoveryData.getDateOfBirth(),secretKey));
        privateInformationDTO.setMothersMaidenName(encripterDecripter.decrypt(recoveryData.getMothersMaidenName(),secretKey));
        privateInformationDTO.setChildhoodBestFriendName(encripterDecripter.decrypt(recoveryData.getChildhoodBestFriendName(),secretKey));
        privateInformationDTO.setChildhoodPetName(encripterDecripter.decrypt(recoveryData.getChildhoodPetName(),secretKey));
        privateInformationDTO.setOwnQuestion(encripterDecripter.decrypt(recoveryData.getOwnQuestion(),secretKey));
        privateInformationDTO.setOwnAnswer(encripterDecripter.decrypt(recoveryData.getOwnAnswer(),secretKey));
        return ResponseEntity.ok(privateInformationDTO);
    }



}
