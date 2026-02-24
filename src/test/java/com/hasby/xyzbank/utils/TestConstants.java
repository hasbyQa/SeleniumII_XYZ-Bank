package com.hasby.xyzbank.utils;

// Central place for all test data
public final class TestConstants {

    private TestConstants() {} // prevent instantiation

    // Manager test data
    public static final String MGR_FIRST_NAME = "TestJohn";
    public static final String MGR_LAST_NAME = "TestDoe";
    public static final String MGR_POST_CODE = "E725JB";

    // Customer test data — different names to avoid collision
    public static final String CUST_FIRST_NAME = "CustTest";
    public static final String CUST_LAST_NAME = "User";
    public static final String CUST_FULL_NAME = CUST_FIRST_NAME + " " + CUST_LAST_NAME;
    public static final String CUST_POST_CODE = "12345";

    // Shared
    public static final String CURRENCY = "Dollar";
}