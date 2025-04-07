package com.api.jewelry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.jewelry.service.PriceService;
import com.api.jewelry.ui.model.request.PriceRequestModel;
import com.api.jewelry.ui.model.response.PriceResponseModel;


@RestController
@RequestMapping("/price")
public class PriceController {
	
	@Autowired
	PriceService priceService;
	
	@PostMapping
	public PriceResponseModel savePrice(@RequestBody PriceRequestModel priceDetail) {
		PriceResponseModel returnValue = priceService.savePrice(priceDetail);
		return returnValue;
	}
	
	@GetMapping
	public List<PriceResponseModel> getAllPrices(
			@RequestParam(value = "inventoryId", defaultValue = "") long inventoryId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit){
		List<PriceResponseModel> returnValue = priceService.getAllPrices(page,limit,inventoryId);
		return returnValue;
	}
	
	@GetMapping(path = "/{priceId}")
	public PriceResponseModel getPrice(@PathVariable long priceId) {
		PriceResponseModel returnValue = priceService.getPrice(priceId);
		return returnValue;
	}
	
	@PutMapping(path = "/{priceId}")
	public PriceResponseModel updatePrice(@PathVariable long priceId, @RequestBody PriceRequestModel priceDetail) {
		PriceResponseModel returnValue = priceService.updatePrice(priceId, priceDetail);
		return returnValue;
	}
	
	@DeleteMapping(path = "/{priceId}")
	public String deletePrice(@PathVariable long priceId) {
		String returnValue = priceService.deletePrice(priceId);
		return returnValue;
	}

}
