package com.qianyiniao.um.ldap;


public class DnBuilder {

    public static String USER_DN_TEXT = "uid=%s,ou=people,dc=my-domain,dc=com";
    public static String ALIAS_DN_TEXT = "uid=%s,ou=alias,o=ichson.com";
    public static String STAFF_DN_TEXT = "uid=%s,ou=staff,o=ichson.com";
    public static String ROLE_DN_TEXT = "cn=%s,ou=roles,%s";


    public static String userDn(String uid) {
        return String.format(USER_DN_TEXT, uid);
    }


}
