package com.api.jewelry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.jewelry.service.KaratService;
import com.api.jewelry.ui.model.request.KaratRequestModel;
import com.api.jewelry.ui.model.response.KaratResponeModel;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/karats")
public class KaratController {

private final KaratService karatService;

@Autowired
public KaratController(KaratService karatService) {
   this.karatService = karatService;
}

@GetMapping
public ResponseEntity<List<KaratResponeModel>> getAllKarats() {
   List<KaratResponeModel> karats = karatService.getAllKarats();
   return ResponseEntity.ok(karats);
}

@GetMapping("/{id}")
public ResponseEntity<KaratResponeModel> getKaratById(@PathVariable Long id) {
	KaratResponeModel karatResponse = karatService.getKaratById(id);
   return ResponseEntity.ok(karatResponse);
}

@PostMapping
public ResponseEntity<KaratResponeModel> createKarat(@Valid @RequestBody KaratRequestModel karatRequest) {
	KaratResponeModel createdKarat = karatService.createKarat(karatRequest);
   return new ResponseEntity<>(createdKarat, HttpStatus.CREATED);
}

@PutMapping("/{id}")
public ResponseEntity<KaratResponeModel> updateKarat(
       @PathVariable Long id,
       @Valid @RequestBody KaratRequestModel karatRequest) {
	KaratResponeModel updatedKarat = karatService.updateKarat(id, karatRequest);
   return ResponseEntity.ok(updatedKarat);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteKarat(@PathVariable Long id) {
   karatService.deleteKarat(id);
   return ResponseEntity.noContent().build();
}
}
