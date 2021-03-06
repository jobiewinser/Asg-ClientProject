package com.nsa.team10.asgproject.services.implementations;

import com.nsa.team10.asgproject.FilteredPageRequest;
import com.nsa.team10.asgproject.PaginatedList;
import com.nsa.team10.asgproject.config.DefaultUserDetails;
import com.nsa.team10.asgproject.repositories.daos.CandidateDao;
import com.nsa.team10.asgproject.repositories.interfaces.ICandidateRepository;
import com.nsa.team10.asgproject.services.dtos.Mail;
import com.nsa.team10.asgproject.services.dtos.NewCandidateDto;
import com.nsa.team10.asgproject.services.interfaces.ICandidateService;
import com.nsa.team10.asgproject.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class CandidateService implements ICandidateService
{
    private final ICandidateRepository candidateRepository;
    private final IEmailService emailService;

    @Autowired
    public CandidateService(ICandidateRepository candidateRepository, IEmailService emailService)
    {
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
    }

    public void create(NewCandidateDto newCandidate)
    {
        if (!AccountService.getCurrentUserDetails().isPresent())
            throw new AccessDeniedException("Need to be logged in");

        var userId = AccountService.getCurrentUserDetails().get().getUser().getId();
        candidateRepository.create(userId, newCandidate);
    }

    @Override
    public PaginatedList<CandidateDao> findAll(FilteredPageRequest pageRequest)
    {
        return candidateRepository.findAll(pageRequest);
    }

    @Override
    public Optional<CandidateDao> findByEmail(String email)
    {
        return candidateRepository.findByEmail(email);
    }

    public PaginatedList<CandidateDao> findAllNeedAssigning(FilteredPageRequest pageRequest)
    {
        return candidateRepository.findAllNeedAssigning(pageRequest);
    }

    @Override
    public Optional<CandidateDao> findById(long candidateId)
    {
        return candidateRepository.findById(candidateId);
    }

    @Override
    @PreAuthorize("hasAuthority('Candidate')")
    public void sendReceipt(boolean hasPayed)
    {
        var userDetails = AccountService.getCurrentUserDetails();
        if (userDetails.isPresent())
        {
            var mail = new Mail();
            var user = userDetails.get().getUser();
            mail.setTo(user.getEmail());
            mail.setSubject("Payment Received");
            var model = new HashMap<String, Object>();
            model.put("link", "localhost:8080");
            mail.setModel(model);
            emailService.sendEmail(mail, "receipt.ftl");

            candidateRepository.setHasPayed(user.getId(), true);
        }
    }
}
