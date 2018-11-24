package com.nsa.team10.asgproject.dal.repositories.interfaces;

import com.nsa.team10.asgproject.dal.daos.UserDao;
import com.nsa.team10.asgproject.dal.daos.UserWithPasswordDao;
import com.nsa.team10.asgproject.validation.UserConflictException;

import java.util.Optional;

public interface IUserRepository
{
    void register(UserDao newUser, String hashedPassword) throws UserConflictException;
    Optional<UserWithPasswordDao> getUserWithPasswordByEmail(String email);
}
