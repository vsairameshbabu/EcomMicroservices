package com.app.ecom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

//    private List<User> userList = new ArrayList<>();
//    private Long nextId = 1L;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
//        user.setId(nextId++);
//        userList.add(user);
        return userRepository.save(user);
    }

    public Optional<User> fetchUser(Long id) {
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst();
        return userRepository.findById(id);
    }

    public boolean updateUser(Long id, User updatedUser) {
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst()
//                .map(existingUser -> {
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                    return true;
//                })
//                .orElse(false);
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
}
