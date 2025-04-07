package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.KaratRequestModel;
import com.api.jewelry.ui.model.response.KaratResponeModel;

public interface KaratService {

List<KaratResponeModel> getAllKarats();

KaratResponeModel getKaratById(Long id);

KaratResponeModel createKarat(KaratRequestModel karatRequest);

KaratResponeModel updateKarat(Long id, KaratRequestModel karatRequest);

void deleteKarat(Long id);
}