package com.api.jewelry.service;

import java.util.List;

import com.api.jewelry.ui.model.request.AddRoleRequestModel;
import com.api.jewelry.ui.model.response.RoleResponseModel;

public interface RolesService {

	String addRole(AddRoleRequestModel roleDetails);

	List<RoleResponseModel> getRoles();

	String updateRole(long roleId, AddRoleRequestModel roleDetails);

}
