class PlayerWonException extends Exceptions {
    private String positions;

    public PlayerWonException(String positions) {
        super("Player has won the game.");
        this.positions = positions;
    }

    public String getPositions() {
        return positions;
    }
}
public class Exceptions extends Exception {
    public Exceptions(String message) {
        super(message);
    }
}
class GameDrawException extends Exceptions {
    public GameDrawException() {
        super("The game ended in a draw.");
    }
}

class PositionAlreadyMarkedException extends Exceptions {
    public PositionAlreadyMarkedException() {
        super("Position is already marked.");
    }
}
class InvalidMoveException extends Exceptions {
    InvalidMoveException(String message) {
        super(message);
    }
}


