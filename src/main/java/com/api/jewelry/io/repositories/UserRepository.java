package com.api.jewelry.io.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email); 
	UserEntity findByUserId(String userId);
	UserEntity findByEmailAndEncryptedPassword(String email, String encryptedPassword);
	Page<UserEntity> findByFirstNameContainingOrLastNameContainingOrPhoneContainingOrEmailContainingOrUserStatusContaining(
			String searchKey, String searchKey2, String searchKey4, String searchKey5,
			String searchKey6, Pageable pageableRequest);
	Page<UserEntity> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName,
			Pageable pageableRequest);
	UserEntity findByEmailVerificationToken(String emailVerificationToken);
	UserEntity findByEmailAndPasswordResetCode(String email, String resetCode);
	Page<UserEntity> findByUserTypeNot(String string, Pageable pageableRequest);
	Page<UserEntity> findByUserType(String string, Pageable pageableRequest);

	
}