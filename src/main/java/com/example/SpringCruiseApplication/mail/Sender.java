package com.example.SpringCruiseApplication.mail;

import com.example.SpringCruiseApplication.entity.CruiseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class Sender {
    JavaMailSender javaMailSender;

    public Sender() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        javaMailSender = context.getBean(JavaMailSender.class);
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(javaMailSender.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }

    public void sendSimpleMessage(
            String[] to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(javaMailSender.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
    public void sendMessageAboutAccepting(CruiseRequest request) throws MessagingException {
        StringBuilder message = new StringBuilder();
        message.append("Hello, ").append(request.getSender().getName()+". Your request on cruise ")
                .append(request.getCruise().getRoute().portsString() +" on "+
                        request.getCruise().getDates().get(0))
                .append(" was just accepted. So your account balance was changed." +
                        " If you didn't have enough money on the balance. " +
                        "We recommend you to add some until your balance will equal or more then 0." +
                        " Ticket about cruise was added to your account. You can check in 'Tickets' section." +
                        "Have a nice day. ");
        String subject = "Your request about cruise was accepted";
        sendSimpleMessage(request.getSender().getEmail(),subject,message.toString());
    }
    public void sendMessageAboutRefusing(CruiseRequest request) throws MessagingException {
        StringBuilder message = new StringBuilder();
        message.append("Hello, ").append(request.getSender().getName()+". Your request on cruise ")
                .append(request.getCruise().getRoute().portsString() +" on "+
                        request.getCruise().getDates().get(0))
                .append(" was just refused. We are sorry, but we can't explain you details");
        String subject = "Your request about cruise was refused";
        sendSimpleMessage(request.getSender().getEmail(),subject,message.toString());
    }

    public void sendMes(String email) {
        StringBuilder message = new StringBuilder();
        message.append("Hello, ").append(email + ". Your request on cruise ")
                .append(" on ")
                .append(" was just accepted. So your account balance was changed." +
                        " If you didn't have enough money on the balance. " +
                        "We recommend you to add some until your balance will equal or more then 0." +
                        " Ticket about cruise was added to your account. You can check in 'Tickets' section." +
                        "Have a nice day. ");
        sendSimpleMessage(email, "Hello", message.toString());
    }
}
