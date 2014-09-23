/*
 * to change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.smtp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.jearl.ejb.model.email.EMailRecipient;
import org.jearl.ejb.model.email.EMailRecipientTypeEnum;
import org.jearl.email.EMailSender;
import org.jearl.email.exception.EMailException;
import org.jearl.email.model.EMail;
import org.jearl.email.model.EMailLogger;
import org.jearl.email.smtp.model.SmtpProperties;
import org.jearl.logback.ErrorCategory;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;

/**
 *
 * @author bamasyali
 */
public class SmtpSender implements EMailSender<SmtpProperties> {

    private final JearlLogger logger;

    public SmtpSender() {
        this.logger = Logger.getJearlLogger(SmtpSender.class);
    }

    @Override
    public void sendMail(final SmtpProperties properties, EMail email, Boolean isThreaded) throws EMailException {

        if (email.getFrom() == null) {
            email.setFrom(properties.getUsername());
        }

        if (email.getTo().isEmpty() && email.getCc().isEmpty() && email.getBcc().isEmpty()) {
            throw new EMailException("Need Recipients!!!");
        }

        if (email.getEmailTopic() == null) {
            throw new EMailException("Need Topic!!!");
        }

        if (email.getEmailContent() == null) {
            throw new EMailException("Need Content!!!");
        }

        if (email.getReplyTo() == null) {
            email.setReplyTo(properties.getUsername());
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", properties.getHost());
        props.put("mail.from", email.getFrom());
        props.put("mail.smtp.port", properties.getPort());
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", properties.getTls());
        //props.put("mail.debug", "true");
        props.put("mail.smtp.auth", properties.getAuth());
        props.put("mail.smtp.ssl.enable", properties.getSsl());

        class PopupAuthenticator extends Authenticator {

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getUsername(), properties.getPassword());
            }
        }

        Authenticator authenticator = null;
        if (properties.getAuth().equals("true")) {
            authenticator = new PopupAuthenticator();
        }
        Session session = Session.getInstance(props, authenticator);
        //session.setDebug(true);
        try {
            final MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            msg.setReplyTo(InternetAddress.parse(email.getReplyTo()));
            Integer sizeOfTo = email.getSizeOfTo();
            for (int i = 0; i < sizeOfTo; i++) {
                InternetAddress address = new InternetAddress(email.getTo().get(i));
                msg.addRecipient(MimeMessage.RecipientType.TO, address);
            }
            Integer sizeOfCc = email.getSizeOfCc();
            for (int i = 0; i < sizeOfCc; i++) {
                InternetAddress address = new InternetAddress(email.getCc().get(i));
                msg.addRecipient(MimeMessage.RecipientType.CC, address);
            }
            Integer sizeOfBcc = email.getSizeOfBcc();
            for (int i = 0; i < sizeOfBcc; i++) {
                InternetAddress address = new InternetAddress(email.getBcc().get(i));
                msg.addRecipient(MimeMessage.RecipientType.BCC, address);
            }

            msg.setSubject(email.getEmailTopic(), "utf-8");
            msg.setSentDate(new Date(System.currentTimeMillis()));


            Multipart multipart = new MimeMultipart();

            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(email.getEmailContent(), "text/html; charset=utf-8");
            multipart.addBodyPart(mbp1);

            for (File file : email.getFileList()) {
                MimeBodyPart mbp2 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
                multipart.addBodyPart(mbp2);
            }

            msg.setContent(multipart);
            if (isThreaded) {
                Thread thread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            Transport.send(msg);
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ErrorCategory.EMAIL_ERROR, ex);
                        }
                    }
                };
                thread.start();
            } else {
                Transport.send(msg);
            }
        } catch (Exception mex) {
            // Prints all nested (chained) exceptions as well
            throw new EMailException("EMail Sent Error : " + mex.getMessage(), mex);
        }
    }

    @Override
    public void sendMail(SmtpProperties properties, List<EMail> emails) throws EMailException {
        for (EMail email : emails) {
            sendMail(properties, email, false);
        }
    }

    @Override
    public void sendMail(final SmtpProperties properties, List<EMail> emails, Integer threadNumber) throws EMailException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadNumber);
        for (final EMail email : emails) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        sendMail(properties, email, false);
                    } catch (EMailException ex) {
                        logger.error(ex.getMessage(), ErrorCategory.EMAIL_ERROR, ex);
                    }
                }
            };
            executor.execute(runnable);
        }
    }

    @Override
    public void log(EMailLogger logger, List<EMail> emails) {
        for (EMail email : emails) {
            try {
                org.jearl.ejb.model.email.EMail mail = logger.initEmail();
                mail.setMailContent(email.getEmailContent());

                mail.setMailSenderip(logger.getUserIp());
                mail.setMailSenttime(new Date(System.currentTimeMillis()));
                mail.setMailTopic(email.getEmailTopic());
                mail.setMailUser(logger.getUser());
                mail.setMailAccount(logger.getEMailAccount());
                List<EMailRecipient> recipients = new ArrayList<EMailRecipient>();
                if (email.getTo() != null) {
                    List<String> strings = email.getTo();
                    for (String to : strings) {
                        EMailRecipient recipient = logger.initRecipient();
                        recipient.setRcpAdress(to);
                        recipient.setRcpMail(mail);
                        recipient.setRcpRecipientType(logger.initRecipientType(EMailRecipientTypeEnum.TO));
                        recipients.add(recipient);
                    }
                }
                if (email.getCc() != null) {
                    List<String> strings = email.getCc();
                    for (String to : strings) {
                        EMailRecipient recipient = logger.initRecipient();
                        recipient.setRcpAdress(to);
                        recipient.setRcpMail(mail);
                        recipient.setRcpRecipientType(logger.initRecipientType(EMailRecipientTypeEnum.CC));
                        recipients.add(recipient);
                    }
                }
                if (email.getBcc() != null) {
                    List<String> strings = email.getBcc();
                    for (String to : strings) {
                        EMailRecipient recipient = logger.initRecipient();
                        recipient.setRcpAdress(to);
                        recipient.setRcpMail(mail);
                        recipient.setRcpRecipientType(logger.initRecipientType(EMailRecipientTypeEnum.BCC));
                        recipients.add(recipient);
                    }
                }
                mail.setMailFrom(email.getFrom());
                mail.setMailRecipientList(recipients);
                logger.getEntityManager().persist(mail);

            } catch (Exception ex) {
                //logger.
            }
        }
    }
}
