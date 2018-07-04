package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/26/2017.
 */

public class DocumentsItem {
   private String documentid;
    private String documenttype;
    private String documentno;
    private String documenturl;
    private String userid;

    public DocumentsItem(String documentid, String documenttype, String documentno, String documenturl, String userid) {
        this.documentid = documentid;
        this.documenttype = documenttype;
        this.documentno = documentno;
        this.documenturl = documenturl;
        this.userid = userid;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }

    public String getDocumentno() {
        return documentno;
    }

    public void setDocumentno(String documentno) {
        this.documentno = documentno;
    }

    public String getDocumenturl() {
        return documenturl;
    }

    public void setDocumenturl(String documenturl) {
        this.documenturl = documenturl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

