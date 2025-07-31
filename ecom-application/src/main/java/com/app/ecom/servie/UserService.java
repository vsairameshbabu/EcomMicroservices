package com.app.ecom.servie;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public User addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(userRequest, user);
        return userRepository.save(user);
    }


    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id).map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id).map(user -> {
            updateUserFromRequest(userRequest, user);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(user.getAddress().getId().toString());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

    private void updateUserFromRequest(UserRequest userRequest, User user) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipCode(userRequest.getAddress().getZipCode());
            user.setAddress(address);
        }
    }
}
