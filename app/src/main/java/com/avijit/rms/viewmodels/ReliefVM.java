package com.avijit.rms.viewmodels;

import com.avijit.rms.models.ReliefPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReliefVM {
    private String id;
    private String name;
    private String nid;
    private String contact;
    private Date date;
    private String familyMember;
    private Date receivedDate;
    private String receivedFrom;
    private List<Good> goods;


    public ReliefVM(String id, String name, String nid, String contact, Date date, String familyMember, Date receivedDate, String receivedFrom, List<Good> goods) {
        this.id = id;
        this.name = name;
        this.nid = nid;
        this.contact = contact;
        this.date = date;
        this.familyMember = familyMember;
        this.receivedDate = receivedDate;
        this.receivedFrom = receivedFrom;
        this.goods = goods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public static List<ReliefVM> getReliefs()
    {
        List<ReliefVM> reliefVMS = new ArrayList<ReliefVM>();
        List<Good> g = new ArrayList<>();
        g.add(new Good("Rice",5,"KG"));
        g.add(new Good("Egg",24,"pcs"));
        g.add(new Good("Soap",24,"PCS"));
        reliefVMS.add(new ReliefVM(
                "1234","Ram","12345","01878362888",new Date(),"4",new Date(),"Bidyananda",
                g
        ));
        reliefVMS.add(new ReliefVM(
                "1234","Sham","12345","01878362888",new Date(),"4",new Date(),"Bidyananda",
                g
        ));
        reliefVMS.add(new ReliefVM(
                "1234","Jodu","12345","01878362888",new Date(),"4",new Date(),"Bidyananda",
                g
        ));
        reliefVMS.add(new ReliefVM(
                "1234","Modhu","12345","01878362888",new Date(),"4",new Date(),"Bidyananda",
                g
        ));
        return reliefVMS;

    }


}
class Good {
    String product;
    int quantity;
    String unit;

    public Good(String product, int quantity, String unit) {
        this.product = product;
        this.quantity = quantity;
        this.unit = unit;
    }
}
