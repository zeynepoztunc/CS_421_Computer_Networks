import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

public class TicTacToeServer {
    public static void main(String[] args) throws IOException {

        //give an error if the command line arguments are wrong
        if (args.length != 1) {
            System.out.println("You should enter the following way: java TicTacToeServer <port>");
            return;
        }

        //get the port number from the terminal
        //int port = 12345;
        int port= Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        Socket playerSocket;

        while (true) {
            //create the first player
            playerSocket = serverSocket.accept();
            Client player1 = new Client(playerSocket);
            System.out.println("A client is connected, and it is assigned with the symbol X and ID=0.");

            //create the second player
            playerSocket = serverSocket.accept();
            Client player2 = new Client(playerSocket);
            System.out.println("A client is connected, and it is assigned with the symbol O and ID=1.");

            //start the game
            TicTacToeGame newGame = new TicTacToeGame(player1, player2);
            System.out.println("The game is started.");
            newGame.run();
        }
    }
}
