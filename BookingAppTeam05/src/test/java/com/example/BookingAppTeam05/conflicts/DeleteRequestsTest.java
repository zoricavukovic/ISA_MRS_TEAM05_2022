package com.example.BookingAppTeam05.conflicts;

import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.service.DeleteAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteRequestsTest {

    @Autowired
    private DeleteAccountService deleteAccountService;

    @Transactional
    @Rollback(true)
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testOptimisticLockException() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                DeleteAccountRequest request = deleteAccountService.findById(1L);
                request.setProcessed(true);
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                deleteAccountService.save(request);
            }
        });
        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                DeleteAccountRequest request = deleteAccountService.findById(1L);
                request.setProcessed(true);
                deleteAccountService.save(request);
            }
        });
        try {
            future1.get();
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass());
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
