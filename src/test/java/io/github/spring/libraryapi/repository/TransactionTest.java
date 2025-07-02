package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionTest {

    @Autowired
    TransactionService transactionService;


    /**
     * Commit -> confirm transaction
     * Rollback -> undo operations
     */

    void transactionTest(){
       transactionService.run();
    }


    void transactionManageState(){
        transactionService.updateWithoutUpdate();
    }
}
