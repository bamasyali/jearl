/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.sms.service.impl;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.jearl.log.Logger;
import org.jearl.sms.exception.SmsException;
import org.jearl.sms.model.SmsBody;
import org.jearl.sms.service.SmsService;

/**
 *
 * @author can
 */
public class SmsServiceImpl implements SmsService {

    private static final Integer PORT = 80;
    private static final String HOST = "processor.smsorigin.com";
    private static final String PATH = "/xml/process.aspx";

    public SmsServiceImpl() {
    }

    public String getXmlString(SmsBody sms) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(SmsBody.class);
        Marshaller marshaller = jc.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(sms, writer);
        return writer.toString();
    }

    public void send(SmsBody sms, String text, List<String> numbers) throws SmsException {
        try {
            sms.setMesgbody(text);
            String num = "";
            for (int i = 0; i < numbers.size(); i++) {
                num += numbers.get(i);
                if (i < numbers.size() - 1) {
                    num += ",";
                }
            }
            sms.setNumbers(num);
            String xmlStr = getXmlString(sms);
            xmlStr = xmlStr.substring(xmlStr.indexOf('>') + 1);
            InetAddress addr = InetAddress.getByName(HOST);
            Socket sock = new Socket(addr, PORT);
            //Send header
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            wr.write("POST " + PATH + " HTTP/1.0\r\n");
            wr.write("Host: processor.smsorigin.com\r\n");
            wr.write("Content-Length: " + xmlStr.length() + "\r\n");
            wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
            wr.write("\r\n");

            //Send data
            wr.write(xmlStr);
            wr.flush();
            // Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Logger.getLogger(SmsServiceImpl.class).info(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(SmsServiceImpl.class).error(ex, ex);
            throw new SmsException(ex);
        }
    }
}
