package com.szerviz.data.dao;

import com.szerviz.data.model.User;

public interface IUserDAO {

    boolean addUser( User user );

    User getUserByEmail( final String name );

    boolean updateUser( final User user );

}
