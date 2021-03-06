package com.nsa.team10.asgproject.services.implementations;

import com.nsa.team10.asgproject.config.DefaultUserDetails;
import com.nsa.team10.asgproject.repositories.daos.UserDao;
import com.nsa.team10.asgproject.repositories.interfaces.IUserRepository;
import com.nsa.team10.asgproject.services.dtos.Mail;
import com.nsa.team10.asgproject.services.dtos.NewUserDto;
import com.nsa.team10.asgproject.services.interfaces.IAccountService;
import com.nsa.team10.asgproject.services.interfaces.IEmailService;
import com.nsa.team10.asgproject.validation.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AccountService implements IAccountService, UserDetailsService
{
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    @Value("${server.basedomain}")
    private String baseDomain;

    @Autowired
    public AccountService(IUserRepository userRepository, PasswordEncoder passwordEncoder, IEmailService emailService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void register(NewUserDto newUser) throws ConflictException
    {
        var hashedPassword = passwordEncoder.encode(newUser.getPassword());
        var user = new UserDao(newUser.getForename(), newUser.getSurname(), newUser.getEmail().toLowerCase(), newUser.getPhoneNumber(), UserDao.Role.Guest);
        userRepository.create(user, hashedPassword);
        var activationToken = userRepository.generateActivationToken(user.getEmail());
        var mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject("Welcome to Aviation Systems Group");
        var model = new HashMap<String, Object>();
        model.put("forename", user.getForename());
        model.put("link", baseDomain + "/account/activate?email=" + user.getEmail() + "&token=" + activationToken);
        mail.setModel(model);
        emailService.sendEmail(mail, "activation.ftl");
    }

    @Override
    public boolean verifyActivationToken(String email, String token)
    {
        return userRepository.verifyActivationToken(email, token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        var user = userRepository.findWithPasswordByEmail(email);
        if (!user.isPresent()) throw new UsernameNotFoundException(email);
        else return new DefaultUserDetails(user.get());
    }

    public static Optional<DefaultUserDetails> getCurrentUserDetails()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return Optional.of((DefaultUserDetails) authentication.getPrincipal());
        else return Optional.empty();
    }
}
