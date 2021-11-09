package chess;

import chess.navigation.BoundVector;
import chess.enums.ChessPieceColor;
import chess.enums.ChessType;
import chess.enums.TheStateOfTheGame;
import chess.validation.ChessArbiter;

import java.util.Scanner;

public class ChessPlay {

    ChessBoard chessBoard;
    TheStateOfTheGame stateOfTheGame;

    /**
     * The ChessPlay constructor
     */
    public ChessPlay() {
        this.chessBoard = ChessBoard.create();
        this.stateOfTheGame = TheStateOfTheGame.PLAYING;
    }

    /**
     * The method prompts a user to enter which of the 4 types:
     * ROOK, KNIGHT, BISHOP, QUEEN
     * the pawn should be promoted to
     *
     * @return selected chess type
     */
    private static ChessType makePawnPromotionMenu() {
        System.out.println("Promote the pawn to:");
        System.out.println("a) queen");
        System.out.println("b) bishop");
        System.out.println("c) knight");
        System.out.println("d) rook");
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter promotion type: ");
        String line = keyboard.nextLine();
        while (!line.matches("[abcd]")) {
            System.out.println("Invalid option! Write a, b, c or d.");
            System.out.print("Enter promotion type: ");
            line = keyboard.nextLine();
        }
        System.out.println("Pawn promoted!");
        return switch (line) {
            case "a":
                yield ChessType.QUEEN;
            case "b":
                yield ChessType.BISHOP;
            case "c":
                yield ChessType.KNIGHT;
            case "d":
                yield ChessType.ROOK;
            default:
                throw new IllegalArgumentException("Invalid pawn promotion option!");
        };
    }

    /**
     * The method prompts a user to enter a correct chess move.
     * The method prints error message if the move was incorrect
     *
     * @return correct chess move represented as a BoundVector
     */
    BoundVector getCorrectChessMove() {
        Scanner keyboard = new Scanner(System.in);
        String message = chessBoard.getCurrentColor() == ChessPieceColor.WHITE ? "Move whites: " : "Move blacks: ";
        System.out.print(message);
        String move = keyboard.nextLine();
        while (!move.matches("([a-h][1-8]){2}") || !ChessArbiter.isStrictlyLegalMove(chessBoard, ChessDoer.getBoundVector(move))) {
            System.out.println("Incorrect chess move!");
            System.out.print(message);
            move = keyboard.nextLine();
        }
        return ChessDoer.getBoundVector(move);
    }

    /**
     * The method prompts a user to move either a white chesspiece or a black chesspiece.
     * It does so until either blacks or whites checkmate the enemy king or there is a stalemate
     * After that happens, the information about it is printed.
     */
    public void playChess() {
        do {
            System.out.println(chessBoard);
            BoundVector move = getCorrectChessMove();
            chessBoard = ChessDoer.makeStandardChessMove(chessBoard, move, ChessPlay::makePawnPromotionMenu);
            if (ChessArbiter.currentPlayerHasNoStrictlyLegalMoves(chessBoard)) {
                if (ChessArbiter.isCurrentKingInCheck(chessBoard)) {
                    stateOfTheGame = TheStateOfTheGame.CHECKMATE;
                } else {
                    stateOfTheGame = TheStateOfTheGame.STALEMATE;
                }
            }
        } while (stateOfTheGame == TheStateOfTheGame.PLAYING);
        String stalemateMessage = "There is stalemate. No one wins!";
        String player = chessBoard.getCurrentColor().oppositeColor() == ChessPieceColor.BLACK ? "Blacks" : "Whites";
        String victoryMessage = player + " win by checkmate!";
        System.out.println(chessBoard);
        switch (stateOfTheGame) {
            case STALEMATE -> System.out.println(stalemateMessage);
            case CHECKMATE -> System.out.println(victoryMessage);
        }
    }

}


