package com.clevertec.bankmanager.web.rest.controller.factory;


import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.service.TransactionService;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.service.factory.ServiceFactory;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestAccountCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestAccountsCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestCreateAccountCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestDeleteAccountCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestDepositCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestUpdateAccountCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.account.RestWithdrawCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.bank.RestBankCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.bank.RestBanksCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.bank.RestCreateBankCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.bank.RestDeleteBankCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.error.RestErrorCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestCreateTransactionCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestDeleteTransactionCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestTransactionCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestTransactionsCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestTransferCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.transaction.RestUpdateTransactionCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.user.RestCreateUserCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.user.RestDeleteUserCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.user.RestUpdateUserCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.user.RestUserCommand;
import com.clevertec.bankmanager.web.rest.controller.commands.user.RestUsersCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * RestCommandRegister used as factory for initialize commands.
 * Enum are available globally, and used as a singleton.
 */
@Getter
@RequiredArgsConstructor
public enum RestCommandRegister {
    //ERROR COMMANDS
    ERROR(new RestErrorCommand()),

    //ACCOUNT COMMANDS
    ACCOUNT(new RestAccountCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    ACCOUNTS(new RestAccountsCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    CREATE_ACCOUNT(new RestCreateAccountCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    UPDATE_ACCOUNT(new RestUpdateAccountCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    DELETE_ACCOUNT(new RestDeleteAccountCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    DEPOSIT(new RestDepositCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),
    WITHDRAW(new RestWithdrawCommand(ServiceFactory.INSTANCE.getService(AccountService.class))),

    //BANK COMMANDS
    BANK(new RestBankCommand(ServiceFactory.INSTANCE.getService(BankService.class))),
    BANKS(new RestBanksCommand(ServiceFactory.INSTANCE.getService(BankService.class))),
    CREATE_BANK(new RestCreateBankCommand(ServiceFactory.INSTANCE.getService(BankService.class))),
    UPDATE_BANK(new RestCreateBankCommand(ServiceFactory.INSTANCE.getService(BankService.class))),
    DELETE_BANK(new RestDeleteBankCommand(ServiceFactory.INSTANCE.getService(BankService.class))),

    //TRANSACTION COMMANDS
    TRANSACTION(new RestTransactionCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),
    TRANSACTIONS(new RestTransactionsCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),
    CREATE_TRANSACTION(new RestCreateTransactionCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),
    UPDATE_TRANSACTION(new RestUpdateTransactionCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),
    DELETE_TRANSACTION(new RestDeleteTransactionCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),
    TRANSFER(new RestTransferCommand(ServiceFactory.INSTANCE.getService(TransactionService.class))),

    //USER COMMANDS
    USER(new RestUserCommand(ServiceFactory.INSTANCE.getService(UserService.class))),
    USERS(new RestUsersCommand(ServiceFactory.INSTANCE.getService(UserService.class))),
    CREATE_USER(new RestCreateUserCommand(ServiceFactory.INSTANCE.getService(UserService.class))),
    UPDATE_USER(new RestUpdateUserCommand(ServiceFactory.INSTANCE.getService(UserService.class))),
    DELETE_USER(new RestDeleteUserCommand(ServiceFactory.INSTANCE.getService(UserService.class)));

    /** RestCommand used in constructor for create new commands */
    private final RestCommand command;
}
