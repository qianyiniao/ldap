package com.qianyiniao.um.ldap;


import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by lilei on 2017/8/30.
 */
public class DirAccessImpl implements DirAccess {

    private String ldapContext = "com.sun.jndi.ldap.LdapCtxFactory";

    private String ldapUrl ="ldap://60.205.206.96:389";

    private String ldapUser = "cn=Manager,dc=my-domain,dc=com";

    private String ldapPassword = "secret";

    @Override
    public InitialDirContext getDirContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(DirContext.INITIAL_CONTEXT_FACTORY, ldapContext);
        env.put(DirContext.PROVIDER_URL, ldapUrl);
        env.put(DirContext.SECURITY_PRINCIPAL, ldapUser);
        env.put(DirContext.SECURITY_CREDENTIALS, ldapPassword);
        return new InitialDirContext(env);
    }

    @Override
    public Object addEntry(InitialDirContext ctx, String dn, Attributes attributes) throws NamingException {
        return ctx.createSubcontext(dn, attributes);
    }

    @Override
    public void deleteEntry(InitialDirContext context, String dn) throws NamingException {
        context.destroySubcontext(dn);
    }

    @Override
    public void updateEntry(InitialDirContext ctx, String dn, int mod_op, Attributes attributes) throws NamingException {
        ctx.modifyAttributes(dn, mod_op, attributes);
    }

    @Override
    public SearchResult searchEntry(InitialDirContext ctx, String baseDN, String filters) throws NamingException {
        NamingEnumeration<SearchResult> results = null;
        try {
            results = ctx.search(baseDN, filters, setSerchControls(SearchControls.SUBTREE_SCOPE));
            if (results.hasMore()) {
                return results.next();
            }
            return null;
        } catch (Exception e) {
            throw new NamingException(e.getMessage());
        } finally {
            closeResult(results);
        }
    }

    @Override
    public List<SearchResult> searchEntries(InitialDirContext ctx, String baseDN, String filters, int scope)
            throws NamingException {
        ArrayList<SearchResult> entries = new ArrayList<SearchResult>();
        NamingEnumeration<SearchResult> results = null;
        try {
            results = ctx.search(baseDN, filters, setSerchControls(scope));
            while (results.hasMore()) {
                entries.add(results.next());
            }
            return entries;
        } catch (Exception e) {
            throw new NamingException(e.getMessage());
        } finally {
            closeResult(results);
        }
    }

    @Override
    public boolean authentication(String dn, String password) throws NamingException {
        InitialDirContext ctx = null;
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(DirContext.INITIAL_CONTEXT_FACTORY, ldapContext);
            env.put(DirContext.PROVIDER_URL, ldapUrl);
            env.put(DirContext.SECURITY_PRINCIPAL, dn);
            env.put(DirContext.SECURITY_CREDENTIALS, password);
            ctx = new InitialDirContext(env);
            return true;
        } catch(Exception e){
            return false;
        }finally {
            this.closeContext(ctx);
        }
    }

    @Override
    public List<String> searchRole(InitialDirContext context, String baseDN, String filter) throws NamingException {
        List<String> result = new ArrayList<>();
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(new String[]{LdapAttr.CN});
        NamingEnumeration<SearchResult> search = context.search(baseDN, filter, searchControls);
        while (search.hasMore()) {
            SearchResult searchResult = search.next();
            String roleName = (String) searchResult.getAttributes().get(LdapAttr.CN).get();
            result.add(roleName);
        }
        return result;
    }

    @Override
    public void closeContext(InitialDirContext context) {
        if (context != null)
            try {
                context.close();
            } catch (NamingException e) {
            }
    }

    private SearchControls setSerchControls(int scope) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(scope);
        searchControls.setReturningAttributes(null);
        searchControls.setReturningObjFlag(true);
        return searchControls;
    }

    private void closeResult(NamingEnumeration<SearchResult> results) {
        if (results != null)
            try {
                results.close();
            } catch (Exception e) {
        }
    }
}
