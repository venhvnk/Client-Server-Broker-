
package ase4;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Broker extends Thread{
public static String [] folderName;
public static int port = 9090;

//function to initialise folders ports 
public int search(String[] arr, String key){
    for(int j=0;j<arr.length;j++){
        if(key.equalsIgnoreCase(arr[j])){
            return j;
        }

    }
    return -1;
}


@Override
public void run(){
    try {
        System.out.println("Please enter the folder's path");
        Scanner sc = new Scanner(System.in);
        String folder=sc.nextLine();
        File path = new File(folder);
        //store file content in an array
        folderName = path.list();
        for(int j=0; j<folderName.length;j++){
            System.out.println(  j + " " + folderName[j]); //displaying available folders

        }
        //create server socket of broker
        ServerSocket broker = new ServerSocket(port);
        System.out.println(  "Waiting for the client to connect....");
        Socket bsocket = broker.accept(); //waiting for the client to connect then create a broker socket
        System.out.println("connection succeeded" );
        PrintWriter pw = new PrintWriter(bsocket.getOutputStream(),true);
        pw.println("Please choose a folder...");
        pw.println(Arrays.toString(folderName));//hedi li dir display l folders
        BufferedReader bw = new BufferedReader(new InputStreamReader(bsocket.getInputStream())); 
        String choice = bw.readLine();
        //reading the folder's index
        int result = search(folderName, choice);
        if(result == -1){
            System.err.println( choice + " : Sorry,file doesn't exist.");

        }
        else{

            System.out.println("The client has chosen the file " + choice);
            Server s = new Server (folder,folderName[result],result+1);
            s.start();
            //sendd the port to client
            try (DataOutputStream dos = new DataOutputStream(bsocket.getOutputStream())) {
                dos.write(result+1);
            }
            pw.close();
            bsocket.close();
        }
    } catch (IOException ex) {
        Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void main(String[] args){
    new Thread(new Broker()).start();
}
}
