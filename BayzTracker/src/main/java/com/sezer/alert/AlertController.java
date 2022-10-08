package com.sezer.alert;

import com.sezer.exception.UnsupportedCurrencyCreationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController @Data @Slf4j
@RequestMapping(path = "/api/alert")
public class AlertController {
    private final AlertService alertService;
    private final AlertMapper alertMapper;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addAlert(@RequestBody @Valid AlertDTO alertDTO) {
        Alert alert = null;
        try {
            alert = alertService.addAlert(alertMapper.alertDTOToAlert(alertDTO));
        } catch (UnsupportedCurrencyCreationException e) {
            log.error(e.toString());
            return new ResponseEntity<>(alertDTO, HttpStatus.NOT_ACCEPTABLE);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicateKeyException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Duplicate Key Exception: That alert already exist", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(alertMapper.alertToAlertDTO(alert), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteAlert(@RequestParam("id") Long id) {
        if(id == null || id < 0) {
            String message = new InvalidParameterException().getMessage();
            log.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            try {
                alertService.removeAlert(id);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @GetMapping(path = "/getBySymbol")
    public ResponseEntity<?> getBySymbol(@RequestParam("symbol") String symbol) {
        List<AlertDTO> alertDTO = new ArrayList<>();
        if(symbol == null || symbol.strip().equals("")) {
            String message = new InvalidParameterException().getMessage();
            log.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            alertService.getBySymbol(symbol).forEach(e -> alertDTO.add(alertMapper.alertToAlertDTO(e)));
        }
        return new ResponseEntity<>(alertDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<?> getAll() {
        List<AlertDTO> alertDTO = new ArrayList<>();
        alertService.getAllAlerts().forEach(e -> alertDTO.add(alertMapper.alertToAlertDTO(e)));
        return new ResponseEntity<>(alertDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<?> update(@RequestParam("id") Long id, @RequestParam("targetPrice") float targetPrice) {
        AlertDTO alertDTO = null;
        if(id == null || targetPrice <= 0.0f) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        try {
            alertDTO = alertMapper.alertToAlertDTO(alertService.update(id, targetPrice));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Duplicate Key Exception: That alert already exist", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(alertDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "/cancel")
    public ResponseEntity<?> cancel(@RequestParam("id") Long id) {
        AlertDTO alertDTO = null;
        if(id == null) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        try {
            alertService.cancel(id);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Alert canceled", HttpStatus.OK);
    }

    @PatchMapping(path = "/ack")
    public ResponseEntity<?> acknowledge(@RequestParam("id") Long id) {
        AlertDTO alertDTO = null;
        if(id == null) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        try {
            alertService.acknowledge(id);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>("Alert acknowledged", HttpStatus.OK);
    }
}
