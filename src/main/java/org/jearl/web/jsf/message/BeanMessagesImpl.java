/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf.message;

/**
 *
 * @author bamasyali
 */
public class BeanMessagesImpl implements BeanMessages {

    private Message defaultErrorMessage = new Message("Error", "System Error");

    @Override
    public Message getSetIdSuccessMessage() {
        return null;
    }

    @Override
    public Message getSetIdErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getSetEntitySuccessMessage() {
        return null;
    }

    @Override
    public Message getCreateSuccessMesage() {
        return null;
    }

    @Override
    public Message getSetEntityErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getEditSuccessMessage() {
        return null;
    }

    @Override
    public Message getCreateErrorMesage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getEditErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getRemoveSuccessMessage() {
        return null;
    }

    @Override
    public Message getRemoveErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getRemovePyhsicalSuccessMessage() {
        return null;
    }

    @Override
    public Message getRemovePyhsicalErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getReloadSuccessMessage() {
        return null;
    }

    @Override
    public Message getReloadErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getReloadNoChangeSuccessMessage() {
        return null;
    }

    @Override
    public Message getReloadNoChangeErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getFindAllSuccessMessage() {
        return null;
    }

    @Override
    public Message getFindAllErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getHandleFileUploadSuccessMesage() {
        return null;
    }

    @Override
    public Message getHandleFileUploadErrorMesage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getRemoveDocumentSuccessMessage() {
        return null;
    }

    @Override
    public Message getRemoveDocumentErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getFileListSuccessMessage() {
        return null;
    }

    @Override
    public Message getFileListErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getGetDocumentValueErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getSetDocumentValueErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getMakeFavoriteSuccessMessage() {
        return null;
    }

    @Override
    public Message getMakeFavoriteErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getGetFavoriteListErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getGetFavoriteErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getRemoveFavoriteSuccessMessage() {
        return null;
    }

    @Override
    public Message getRemoveFavoriteErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public Message getCreateReportSuccessMessage() {
        return null;
    }

    @Override
    public Message getCreateReportErrorMessage() {
        return defaultErrorMessage;
    }
}
