package com.whoisspy;
import com.whoisspy.client.Client;

import javax.swing.*;

/**
 *
 * @author Kevin Xu, B10609011
 *
 */
public class Main {

    public static void main(String[] args) {
//        if (args.length != 0) {
//            if (args[0].toLowerCase().equals("client")) {
//                System.out.println("opening client...");
//            } else if (args[0].toLowerCase().equals("server")) {
//                System.out.println("opening server...");
//            } else {
//                System.out.println("unknown args.");
//            }
//        } else if (args.length == 0) {
//            System.out.println("Please input 'client' or 'server' to args.");
//        }

        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        Client client = new Client("誰是臥底", "0.0.1");
        client.Start();
    }
}
