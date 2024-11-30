package com.example.login.service.impl;

import com.example.login.entity.Role;
import com.example.login.reponsitory.RoleRepository;
import com.example.login.reponsitory.UserRepository;
import com.example.login.dto.UserDto;
import com.example.login.entity.User;
import com.example.login.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // Mã hóa mật khẩu bằng Spring Security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Kiểm tra hoặc tạo vai trò "ROLE_ADMIN"
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_ADMIN");
            role = roleRepository.save(role);
        }
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ", 2); // Phân tách họ tên, chỉ chia thành 2 phần
        userDto.setFirstName(str[0]);
        userDto.setLastName(str.length > 1 ? str[1] : ""); // Xử lý tên nếu không có họ
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
