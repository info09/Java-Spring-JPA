package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private IUserRepository _userRepository;

    public User createRequest(UserCreateRequest request){
        if(_userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);
        
        var user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return _userRepository.save(user);
    }

    public User updateRequest(String userId, UserUpdateRequest request){
        var user = getByUserId(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return _userRepository.save(user);
    }

    public List<User> getAll(){
        return  _userRepository.findAll();
    }

    public User getByUserId(String id){
        return _userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String deleteUser(String id){
        var user = getByUserId(id);
        _userRepository.delete(user);
        return user.getId();
    }
}
