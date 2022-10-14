package com.rkc.zds.dto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.rkc.zds.exceptions.Exceptions;

/*
 * In memory Dao. Less than ideal but just for an example.
 */
public class UserDao {
    private final ConcurrentMap<String, UserDto> userMap;

    public UserDao() {
        this.userMap = new ConcurrentHashMap<>();
    }

    public UserDto create(String email, Set<UserDto.Role> roles) {
        UserDto user = new UserDto(email, roles, LocalDate.now());

        // If we get a non null value that means the user already exists in the Map.
        if (null != userMap.putIfAbsent(user.getEmail(), user)) {
            return null;
        }
        return user;
    }

    public UserDto get(String email) {
        return userMap.get(email);
    }

    // Alternate implementation to throw exceptions instead of return nulls for not found.
    public UserDto getThrowNotFound(String email) throws Exception {
        UserDto user = userMap.get(email);
        if (null == user) {
            throw Exceptions.notFound(String.format("User %s not found", email));
        }
        return user;
    }

    public UserDto update(UserDto user) {
        // This means no user existed so update failed. return null
        if (null == userMap.replace(user.getEmail(), user)) {
            return null;
        }
        // Update succeeded return the user
        return user;
    }

    public boolean delete(String email) {
        return null != userMap.remove(email);
    }

    public List<UserDto> listUsers() {
        return userMap.values()
                      .stream()
                      .sorted(Comparator.comparing((UserDto u) -> u.getEmail()))
                      .collect(Collectors.toList());
    }
}