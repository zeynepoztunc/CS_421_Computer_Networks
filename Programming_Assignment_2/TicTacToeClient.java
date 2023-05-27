import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {
    public static void main(String[] args) throws IOException {

         //give an error if the command line arguments are wrong
         if (args.length != 1) {
            System.out.println("You should enter the following way: java TicTacToeClient <port>");
            return;
        }

        //get the port number from the terminal
        //int port = 12345;
        int port= Integer.parseInt(args[0]);
        Socket socket = new Socket("localhost", port);
        System.out.println("Connected to the server.");

        //init the input and outpur stream
        OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();

        //create a thread to read input streams
        Thread read = new Thread(() -> {
            while (true) {
                try {
                    if (input.available() > 0) {
                        int data;
                        StringBuilder msg = new StringBuilder();
                        while ((data = input.read()) != 38) {
                            msg.append((char) data);
                        }
                        System.out.println(msg.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        read.start();

        //create a thread to read the output stream
        Thread write = new Thread(() -> {
            Scanner scan = new Scanner(System.in);
            while (true) {
                String msg = scan.nextLine();
                try {
                    output.write((msg + "&").getBytes());
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        write.start();
        
    }
}
