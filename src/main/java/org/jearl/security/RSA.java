/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.security;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.math.BigInteger;
import org.jearl.log.Logger;

public class RSA {

    private final BigInteger publicKey;
    private final BigInteger privateKey;
    private final BigInteger moduloKey;

    public RSA() {
        publicKey = new BigInteger("65537");
        privateKey = new BigInteger("1339927236302694497888086869475783392024951285060575289550217318769769520752422961687799136763137866667409947964937535834638915527049953081427178025279745");
        moduloKey = new BigInteger("8077153355920685182863461107876601928176897752852733880725955888540690312872838535921499381279020127872097854680069281435886046481413612272290701880835791");
    }

    public RSA(BigInteger publicKey, BigInteger privateKey, BigInteger moduloKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.moduloKey = moduloKey;
    }

    private String execute(String input, BigInteger key) {
        String[] splitInput = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < splitInput.length; i++) {
            result.append(execute(new BigInteger(splitInput[i]), key, moduloKey));
            result.append(' ');
        }

        return result.toString().trim();
    }

    private BigInteger execute(BigInteger input, BigInteger key,
            BigInteger moduloKey) {
        return input.modPow(key, moduloKey);
    }

    public String execute(String input) {


        return execute(convert(input).toString(), publicKey);
    }

    public String reverse(String input) {

        String temp = convert(new BigInteger(execute(input, privateKey)));
        return temp;
    }

    private String convert(BigInteger value) {
        return new String(value.toByteArray());
    }

    private BigInteger convert(String value) {
        return new BigInteger(value.getBytes());
    }

    private PropertyDescriptor[] getRequiredProperties() throws IntrospectionException {
        try {
            return new PropertyDescriptor[]{
                        new PropertyDescriptor("publicKey", RSA.class),
                        new PropertyDescriptor("privateKey", RSA.class),
                        new PropertyDescriptor("moduloKey", RSA.class)
                    };
        } catch (IntrospectionException ex) {
            Logger.getLogger(RSA.class).error(ex, ex);
        }

        return null;
    }
}
