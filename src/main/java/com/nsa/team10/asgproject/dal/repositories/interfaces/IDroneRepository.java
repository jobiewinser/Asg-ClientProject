package com.nsa.team10.asgproject.dal.repositories.interfaces;

import com.nsa.team10.asgproject.FilteredPageRequest;
import com.nsa.team10.asgproject.PaginatedList;
import com.nsa.team10.asgproject.dal.daos.DroneDao;
import com.nsa.team10.asgproject.validation.ConflictException;

import java.util.List;

public interface IDroneRepository
{
    void create(DroneDao drone) throws ConflictException;
    PaginatedList<DroneDao> findAll(FilteredPageRequest pageRequest);
    List<DroneDao> search(String search);
}
