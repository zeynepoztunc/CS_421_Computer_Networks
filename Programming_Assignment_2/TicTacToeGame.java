
import java.io.IOException;

public class TicTacToeGame implements Runnable {

    Client player1,player2;
    TicTacToe game;

    public TicTacToeGame(Client client1, Client client2) throws IOException {
        //initialize the clients
        this.player1 = client1;
        this.player2 = client2;
        game = new TicTacToe();
    }

    private boolean playGame(Client c1, Client c2, String mark) {
        String msg="";
        Client currentPlayer;
        Client otherPlayer;
        String currentPlayerSymbol;

        if (mark.equals("x")) {
            currentPlayer = c1;
            otherPlayer = c2;
            currentPlayerSymbol = "Player 1";
        } else {
            currentPlayer = c2;
            otherPlayer = c1;
            currentPlayerSymbol = "Player 2";
        }
        try {
            boolean marked = false;
            while (!marked) {
                try {
                    System.out.println("Waiting for " + currentPlayerSymbol + "'s move.");
                    c1.writeToClient("Please enter your move 1-9: ");
                    msg = c1.readFromClient();    //read client1 next move
                    game.makeMove(msg, mark); //send client1 move to the game program
                    marked = true;     // if the given move is marked successfully then exit
                    System.out.println("Received " + mark + " on " + msg + ". It is a legal move.");
                } catch (PositionAlreadyMarkedException ge) {
                    c1.writeToClient("This is an illegal move. Please change your move!");
                    System.out.println("Received " + mark + " on " + msg + ". It is a legal move.");
                }
            }

            //send current state to the players
            c1.writeToClient(game.printArray());
            c2.writeToClient(game.printArray());

            c1.writeToClient("Turn information: Player 1's turn");
            c2.writeToClient("Turn information: Your turn");
        } catch (PlayerWonException exception) {
            c1.writeToClient("\n.You have won the game \n" );
            c2.writeToClient("\nYou have lost the game \n");
            String boardState = game.printArray();
            c1.writeToClient(boardState);
            c2.writeToClient(boardState);
            return true;
        } catch (GameDrawException drawException) {
            c1.writeToClient("It is a draw!");
            c2.writeToClient("It is a draw!");
            c1.writeToClient(game.printArray());
            c2.writeToClient(game.printArray());
          
            return true;
        } catch (InvalidMoveException ime) {
            currentPlayer.writeToClient("Received " + mark + " on " + msg + ". It is an illegal move. (" + ime.getMessage() + ")");
        }
        
        return false;
    }

    @Override
    public void run() {
        // Display the initial state of the board
        String initialBoardState = "State of the board\n\n" + game.printArray();
        player1.writeToClient(initialBoardState);
        player2.writeToClient(initialBoardState);
    
        boolean finish = false;
        boolean player1Turn = true;
        String player1Symbol = "x";
        String player2Symbol = "o";
    
        while (!finish) {
            // Current player's turn
            Client currentPlayer = player1Turn ? player1 : player2;
            Client otherPlayer = player1Turn ? player2 : player1;
            String currentPlayerSymbol = player1Turn ? player1Symbol : player2Symbol;
    
            finish = playGame(currentPlayer, otherPlayer, currentPlayerSymbol);
            player1Turn = !player1Turn;
        }
    
        // Display the final state of the board
        String finalBoardState = "State of the board\n" + game.printArray();
        player1.writeToClient(finalBoardState);
    
        // Close connections
        player1.closeConnections();
        player2.closeConnections();

    }
    
}
