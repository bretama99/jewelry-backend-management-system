package com.api.jewelry.service.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.jewelry.exception.AppException;
import com.api.jewelry.io.entity.CategoryEntity;
import com.api.jewelry.io.entity.CustomerInventoryTransactionEntity;
import com.api.jewelry.io.entity.CustomerPurchasedInventoryEntity;
import com.api.jewelry.io.entity.InventoryCostPriceListEntity;
import com.api.jewelry.io.entity.InventoryEntity;
import com.api.jewelry.io.entity.InventorySellPriceListEntity;
import com.api.jewelry.io.entity.KaratEntity;
import com.api.jewelry.io.entity.PriceEntity;
import com.api.jewelry.io.entity.TransactionEntity;
import com.api.jewelry.io.repositories.CategoryRepository;
import com.api.jewelry.io.repositories.PriceRepository;
import com.api.jewelry.io.repositories.CustomerInventoryTransactionRepository;
import com.api.jewelry.io.repositories.CustomerPurchasedInventoryRepository;
import com.api.jewelry.io.repositories.InventoryCostPriceListRepository;
import com.api.jewelry.io.repositories.InventoryRepository;
import com.api.jewelry.io.repositories.InventorySellPriceListRepository;
import com.api.jewelry.io.repositories.KaratRepository;
import com.api.jewelry.io.repositories.TransactionRepository;
import com.api.jewelry.service.InventoryService;
import com.api.jewelry.shared.GenerateRandomString;
import com.api.jewelry.ui.model.request.ImportInventoryDataRequestModel;
import com.api.jewelry.ui.model.request.ImportInventoryRequestModel;
import com.api.jewelry.ui.model.request.InventoryCategoryRequestModel;
import com.api.jewelry.ui.model.request.InventoryRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.response.InventoryResponseModel;
import com.api.jewelry.ui.model.response.KaratResponeModel;
import com.api.jewelry.ui.model.response.PriceResponseModel;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	PriceRepository customerInventorySellPriceRepository;
	
	@Value("${file.upload-dir}")
    private String uploadDirectory;
	
	@Autowired
	GenerateRandomString generateRandomString;
	
	@Autowired
	TransactionRepository inventoryTransactionDetailRepository;
	
	@Autowired
	CustomerPurchasedInventoryRepository customerPurchasedInventoryRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Autowired
	PriceRepository priceRepository;
	
	@Autowired
	KaratRepository karatRepository;
	
	@Override
	public InventoryResponseModel insertNewInventory(InventoryRequestModel inventoryDetails) throws IOException {

		
		InventoryResponseModel returnvalue = new InventoryResponseModel();
		InventoryEntity inventoryEntity = new InventoryEntity();
		BeanUtils.copyProperties(inventoryDetails, inventoryEntity);
		
		if (inventoryDetails.getProductImage() != null) {
//			String rootDirectory = new File("").getAbsolutePath();
			String rootDirectory = "New projects/upworkkk/vuetify/gold/src/assets/images";
			//			String rootDirectory = "C:/wamp64/www/images";
			
			String uploadDir = rootDirectory + "/inventory-item-images/";
			File directory = new File(uploadDir);
		    if (!directory.exists()){
		        directory.mkdirs();
		    }
					    
			byte[] bytes = inventoryDetails.getProductImage().getBytes();
			Instant instant = Instant.now();
			long timeStampSeconds = instant.getEpochSecond();
			String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;
			
			String fileName = inventoryDetails.getProductImage().getOriginalFilename();
			String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = randomText + "." +  extention;
			
			Path path = Paths.get(uploadDir + newFileName);
		    Files.write(path, bytes);
		    
			inventoryEntity.setProductImage(newFileName);
		}
		
		InventoryEntity StoredInventoryDetail = inventoryRepository.save(inventoryEntity);
		
		BeanUtils.copyProperties(StoredInventoryDetail, returnvalue);
		
		return returnvalue;
	}

	@Override
	public InventoryResponseModel getInventoryByInventoryId(long inventoryId) {
		
		InventoryResponseModel returnvalue = new InventoryResponseModel();
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryId);
		
		if(inventoryEntity == null) throw new AppException("Inventory Item not found.");
		
		BeanUtils.copyProperties(inventoryEntity, returnvalue);
		
		return returnvalue;
	}

	@Override
	public List<InventoryResponseModel> getInventeryItems(String customerType, int page, int limit) {
		
		List<InventoryResponseModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("inventoryId").descending());
	    
	    Page<InventoryEntity> inventoryPage;
		    inventoryPage = inventoryRepository.findAll(pageableRequest);
	    int totalPages = inventoryPage.getTotalPages();
	    List<InventoryEntity> inventoryItems = inventoryPage.getContent();
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	InventoryResponseModel inventoryRequestModel = new InventoryResponseModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
	    	PriceEntity priceEntity = priceRepository.findTopByInventoryId(inventoryEntity.getInventoryId());
	    	PriceResponseModel priceResponseModel = new PriceResponseModel();
	    	CategoryEntity categoryEntity=categoryRepository.findByCategoryIdAndIsDeleted(inventoryEntity.getCategoryId(), false);
	    	if(categoryEntity!=null) {
	    		inventoryRequestModel.setCategory(categoryEntity.getCategory());
	    	}
	    	if(priceEntity!=null) {
	    		BeanUtils.copyProperties(priceEntity, priceResponseModel);
	    		inventoryRequestModel.setPriceResponseModel(priceResponseModel);
	    		Optional<KaratEntity> karatEntity = karatRepository.findById(priceEntity.getKaratId());
	    		KaratResponeModel karatResponeModel = new KaratResponeModel();
	    		if(karatEntity!=null) {
	    			BeanUtils.copyProperties(karatEntity.get(), karatResponeModel);
	    			inventoryRequestModel.setKaratResponeModel(karatResponeModel);
	    		}
	    	}
	    	if(returnValue.size() == 0) {
	    		inventoryRequestModel.setTotalPages(totalPages);
	    	}
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
		
	}

	@Override
	public InventoryResponseModel updateInventoryItem(long inventoryId, InventoryRequestModel inventoryItem) throws IOException {
		
		InventoryResponseModel returnValue = new InventoryResponseModel();
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryId);
		
		if(inventoryEntity == null) 
			throw new RuntimeException("Inventory Item not found.");
		
		BeanUtils.copyProperties(inventoryItem, inventoryEntity);
		
		if(inventoryItem.getProductImage() != null) {
			
			//String rootDirectory = new File("").getAbsolutePath();
			String rootDirectory = "New projects/upworkkk/vuetify/gold/src/assets/images";
			//			String rootDirectory = "C:/wamp64/www/images";
			String uploadDir = rootDirectory + "/inventory-item-images/";
			File directory = new File(uploadDir);
		    if (!directory.exists()){
		        directory.mkdirs();
		    }
		    
			byte[] bytes = inventoryItem.getProductImage().getBytes();
			Instant instant = Instant.now();
			long timeStampSeconds = instant.getEpochSecond();
			String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;
			
			String fileName = inventoryItem.getProductImage().getOriginalFilename();
			String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = randomText + "." +  extention;
			
			Path path = Paths.get(uploadDir + newFileName);
		    Files.write(path, bytes);
		    
			inventoryEntity.setProductImage(newFileName);
		}
		
		InventoryEntity updatedInventoryItem = inventoryRepository.save(inventoryEntity);
		
		BeanUtils.copyProperties(updatedInventoryItem, returnValue); 
		return returnValue;
	}

	@Override
	public List<InventoryResponseModel> searchInventeryItems(SearchRequestModel searchkeyDetail, int page, int limit) {
		
		String searchKey = searchkeyDetail.getSearchKey();
		List<InventoryResponseModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit);
	    Page<InventoryEntity> inventoryPage;
	    List<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntities = new ArrayList<>();
	    if("customer".equalsIgnoreCase(searchkeyDetail.getCustomerType())){
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	customerPurchasedInventoryEntities = customerPurchasedInventoryRepository.findByCustomerIdAndIsDeleted(auth.getName(), false);
	    	List<Long> inventoryIds = customerPurchasedInventoryEntities.stream().map(i->i.getInventoryId()).collect(Collectors.toList());
	    	inventoryPage = inventoryRepository.findByProductNameContainingOrDescriptionContaining(searchKey,searchKey,pageableRequest);
	    } else {
	    	inventoryPage = inventoryRepository.findByProductNameContainingOrDescriptionContaining(searchKey,searchKey,pageableRequest);
	    }
	    int totalPages = inventoryPage.getTotalPages();
	    List<InventoryEntity> inventoryItems = inventoryPage.getContent();
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	//int size = returnValue.size();
	    	InventoryResponseModel inventoryRequestModel = new InventoryResponseModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
		    if("customer".equalsIgnoreCase(searchkeyDetail.getCustomerType())){
		    	Optional<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntity = customerPurchasedInventoryEntities.stream().filter(cpi->cpi.getInventoryId() == inventoryEntity.getInventoryId()).findFirst();
		    	if(customerPurchasedInventoryEntity.isPresent()) {
		    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    		
		    	}
		    }
//	    	if(returnValue.size() == 0) {
//	    		inventoryRequestModel.setTotalPages(totalPages);
//	    	}
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
	}


	@Override
	public String importInventoryItems(ImportInventoryRequestModel importInventoryDetails) throws IOException {
		
		String returnValue = "";
		XSSFWorkbook workbook = new XSSFWorkbook(importInventoryDetails.getImportExcel().getInputStream());
	    XSSFSheet worksheet = workbook.getSheetAt(0);
	    int i;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

	    	InventoryEntity inventoryEntity = new InventoryEntity();
	    	
	        XSSFRow row = worksheet.getRow(i);
	        
//			//-----------------------------------------Save Inventory Items----------------------------------------------------------------
//	        	if(row.getCell(0,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setInventoryGenericName(row.getCell(0).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(1,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setDosageForm(row.getCell(1).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(2,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setStrength(row.getCell(2).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(3,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setVolume(row.getCell(3).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(4,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setInventoryType(row.getCell(4).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(5,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setSubCategory(row.getCell(5).getStringCellValue());
//		        }
//		        
//		        if(row.getCell(6,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//		        	inventoryEntity.setInventoryBrandName(row.getCell(6).getStringCellValue());
//		        }
//		        
//			if (row.getCell(7, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
//				inventoryEntity.setMinimumStockQuantity((float) row.getCell(7).getNumericCellValue());
//			}
//		        
//			if (row.getCell(8, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
//				inventoryEntity.setMeasuringUnit(row.getCell(8).getStringCellValue());
//			}
//			
//			if (row.getCell(12, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
//				inventoryEntity.setAvailableQuantity((float) row.getCell(12).getNumericCellValue());
//			}
		        
			InventoryEntity savedDetail = inventoryRepository.save(inventoryEntity);
			
			//-----------------------------------Save Inventory Sell Price List---------------------------------------------------			
			InventorySellPriceListEntity inventorySellPriceListEntity = new InventorySellPriceListEntity();
			if (row.getCell(9, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventorySellPriceListEntity.setSellPrice((float) row.getCell(9).getNumericCellValue());
			}
			
			if (row.getCell(11, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventorySellPriceListEntity.setDiscountAmount((float) row.getCell(11).getNumericCellValue());
			}
			
			inventorySellPriceListEntity.setEffectiveDateTime(Instant.now());
			inventorySellPriceListEntity.setNowEffectiveDateTimeDifference(0);
			inventorySellPriceListEntity.setInventoryId(savedDetail.getInventoryId());
			inventorySellPriceListRepository.save(inventorySellPriceListEntity);
			
			//-----------------------------------Save Inventory Cost Price List---------------------------------------------------			
			InventoryCostPriceListEntity inventoryCostPriceListEntity = new InventoryCostPriceListEntity();
			if (row.getCell(10, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryCostPriceListEntity.setCostOfInventory((float) row.getCell(10).getNumericCellValue());
			}
			
			if (row.getCell(12, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryCostPriceListEntity.setQuantity((float) row.getCell(12).getNumericCellValue());
			}
			
			inventoryCostPriceListEntity.setCostDateTime(Instant.now());
			inventoryCostPriceListEntity.setInventoryId(savedDetail.getInventoryId());
			inventoryCostPriceListRepository.save(inventoryCostPriceListEntity);
			
			TransactionEntity inventoryTransactionDetailEntity = new TransactionEntity();

			inventoryTransactionDetailEntity.setInventoryId(savedDetail.getInventoryId());
			inventoryTransactionDetailEntity.setTransactionTime(Instant.now());
			inventoryTransactionDetailEntity.setType("In");
			inventoryTransactionDetailRepository.save(inventoryTransactionDetailEntity);
			
			if (row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
				List<CategoryEntity> categoryEntities = categoryRepository.findByIsDeletedAndCategory(false,
				    row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				if (categoryEntities.size() == 0) {
					CategoryEntity categoryEntity = new CategoryEntity();
					categoryEntity.setCategory(row.getCell(4).getStringCellValue());
					categoryRepository.save(categoryEntity);
				}
			}
			
	        }
	        
	          
		if (i > 1 && (worksheet.getPhysicalNumberOfRows()) > 1) {
			returnValue = "Inventory Items Imported Successfully";
		}
		
		return returnValue;
	}

	@Override
	public List<InventoryResponseModel> getSelectedInventeryItems(Long[] inventoryIds) {
		List<InventoryResponseModel> returnValue = new ArrayList<>(); 

		List<InventoryEntity> inventoryItems = inventoryRepository.findByInventoryIdIn(inventoryIds);
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	InventoryResponseModel inventoryRequestModel = new InventoryResponseModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
	}
	@Override
	public String importPatientData(ImportInventoryDataRequestModel requestDetail) throws IOException {

		String returnValue = "";

		XSSFWorkbook workbook = new XSSFWorkbook(requestDetail.getImportExcel().getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter(Locale.US);
		int i;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String brandName = formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			
			System.out.println("brandName "+brandName);
			//			CategoryEntity categoryEntity = categoryRepository.findByInventoryTypeAndIsDeleted(brandName, false);
			//			if(categoryEntity == null) {
			//			 categoryEntity = new CategoryEntity();
			//			 categoryEntity.setInventoryType(category);
			//			 categoryEntity = categoryRepository.save(categoryEntity);
			//			}
		}
			
		return returnValue;
	}
	
	@Override
	public String importInventoryCategory(InventoryCategoryRequestModel importInventoryCategoryDetails) throws IOException {
		String returnValue = "";
		XSSFWorkbook workbook = new XSSFWorkbook(importInventoryCategoryDetails.getImportExcel().getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int i = 0;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			
			XSSFRow row = worksheet.getRow(i);
			
			if (row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
				List<CategoryEntity> categoryEntities = categoryRepository.findByIsDeletedAndCategory(false,
				    row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				if (categoryEntities.size() > 0)
					throw new AppException("Record already exist!");
				
				CategoryEntity categoryEntity = new CategoryEntity();
				categoryEntity.setCategory(row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				categoryRepository.save(categoryEntity);
				
			}	
		}
		
		if (i > 1 && (worksheet.getPhysicalNumberOfRows()) > 1) {
			returnValue = "Inventory Items Imported Successfully";
		}
		
		return returnValue;
	}
	
	private double getCustomerAvailableQuantity(CustomerPurchasedInventoryEntity i) {
		double inQuantity=0;
		double outQuantity =0;
		List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(i.getInventoryId(), false);
	   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity: customerInventoryTransactionEntities) {   
		   if("In".equalsIgnoreCase(customerInventoryTransactionEntity.getTransactionType())) {
				  inQuantity += customerInventoryTransactionEntity.getQuantity();
		   }
		   else if("Out".equalsIgnoreCase(customerInventoryTransactionEntity.getTransactionType())) {
			  outQuantity += customerInventoryTransactionEntity.getQuantity();
		   }           
	   }
	   return inQuantity - outQuantity;
	}
}
