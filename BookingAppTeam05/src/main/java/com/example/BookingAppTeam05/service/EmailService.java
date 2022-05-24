package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ComplaintReviewDTO;
import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import java.time.format.DateTimeFormatter;
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

    @Async
    public void notifyOwnerAboutApprovedReviewOnHisEntity(RatingReviewDTO review) throws InterruptedException {
        String subject = "You have one more review on your entity. Please check message.";

        String clientName = review.getReservation().getClient().getFirstName();
        String clientSurname = review.getReservation().getClient().getLastName();

        StringBuilder content = new StringBuilder();

        content.append("Dear ")
                .append(review.getOwner().getFirstName())
                .append(" ")
                .append(review.getOwner().getLastName())
                .append(",\n\n\t").append("Client ").append(clientName).append(" ").append(clientSurname).append(" posted review on your ").append(review.getReservation().getBookingEntity().getName())
                .append("\n\t -------------------------").append("\n\t Date: ").append(review.getReviewDate()).append("\n\t Rating: ").append(review.getValue()).append("\n\t Comment: ").append(review.getComment())
                .append("\n\t -------------------------")
                .append("\n\n\nYour bookingApp.com");

        sendToEmail(review.getOwner().getEmail(), subject, content.toString());
    }

    @Async
    public void sendEmailAsAdminResponseFromComplaintToOwnerOrClient(ComplaintReviewDTO complaint, boolean sendToClient) throws InterruptedException {
        String subject = "Admin has reviewed complaint with id: " +  complaint.getId() + " . Check the response";
        StringBuilder content = new StringBuilder();
        String firstName = "";
        String lastName = "";
        String email = "";

        if (sendToClient) {
            firstName = complaint.getReservation().getClient().getFirstName();
            lastName = complaint.getReservation().getClient().getLastName();
            email = complaint.getReservation().getClient().getEmail();
        } else {
            firstName = complaint.getOwner().getFirstName();
            lastName = complaint.getOwner().getLastName();
            email = complaint.getOwner().getEmail();
        }

        content.append("Dear ")
                .append(firstName)
                .append(" ")
                .append(lastName)
                .append(",\n\n\t")
                .append("Complaint with id: ")
                .append(complaint.getId())
                .append(" has been reviewed by admin.")
                .append("\n\t Booking entity: ")
                .append(complaint.getReservation().getBookingEntity().getName())
                .append("\n\t Owner: " + complaint.getOwner().getFirstName() + " " + complaint.getOwner().getLastName())
                .append("\n\t Reserevation period: ")
                .append(complaint.getReservation().getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append(" - ")
                .append((complaint.getReservation().getStartDate().plusDays(complaint.getReservation().getNumOfDays())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")))
                .append("\n\t ______________________________")
                .append("\n\t Client complaint: ")
                .append(complaint.getClientComment())
                .append("\n\t Admin response: ")
                .append(complaint.getAdminResponse())
                .append("\n\t ______________________________")
                .append("\n\t")
                .append("\n\n\nYour bookingApp.com");
        sendToEmail(email, subject, content.toString());
    }

    @Async
    public void sendEmailAsAdminResponseFromComplaint(ComplaintReviewDTO complaint) throws InterruptedException {
        sendEmailAsAdminResponseFromComplaintToOwnerOrClient(complaint, true);
        sendEmailAsAdminResponseFromComplaintToOwnerOrClient(complaint, false);
    }

    @Async
    public void sendEmailAsAdminResponseFromDeleteAccountRequest(DeleteAccountRequestDTO d) throws InterruptedException {
        String subject = "Request on deleting your account is processed. Please check message.";

        String firstName = d.getUser().getFirstName();
        String lastName = d.getUser().getLastName();
        String statusMessage = "";
        String adminResponse = "";

        if (d.isAccepted())
            statusMessage = "Your account is successfuly deleted";
        else
            statusMessage = "Your request for deleting the account is not approved.";

        if (d.isAccepted())
            adminResponse = "Your account is successfuly deleted";
        else
            adminResponse = d.getAdminResponse();


        StringBuilder content = new StringBuilder();

        content.append("Dear ")
                .append(firstName)
                .append(" ")
                .append(lastName)
                .append(",\n\n\t")
                .append(statusMessage)
                .append("\n\t -------------------------")
                .append("\n\t Reason for deleting: " + d.getReason())
                .append("\n\t Admin response: " + adminResponse)
                .append("\n\n\nYour bookingApp.com");

        sendToEmail(d.getUser().getEmail(), subject, content.toString());
    }
}
