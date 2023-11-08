package com.codegym.laptopshop.service.impl;

import com.codegym.laptopshop.dto.RoleDto;
import com.codegym.laptopshop.dto.UserDto;
import com.codegym.laptopshop.entity.Role;
import com.codegym.laptopshop.entity.User;
import com.codegym.laptopshop.repository.UserRepository;
import com.codegym.laptopshop.service.UserService;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepositoty;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepositoty, ModelMapper modelMapper, PasswordEncoder passwordEncoder ) {
        this.userRepositoty = userRepositoty;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public Iterable<UserDto> findAll() {
        Iterable<User> users = userRepositoty.findByActivated(true);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<User> user = userRepositoty.findById(id);
        return user.map(u -> modelMapper.map(u, UserDto.class));
    }


    @Override
    public void save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (!userDto.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
            user.setPassword(hashedPassword);
        }
        userRepositoty.save(user);
    }

    @Override
    public void remove(Long id) {
        User user = userRepositoty.findById(id).get();
        user.setActivated(false);;
        userRepositoty.save(user);
    }


    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> user = userRepositoty.findByUsername(username);
        return user.map(u -> modelMapper.map(u, UserDto.class));
    }

    @Override
    public Iterable<UserDto> findByActivatedAndRole(Boolean isActivated, RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        Iterable<User> users = userRepositoty.findByActivatedAndRole(true, role);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePassword(UserDto userDto) {
        Optional<User> user = userRepositoty.findById(userDto.getId());
        String oldPassword = user.get().getPassword();
        System.out.println(oldPassword);
        if (user.isPresent()) {
//            String hashedPassword = BCrypt.hashpw(user.get().getPassword(), BCrypt.gensalt(10));
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
            user.get().setPassword(hashedPassword);
            userRepositoty.save(user.get());
        }
    }


    @Override
    public void updateNewPassword(UserDto userDto) {
        User user = userRepositoty.findById(userDto.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String oldPassword = user.getPassword();
        String newPassword = userDto.getNewPassword();

        System.out.println(oldPassword);

        if(this.passwordEncoder.matches(oldPassword,user.getPassword())){
            // change the passWord
            user.setPassword(this.passwordEncoder.encode(newPassword));
            userRepositoty.save(user);
        } else {
            // error
            System.out.println("error");
        }
    }



    @Override
    public void updateRole(UserDto userDto) {
        Role role = userDto.getRole();
        User user = userRepositoty.findById(userDto.getId()).get();
        user.setRole(role);
        userRepositoty.save(user);
    }

    @Override
    public Iterable<UserDto> findByFullNameContainingAndActivated(String fullname, Boolean isActivated) {
        Iterable<User> users = userRepositoty.findByFullNameContainingAndActivated(fullname, true);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public Optional<UserDto> createAndGetUser(UserDto userDto) {
//        User user = modelMapper.map(userDto, User.class);
//        userRepositoty.save(user);
//        User newUser = userRepositoty.findById(user.getId()).get();
//        UserDto newUserDto = modelMapper.map(newUser, UserDto.class);
//        return Optional.ofNullable(newUserDto);
//    }

    @Override
    public Optional<UserDto> createAndGetUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        if (!userDto.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
            user.setPassword(hashedPassword);
        }
        userRepositoty.save(user);
        User newUser = userRepositoty.findById(user.getId()).get();
        UserDto newUserDto = modelMapper.map(newUser, UserDto.class);

        return Optional.ofNullable(newUserDto);
    }




    @Override
    public void updateAllData(UserDto userDto) {
        User user = userRepositoty.findById(userDto.getId()).get();
        user.setAvatar(userDto.getAvatar());
        user.setFullName(userDto.getFullname());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        userRepositoty.save(user);
    }

}

