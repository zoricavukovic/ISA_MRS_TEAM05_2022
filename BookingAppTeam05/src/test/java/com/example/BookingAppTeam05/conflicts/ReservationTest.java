package com.example.BookingAppTeam05.conflicts;

import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.service.ReservationService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
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
public class ReservationTest {

    @Autowired
    private BookingEntityService bookingEntityService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Transactional
    @Rollback(true)
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testReservationOptimisticLockException() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                BookingEntity bookingEntity = bookingEntityService.findBookingEntityById(1L);
                bookingEntity.setLocked(true);
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                bookingEntityService.save(bookingEntity);
                bookingEntity.setLocked(false);
                bookingEntityService.save(bookingEntity);
            }
        });
        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                BookingEntity bookingEntity = bookingEntityService.findBookingEntityById(1L);
                bookingEntity.setLocked(true);
                bookingEntityService.save(bookingEntity);
                bookingEntity.setLocked(false);
                bookingEntityService.save(bookingEntity);
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


    @Transactional
    @Rollback(true)
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testFastReservationOptimisticLockException() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                Reservation fastReservation = reservationService.findById(17L);
                fastReservation.setClient((Client) userService.findUserById(7L));
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                reservationService.save(fastReservation);
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                Reservation fastReservation = reservationService.findById(17L);
                fastReservation.setClient((Client) userService.findUserById(7L));
                reservationService.save(fastReservation);
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
