package com.clevertec.bankmanager.shared.util.writer;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.shared.exception.service.ServiceIOException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Util class for write cheque.
 */
public class ChequeWriter {

    /** Resource bundle get messages for cheque from messages.properties. */
    private static final ResourceBundle message = ResourceBundle.getBundle("messages");

    /**
     * Write cheque with date, time, transaction information in source root in folder cheque.
     *
     * @param transactionDto expected object type of TransactionDto.
     */
    public static void writeCheque(TransactionDto transactionDto) {
        LocalDateTime dateTime = transactionDto.getDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh.mm.ss");
        Path path = Paths.get("cheque/transactionDto" + dateTime.format(formatter) + ".txt");
        checkDirectoryAndFile(path);
        String text = formatText(transactionDto);
        try {
            Files.writeString(path, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ServiceIOException(e);
        }
    }

    /**
     * Checks for the presence of a file and folder at the specified path.
     *
     * @param path expected object type of Path.
     */
    private static void checkDirectoryAndFile(Path path) {
        try {
            Files.createDirectory(path.getParent());
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            throw new ServiceIOException(e);
        }
    }

    /**
     * Formats the text for printing the transaction receipt.
     *
     * @param transactionDto expected object type of TransactionDto.
     * @return String formatted text for printing the receipt.
     */
    private static String formatText(TransactionDto transactionDto) {
        String msgBankCheque = message.getString("msg.cheque.bank_cheque");
        String msgCheque = message.getString("msg.cheque.cheque");
        String msgBankSender = message.getString("msg.cheque.bank_sender");
        String msgBankRecipient = message.getString("msg.cheque.bank_recipient");
        String msgAccountSender = message.getString("msg.cheque.account_sender");
        String msgAccountRecipient = message.getString("msg.cheque.account_recipient");
        String msgAmount = message.getString("msg.cheque.amount");
        String msgCurrency = message.getString("msg.cheque.currency");

        LocalDateTime now = transactionDto.getDateTime();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");
        String date = now.toLocalDate().format(dateFormat);
        String time = now.toLocalTime().format(timeFormat);

        String senderBank = transactionDto.getSenderAccount().getBank().getName();
        String recipientBank = transactionDto.getRecipientAccount().getBank().getName();
        Long senderAccountNumber = transactionDto.getSenderAccount().getNumber();
        Long recipientAccountNumber = transactionDto.getRecipientAccount().getNumber();
        Double amount = transactionDto.getAmount();
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
                msgBankCheque, msgCheque, transactionDto.getId(), date, time, msgBankSender, senderBank, msgBankRecipient,
                recipientBank, msgAccountSender, senderAccountNumber, msgAccountRecipient, recipientAccountNumber,
                msgAmount, amount);
    }
}
