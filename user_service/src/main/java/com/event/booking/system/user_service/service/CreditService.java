package com.event.booking.system.user_service.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.event.booking.system.user_service.entity.User;
import com.event.booking.system.user_service.exception.UserNotFoundException;
import com.event.booking.system.user_service.repository.UserRepository;

@Service
public class CreditService {

	@Autowired
	private UserRepository userRepository;

	public boolean reserveCredit(Long userId, BigDecimal amount) {

		Optional<User> optionalUser = userRepository.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found for the given ID");
		}

		User user = optionalUser.get();
		BigDecimal newCredits = user.getCredit().subtract(amount);
		int comparison = newCredits.compareTo(BigDecimal.ZERO);

		if (comparison >= 0) {
			user.setCredit(newCredits);
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}

	public void unReserveCredit(Long userId, BigDecimal amount) {

		Optional<User> optionalUser = userRepository.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found for the given ID");
		}

		User user = optionalUser.get();
		BigDecimal newCredits = user.getCredit().add(amount);
		user.setCredit(newCredits);
		userRepository.save(user);
	}

}