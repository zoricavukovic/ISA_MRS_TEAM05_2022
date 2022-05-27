package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ComplaintReviewDTO;
import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.dto.users.NewAccountRequestDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
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
    public void sendActivationMessage(User u) throws InterruptedException {
        String subject = "Activate your profile.";

        String clientName = u.getFirstName();
        String clientSurname = u.getLastName();

        StringBuilder content = new StringBuilder();


        content.append("<b>BookingApp</b>")
                .append("\nDear ")
                .append(u.getFirstName())
                .append(" ")
                .append(u.getLastName())
                .append(",\n\n\t").append("Client ").append(clientName).append(" ").append(clientSurname).append(" posted review on your ")
                .append("\n\t -------------------------").append("\n\t Date: ")
                .append("\n\t -------------------------")
                .append("\n\n\nYour bookingApp.com");
        String html="<p>Hi!</p><a href=\"google.com\">Link text</a>";
        String h = "<!doctype html>\n" +
                "<html ⚡4email data-css-strict>\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <style amp4email-boilerplate>\n" +
                "    body {\n" +
                "      visibility: hidden\n" +
                "    }\n" +
                "  </style>\n" +
                "\n" +
                "  <script async src=\"https://cdn.ampproject.org/v0.js\"></script>\n" +
                "\n" +
                "\n" +
                "  <style amp-custom>\n" +
                "    .u-row {\n" +
                "      display: flex;\n" +
                "      flex-wrap: nowrap;\n" +
                "      margin-left: 0;\n" +
                "      margin-right: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .u-row .u-col {\n" +
                "      position: relative;\n" +
                "      width: 100%;\n" +
                "      padding-right: 0;\n" +
                "      padding-left: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .u-row .u-col.u-col-100 {\n" +
                "      flex: 0 0 100%;\n" +
                "      max-width: 100%;\n" +
                "    }\n" +
                "    \n" +
                "    @media (max-width: 767px) {\n" +
                "      .u-row:not(.no-stack) {\n" +
                "        flex-wrap: wrap;\n" +
                "      }\n" +
                "      .u-row:not(.no-stack) .u-col {\n" +
                "        flex: 0 0 100%;\n" +
                "        max-width: 100%;\n" +
                "      }\n" +
                "    }\n" +
                "    \n" +
                "    body {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    \n" +
                "    table,\n" +
                "    tr,\n" +
                "    td {\n" +
                "      vertical-align: top;\n" +
                "      border-collapse: collapse;\n" +
                "    }\n" +
                "    \n" +
                "    p {\n" +
                "      margin: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .ie-container table,\n" +
                "    .mso-container table {\n" +
                "      table-layout: fixed;\n" +
                "    }\n" +
                "    \n" +
                "    * {\n" +
                "      line-height: inherit;\n" +
                "    }\n" +
                "    \n" +
                "    table,\n" +
                "    td {\n" +
                "      color: #000000;\n" +
                "    }\n" +
                "    \n" +
                "    a {\n" +
                "      color: #0000ee;\n" +
                "      text-decoration: underline;\n" +
                "    }\n" +
                "  </style>\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;background-color: #f9f9f9;color: #000000\">\n" +
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse;vertical-align: top\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"color: #afb0c7; line-height: 170%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 14px; line-height: 23.8px;\">View Email in Browser</span></p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #ffffff;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                              <tr>\n" +
                "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "\n" +
                "                                  <amp-img alt=\"Image\" src=\"https://cdn.templates.unlayer.com/assets/1597218426091-xx.png\" width=\"537\" height=\"137\" layout=\"intrinsic\" style=\"width: 32%;max-width: 32%;\">\n" +
                "\n" +
                "                                  </amp-img>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </table>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                              <tr>\n" +
                "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "\n" +
                "                                  \n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </table>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"font-size: 14px; line-height: 140%;\"><strong>T H A N K S&nbsp; &nbsp;F O R&nbsp; &nbsp;S I G N I N G&nbsp; &nbsp;U P !</strong></p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 31px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 28px; line-height: 39.2px;\"><strong><span style=\"line-height: 39.2px; font-size: 28px;\">Verify Your E-mail Address </span></strong>\n" +
                "                                </span>\n" +
                "                              </p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #ffffff;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 22px; line-height: 35.2px;\">Hi, " + u.getFirstName() + " " + u.getLastName() + " </span></p>\n" +
                "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 18px; line-height: 28.8px;\">You're almost ready to get started. Please click on the button below to verify your email address and enjoy exclusive booking services with us! </span></p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div align=\"center\">\n" +
                "                              <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse;  font-family:'Cabin',sans-serif;\"><tr><td style=\"font-family:'Cabin',sans-serif;\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" style=\"height:46px; v-text-anchor:middle; width:234px;\" arcsize=\"8.5%\" stroke=\"f\" fillcolor=\"#ff6600\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Cabin',sans-serif;\"><![endif]-->\n" +
                "                              <a target=\"_blank\" href=\"http://localhost:3000/activatedAccount/" + u.getEmail()+ "\"  style=\"box-sizing: border-box;display: inline-block;font-family:'Cabin',sans-serif;text-decoration: none;text-align: center;color: #FFFFFF; background-color: #ff6600; border-radius: 4px;  width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; \">\n" +
                "                                <span style=\"display:block;padding:14px 44px 13px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">VERIFY YOUR EMAIL</span></strong>\n" +
                "                                </span>\n" +
                "                                </span>\n" +
                "                              </a>\n" +
                "                              <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px 60px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">Thanks,</span></p>\n" +
                "                              <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">The BookingApp Team</span></p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #e5eaf5;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +

                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <div style=\"padding: 0px;\">\n" +
                "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
                "              <div class=\"u-row\">\n" +
                "\n" +
                "                <div class=\"u-col u-col-100\">\n" +
                "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "\n" +
                "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                            <div style=\"color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">\n" +
                "                              <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights &copy; BookingApp All Rights Reserved</span></p>\n" +
                "                            </div>\n" +
                "\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <!--[if mso]></div><![endif]-->\n" +
                "  <!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
        HTMLMailService mm = (HTMLMailService) context.getBean("htmlMail");
        mm.sendMail(u.getEmail(), u.getEmail(), subject, h);
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

    @Async
    public void sendEmailAsAdminResponseFromNewAccountRequest(NewAccountRequestDTO d) throws InterruptedException {
        String subject = "Your account request is processed. Please check message.";

        String firstName = d.getUser().getFirstName();
        String lastName = d.getUser().getLastName();
        String statusMessage = "";
        String adminResponse = "";

        if (d.isAccepted())
            statusMessage = "Your account is successfuly approved. Welcome to bookingApp.com!";
        else
            statusMessage = "Your account request is not approved.";

        if (d.isAccepted())
            adminResponse = "Your account is successfuly approved. Welcome to bookingApp.com!";
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
                .append("\n\t Admin response: " + adminResponse)
                .append("\n\n\nYour bookingApp.com");

        sendToEmail(d.getUser().getEmail(), subject, content.toString());
    }
}

