/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ase4;

import static ase4.Broker.folderName;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author azzouz abdennour
 */
public class Client extends Thread {
public static String localhost = "127.0.0.1";
public static int port = 9090;


@Override
public void run(){
    try {
        String data;
        String[] words;
        int cmpt =0;
        int global =0;
        Socket csocket = new Socket(localhost,port);
        BufferedReader bw = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
        String question = bw.readLine();
        String list = bw.readLine();
        System.out.println( question);
        System.out.println( list);
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        PrintWriter pw = new PrintWriter(csocket.getOutputStream(), true);
        pw.println(choice);
        DataInputStream dis = new DataInputStream(csocket.getInputStream());
        int newport = dis.read();//receiving the port sent by the broker
        System.out.println("The server's port is  " + newport);
        System.out.println("Do you want to connect to the server ?");
        System.out.println("Yes");
        System.out.println("No");
        String reponse = sc.nextLine();
        if(reponse.equalsIgnoreCase("Yes")){
            csocket = new Socket(localhost,newport);
            DataInputStream ds = new DataInputStream(csocket.getInputStream());
            String request = ds.readUTF();//receiving the question
            System.out.println(request );
            int nbr = sc.nextInt();
            DataOutputStream dos = new DataOutputStream(csocket.getOutputStream());
            dos.write(nbr);//sending number of files to be treaed to the server
            System.out.println("Waiting for the wanted word.....");
            String word = ds.readUTF();//send the wanted word
            System.out.println("Here is the wanted word: " +  word);
            System.out.println("The treated files are :");
            
            for(int i=0;i<nbr;i++){
                String pathComplet=ds.readUTF();//receives file's path
                System.out.println(i + " " + pathComplet);

                File file = new File(pathComplet);
                BufferedReader reader = new BufferedReader(new FileReader(file));//for reading files
                while((data=reader.readLine())!=null){
                    words = data.split(" ");
                    for(int j=0; j<words.length;j++){
                        if(word.equalsIgnoreCase(words[j])){
                            cmpt+=1;
                        }
                    }
                }
                String response = "The occuranece number in the file : " + i +" is : " + cmpt;
                dos.writeUTF(response);
                global = global +cmpt;
            }
            String total = "Total number of the word " +word + " is : "+ global ;
            dos.writeUTF(total);
        }
        else{
            System.err.println("Connection Error ");
            csocket.close();
        }



    } catch (IOException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public static void main(String[] args){
    new Thread(new Client()).start();
}
}
