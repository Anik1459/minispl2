package com.example.oopproject.service;

import com.example.oopproject.dao.UserDAO;
import com.example.oopproject.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO dao;

    public UserService() {
        this(new UserDAO());
    }

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public int register(User user) throws SQLException {
        if (user == null) throw new IllegalArgumentException("User is required.");
        if (isBlank(user.getName())) throw new IllegalArgumentException("Name is required.");
        if (isBlank(user.getEmail())) throw new IllegalArgumentException("Email is required.");
        if (isBlank(user.getPassword())) throw new IllegalArgumentException("Password is required.");
        if (isBlank(user.getRole())) throw new IllegalArgumentException("Role is required.");
        return dao.create(user);
    }

    public User login(String email, String password) throws SQLException {
        if (isBlank(email) || isBlank(password)) return null;

        User user = dao.findByEmail(email);
        return user != null && password.equals(user.getPassword()) ? user : null;
    }

    public List<User> getAllUsers() throws SQLException {
        return dao.findAll();
    }

    public boolean update(User user) throws SQLException {
        return dao.update(user);
    }

    public boolean delete(int id) throws SQLException {
        return dao.delete(id);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
