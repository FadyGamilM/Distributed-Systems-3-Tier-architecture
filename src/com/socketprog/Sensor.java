package com.socketprog;
import java.net.*;
import java.io.*;
public class Sensor {
    public static String getRecommendations(){
        return new String("Go To Road 32 then Road 42 then take the first U-turn\nRequired Time Is 22 minutes\nState of Streets is High Traffic.");
    }
    public static void main(String args[])/*throws IOException*/{
        try{
            ServerSocket sensor_server = new ServerSocket(33000);

            System.out.println("sensors are running up on port 33000 ...");
            while (true)
            {
                Socket client2 = sensor_server.accept();

                DataInputStream sensors_is = new DataInputStream(client2.getInputStream());
                DataOutputStream sensors_os = new DataOutputStream(client2.getOutputStream());

                while (true){
                    String server_request = sensors_is.readUTF();
                    System.out.println("[SERVER] : "+server_request);
                    sensors_os.writeUTF(getRecommendations());
                    System.out.println("Readings are sent to server successfully..");
                    if (server_request.equals("close"))
                        break;
                }
                sensors_is.close();
                sensors_os.close();
                client2.close();
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
