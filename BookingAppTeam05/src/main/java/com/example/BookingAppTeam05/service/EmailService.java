package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;
    private Environment env;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, Environment env){
        this.javaMailSender = javaMailSender;
        this.env = env;
    }

    @Async
    public void sendNotificaitionAsync(Reservation reservation, List<Client> subscribers) throws MailException, InterruptedException {
        System.out.println("Async metoda se izvrsava u drugom Threadu u odnosu na prihvaceni zahtev. Thread id: " + Thread.currentThread().getId());
        System.out.println("Slanje emaila...");
        for (Client client: subscribers) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(client.getEmail());
            mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
            mail.setSubject("New Hot Discounts are HERE for YOUR FAVOURITE " + reservation.getBookingEntity().getName());
            mail.setText("Dear " + client.getFirstName() + " " + client.getLastName() + ",\n\n\t" +
                            "You have chance to reserve your favourite " + reservation.getBookingEntity().getName() + "!" +
                    "\n\tFast reservation period: " + reservation.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")) + " - " + (reservation.getStartDate().plusDays(reservation.getNumOfDays())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))+
                    "\n\tMax number of persons: " + reservation.getNumOfPersons() +
                    "\n\tCost: €" + reservation.getCost() +
                    "\n\nCheck your profile to see this discount.\nYour bookingApp.com");

            javaMailSender.send(mail);
        }
        System.out.println("Email poslat!");
    }

}
