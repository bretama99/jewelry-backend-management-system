package com.api.jewelry.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.jewelry.shared.dto.UserDto;
import com.api.jewelry.ui.model.request.OrderDetailRequestModel;
import com.api.jewelry.ui.model.request.OrderItemStatusRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.request.UploadOrderDocumentRequestModel;
import com.api.jewelry.ui.model.request.UserNotificationsRequestModel;
import com.api.jewelry.ui.model.response.UserNotificationsResponseModel;

public interface UserNotificationsService {
	UserNotificationsResponseModel getUserNotification(long userNotificationsId);

	List<UserNotificationsResponseModel> getUserNotificationsBySender(String senderId, int page, int limit);
	List<UserNotificationsResponseModel> getUserNotificationsByReceiver(String receiverId, int page, int limit, HttpServletRequest request);
	List<UserNotificationsResponseModel> getUserNotificationsBySenderAndReceiver(String senderId, String receiverId, int page, int limit);

	UserNotificationsResponseModel saveUserNotification(UserNotificationsRequestModel userNotificationsRequestModel);
	UserNotificationsResponseModel updateUserNotification(long userNotificationsId, UserNotificationsRequestModel userNotificationsRequestModel);
	String deleteUserNotification(long userNotificationsId);

	UserNotificationsResponseModel setSeenUserNotification(long userNotificationsId);

}
