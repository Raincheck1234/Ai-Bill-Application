package DAO;

import model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvTransactionDao implements TransactionDao {

//HEAD 交易时间	交易类型	交易对方	商品	收/支 支付方式 //金额(元)		//当前状态	//交易单号	//商户单号	//备注
    //
    @Override
    public List<Transaction> loadFromCSV(String filePath) throws IOException {
        // 使用BOMInputStream自动处理UTF-8 BOM头
        try (Reader reader = new InputStreamReader(
                new BOMInputStream(Files.newInputStream(Paths.get(filePath))),
                StandardCharsets.UTF_8)) {

            // 配置CSV格式（关键修改点）
            CSVFormat format = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase(false)  // 禁用忽略大小写
                    .withTrim(false);             // 禁用自动trim（防止意外空格问题）

            try (CSVParser csvParser = new CSVParser(reader, format)) {


                List<Transaction> transactions = new ArrayList<>();
                for (CSVRecord record : csvParser) {
                    Transaction transaction = parseRecord(record);
                    transactions.add(transaction);
                }
                reader.close();
                return transactions;
            }
        }
    }


    private Transaction parseRecord(CSVRecord record) {
        // 重要：处理字段类型转换异常
        return new Transaction(
                record.get("交易时间"),
                record.get("交易类型"),
                record.get("交易对方"),
                record.get("商品"),
                record.get("收/支"),
                convertStringToDouble(record.get("金额(元)")),
//                Double.parseDouble(record.get("金额(元)").substring(1)), // 注意数字转换
                record.get("支付方式"),
                record.get("当前状态"),
                record.get("交易单号"), // 注意字段名拼写问题
                record.get("商户单号"),
                record.get("备注")
        );
    }

    // 根据交易单号删除交易记录
    public boolean deleteTransaction(String filePath, String orderNumber) throws IOException {
        List<Transaction> transactions = loadFromCSV(filePath);
        List<Transaction> updatedTransactions = transactions.stream()
                .filter(t -> !t.getOrderNumber().trim().equals(orderNumber))
                .collect(Collectors.toList());

        if (transactions.size() == updatedTransactions.size()) {
            return false;
        }

        writeTransactionsToCSV(filePath, updatedTransactions);
        return true;
    }

    // 添加交易
    public void addTransaction(String filePath, Transaction newTransaction) throws IOException {
        boolean fileExists = Files.exists(Paths.get(filePath));

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
             CSVPrinter csvPrinter = new CSVPrinter(writer, fileExists ? getCsvFormatWithoutHeader() : getCsvFormatWithHeader(filePath))) {

            csvPrinter.printRecord(
                    newTransaction.getTransactionTime(),
                    newTransaction.getTransactionType(),
                    newTransaction.getCounterparty(),
                    newTransaction.getCommodity(),
                    newTransaction.getInOut(),
                    "¥" + newTransaction.getPaymentAmount(),
                    newTransaction.getPaymentMethod(),
                    newTransaction.getCurrentStatus(),
                    newTransaction.getOrderNumber(),
                    newTransaction.getMerchantNumber(),
                    newTransaction.getRemarks()
            );

            csvPrinter.flush();
        }
    }
    public double convertStringToDouble(String s) {
        // 步骤1：移除所有非数字字符（保留小数点和负号）
        String cleanStr = s.replaceAll("[^\\d.-]", "");

        try {
            return Double.parseDouble(cleanStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的数字格式: " + s);
        }
    }

    // 统一写回 CSV
    private void writeTransactionsToCSV(String filePath, List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "交易时间", "交易类型", "交易对方", "商品", "收/支", "金额(元)", "支付方式", "当前状态", "交易单号", "商户单号", "备注")))  {

            for (Transaction t : transactions) {
                csvPrinter.printRecord(
                        t.getTransactionTime(),
                        t.getTransactionType(),
                        t.getCounterparty(),
                        t.getCommodity(),
                        t.getInOut(),
                        "¥" + t.getPaymentAmount(),
                        t.getPaymentMethod(),
                        t.getCurrentStatus(),
                        t.getOrderNumber(),
                        t.getMerchantNumber(),
                        t.getRemarks()
                );
            }
        }
    }

    // 获取 CSV 格式（包含表头）
    private CSVFormat getCsvFormatWithHeader(String filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String headerLine = reader.readLine();
            return CSVFormat.DEFAULT.withHeader(headerLine.split(",")).withTrim();
        }
    }

    // 获取 CSV 格式（不包含表头）
    private CSVFormat getCsvFormatWithoutHeader() {
        return CSVFormat.DEFAULT.withTrim();
    }

    public boolean changeInformation(String orderNumber, String head, String value,String path) throws IOException{
        List<Transaction> transactions=loadFromCSV(path);
        for (int i = 0; i < transactions.size(); i++) {
            if(transactions.get(i).getOrderNumber().trim().equals(orderNumber)){
                Transaction newTransaction=transactions.get(i);
                switch (head){
                    case "transactionTime" :{
                        newTransaction.setTransactionTime(value);
                        break;}
                    case "transactionType" :{
                        newTransaction.setTransactionType(value);
                        break;
                    }
                    case "counterparty" :{
                        newTransaction.setCounterparty(value);
                        break;
                    } case "commodity" :{
                        newTransaction.setCommodity(value);
                        break;
                    } case "inOut" :{
                        newTransaction.setInOut(value);
                        break;
                    } case "paymentAmount" :{
                        newTransaction.setPaymentAmount(Double.parseDouble(value));
                        break;
                    } case "paymentMethod" :{
                        newTransaction.setPaymentMethod(value);
                        break;
                    } case "currentStatus" :{
                        newTransaction.setCurrentStatus(value);
                        break;
                    } case "orderNumber" :{
                        newTransaction.setOrderNumber(value);
                        break;
                    } case "merchantNumber" :{
                        newTransaction.setMerchantNumber(value);
                        break;
                    } case "remarks" :{
                        newTransaction.setRemarks(value);
                        break;
                    } default:{
                        System.err.println("error head");
                    }
                }

                boolean flag=deleteTransaction(path,transactions.get(i).getOrderNumber());
                if(flag) {
                    addTransaction(path,newTransaction);
                    System.out.println("success to delete");
                }
                else System.err.println("failed to delete");


            }
        }
        return true;
    }
}
