package com.api.jewelry.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.jewelry.ui.model.request.OrderDetailRequestModel;
import com.api.jewelry.ui.model.request.OrderItemStatusRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.request.UploadOrderDocumentRequestModel;
import com.api.jewelry.ui.model.response.OrderDetailResponseModel;

public interface OrderService {

	OrderDetailResponseModel createOrder(OrderDetailRequestModel orderList);

	OrderDetailResponseModel getOrderByOrderId(long orderId);

	List<OrderDetailResponseModel> getOrders(SearchRequestModel searchDetail, int page, int limit);
	OrderItemStatusRequestModel sellOrders(OrderItemStatusRequestModel sellOrder);
	List<OrderDetailResponseModel> searchOrders(SearchRequestModel searchkeyDetail, int page, int limit);
	OrderDetailResponseModel getOrdersByOrderNumber(String orderNumber, Integer orderTypeId);

	List<OrderDetailResponseModel> getMyOrderHistory(String userId, int page, int limit);
	


}
