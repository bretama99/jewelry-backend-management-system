package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.PriceRequestModel;
import com.api.jewelry.ui.model.response.PriceResponseModel;

public interface PriceService {

	List<PriceResponseModel> getAllPrices(int page, int limit, long inventoryId);

	PriceResponseModel getPrice(long customerInventorySellPriceId);

	PriceResponseModel savePrice(PriceRequestModel customerInventorySellPriceDetail);

	PriceResponseModel updatePrice(long customerInventorySellPriceId,
			PriceRequestModel customerInventorySellPriceDetail);

	String deletePrice(long customerInventorySellPriceId);

}
