
package ase4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{
    private  String folder;
    private String folderName;
    private int port;
    public static ServerSocket server;
    public static String [] fileName;

    public Server(String folder, String folderName, int port) throws IOException {
        this.folder=folder;
        this.folderName=folderName;
        this.port=port;
        server = new ServerSocket(port);
    }

    private Server() {
        //To change body of generated methods, choose Tools | Templates.
    }

    public String getfolderName() {
        return folderName;
    }

    public int getPort() {
        return port;
    }


    @Override
    public void run(){
        try {
            System.out.println("Server for the file "+ folderName + " has been created ,the port is : " +port);
            File path = new File(folder + "\\" + folderName);
            fileName = path.list();
            System.out.println("The files of this folder are : ");
            for(int i=0; i<fileName.length; i++){
                System.out.println(fileName[i]);
            }
            System.out.println("Waiting for connection...");
            Socket socket = server.accept();
            System.out.println("Connection client-server succeeded via port " +port);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String question = "How many files you want to treat?";
            dos.writeUTF(question);//send the question to the client
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int nbr = dis.read();
            System.out.println("The client wants " + nbr + " files to be treated");
            System.out.println("Type the word you want to search for...");
            Scanner sc = new Scanner(System.in);
            String word = sc.nextLine();
            System.out.println( "The word " +word +" to be searched for has been sent the client");
            dos.writeUTF(word);
            for(int j=0;j<nbr;j++){
                String pathComplet = folder + "\\" +folderName+"\\"+fileName[j];// display the path of folders
                dos.writeUTF(pathComplet);
                String response=dis.readUTF();
                System.out.println(response);

            }
            String total = dis.readUTF();
            System.out.println(total);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public static void main(String[] args){
    new Thread(new Server()).start();
}
}
