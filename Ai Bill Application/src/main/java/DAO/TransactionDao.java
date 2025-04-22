package DAO;

import model.Transaction;

import java.io.IOException;
import java.util.List;

public interface TransactionDao{
    List<Transaction> loadFromCSV(String filePath) throws IOException;
}
