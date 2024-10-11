package com.event.booking.system.user_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.event.booking.system.user_service.feign.TicketClient;
import com.event.booking.system.user_service.dto.ResponseBody;
import com.event.booking.system.user_service.dto.TicketDto;
import com.event.booking.system.user_service.dto.UserDto;
import com.event.booking.system.user_service.entity.User;
import com.event.booking.system.user_service.exception.UserNotFoundException;
import com.event.booking.system.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private UserRepository userRepository;
	private TicketClient ticketClient;

	@Autowired
	public UserService(UserRepository userRepository, TicketClient ticketClient) {
		super();
		this.userRepository = userRepository;
		this.ticketClient = ticketClient;
	}

	@Transactional
	public List<UserDto> getAllUsers() {
		LOGGER.debug("Entered into getAllUsers method");

		List<User> users = userRepository.findAll();
		return users.stream().map(this::copyBeans).collect(Collectors.toList());
	}

	@Transactional
	public Long saveUser(UserDto userDto) {
		LOGGER.debug("Entered into saveUser method");

		User user = new User();
		user.setTitle(userDto.getTitle());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setUserName(userDto.getUserName());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setCredit(userDto.getCredit());
		return userRepository.save(user).getPkUserId();
	}

	@Transactional
	public Long updateUser(Long userId, UserDto userDto) {
		LOGGER.debug("Entered into updateUser method");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found for the given ID"));

		user.setTitle(userDto.getTitle());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setUserName(userDto.getUserName());
		user.setPhoneNumber(userDto.getPhoneNumber());
		User savedUser = userRepository.save(user);
		return savedUser.getPkUserId();
	}

	@Transactional
	public void deleteUser(Long pkUserId) {
		LOGGER.debug("Entered into deleteUser method");

		Optional<User> optionalUser = userRepository.findById(pkUserId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found for the given ID");
		}

		userRepository.deleteById(pkUserId);
	}

	@Transactional
	public UserDto getUserById(Long userId) {
		LOGGER.debug("Entered into getUserById method");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found for the given ID"));
		return copyBeans(user);
	}

	@Transactional
	public List<TicketDto> getTicketsByUserId(Long userId) {
		LOGGER.debug("Entered into getTicketsByUserId method");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found for the given ID"));

		List<TicketDto> ticketDtos = Collections.emptyList();
		ResponseEntity<ResponseBody<List<TicketDto>>> response = ticketClient.getTickets(user.getPkUserId(), null);
		if (response.getStatusCode() == HttpStatus.OK) {
			ticketDtos = response.getBody().getData();
		} else {
			LOGGER.debug("Couldn't retrieved tickets information for the user {} ", user.getUserName());
			throw new RuntimeException("Exception occurred while fetching tickets for user " + userId);
		}
		return ticketDtos;

	}

	@Transactional
	public String cancelTickets(Long userId, Long eventId) {
		LOGGER.debug("Entered into cancelTickets method");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found for the given ID"));

		ResponseEntity<ResponseBody<String>> response = ticketClient.cancelTickets(user.getPkUserId(), eventId);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody().getData();
		} else {
			LOGGER.debug("Couldn't retrieved tickets information for the user {} ", user.getUserName());
			throw new RuntimeException("Exception occurred while cancelling tickets for user " + userId);
		}
	}

	private UserDto copyBeans(User user) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

}