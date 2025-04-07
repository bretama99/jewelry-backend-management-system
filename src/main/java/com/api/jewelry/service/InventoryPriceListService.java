package com.api.jewelry.service;

import com.api.jewelry.ui.model.response.InventorySellPriceDetail;

public interface InventoryPriceListService {

	InventorySellPriceDetail getInventoryItemPriceList(long inventoryId, Integer page, Integer limit);

}
