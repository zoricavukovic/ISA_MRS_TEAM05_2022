package com.example.BookingAppTeam05.conflicts;

import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.users.UserService;
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
public class NewAccoutRequestTest {

    @Autowired
    private UserService userService;

    @Transactional
    @Rollback(true)
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testOptimisticLockException() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                User user = userService.findUserById(22L);
                user.setNotYetActivated(false);
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                userService.save(user);
            }
        });
        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                User user = userService.findUserById(22L);
                user.setNotYetActivated(false);
                userService.save(user);
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
