package com.socketprog;
import java.net.*;
import java.io.*;

public class Server {
    public static void main (String args[]) throws IOException{
        /** SERVER-CLIENT SOCKET */
        ServerSocket server = new ServerSocket(22000);
        /** SENSOR-SERVER SOCKET **/
        Socket Server_as_client_to_sensor= new Socket("127.0.0.1",33000);

        System.out.println("Server Running up on port 22000 ...");
        while (true){
            /** SERVER IS WAITING FOR ANY CLIENT CONNECTION ... **/
            Socket client = server.accept();
            /** INPUT AND OUTPUT STREAM BETWEEN SERVER AND CLIENT */
            DataInputStream is = new DataInputStream(client.getInputStream());
            DataOutputStream os = new DataOutputStream(client.getOutputStream());

            /** INPUT AND OUTPUT STREAM BETWEEN SENSORS AND CLIENTS */
            DataInputStream sensor_is = new DataInputStream(Server_as_client_to_sensor.getInputStream());
            DataOutputStream sensor_os = new DataOutputStream(Server_as_client_to_sensor.getOutputStream());

            while (true){
                os.writeUTF("Connection Is Established Successfully, How Can I Help You ? ");
                String Request=is.readUTF();
                System.out.println("[CLIENT] : "+Request);


                os.writeUTF("Got It, But We Need More Info Before Sending You The Best Route.\n[1] Enter Your Location: ");
                os.flush();
                String location = is.readUTF();
                System.out.println("[CLIENT] : "+location);

                os.writeUTF("[2] Enter Your Destination: ");
                os.flush();
                String destination = is.readUTF();
                System.out.println("[CLIENT] : "+destination);


                sensor_os.writeUTF("Please Send Me The Best Recommendation for this user, The required time for the trip, and the state of streets right now.");
                sensor_os.flush();
                String readings = sensor_is.readUTF();
                System.out.println("[SENSOR] : "+readings);


                os.writeUTF("recommendation for the best route is: "+readings+"\nFor New Request Enter [YES]\n To End This Session Enter [NO]");
                os.flush();
                String decision = is.readUTF();
                System.out.println("[CLIENT] : "+decision);

                if (decision.equalsIgnoreCase("NO")){
                    os.writeUTF("[SERVER] : session is closed, see you later..");
                    break;
                }
            }

            is.close();
            os.close();
            sensor_is.close();
            sensor_os.close();
            client.close();
            Server_as_client_to_sensor.close();

        }

    }
}
