package chess;

import boards.Board;
import boards.BoundVector;
import boards.FreeVector;
import boards.Position;

import java.util.Scanner;

public class ChessPlay {
    private Board chessboard;
    private String theStateOfTheGame = "none wins";
    private int turn = 1;

    /**
     * The ChessPlay constructor
     */
    public ChessPlay() {
        char[] accepted = {'♔', '♕', '♖', '♗', '♘', '♙', '♚', '♛', '♜', '♝', '♞', '♟', ' '};
        char[][] contents = {
                {'♖', '♘', '♗', '♕', '♔', '♗', '♘', '♖'},
                {'♙', '♙', '♙', '♙', '♙', '♙', '♙', '♙'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'♟', '♟', '♟', '♟', '♟', '♟', '♟', '♟'},
                {'♜', '♞', '♝', '♛', '♚', '♝', '♞', '♜'},
        };
        this.chessboard = new Board(accepted, contents);
    }

    /*
    This is an initialization of 12 character constants representing various chess pieces.
    The intention was to use those named constants instead of actual character constants
    for easier understanding of the code below it.
     */
    final char
            blackKing = '♔', blackQueen = '♕', blackRook = '♖', blackBishop = '♗', blackKnight = '♘', blackPawn = '♙',
            whiteKing = '♚', whiteQueen = '♛', whiteRook = '♜', whiteBishop = '♝', whiteKnight = '♞', whitePawn = '♟';

    /**
     * The method checks if an argument is a unicode chess symbol
     * @param chessPiece tested character
     * @return true if tested character is a unicode chess symbol, otherwise false
     */
    boolean isChessPiece(char chessPiece) {
        return chessPiece >= blackKing && chessPiece <= whitePawn;
    }

    /**
     * The method checks if an argument is a space character
     * @param chessPiece tested character
     * @return true if tested character is space, otherwise false
     */
    boolean isEmpty(char chessPiece) {
        return chessPiece == ' ';
    }

    /**
     * The method checks if an argument is a unicode chess symbol for black chesspiece
     * @param chessPiece tested character
     * @return true if tested character is a black chesspiece, otherwise false
     */
    boolean isBlackChessPiece(char chessPiece) {
        return chessPiece >= blackKing && chessPiece <= blackPawn;
    }

    /**
     * The method checks if an argument is a unicode chess symbol for white chesspiece
     * @param chessPiece tested character
     * @return true if tested character is a white chesspiece, otherwise false
     */
    boolean isWhiteChessPiece(char chessPiece) {
        return chessPiece >= whiteKing && chessPiece <= whitePawn;
    }

    /**
     * The method checks if BoundVector object given as an argument represents
     * moving a chesspiece to an empty tile
     * @param boundVector BoundVector object representing move on a chessboard
     * @return true if boundVector represents a move of a chespiece to an empty tile, otherwise false
     */
    boolean isChessMoveToEmptyTile(BoundVector boundVector) {
        char firstElement = chessboard.getElement(boundVector.origin());
        char secondElement = chessboard.getElement(boundVector.destination());
        return isChessPiece(firstElement) && isEmpty(secondElement);
    }

    /**
     * The method checks if BoundVector object given as an argument represents
     * moving a chesspiece to a tile with a chesspiece of the opposite color
     * @param boundVector BoundVector object representing move on a chessboard
     * @return true if boundVector represents a move of a chespiece
     * to a tile with a chesspiece of the opposite color, otherwise false
     */
    boolean isChessMoveToOppositeColor(BoundVector boundVector) {
        char firstElement = chessboard.getElement(boundVector.origin());
        char secondElement = chessboard.getElement(boundVector.destination());
        return (isWhiteChessPiece(firstElement) && isBlackChessPiece(secondElement)) ||
                (isBlackChessPiece(firstElement) && isWhiteChessPiece(secondElement));
    }

    /**
     * The method checks if all the tiles between origin and destination
     * of a BoundVector object given as an argument are empty
     * Method throws an exception if FreeVector of this BoundVector is not orthogonal or diagonal
     * @param boundVector tested object of class BoundVector
     * @return true if all the tiles between origin and destination of this BoundVector are empty, otherwise false
     */
    private boolean isPathEmpty(BoundVector boundVector) {
        Position[] positions = boundVector.getStandardPath();
        boolean test = true;
        for (int i = 1; i < positions.length - 1; i++) {
            if (!isEmpty(chessboard.getElement(positions[i]))) {
                test = false;
                break;
            }
        }
        return test;
    }

    /**
     * The method checks if this BoundVector represents a correct king move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct king move, otherwise false
     */
    boolean isCorrectKingMove(BoundVector boundVector) {
        FreeVector freeVector = boundVector.getFreeVector();
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (chesspiece == whiteKing || chesspiece == blackKing) {
            return (isChessMoveToEmptyTile(boundVector) || isChessMoveToOppositeColor(boundVector))
                    && freeVector.isOneTiled()
                    && isPathEmpty(boundVector);
        } else {
            return false;
        }
    }

    /**
     * The method checks if this BoundVector represents a correct queen move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct queen move, otherwise false
     */
    boolean isCorrectQueenMove(BoundVector boundVector) {
        FreeVector freeVector = boundVector.getFreeVector();
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (chesspiece == whiteQueen || chesspiece == blackQueen) {
            return (isChessMoveToEmptyTile(boundVector) || isChessMoveToOppositeColor(boundVector))
                    && (freeVector.isOrthogonal() || freeVector.isDiagonal())
                    && isPathEmpty(boundVector);
        } else {
            return false;
        }

    }

    /**
     * The method checks if this BoundVector represents a correct rook move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct rook move, otherwise false
     */
    boolean isCorrectRookMove(BoundVector boundVector) {
        FreeVector freeVector = boundVector.getFreeVector();
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (chesspiece == whiteRook || chesspiece == blackRook) {
            return (isChessMoveToEmptyTile(boundVector) || isChessMoveToOppositeColor(boundVector))
                    && freeVector.isOrthogonal()
                    && isPathEmpty(boundVector);
        } else {
            return false;
        }
    }

    /**
     * The method checks if this BoundVector represents a correct bishop move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct bishop move, otherwise false
     */
    boolean isCorrectBishopMove(BoundVector boundVector) {
        FreeVector freeVector = boundVector.getFreeVector();
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (chesspiece == whiteBishop || chesspiece == blackBishop) {
            return (isChessMoveToEmptyTile(boundVector) || isChessMoveToOppositeColor(boundVector))
                    && freeVector.isDiagonal()
                    && isPathEmpty(boundVector);
        } else {
            return false;
        }

    }

    /**
     * The method checks if this BoundVector represents a correct knight move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct knight move, otherwise false
     */
    boolean isCorrectKnightMove(BoundVector boundVector) {
        FreeVector freeVector = boundVector.getFreeVector();
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (chesspiece == whiteKnight || chesspiece == blackKnight) {
            return (isChessMoveToEmptyTile(boundVector) || isChessMoveToOppositeColor(boundVector))
                    && freeVector.isLShaped();
        } else {
            return false;
        }

    }

    /**
     * The method checks if this BoundVector represents a correct pawn move on a chessboard
     * @param boundVector that represents a move on a chessboard
     * @return true if boundVector represents a correct pawn move, otherwise false
     */
    boolean isCorrectPawnMove(BoundVector boundVector) {
        char chesspiece = chessboard.getElement(boundVector.origin());
        FreeVector freeVector = boundVector.getFreeVector();
        boolean test =
                (freeVector.isDiagonal() && freeVector.isOneTiled() && isChessMoveToOppositeColor(boundVector))
                        ||
                        (freeVector.isOrthogonal() && freeVector.isOneTiled() && isChessMoveToEmptyTile(boundVector));
        if (chesspiece == whitePawn) {
            return test && freeVector.isTowardsPositiveY();
        } else if (chesspiece == blackPawn) {
            return test && freeVector.isTowardsNegativeY();
        } else {
            return false;
        }
    }

    /**
     * The method checks if it's time for white chess to move
     * @return true if it's time for whites, otherwise false
     */
    boolean isWhitesTurn() {
        return turn % 2 != 0;
    }

    /**
     * The method checks if it's time for black chess to move
     * @return true if it's time for blacks, otherwise false
     */
    boolean isBlacksTurn() {
        return turn % 2 == 0;
    }

    /**
     * The method checks if it's time for a color of a chesspiece character given as a method argument to move
     * @return true if it's time for the color of a given character, otherwise false
     */
    boolean isCorrectTurn(char chessPiece) {
        return (isWhiteChessPiece(chessPiece) && isWhitesTurn())
                || (isBlackChessPiece(chessPiece) && isBlacksTurn());
    }

    /**
     * The method checks if the chess move represented by a String move is correct
     * @param move chess move represented as a String object
     * @return true if a chess move is correct, otherwise false
     */
    boolean isChessMoveCorrect(String move) {
        if (move == null || !move.matches("([a-h][1-8]){2}"))
            return false;
        BoundVector boundVector = BoundVector.getBoundVector(move);
        char chesspiece = chessboard.getElement(boundVector.origin());
        if (!isCorrectTurn(chesspiece)) {
            return false;
        }
        return isCorrectKingMove(boundVector) || isCorrectQueenMove(boundVector)
                || isCorrectRookMove(boundVector) || isCorrectBishopMove(boundVector)
                || isCorrectKnightMove(boundVector) || isCorrectPawnMove(boundVector);
    }

    /**
     * The method performs a move represented by a String move
     * If the chess move is incorrect it throws an exception
     * @param move chess move represented as a String
     */
    void makeChessMove(String move) {
        if (!isChessMoveCorrect(move))
            throw new IllegalArgumentException("Incorrect chess move");
        BoundVector boundVector = BoundVector.getBoundVector(move);
        char firstElement = chessboard.getElement(boundVector.origin());
        char secondElement = chessboard.getElement(boundVector.destination());
        chessboard.setElement(boundVector.origin(), ' ');
        chessboard.setElement(boundVector.destination(), firstElement);
        turn++;
        switch (secondElement) {
            case whiteKing -> theStateOfTheGame = "blacks win";
            case blackKing -> theStateOfTheGame = "whites win";
            default -> theStateOfTheGame = "none wins";
        }
    }

    /**
     * The method prompts a user to enter a correct chess move.
     * The method prints error message if the move was incorrect
     * @return correct chess move represented as a String
     */
    String getCorrectChessMove() {
        Scanner keyboard = new Scanner(System.in);
        String message = isWhitesTurn() ? "Move whites: " : "Move blacks: ";
        System.out.print(message);
        String move = keyboard.nextLine();
        while (!isChessMoveCorrect(move)) {
            System.out.println("Incorrect chess move!");
            System.out.print(message);
            move = keyboard.nextLine();
        }
        return move;
    }

    /**
     * The method prompts a user to move either a white chesspiece or a black chesspiece.
     * It does so until either blacks or whites capture the enemy king.
     * After that happens, the information about a winner is printed.
     */
    public void playChess() {
        do {
            System.out.println(chessboard);
            String move = getCorrectChessMove();
            makeChessMove(move);
        } while (theStateOfTheGame.equals("none wins"));
        System.out.println(chessboard);
        System.out.println(theStateOfTheGame);
    }

}


