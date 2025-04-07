package com.api.jewelry.service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.jewelry.io.entity.KaratEntity;
import com.api.jewelry.io.repositories.KaratRepository;
import com.api.jewelry.service.KaratService;
import com.api.jewelry.ui.model.request.KaratRequestModel;
import com.api.jewelry.ui.model.response.KaratResponeModel;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KaratServiceImpl implements KaratService {

private final KaratRepository karatRepository;

@Autowired
public KaratServiceImpl(KaratRepository karatRepository) {
   this.karatRepository = karatRepository;
}

@Override
public List<KaratResponeModel> getAllKarats() {
   return karatRepository.findAll().stream()
           .map(this::mapToResponse)
           .collect(Collectors.toList());
}

@Override
public KaratResponeModel getKaratById(Long id) {
   KaratEntity karat = karatRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("Karat not found with id: " + id));
   return mapToResponse(karat);
}

@Override
@Transactional
public KaratResponeModel createKarat(KaratRequestModel karatRequest) {
   KaratEntity karat = mapToEntity(karatRequest);
   KaratEntity savedKarat = karatRepository.save(karat);
   return mapToResponse(savedKarat);
}

@Override
@Transactional
public KaratResponeModel updateKarat(Long id, KaratRequestModel karatRequest) {
   if (!karatRepository.existsById(id)) {
       throw new EntityNotFoundException("Karat not found with id: " + id);
   }
   
   KaratEntity karat = mapToEntity(karatRequest);
   karat.setId(id);
   KaratEntity updatedKarat = karatRepository.save(karat);
   return mapToResponse(updatedKarat);
}

@Override
@Transactional
public void deleteKarat(Long id) {
   if (!karatRepository.existsById(id)) {
       throw new EntityNotFoundException("Karat not found with id: " + id);
   }
   karatRepository.deleteById(id);
}

private KaratEntity mapToEntity(KaratRequestModel karatRequest) {
	KaratEntity karat = new KaratEntity();
   karat.setKarat(karatRequest.getKarat());
   karat.setPurity(karatRequest.getPurity());
   karat.setBuyingDiscount(karatRequest.getBuyingDiscount());
   karat.setWeight(karatRequest.getWeight());
   return karat;
}

private KaratResponeModel mapToResponse(KaratEntity karat) {
	KaratResponeModel response = new KaratResponeModel();
   response.setId(karat.getId());
   response.setKarat(karat.getKarat());
   response.setPurity(karat.getPurity());
   response.setBuyingDiscount(karat.getBuyingDiscount());
   response.setWeight(karat.getWeight());
   return response;
}
}