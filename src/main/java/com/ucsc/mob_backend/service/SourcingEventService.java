package com.ucsc.mob_backend.service;

import com.ucsc.mob_backend.dto.EventDTO;
import com.ucsc.mob_backend.dto.PrivateKeyDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.entity.DataSourcingEvents;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.entity.Userdetails;
import com.ucsc.mob_backend.repository.SourcingEventRepository;
import com.ucsc.mob_backend.repository.UserDetailsRepository;
import com.ucsc.mob_backend.util.EncripterDecripter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SourcingEventService {
    private final SourcingEventRepository sourcingEventRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final EncripterDecripter encripterDecripter;
    public ResponseEntity<SingleLineResponceDTO> saveEvent(EventDTO eventDTO) {
        DataSourcingEvents dataSourcingEvents = new DataSourcingEvents();
        dataSourcingEvents.setEvent_name(eventDTO.getEvent_name());
        dataSourcingEvents.setEvent_description(eventDTO.getEvent_description());
        sourcingEventRepository.save(dataSourcingEvents);
        return ResponseEntity.ok(new SingleLineResponceDTO("Data Sourcing Event Saved"));
    }

    public ResponseEntity<List<DataSourcingEvents>> getAllEvents() {
        return ResponseEntity.ok(sourcingEventRepository.findAll());
    }

    public ResponseEntity<List<EventDTO>> getAllEventswithTag(PrivateKeyDTO privateKeyDTO,UserData userData) {
        SecretKey secretKey =encripterDecripter.generateKey(privateKeyDTO.getPrivate_key());
        String encryptedUserId = encripterDecripter.encrypt(String.valueOf(userData.getId()),secretKey);
        List<DataSourcingEvents> events = sourcingEventRepository.findAll();
        List<EventDTO> eventDTOS = events.stream().map(event -> {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setEvent_name(event.getEvent_name());
            eventDTO.setEvent_description(event.getEvent_description());
            eventDTO.setCount(event.getCount());
            eventDTO.setEvent_id(event.getEvent_id());
            eventDTO.setTag("Open");
            return eventDTO;
        }).toList();

        List<Userdetails> results = userDetailsRepository.findAllByUseridencrypted(encryptedUserId).stream().peek(userdetails -> userdetails.setUseridencrypted(null)).collect(Collectors.toList());
        for (Userdetails userdetails : results) {
            int id=userdetails.getDataSourcingEvents().getEvent_id();
            eventDTOS.stream().filter(eventDTO -> eventDTO.getEvent_id()==id).forEach(eventDTO -> eventDTO.setTag("Subscribed"));
        }
        return ResponseEntity.ok(eventDTOS);
    }
}
