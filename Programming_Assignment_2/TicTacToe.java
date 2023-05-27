import java.util.Arrays;

public class TicTacToe {
    int[] board;
    int count;

    TicTacToe() {
        count = 0;
        board = new int[9];
    }
    
    private void checkForWin(int sum) throws PlayerWonException {
        int[][] winningCombinations = {
            {0, 1, 2}, {0, 3, 6}, {2, 5, 8}, {6, 7, 8},
            {0, 4, 8}, {1, 4, 7}, {2, 4, 6}, {3, 4, 5}
        };
    
        for (int[] combination : winningCombinations) {
            if (board[combination[0]] + board[combination[1]] + board[combination[2]] == sum) {
                String positions = Arrays.toString(combination)
                        .replaceAll("\\[|\\]|\\s", "")
                        .replace(",", ", ");
                throw new PlayerWonException(positions);
            }
        }
    }
    private boolean shouldGameContinue(int mark) throws GameDrawException, PlayerWonException {
        if (count == 9)
            throw new GameDrawException();
        else {
            checkForWin(mark == 1 ? 3 : -3);
        }
        return true;
    }

    private void mark(int position, int markIndex) throws PositionAlreadyMarkedException {
        if (board[position - 1] == 0) {
            board[position - 1] = markIndex;
            count++;
        } else
            throw new PositionAlreadyMarkedException();
    }


    public void makeMove(String str, String xOrO) throws PositionAlreadyMarkedException, GameDrawException, PlayerWonException, InvalidMoveException {
        int position = Integer.parseInt(str);
        if (position < 1 || position > 9) {
            throw new InvalidMoveException("The move is out of the board.");
        }
        int m = xOrO.equals("x") ? 1 : -1; 
        mark(position, m);
        shouldGameContinue(m);
    }

    public String printArray() {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            String mark = board[i] == 0 ? " " : (board[i] == -1 ? "O" : "X");
            msg.append(String.format("%2s", mark));
            if ((i + 1) % 3 == 0) {
                msg.append("\n---------\n");
            } else {
                msg.append("|");
            }
        }
        msg.append("\n");
        return msg.toString();
    }
    

}
