/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.lotus;

import de.bea.domingo.*;
import java.util.List;
import org.jearl.email.EMailSender;
import org.jearl.email.exception.EMailException;
import org.jearl.email.lotus.model.LotusProperties;
import org.jearl.email.model.EMail;
import org.jearl.email.model.EMailLogger;
import org.jearl.logback.ErrorCategory;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;

/**
 *
 * @author bamasyali
 */
public class LotusSender implements EMailSender<LotusProperties> {

    private final JearlLogger logger;

    public LotusSender() {
        this.logger = Logger.getJearlLogger(LotusSender.class);
    }

    @Override
    public void sendMail(LotusProperties properties, EMail email, Boolean isThreaded) throws EMailException {

        DDatabase database = connectLotusNotes(properties);
        try {
            DRichTextItem drti = null;
            DDocument d = database.createDocument();
            try {
                drti = d.createRichTextItem("Body");
                drti.addNewLine(2);
                drti.appendText(email.getEmailTopic());
                drti.addNewLine(2);
                drti.appendText(email.getEmailContent());
                drti.addNewLine(3);
                drti.appendText("");
                drti.addNewLine();

                d.setSaveMessageOnSend(true);
                d.appendItemValue("Subject", email.getEmailTopic());
                d.appendItemValue("SendTo", email.getTo());
                d.appendItemValue("CopyTo", email.getCc());
                d.appendItemValue("replayTo", email.getReplyTo());

                for (String s : email.getTo()) {
                    d.send(s);
                }

                logger.info("Sent");

            } catch (Exception e) {
                logger.error(e, ErrorCategory.DATABASE_ERROR, e);
            }
        } catch (Exception e1) {
            logger.error(e1, ErrorCategory.DATABASE_ERROR, e1);
        }
    }

    private DDatabase connectLotusNotes(LotusProperties properties) {
        DDatabase dDatabase = null;
        DNotesFactory factory = DNotesFactory.getInstance();
        DSession session;
        try {
            session = factory.getSession(properties.getHostName(), properties.getUserName(), properties.getPassword());
            dDatabase = session.getDatabase(properties.getHostName(), properties.getDatabase());
        } catch (Exception e) {
            logger.error(e, ErrorCategory.DATABASE_ERROR, e);
        }
        if (dDatabase.isOpen()) {
            //Logger.getLogger(LotusSender.class).error("Lotus Notes Database opened");
            return dDatabase;
        } else {
            //Logger.getLogger(LotusSender.class).error("Lotus Notes Database not opened");
            return null;
        }
    }

    @Override
    public void sendMail(LotusProperties properties, List<EMail> emails) throws EMailException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMail(LotusProperties properties, List<EMail> emails, Integer threadNumber) throws EMailException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(EMailLogger logger, List<EMail> emails) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
