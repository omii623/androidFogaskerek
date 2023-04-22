package com.szerviz.data.controller;

import com.szerviz.data.dao.IUserDAO;
import com.szerviz.data.dao.UserDAO;
import com.szerviz.data.model.User;

public class UserController {
    private static UserController instance = new UserController();

    private UserController(){}

    public static UserController getInstance() {return instance;}

    private IUserDAO dao = new UserDAO();

    public boolean updateUser(User updateUser){
        return dao.updateUser(updateUser);
    }
}
