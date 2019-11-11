import com.qianyiniao.um.ldap.*;
import org.junit.Test;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.security.MessageDigest;


/**
 * Created by lilei on 2017/8/30.
 */
public class LdapTest{
    private static DirAccessImpl dirAccess = new DirAccessImpl();
    public static String user = "ou=people,dc=my-domain,dc=com";

    @Test
    public void testAuthentication(){
        InitialDirContext context = null;
        try {
            context = dirAccess.getDirContext();
            SearchResult searchResult = dirAccess.searchEntry(context, BaseDns.user, "uid=" + "lilei");
            boolean flag =  dirAccess.authentication(searchResult.getNameInNamespace(), "lileid");
            System.out.println(flag);
        } catch (Exception e) {
            System.out.println("error");
        } finally {
            dirAccess.closeContext(context);
        }
    }

    @Test
    public void testAdd(){
        InitialDirContext context = null;
        try{
            context = dirAccess.getDirContext();
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute(ObjectClass.OBJECT_CLASS_NAME);
            objclassSet.add(ObjectClass.user);
            attrsbu.put(objclassSet);
            attrsbu.put(LdapAttr.UID, "liutan");
            attrsbu.put(LdapAttr.CN, "刘郯");
            attrsbu.put(LdapAttr.SN, "刘郯");
            attrsbu.put(LdapAttr.USER_PASSWORD, "liutan");
            attrsbu.put(LdapAttr.MAIL, "136179746@qq.com");
            attrsbu.put(LdapAttr.MOBILE, "13699479211");

            Object a = dirAccess.addEntry(context, DnBuilder.userDn("liutan"), attrsbu);
            System.out.println(a);
        }catch (Exception e){
            dirAccess.closeContext(context);
        }
    }


    @Test
    public void testDelete(){
        InitialDirContext context = null;
        try{
            context = dirAccess.getDirContext();
            dirAccess.deleteEntry(context,DnBuilder.userDn("lilei"));
        }catch (Exception e){
            System.out.println("error");
            dirAccess.closeContext(context);
        }
    }

    @Test
    public void testUpdate(){
        InitialDirContext context = null;
        try{
            context = dirAccess.getDirContext();
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute(ObjectClass.OBJECT_CLASS_NAME);
            objclassSet.add(ObjectClass.user);
            attrsbu.put(objclassSet);
            attrsbu.put(LdapAttr.UID, "lilei");
            attrsbu.put(LdapAttr.CN, "lileiupdate");
            attrsbu.put(LdapAttr.SN, "lilei");
            attrsbu.put(LdapAttr.USER_PASSWORD, "lilei");
            SearchResult searchResult = dirAccess.searchEntry(context, BaseDns.user, "uid=" + "lilei");
            dirAccess.updateEntry(context, searchResult.getNameInNamespace(), InitialDirContext.REPLACE_ATTRIBUTE, attrsbu);
        }catch (Exception e){
            dirAccess.closeContext(context);
        }
    }

    @Test
    public void searchEntry(){
        InitialDirContext context = null;
        try {
            context = dirAccess.getDirContext();
            SearchResult searchResult = dirAccess.searchEntry(context, BaseDns.user, "uid=" + "lildei");
            Attributes attributes = searchResult.getAttributes();
            for (NamingEnumeration var = attributes.getAll();var.hasMoreElements();) {
                Object o =var.nextElement();
                System.out.println(o);
            }
        } catch (Exception e) {
            System.out.println("error");
        } finally {
            dirAccess.closeContext(context);
        }
    }

    @Test
    public void getPwd(){
        try {
            String password = "123456";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bs = md.digest();
//            byte[] base64MD5Password = Base64.encode(bs);
            System.out.println(new String(bs));
        }catch(Exception e){
            System.out.println("error");

        }
    }

}
