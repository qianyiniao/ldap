package com.qianyiniao.um.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * Created by lilei on 2017/8/30.
 */
public interface DirAccess {

  public InitialDirContext getDirContext() throws NamingException;
  
  public void closeContext(InitialDirContext context);

  public Object addEntry(InitialDirContext context, String dn, Attributes attributes) throws NamingException;


  public void deleteEntry(InitialDirContext context, String dn) throws NamingException;

  public void updateEntry(InitialDirContext ctx, String dn, int mod_op, Attributes attributes) throws NamingException;

  public SearchResult searchEntry(InitialDirContext context, String baseDN, String filters) throws NamingException;

  public List<SearchResult> searchEntries(InitialDirContext context, String baseDN, String filters, int scope) throws NamingException;
  
  public boolean authentication(String dn, String password) throws NamingException;

  public List<String> searchRole(InitialDirContext context, String baseDN, String filter) throws NamingException;

}
