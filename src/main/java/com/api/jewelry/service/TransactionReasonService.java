package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.TransactionReasonRequestModel;
import com.api.jewelry.ui.model.response.TransactionReasonResponseModel;

public interface TransactionReasonService {

	String deleteTransactionReason(Integer transactionReasonId);

	TransactionReasonResponseModel updateTransactionReason(TransactionReasonRequestModel transactionReasonDetail,
			Integer transactionReasonId);

	TransactionReasonResponseModel getTransactionReason(Integer transactionReasonId);

	List<TransactionReasonResponseModel> getAllTransactionReasons(int page, int limit, String searchKey);

	TransactionReasonResponseModel saveTransctionReason(TransactionReasonRequestModel transactionReasonDetail);

}
