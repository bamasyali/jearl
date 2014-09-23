/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.sms.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bamasyali
 */
@XmlRootElement(name = "MainmsgBody")
public final class SmsBody {

    @XmlElement(name = "Command")
    private String command;
    @XmlElement(name = "PlatformID")
    private String platformID;
    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "PassWord")
    private String passWord;
    @XmlElement(name = "ChannelCode")
    private String channelCode;
    @XmlElement(name = "Mesgbody")
    private String mesgbody;
    @XmlElement(name = "Numbers")
    private String numbers;
    @XmlElement(name = "Type")
    private String type;
    @XmlElement(name = "Originator")
    private String originator;
    @XmlElement(name = "SDate")
    private String sDate;
    @XmlElement(name = "EDate")
    private String eDate;
    @XmlElement(name = "Concat")
    private String concat;

    @XmlTransient
    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    @XmlTransient
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @XmlTransient
    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat;
    }

    @XmlTransient
    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    @XmlTransient
    public String getMesgbody() {
        return mesgbody;
    }

    public void setMesgbody(String mesgbody) {
        this.mesgbody = mesgbody;
    }

    @XmlTransient
    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    @XmlTransient
    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    @XmlTransient
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @XmlTransient
    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    @XmlTransient
    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    @XmlTransient
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlTransient
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
