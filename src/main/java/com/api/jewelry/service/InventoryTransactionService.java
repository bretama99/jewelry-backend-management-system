package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.shared.dto.InventoryItemsDto;
import com.api.jewelry.ui.model.request.InventoryItemsRequestModel;
import com.api.jewelry.ui.model.request.InventorySellPriceRequestModel;
import com.api.jewelry.ui.model.request.InventoryTransactionUpdateRequestModel;
import com.api.jewelry.ui.model.response.CustomerReportResponseModel;
import com.api.jewelry.ui.model.response.TransactionHistoryResponse;

public interface InventoryTransactionService {

	String InsertInventoryItems(List<InventoryItemsRequestModel> inventoryItemsDtos);

	List<InventoryItemsDto> getTransactionHistory(int page, int limit);

	InventoryTransactionUpdateRequestModel updateInventoryTransactionHistory(long inventoryTransactionDetailId, InventoryTransactionUpdateRequestModel newTransactionDetail);

	List<InventoryItemsDto> searchTransactionHistory(long inventoryId, int page, int limit);

	TransactionHistoryResponse getTransactionByInventoryTransactionDetailId(long inventoryTransactionDetailId);

	public void orderPreOrderManagementJob();
	InventorySellPriceRequestModel insertInventorySellPrice(InventorySellPriceRequestModel requestDetail);
	
	List<CustomerReportResponseModel> customerInventoryReport(int page, int limit, String fromDate, String toDate,
	        long inventoryId, String customerId);
	

}
