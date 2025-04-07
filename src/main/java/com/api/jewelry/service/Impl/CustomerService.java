package com.api.jewelry.service.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.jewelry.exception.AppException;
import com.api.jewelry.exception.CustomerAlreadyExistsException;
import com.api.jewelry.exception.CustomerNotFoundException;
import com.api.jewelry.io.entity.Customer;
import com.api.jewelry.io.repositories.CustomerRepository;
import com.api.jewelry.shared.GenerateRandomString;
import com.api.jewelry.ui.model.request.CustomerDto;
import com.api.jewelry.ui.model.request.CustomerRequestModel;
import com.api.jewelry.ui.model.request.UploadProfileRequestModel;
import com.api.jewelry.ui.model.response.CustomerResponseModel;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<CustomerResponseModel> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    public CustomerResponseModel getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        return mapToResponseDto(customer);
    }
    
    public List<CustomerResponseModel> searchCustomers(String query) {
        return customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
	@Autowired
	GenerateRandomString generateRandomString;

    @Transactional
    public CustomerResponseModel createCustomer(CustomerRequestModel request) throws IOException {
        // Check if customer with same email or ID number already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + request.getEmail() + " already exists");
        }
        
        if (customerRepository.existsByIdNumber(request.getIdNumber())) {
            throw new CustomerAlreadyExistsException("Customer with ID Number " + request.getIdNumber() + " already exists");
        }
        
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIdNumber(request.getIdNumber());
		if(request.getAvatar() != null) {

			String rootDirectory = "F:/images";
			String uploadDir = rootDirectory + "/inventory-item-images/";

			File directory = new File(uploadDir);
			if (!directory.exists()) {
			    directory.mkdirs();
			}

			byte[] bytes = request.getAvatar().getBytes();

			Instant instant = Instant.now();
			long timeStampSeconds = instant.getEpochSecond();
			String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;

			String fileName = request.getAvatar().getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = randomText + "." + extension;

			Path path = Paths.get(uploadDir + newFileName);
			Files.write(path, bytes);

			// Save only the filename to DB
			 customer.setAvatar(newFileName);

//		    customer.setAvatar(newFileName);
		}
        customer.setCreatedAt(LocalDateTime.now());
        customer.setLastActive(LocalDateTime.now());
        
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDto(savedCustomer);
    }
    
    @Transactional
    public CustomerResponseModel updateCustomer(Long id, CustomerDto.UpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        
        // Only update avatar if provided
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            customer.setAvatar(request.getAvatar());
        }
        
        // Update lastActive time
        customer.setLastActive(LocalDateTime.now());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDto(updatedCustomer);
    }
    
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        
        customerRepository.deleteById(id);
    }
    
    @Transactional
    public void updateLastActive(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        customer.setLastActive(LocalDateTime.now());
        customerRepository.save(customer);
    }
    
    private CustomerResponseModel mapToResponseDto(Customer customer) {
        return new CustomerResponseModel(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getIdNumber(),
                customer.getAvatar(),
                customer.getCreatedAt(),
                customer.getLastActive()
        );
    }

	public String uploadProfilePicture(UploadProfileRequestModel requestDetail) throws IOException {
		
		String rootDirectory = "/var/www/html/images";
		
		String uploadDir = rootDirectory + "/customer-profile-images/";
		File directory = new File(uploadDir);
	    if (!directory.exists()){
	        directory.mkdirs();
	    }
		
		String returnValue = "Image not Saved";
		byte[] bytes = requestDetail.getProfilePicture().getBytes();
		
		String fileName = requestDetail.getProfilePicture().getOriginalFilename();
		String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
		Instant instant = Instant.now();
		long timeStampSeconds = instant.getEpochSecond();

		String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;

		String newFileName = randomText + "." +  extention;
		Path path = Paths.get(uploadDir + newFileName);
	    Files.write(path, bytes);
	    
	    Optional<Customer> customer = customerRepository.findById(requestDetail.getId());
		
		if(customer == null) 
			throw new AppException("User not found.");
		
		customer.get().setAvatar(newFileName);

		Customer update = customerRepository.save(customer.get());
		if(update.getAvatar() != null) {
			returnValue = "Profile picture Saved";
		}
		
		return returnValue;
		
	}
}
