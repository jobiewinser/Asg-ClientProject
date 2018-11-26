package com.nsa.team10.asgproject.services.interfaces;

import com.nsa.team10.asgproject.dal.daos.BookingDao;
import com.nsa.team10.asgproject.services.dtos.NewBookingDto;
import com.nsa.team10.asgproject.services.dtos.NewUserDto;
import com.nsa.team10.asgproject.validation.UserConflictException;

public interface IAccountService
{
    void register(NewUserDto newUser) throws UserConflictException;
}
