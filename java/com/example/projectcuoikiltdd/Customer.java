package com.example.projectcuoikiltdd;

public class Customer extends User {

    public Customer() {
        super();
    }

    @Override
    public String displayRole() {
        return "Khách hàng";
    }
    public void updatePassword(String oldPass, String newPass, Account.Callback cb) {
        new Account().changePassword(oldPass, newPass, cb);
    }
}
