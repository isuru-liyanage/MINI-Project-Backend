package com.ucsc.mob_backend.service;

import com.ucsc.mob_backend.dto.PrivateInformationDTO;
import com.ucsc.mob_backend.dto.PrivateKeyDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.dto.UserDetailsDTO;
import com.ucsc.mob_backend.entity.DataSourcingEvents;
import com.ucsc.mob_backend.entity.RecoveryData;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.entity.Userdetails;
import com.ucsc.mob_backend.repository.SourcingEventRepository;
import com.ucsc.mob_backend.repository.UserDetailsRepository;
import com.ucsc.mob_backend.util.EncripterDecripter;
import com.ucsc.mob_backend.util.KeyGenarator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserDetailsService {
    private EncripterDecripter encripterDecripter;
    private UserDetailsRepository userDetailsRepository;
    private SourcingEventRepository sourcingEventRepository;
    private KeyGenarator keygen;

    @Transactional
    public ResponseEntity<SingleLineResponceDTO> saveDetails(UserDetailsDTO userDetailsDTO , UserData userData){

        DataSourcingEvents dataSourcingEvents = sourcingEventRepository.findById(userDetailsDTO.getEvent_id()).orElseThrow(() -> new RuntimeException("Event not found"));
        dataSourcingEvents.incrementCount();
        String privatekey=keygen.generateKey(userData.getUsername(),userData.getSalt());
        SecretKey secretKey =encripterDecripter.generateKey(privatekey);
        Userdetails userdetails = new Userdetails();
        userdetails.setDateOfBirth(userDetailsDTO.getDateOfBirth());
        userdetails.setTimeOfBirth(userDetailsDTO.getTimeOfBirth());
        userdetails.setLocationOfBirth(userDetailsDTO.getLocationOfBirth());
        userdetails.setBloodGroup(userDetailsDTO.getBloodGroup());
        userdetails.setSex(userDetailsDTO.getSex());
        userdetails.setHeight(userDetailsDTO.getHeight());
        userdetails.setEhanicity(userDetailsDTO.getEhanicity());
        userdetails.setEyeColour(userDetailsDTO.getEyeColour());
        userdetails.setUseridencrypted(encripterDecripter.encrypt(String.valueOf(userData.getId()),secretKey));
        userdetails.setDataSourcingEvents(dataSourcingEvents);
        userDetailsRepository.save(userdetails);
        sourcingEventRepository.save(dataSourcingEvents);
        return ResponseEntity.ok(new SingleLineResponceDTO("User Details saved successfully"));
    }

    public ResponseEntity<List<Userdetails>> getMyDetails(PrivateKeyDTO privateKeyDTO, UserData userData) {
        SecretKey secretKey =encripterDecripter.generateKey(privateKeyDTO.getPrivate_key());
        String encryptedUserId = encripterDecripter.encrypt(String.valueOf(userData.getId()),secretKey);

        List<Userdetails> result = userDetailsRepository.findAllByUseridencrypted(encryptedUserId).stream().peek(userdetails -> userdetails.setUseridencrypted(null)).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<List<Userdetails>> getAllData() {
        List<Userdetails> result = userDetailsRepository.findAll().stream().peek(userdetails -> userdetails.setUseridencrypted(null)).toList();
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Userdetails> getDetailsbyid(String id) {
        Userdetails userdetails = userDetailsRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new RuntimeException("User details not found"));
        userdetails.setUseridencrypted(null);
        return ResponseEntity.ok(userdetails);
    }

    public ResponseEntity<SingleLineResponceDTO> deleteDetails(String id, PrivateKeyDTO privateKeyDTO, UserData userData) {
        SecretKey secretKey =encripterDecripter.generateKey(privateKeyDTO.getPrivate_key());
        String encryptedUserId = encripterDecripter.encrypt(String.valueOf(userData.getId()),secretKey);
        Userdetails userdetails = userDetailsRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new RuntimeException("User details not found"));
        if (userdetails.getUseridencrypted().equals(encryptedUserId)) {
            userDetailsRepository.delete(userdetails);
            return ResponseEntity.ok(new SingleLineResponceDTO("User details deleted successfully"));
        } else {
            return ResponseEntity.badRequest().body(new SingleLineResponceDTO("User details not found"));
        }
    }
}
