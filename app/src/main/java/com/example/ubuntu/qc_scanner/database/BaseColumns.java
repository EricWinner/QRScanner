package com.example.ubuntu.qc_scanner.database;

/**
 * Created by ubuntu on 17-8-12.
 */

public class BaseColumns {

    //table:qrdata columns
    public static final String QRDATA_ID = "_id";
    public static final String QRDATA_FOREIGN_GROUP_ID = "_foreign_group_id";
    public static final String QRDATA_NUMBER_ID = "_data_number";
    public static final String QRDATA_DATE = "_data_date";
    public static final String QRDATA_TOTAL_AMOUNT = "_total_amount";
    public static final String QRDATA_PEAK_VALUE = "_peak_value";
    public static final String QRDATA_VALLEY_VALUE = "_valley_value";

    //add columns
    public static final String QRDATA_CASE_NUMBER = "_case_number";
    public static final String QRDATA_CASE_NAME   = "_case_name";
    public static final String QRDATA_CASE_TYPE   = "_case_type";

    //table:qrgroup columns
    public static final String QRDATA_GROUP_ID = "_group_id";
    public static final String QRDATA_GROUP_NAME = "_group_name";


}
