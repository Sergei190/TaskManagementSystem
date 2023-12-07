package com.example.taskmanagementsystem.user.service.impl;

import com.example.taskmanagementsystem.exception.ConflictException;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.UserDto;
import com.example.taskmanagementsystem.user.mapper.UserMapper;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.repository.RoleRepository;
import com.example.taskmanagementsystem.user.repository.UserRepository;
import com.example.taskmanagementsystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService( RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto addUser(IncomeUserDto incomeUserDto) {
        User checkUser = findByUserByEmail(incomeUserDto.getEmail());
        if (checkUser != null) {
            throw new ConflictException("Пользователь с указанной почтой уже существует");
        }
        User user = UserMapper.toUser(incomeUserDto);
        user.setRoles(Collections.singleton(roleRepository.findByName("USER").get()));
        user.setPassword(passwordEncoder.encode(incomeUserDto.getPassword()));
        User userStorage = userRepository.save(user);
        return UserMapper.toUserDto(userStorage);
    }

    @Override
    public User findByUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    @Override
    public UserDetails loadUserByEmail(String email) {
        User user = findByUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("Пользователь с почтой: " + email + " не найден");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
