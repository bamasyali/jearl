/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.sms.service;

import java.util.List;
import org.jearl.sms.exception.SmsException;
import org.jearl.sms.model.SmsBody;

/**
 *
 * @author bamasyali
 */
public interface SmsService {

    void send(SmsBody sms, String text, List<String> numbers) throws SmsException;
}
