package com.app.site.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.site.domain.User;
import com.app.site.dto.UserDto;
import com.app.site.repository.UserRepository;
import com.app.site.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
	@Autowired
	public  UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<UserDto> getAllUser() throws Exception {
		return userRepository.findAll()
				.stream()
				.map(user -> new UserDto(user.getId(), user.getName(), user.getPhoneNum()))
				.collect(Collectors.toList());
	}

	@Override
	public User saveUser(User user) throws Exception {
		return userRepository.save(user);
	}


}
