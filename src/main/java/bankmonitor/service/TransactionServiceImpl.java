package bankmonitor.service;

import bankmonitor.model.Transaction;
import bankmonitor.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction createTransaction(final String jsonData) {
        if (jsonData.length() >= 1000) {
            throw new InvalidJsonDataException("Json data max length is 1000 character.");
        }
        Transaction data = new Transaction(jsonData);
        return transactionRepository.save(data);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(final Long id, final String updatedJsonData) {
        if (updatedJsonData.length() >= 1000) {
            throw new InvalidJsonDataException("Json data max length is 1000 character.");
        }
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        final JSONObject updateJson = new JSONObject(updatedJsonData);
        transaction.setData(getUpdatedJsonData(transaction, updateJson).toString());

        return transaction;
    }

    private JSONObject getUpdatedJsonData(final Transaction transaction, final JSONObject updateJson) {
        JSONObject trdata = new JSONObject(transaction.getData());
        if (updateJson.has(Transaction.AMOUNT_KEY)) {
            trdata.put(Transaction.AMOUNT_KEY, updateJson.getInt(Transaction.AMOUNT_KEY));
        }
        if (updateJson.has(Transaction.REFERENCE_KEY)) {
            trdata.put(Transaction.REFERENCE_KEY, updateJson.getString(Transaction.REFERENCE_KEY));
        }

        return trdata;
    }
}
