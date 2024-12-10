package com.app.site.service;

import java.util.List;

import com.app.site.domain.User;
import com.app.site.dto.UserDto;

public interface UserService {

	public List<UserDto> getAllUser() throws Exception;

	public User saveUser(User user) throws Exception;
}
