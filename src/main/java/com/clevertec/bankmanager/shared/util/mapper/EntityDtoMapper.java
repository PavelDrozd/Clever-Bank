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

public class EntityDtoMapper {

    private final ModelMapper mapper;
    private static final EntityDtoMapper INSTANCE = new EntityDtoMapper();

    private EntityDtoMapper() {
        mapper = new ModelMapper();
    }

    public static EntityDtoMapper getInstance() {
        return INSTANCE;
    }

    public UserDto mapToUserDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    public User mapToUser(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }

    public AccountDto mapToAccountDto(Account account) {
        return mapper.map(account, AccountDto.class);
    }

    public Account mapToAccount(AccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    public BankDto mapToBankDto(Bank bank) {
        return mapper.map(bank, BankDto.class);
    }

    public Bank mapToBank(BankDto bankDto) {
        return mapper.map(bankDto, Bank.class);
    }

    public TransactionDto mapToTransactionDto(Transaction transaction) {
        return mapper.map(transaction, TransactionDto.class);
    }

    public Transaction mapToTransaction(TransactionDto transactionDto) {
        return mapper.map(transactionDto, Transaction.class);
    }
}
