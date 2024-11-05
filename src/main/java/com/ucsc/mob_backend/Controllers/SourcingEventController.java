package com.ucsc.mob_backend.Controllers;

import com.ucsc.mob_backend.dto.EventDTO;
import com.ucsc.mob_backend.dto.PrivateKeyDTO;
import com.ucsc.mob_backend.dto.SingleLineResponceDTO;
import com.ucsc.mob_backend.entity.DataSourcingEvents;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.repository.SourcingEventRepository;
import com.ucsc.mob_backend.service.SourcingEventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/sourcingEvent")
public class SourcingEventController {
    private final SourcingEventService sourcingEventService;
    //for admin
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<DataSourcingEvents>> getEvents() {
        return sourcingEventService.getAllEvents();
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/save")
    public ResponseEntity<SingleLineResponceDTO> saveEvent(@RequestBody @Valid EventDTO eventDTO) {
        return sourcingEventService.saveEvent(eventDTO);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/getEvents")
    public ResponseEntity<List<EventDTO>> getEvents(@RequestBody PrivateKeyDTO privateKeyDTO, @AuthenticationPrincipal UserData userData) {
        return sourcingEventService.getAllEventswithTag(privateKeyDTO,userData);
    }


}
