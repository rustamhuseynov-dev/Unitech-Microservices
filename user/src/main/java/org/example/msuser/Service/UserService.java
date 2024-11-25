package org.example.msuser.Service;

import org.example.msuser.DTO.UserRequest;
import org.example.msuser.DTO.UserResponse;
import org.example.msuser.Entity.User;
import org.example.msuser.Mapper.UserMapper;
import org.example.msuser.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    // Method to create a new user
    public UserResponse createUser(UserRequest userRequest) {
        // Map UserRequest to User entity
        User userEntity = userMapper.mapToEntity(userRequest);

        // Save the user entity in the database
        User savedUser = userRepository.save(userEntity);

        // Map the saved User entity back to UserResponse
        return userMapper.mapToDto(savedUser);
    }

    // Method to get a user by ID
    public UserResponse getUserById(Long id) {
        // Try to find the user by ID
        Optional<User> userEntity = userRepository.findById(id);

        // If user doesn't exist, return null or handle it accordingly
        // Or throw an exception, depending on your requirements
        // If user exists, map it to UserResponse
        return userEntity
                .map(user -> userMapper.mapToDto(user))
                .orElse(null);
    }

    // Method to get all users
    public Set<UserResponse> getAllUsers() {
        // Fetch all users from the repository
        List<User> users = userRepository.findAll();

        // Convert the list of User entities to a set of UserResponse DTOs
        return users.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    // Method to update user details
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        // Find user by ID
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            // Update fields with the data from UserRequest
            userToUpdate.setUsername(userRequest.getUsername());
            userToUpdate.setPassword(userRequest.getPassword());
            userToUpdate.setEmail(userRequest.getEmail());
            userToUpdate.setPhoneNumber(userRequest.getPhoneNumber());
            userToUpdate.setFirstName(userRequest.getFirstName());
            userToUpdate.setLastName(userRequest.getLastName());

            // Save updated user
            User updatedUser = userRepository.save(userToUpdate);

            // Return updated UserResponse
            return userMapper.mapToDto(updatedUser);
        } else {
            // Handle user not found (return null or throw an exception)
            return null; // Or throw an exception
        }
    }

    // Method to delete user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
