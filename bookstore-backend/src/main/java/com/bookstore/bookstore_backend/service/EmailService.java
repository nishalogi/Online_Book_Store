package com.bookstore.bookstore_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.model.Order;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("logeshwarivijay16@gmail.com"); // ✅ Correct sender email

        mailSender.send(message);
    }

    public void sendOrderCancellationEmail(String toEmail, String orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Order Cancellation Notification");
            helper.setText("<p>Your order with <strong>Order ID: " + orderId + "</strong> has been canceled successfully.</p>", true);

            helper.setFrom("logeshwarivijay16@gmail.com"); // ✅ Use correct sender email

            mailSender.send(message);
            System.out.println("Cancellation email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(" Error while sending cancellation email.");
        }
    }
    
    public void sendOrderStatusUpdateEmail(Order order) {
        String subject = "Order Status Update - Order #" + order.getId();
        String message = "Your order status has been updated to: " + order.getStatus();
        sendEmail(order.getUser().getEmail(), subject, message);
    }

   

}