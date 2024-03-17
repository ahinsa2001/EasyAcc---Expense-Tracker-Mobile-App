package com.example.easyacc;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {
    private String type, references, note;
    private Date date;
    private double amount = 0%.2f;

    @PrimaryKey
    private long id;

    public Transaction() {

    }

    public Transaction(String type, String references, String note, Date date, double amount, long id) {
        this.type = type;
        this.references = references;
        this.note = note;
        this.date = date;
        this.amount = amount;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getReferences() {
        return references;
    }

    public String getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(long id) {
        this.id = id;
    }
}
