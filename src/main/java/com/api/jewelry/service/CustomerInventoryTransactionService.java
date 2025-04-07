package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.CustomerInventoryTransactionRequestModel;
import com.api.jewelry.ui.model.response.CustomerInventoryTransactionResponseModel;

public interface CustomerInventoryTransactionService {

	CustomerInventoryTransactionResponseModel saveCustomerInventoryTransaction(
			CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail);

	List<CustomerInventoryTransactionResponseModel> getAllCustomerInventoryTransactions(int page, int limit,
			String searchKey, long companyId);

	CustomerInventoryTransactionResponseModel getCustomerInventoryTransaction(long customerInventoryTransactionId);

	CustomerInventoryTransactionResponseModel updateCustomerInventoryTransaction(long customerInventoryTransactionId,
			CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail);

	String deleteCustomerInventoryTransaction(long customerInventoryTransactionId);

	List<CustomerInventoryTransactionResponseModel> getAllCustomerFilteredInventories(int page, int limit,
			String searchKey);

}
