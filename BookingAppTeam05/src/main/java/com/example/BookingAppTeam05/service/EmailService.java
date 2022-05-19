package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

    @Async
    public void sendToEmail(String email, String subject, String content) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject(subject);
        mail.setText(content);
        javaMailSender.send(mail);
    }

    @Async
    public void sendEmailAsAdminResponseFromReportToClientOrOwner(CreatedReportReviewDTO report, boolean sendToClient) throws InterruptedException {
        String subject = "Admin has reviewed report with id: " +  report.getId() + " . Check the response";
        StringBuilder content = new StringBuilder();
        String clientPenalizedByAdmin = "";
        String firstName = "";
        String lastName = "";
        String email = "";

        if (report.isAdminPenalizeClient()) {
            clientPenalizedByAdmin = "Admin approved that client should be penalized with 1 point.";
        } else {
            clientPenalizedByAdmin = "Admin did not approved that client should be panalized with 1 point";
        }
        if (sendToClient) {
            firstName = report.getReservation().getClient().getFirstName();
            lastName = report.getReservation().getClient().getLastName();
            email = report.getReservation().getClient().getEmail();
        } else {
            firstName = report.getOwner().getFirstName();
            lastName = report.getOwner().getLastName();
            email = report.getOwner().getEmail();
        }

        content.append("Dear ")
                .append(firstName)
                .append(" ")
                .append(lastName)
                .append(",\n\n\t")
                .append("Report with id: ")
                .append(report.getId())
                .append(" has been reviewed by admin.")
                .append("\n\t Booking entity: ")
                .append(report.getReservation().getBookingEntity().getName())
                .append("\n\t Reserevation period: ")
                .append(report.getReservation().getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append(" - ")
                .append((report.getReservation().getStartDate().plusDays(report.getReservation().getNumOfDays())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append("\n\t ______________________________")
                .append("\n\t Owner comment: ")
                .append(report.getOwnerComment())
                .append("\n\t Admin response: ")
                .append(report.getAdminResponse())
                .append("\n\t ______________________________")
                .append("\n\t")
                .append(clientPenalizedByAdmin)
                .append("\n\t")
                .append("Note: clients with 3 penalty points can't reserve any entity until next mount")
                .append("\n\n\nYour bookingApp.com");
        sendToEmail(email, subject, content.toString());
    }

    @Async
    public void sendEmailAsAdminResponseFromReport(CreatedReportReviewDTO report) throws InterruptedException {
        sendEmailAsAdminResponseFromReportToClientOrOwner(report, true);
        sendEmailAsAdminResponseFromReportToClientOrOwner(report, false);
    }

    @Async
    public void notifyClientThatHeDidNotCome(Report report) throws InterruptedException {
        String subject = "You did not come to the reservation. Please check message.";
        int currPenaltyPoints = report.getReservation().getClient().getPenalties();

        StringBuilder content = new StringBuilder();

        content.append("Dear ")
                .append(report.getReservation().getClient().getFirstName())
                .append(" ")
                .append(report.getReservation().getClient().getLastName())
                .append(",\n\n\t").append("In report with id: ").append(report.getId()).append(", owner selected that you did not come to reservation.")
                .append("\n\t -------------------------")
                .append("\n\t Booking entity: ")
                .append(report.getReservation().getBookingEntity().getName())
                .append("\n\t Reservation period: ")
                .append(report.getReservation().getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append(" - ")
                .append((report.getReservation().getStartDate().plusDays(report.getReservation().getNumOfDays())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append("\n\t ______________________________").append("\n\t You currently have ").append(currPenaltyPoints).append(" point(s).")
                .append("\n\tNote: clients with 3 penalty points can't reserve any entity until next mount")
                .append("\n\n\t If you think this is mistake, please contact our info centar.")
                .append("\n\n\nYour bookingApp.com");

        sendToEmail(report.getReservation().getClient().getEmail(), subject, content.toString());
    }

}
