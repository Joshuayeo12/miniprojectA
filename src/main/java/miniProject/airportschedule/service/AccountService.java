package miniProject.airportschedule.service;

import miniProject.airportschedule.repo.AccountRepository;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password cannot be blank");
        }

        if (accountRepository.findPasswordByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Store password as plain text (not recommended for production)
        accountRepository.save(username, password);
    }

    public String findPasswordByUsername(String username) {
        return accountRepository.findPasswordByUsername(username);
    }
}
