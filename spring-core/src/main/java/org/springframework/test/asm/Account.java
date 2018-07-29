package org.springframework.test.asm;

import java.io.Serializable;

public class Account implements Serializable {

    public void operation() {
        System.out.println("operation...");
    }
    public static void main(String args[]){
        Account a = new Account();
        a.operation();
    }
}
