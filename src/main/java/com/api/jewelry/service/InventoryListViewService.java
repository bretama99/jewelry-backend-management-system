package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.InventoryListViewSearchRequestModel;
import com.api.jewelry.ui.model.response.InventoryListViewResponse;

public interface InventoryListViewService {

	List<InventoryListViewResponse> getInventeryListView(int page, int limit);

	InventoryListViewResponse getInventoryByInventoryId(long inventoryId);

	List<InventoryListViewResponse> searchInventoryListView(InventoryListViewSearchRequestModel searchDetails, int page,
			int limit);


}
