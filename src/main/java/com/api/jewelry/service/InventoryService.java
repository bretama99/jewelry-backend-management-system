
package com.api.jewelry.service;

import java.io.IOException;
import java.util.List;
import com.api.jewelry.ui.model.request.ImportInventoryDataRequestModel;
import com.api.jewelry.ui.model.request.ImportInventoryRequestModel;
import com.api.jewelry.ui.model.request.InventoryCategoryRequestModel;
import com.api.jewelry.ui.model.request.InventoryRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.response.InventoryResponseModel;

public interface InventoryService {

	InventoryResponseModel insertNewInventory(InventoryRequestModel inventoryDetails) throws IOException;

	InventoryResponseModel getInventoryByInventoryId(long inventoryId);

	List<InventoryResponseModel> getInventeryItems(String customerType, int page, int limit);

	InventoryResponseModel updateInventoryItem(long inventoryId, InventoryRequestModel inventoryItem) throws IOException;

	List<InventoryResponseModel> searchInventeryItems(SearchRequestModel searchkeyDetail, int page, int limit);

	String importInventoryItems(ImportInventoryRequestModel importInventoryDetails) throws IOException;

	List<InventoryResponseModel> getSelectedInventeryItems(Long[] inventoryIds);

	String importPatientData(ImportInventoryDataRequestModel requestDetail) throws IOException;

	String importInventoryCategory(InventoryCategoryRequestModel importInventoryCategoryDetails) throws IOException;	

}
