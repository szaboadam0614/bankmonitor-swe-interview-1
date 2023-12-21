package bankmonitor.model.integration;

import bankmonitor.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @Autowired
    private TransactionRepository transactionRepository;

    public void clean() {
        transactionRepository.deleteAll();
    }

}
