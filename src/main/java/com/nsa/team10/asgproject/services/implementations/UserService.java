package com.nsa.team10.asgproject.services.implementations;

import com.nsa.team10.asgproject.FilteredPageRequest;
import com.nsa.team10.asgproject.PaginatedList;
import com.nsa.team10.asgproject.dal.daos.UserDao;
import com.nsa.team10.asgproject.dal.repositories.interfaces.IUserRepository;
import com.nsa.team10.asgproject.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService
{
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public PaginatedList<UserDao> findAll(FilteredPageRequest pageRequest)
    {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public PaginatedList<UserDao> findAllDisabled(FilteredPageRequest pageRequest)
    {
        return userRepository.findAllDisabled(pageRequest);
    }

    @Override
    public Optional<UserDao> findById(long userId)
    {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<UserDao> findByIdIncDisabled(long userId)
    {
        return userRepository.findByIdIncDisabled(userId);
    }

    @Override
    public boolean disable(long userId)
    {
        var user = userRepository.findById(userId);
        if (user.isPresent())
            return userRepository.disable(userId);
        else return false;
    }

    @Override
    public boolean enable(long userId)
    {
        var user = userRepository.findByIdIncDisabled(userId);
        if (user.isPresent())
            return userRepository.enable(userId);
        else return false;
    }
}
