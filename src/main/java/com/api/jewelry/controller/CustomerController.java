package com.api.jewelry.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.jewelry.service.Impl.CustomerService;
import com.api.jewelry.ui.model.request.CustomerDto;
import com.api.jewelry.ui.model.request.CustomerRequestModel;
import com.api.jewelry.ui.model.request.UploadProfileRequestModel;
import com.api.jewelry.ui.model.response.CustomerResponseModel;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<List<CustomerResponseModel>> getAllCustomers() {
        List<CustomerResponseModel> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable Long id) {
        CustomerResponseModel customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseModel>> searchCustomers(@RequestParam String query) {
        List<CustomerResponseModel> customers = customerService.searchCustomers(query);
        return ResponseEntity.ok(customers);
    }
    
    @PostMapping
    public ResponseEntity<CustomerResponseModel> createCustomer(@ModelAttribute CustomerRequestModel request) throws IOException {
        System.out.print("================================="+request.getAddress()+"===============");

    	CustomerResponseModel createdCustomer = customerService.createCustomer(request);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseModel> updateCustomer(
            @PathVariable Long id,
            @ModelAttribute CustomerDto.UpdateRequest request) {
        CustomerResponseModel updatedCustomer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/activity")
    public ResponseEntity<Void> updateLastActive(@PathVariable Long id) {
        customerService.updateLastActive(id);
        return ResponseEntity.noContent().build();
    }
    
	@PostMapping(path="/uploadprofile")
	public String uploadProfilePicture(@ModelAttribute UploadProfileRequestModel requestDetail) throws IOException{
		
		String returnValue = customerService.uploadProfilePicture(requestDetail);
		
		return returnValue;
	}
}
