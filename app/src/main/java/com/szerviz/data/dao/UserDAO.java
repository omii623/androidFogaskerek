package com.szerviz.data.dao;

import com.szerviz.data.model.User;

public class UserDAO implements IUserDAO{
    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public User getUserByEmail(String name) {
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }
}
