import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
public class Client {

    Socket socket;
    InputStream reader;
    OutputStream writer;

    Client(Socket socket) throws IOException {
        this.socket = socket;
        writer = socket.getOutputStream();
        reader = socket.getInputStream();
    }

    public void writeToClient(String msg) {
        try {
            writer.write((msg + "&").getBytes());
            writer.flush();
        } catch (IOException e) {
            System.out.println("An error has occurred while sending the message");
            e.printStackTrace();
        }
    }

    public String readFromClient() {
        StringBuilder str = new StringBuilder();
        boolean finish = false;
        try {
            while (!finish) {
                if (reader.available() > 0) {
                    int data;
                    while ((data = reader.read()) != 38) {
                        str.append((char) data);
                    }
                    finish = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
    

    public void closeConnections() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
