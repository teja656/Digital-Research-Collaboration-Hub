package com.researchsphere.util;

/**
 * Central session and cookie attribute names.
 */
public final class SessionConstants {

    public static final String SESSION_USER = "loggedInUser";
    public static final String COOKIE_REMEMBER = "rs_remember_email";
    public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 7 days

    private SessionConstants() {
    }
}
