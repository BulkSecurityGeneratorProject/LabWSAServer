package com.platform.web.rest;

import com.platform.service.AuditEventService;
import com.platform.web.propertyeditors.LocaleDateTimeEditor;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping(value = "/api/audits", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditResource {

    private AuditEventService auditEventService;

    @Inject
    public AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocaleDateTimeEditor("yyyy-MM-dd", false));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AuditEvent> getAll() {
        return auditEventService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET,
            params = {"fromDate", "toDate"})
    public List<AuditEvent> getByDates(@RequestParam(value = "fromDate") LocalDateTime fromDate,
                                       @RequestParam(value = "toDate") LocalDateTime toDate) {
        return auditEventService.findByDates(fromDate, toDate);
    }

    @RequestMapping(value = "/{id:.+}",
            method = RequestMethod.GET)
    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        AuditEvent event = auditEventService.find(id);
        if(event != null){
            return new ResponseEntity<AuditEvent>(event, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
}
