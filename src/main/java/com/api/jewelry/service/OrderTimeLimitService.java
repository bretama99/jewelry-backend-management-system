package com.api.jewelry.service;

import com.api.jewelry.ui.model.request.OrderTimeLimitRequestModel;
import com.api.jewelry.ui.model.response.OrderTimeLimitResponseModel;

public interface OrderTimeLimitService {
	
	OrderTimeLimitResponseModel saveOrderTimeLimit(OrderTimeLimitRequestModel requestDetail);
	
	OrderTimeLimitResponseModel getOrderTimeLimits();
	
	OrderTimeLimitResponseModel updateOrderTimeLimit(Integer orderTimeLimitId, OrderTimeLimitRequestModel requestDetail);
	
	String deleteOrderTimeLimit(Integer orderTimeLimitId);
	
	OrderTimeLimitResponseModel getOrderTimeLimit(Integer orderTimeLimitId);
	
}
