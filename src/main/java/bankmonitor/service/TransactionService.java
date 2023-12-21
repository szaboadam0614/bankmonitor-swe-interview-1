package bankmonitor.service;

import bankmonitor.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction createTransaction(final String jsonData);

    Transaction updateTransaction(final Long id, final String updatedJsonData);

}
