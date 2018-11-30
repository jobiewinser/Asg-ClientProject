package com.nsa.team10.asgproject.services.implementations;

import com.nsa.team10.asgproject.FilteredPageRequest;
import com.nsa.team10.asgproject.PaginatedList;
import com.nsa.team10.asgproject.config.DefaultUserDetails;
import com.nsa.team10.asgproject.dal.daos.UserDao;
import com.nsa.team10.asgproject.dal.repositories.interfaces.IUserRepository;
import com.nsa.team10.asgproject.services.dtos.NewUserDto;
import com.nsa.team10.asgproject.services.interfaces.IUserService;
import com.nsa.team10.asgproject.validation.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService
{
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(NewUserDto newUser) throws ConflictException
    {
        var hashedPassword = passwordEncoder.encode(newUser.getPassword());
        var user = new UserDao(newUser.getForename(), newUser.getSurname(), newUser.getEmail().toLowerCase(), newUser.getPhoneNumber(), UserDao.Role.Candidate);
        userRepository.register(user, hashedPassword);
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        var user = userRepository.findWithPasswordByEmail(email);
        if (!user.isPresent()) throw new UsernameNotFoundException(email);
        else return new DefaultUserDetails(user.get());
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

    @Override
    public boolean delete(long userId)
    {
        var user = userRepository.findByIdIncDisabled(userId);
        if (user.isPresent())
            return userRepository.delete(userId);
        else return false;
    }
}