package chess.element;

import chess.enums.ChessPieceColor;
import chess.enums.ChessType;

public record ChessPiece(ChessType type, ChessPieceColor color, boolean wasMoved) {

    public ChessPiece {
        if (type == null) {
            throw new IllegalArgumentException("Chess piece type cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Chess piece color cannot be null");
        }
    }

    /**
     * @return character representing this chess piece
     */
    public char character() {
        char ch = switch (type) {
            case PAWN:
                yield '♙';
            case ROOK:
                yield '♖';
            case KNIGHT:
                yield '♘';
            case BISHOP:
                yield '♗';
            case QUEEN:
                yield '♕';
            case KING:
                yield '♔';
        };
        ch += switch (color) {
            case BLACK -> 0;
            case WHITE -> 6;
        };
        return ch;
    }

}


