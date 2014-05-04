package com.rc.droid_stalker_backend.exceptions;

/**
 * This is the base exception class for all the custom exceptions raised by droid_stalker
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/24/14
 * Time  : 12:30 PM
 */
public class DroidStalkerBaseException extends Exception {
    protected String mSystemMessage;
    protected String mUserMessage;

    /**
     * Constructor
     *
     * @param systemMessage
     * @param userMessage
     */
    protected DroidStalkerBaseException(final String systemMessage, final String userMessage) {
        mSystemMessage = systemMessage;
        mUserMessage = userMessage;
    }
}
