package com.example.paz.officewar; /**
 * Created by paz on 30/04/16.
 */
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class TcpClient {
    public static final byte IMG = 0x01;
    public static final byte HIT = 0x02;

    private String serverMessage;
    public static final String SERVERIP = "10.0.0.11"; //your computer IP address
    public static final int SERVERPORT = 5008;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    //BufferedReader in;
    DataInputStream in;
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public String receiveMesage(){
        String buffer = "";
        try {

            buffer = (in.readLine()); // Read one line and output it

            in.close();
        } catch (Exception e) {

        }
        return buffer;
    }
    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                in = new DataInputStream(socket.getInputStream());
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    boolean end = false;
                    byte[] b = new byte[921601];
                    int[] c = new int[921601];
                    int j = 0;
                    while(!end) {
                        in.read(b,0,1);
                        if(b[0] == IMG) {
                            c[0] = IMG;
                            j++;
                            while(!end) {
                                in.read(b,0,100);
                                for (int i = 0; i < 100; i++) {
                                    c[j] = b[i] & 0xFF;
                                    j++;
                                }

                                if (j == 921601) {
                                    end = true;
                                }
                            }
                        }else if(b[0] == HIT) {
                            c[0] = HIT;
                            j++;
                            in.read(b,0,1);
                            c[1] = b[0] & 0xff;
                            end = true;
                        }
                    }


                    if (c != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class

                        mMessageListener.messageReceived(c);

                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(int[] message);
    }
}