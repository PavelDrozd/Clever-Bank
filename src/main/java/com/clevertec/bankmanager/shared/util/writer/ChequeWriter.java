package com.clevertec.bankmanager.shared.util.writer;

import com.clevertec.bankmanager.data.dto.TransactionDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChequeWriter {

    private static final ResourceBundle message = ResourceBundle.getBundle("messages");

    public static void writeCheck(TransactionDto transaction) {
        LocalDateTime dateTime = transaction.getDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh.mm.ss");
        Path path = Paths.get("cheque/transaction" + dateTime.format(formatter) + ".txt");
        checkDirectoryAndFile(path);
        String text = formatText(transaction);
        try {
            Files.writeString(path, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkDirectoryAndFile(Path path) {
        try {
            Files.createDirectory(path.getParent());
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatText(TransactionDto transaction) {
        String msgBankCheque = message.getString("msg.cheque.bank_cheque");
        String msgCheque = message.getString("msg.cheque.cheque");
        String msgBankSender = message.getString("msg.cheque.bank_sender");
        String msgBankRecipient = message.getString("msg.cheque.bank_recipient");
        String msgAccountSender = message.getString("msg.cheque.account_sender");
        String msgAccountRecipient = message.getString("msg.cheque.account_recipient");
        String msgAmount = message.getString("msg.cheque.amount");
        String msgCurrency = message.getString("msg.cheque.currency");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");
        String date = now.toLocalDate().format(dateFormat);
        String time = now.toLocalTime().format(timeFormat);

        String senderBank = transaction.getSenderAccount().getBank().getName();
        String recipientBank = transaction.getRecipientAccount().getBank().getName();
        Long senderAccountNumber = transaction.getSenderAccount().getNumber();
        Long recipientAccountNumber = transaction.getRecipientAccount().getNumber();
        Double amount = transaction.getAmount();
        return String.format("""
                        _________________________________________
                        |\t\t\t%14s\t\t\t\t|
                        | %3s:\t\t\t\t%19d\t|
                        | %10s\t\t\t\t%11s\t|
                        | %16s:\t%19s\t|
                        | %15s:\t%19s\t|
                        | %16s:\t%19d\t|
                        | %15s:\t%19d\t|
                        | %5s:\t\t\t%15.2f BYN |
                        |_______________________________________|
                        """,
                msgBankCheque, msgCheque, transaction.getId(), date, time, msgBankSender, senderBank, msgBankRecipient,
                recipientBank, msgAccountSender, senderAccountNumber, msgAccountRecipient, recipientAccountNumber,
                msgAmount, amount);
    }
}
