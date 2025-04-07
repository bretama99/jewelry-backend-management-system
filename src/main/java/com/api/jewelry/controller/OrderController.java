package com.api.jewelry.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.jewelry.service.OrderService;
import com.api.jewelry.ui.model.request.OrderDetailRequestModel;
import com.api.jewelry.ui.model.request.OrderItemStatusRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.request.UploadOrderDocumentRequestModel;
import com.api.jewelry.ui.model.response.OrderDetailResponseModel;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;



	@GetMapping(path="/{orderId}")
	public OrderDetailResponseModel getOrderByOrderId(@PathVariable long orderId) {

		OrderDetailResponseModel returnValue = orderService.getOrderByOrderId(orderId);
		return returnValue;
	}
	
	@GetMapping(path="search/{orderNumber}/{orderTypeId}")
	public OrderDetailResponseModel getOrdersByOrderNumber(@PathVariable String orderNumber, @PathVariable(required = false) Integer orderTypeId) {

		OrderDetailResponseModel returnValue = orderService.getOrdersByOrderNumber(orderNumber, orderTypeId);
		return returnValue;
	}
	
	@GetMapping
	public List<OrderDetailResponseModel> getOrders(
			@RequestParam(value="searchKey", defaultValue = "") String searchKey, 
			@RequestParam(value="orderTypeId", defaultValue = "") Integer orderTypeId, 
			@RequestParam(value="orderStatusTypeId", defaultValue = "") Integer orderStatusTypeId, 
			@RequestParam(value="fromDate", defaultValue = "") String fromDate, 
			@RequestParam(value="toDate", defaultValue = "") String toDate, 
			@RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value="limit", defaultValue = "25") int limit){
		
		SearchRequestModel searchDetail=new SearchRequestModel();
		searchDetail.setSearchKey(searchKey);
		searchDetail.setOrderTypeId(orderTypeId);
		searchDetail.setOrderStatusTypeId(orderStatusTypeId);
		searchDetail.setFromDate(fromDate);
		searchDetail.setToDate(toDate);
		List<OrderDetailResponseModel> returnValue = orderService.getOrders(searchDetail,page,limit);
		
		return returnValue;
	}
	
	@GetMapping(path="/myorderhistory/{userId}")
	public List<OrderDetailResponseModel> getMyOrderHistory(@PathVariable("userId") String userId, @RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value="limit", defaultValue = "25") int limit){
		
		List<OrderDetailResponseModel> returnValue= orderService.getMyOrderHistory(userId,page,limit);
		
		return returnValue;
	}

	
	
	@GetMapping(path="/search")
	public List<OrderDetailResponseModel> searchOrders(@RequestBody SearchRequestModel searchkeyDetail, @RequestParam(value="page", defaultValue = "1") int page,
								   @RequestParam(value="limit", defaultValue = "25") int limit){
		
		
		List<OrderDetailResponseModel> returnValue = orderService.searchOrders(searchkeyDetail,page,limit);
		
		return returnValue;
	}
	
	@PostMapping(path="/sell")
	public OrderItemStatusRequestModel sellOrders(@RequestBody OrderItemStatusRequestModel sellOrder){
				
		OrderItemStatusRequestModel retunValue = orderService.sellOrders(sellOrder);
		return retunValue;
	}
	
	@PostMapping
	public OrderDetailResponseModel orderItems(@RequestBody OrderDetailRequestModel orderList) {
		
		OrderDetailResponseModel returnValue = orderService.createOrder(orderList);
		
		return returnValue;
	}

}
