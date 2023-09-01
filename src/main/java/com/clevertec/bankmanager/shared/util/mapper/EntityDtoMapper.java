package com.clevertec.bankmanager.shared.util.mapper;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.store.entity.Account;
import com.clevertec.bankmanager.store.entity.Bank;
import com.clevertec.bankmanager.store.entity.Transaction;
import com.clevertec.bankmanager.store.entity.User;
import org.modelmapper.ModelMapper;

/**
 * This class used for mapping entity and DTO classes.
 * It's a box class of ModelMapper.
 * EntityDtoMapper use singleton pattern with eager initialization.
 */
public class EntityDtoMapper {

    /** ModelMapper field for mapping objects. */
    private final ModelMapper mapper;
    /** Initialize EntityDtoMapper. */
    private static final EntityDtoMapper INSTANCE = new EntityDtoMapper();

    /**
     * Private constructor initialize ModelMapper class.
     */
    private EntityDtoMapper() {
        mapper = new ModelMapper();
    }

    /**
     * Public static method for getting instance of this class.
     *
     * @return instance of this class.
     */
    public static EntityDtoMapper getInstance() {
        return INSTANCE;
    }

    /**
     * Method mapping User to UserDto.
     *
     * @param user expected User type class.
     * @return mapped UserDto class.
     */
    public UserDto mapToUserDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    /**
     * Method mapping UserDto to User.
     *
     * @param userDto expected UserDto type class.
     * @return mapped User class.
     */
    public User mapToUser(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }

    /**
     * Method mapping Account to AccountDto.
     *
     * @param account expected Account type class.
     * @return mapped AccountDto class.
     */
    public AccountDto mapToAccountDto(Account account) {
        return mapper.map(account, AccountDto.class);
    }

    /**
     * Method mapping AccountDto to Account.
     *
     * @param accountDto expected AccountDto type class.
     * @return mapped Account class.
     */
    public Account mapToAccount(AccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    /**
     * Method mapping Bank to BankDto.
     *
     * @param bank expected Bank type class.
     * @return mapped BankDto class.
     */
    public BankDto mapToBankDto(Bank bank) {
        return mapper.map(bank, BankDto.class);
    }

    /**
     * Method mapping BankDto to Bank.
     *
     * @param bankDto expected BankDto type class.
     * @return mapped Bank class.
     */
    public Bank mapToBank(BankDto bankDto) {
        return mapper.map(bankDto, Bank.class);
    }

    /**
     * Method mapping Transaction to TransactionDto.
     *
     * @param transaction expected Transaction type class.
     * @return mapped TransactionDto class.
     */
    public TransactionDto mapToTransactionDto(Transaction transaction) {
        return mapper.map(transaction, TransactionDto.class);
    }

    /**
     * Method mapping TransactionDto to Transaction.
     *
     * @param transactionDto expected TransactionDto type class.
     * @return mapped Transaction class.
     */
    public Transaction mapToTransaction(TransactionDto transactionDto) {
        return mapper.map(transactionDto, Transaction.class);
    }
}
