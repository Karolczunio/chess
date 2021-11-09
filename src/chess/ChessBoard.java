package chess;

import chess.navigation.BoundVector;
import chess.navigation.Position;
import chess.enums.ChessPieceColor;
import chess.enums.ChessTileColor;
import chess.enums.ChessType;
import chess.enums.PromotionType;
import chess.element.ChessPiece;
import chess.element.ChessSlot;
import chess.element.ChessTile;

import java.util.Arrays;

public class ChessBoard {
    private final static int x = 8;
    private final static int y = 8;

    private final ChessPieceColor currentColor;
    private final ChessSlot[][] contents;

    /**
     * Private ChessBoard constructor
     *
     * @param currentColor color that is on the move
     * @param contents     defines positions of chesstiles and chesspieces
     */
    private ChessBoard(ChessPieceColor currentColor, ChessSlot[][] contents) {
        this.currentColor = currentColor;
        this.contents = contents;
    }

    /**
     * The method generates contents that Chessboard should be initialized with
     *
     * @return initial contents
     */
    private static ChessSlot[][] getInitialContents() {
        ChessSlot[][] contents = new ChessSlot[y][x];
        ChessType[] types = new ChessType[]{
                ChessType.ROOK, ChessType.KNIGHT, ChessType.BISHOP,
                ChessType.QUEEN, ChessType.KING,
                ChessType.BISHOP, ChessType.KNIGHT, ChessType.ROOK};
        ChessPieceColor pieceColor;
        ChessType pieceType = null;
        ChessTileColor tileColor;
        PromotionType promotionType;
        ChessPiece piece;
        ChessTile tile;
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                tileColor = (i % 2 == j % 2) ? ChessTileColor.WHITE : ChessTileColor.BLACK;
                pieceColor = (i == 0 || i == 1) ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
                promotionType = PromotionType.FOR_NONE;
                if (i == 0) {
                    promotionType = PromotionType.FOR_WHITES;
                    pieceType = types[j];
                }
                if (i == 1 || i == 6) {
                    pieceType = ChessType.PAWN;
                }
                if (i == 7) {
                    promotionType = PromotionType.FOR_BLACKS;
                    pieceType = types[j];
                }
                tile = new ChessTile(tileColor, promotionType);
                piece = (i == 2 || i == 3 || i == 4 || i == 5) ? null : new ChessPiece(pieceType, pieceColor, false);
                contents[i][j] = new ChessSlot(tile, piece);
            }
        }
        return contents;
    }

    /**
     * The factory method creating a new ChessBoard
     *
     * @return new ChessBoard object
     */
    public static ChessBoard create() {
        return new ChessBoard(ChessPieceColor.WHITE, getInitialContents());
    }

    /**
     * The method checks whether a given position is within boundaries of this chessboard
     *
     * @param position position to check
     * @return true if position is valid, otherwise false
     */
    public boolean isValidPosition(Position position) {
        return position != null && position.x() >= 0 && position.x() < x && position.y() >= 0 && position.y() < y;
    }

    /**
     * The method checks whether a given boundvector's origin and destination are within boundaries of this chessboard
     *
     * @param boundVector bound vector to check
     * @return true if bound vector is valid, otherwise false
     */
    public boolean isValidBoundVector(BoundVector boundVector) {
        return boundVector != null && isValidPosition(boundVector.origin()) && isValidPosition(boundVector.destination());
    }

    /**
     * This method returns all the positions that can be accessed on this chessboard
     *
     * @return all valid positions
     */
    public Position[] getAllValidPositions() {
        Position[] positions = new Position[x * y];
        for (int j = 0; j < y; j++) {
            for (int k = 0; k < x; k++) {
                positions[j * x + k] = new Position(k, j);
            }
        }
        return positions;
    }

    /**
     * This method returns position of the first encountered chesspiece of the given type and color
     * If there are no chesspieces satisfying those criteria the method throws IllegalStateException
     *
     * @param type  type of the chesspiece we look for
     * @param color color of the chesspiece we look for
     * @return position of the first chesspiece satisfying above criteria
     */
    public Position getChessPiecePosition(ChessType type, ChessPieceColor color) {
        Position[] positions = getAllValidPositions();
        ChessPiece chessPiece;
        for (Position position : positions) {
            chessPiece = this.getElement(position).piece();
            if (chessPiece != null && chessPiece.type() == type && chessPiece.color() == color) {
                return position;
            }
        }
        throw new IllegalStateException("No such chesspiece");
    }

    /**
     * This method returns positions of all encountered chesspieces of the given color
     * If there are no chesspieces of this color the method returns an empty array
     *
     * @param color color of the chesspieces we look for
     * @return positions of all chesspieces of the given color
     */
    public Position[] getChessPiecePositionsOfColor(ChessPieceColor color) {
        Position[] positions = getAllValidPositions();
        ChessPiece chessPiece;
        Position[] result = new Position[16];
        int index = 0;
        for (Position position : positions) {
            chessPiece = this.getElement(position).piece();
            if (chessPiece != null && chessPiece.color() == color) {
                result[index] = position;
                index++;
            }
        }
        return Arrays.copyOf(result, index);
    }

    /**
     * The private method throwing an exception if a given position is invalid
     *
     * @param position position to check
     */
    private void checkPosition(Position position) {
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Position is outside of the board");
        }
    }

    /**
     * The method returns ChessSlot object from position where. Throws exception if position is invalid.
     *
     * @param where the position on the board from which the object should be taken
     * @return the ChessSlot object on the board at position where
     */
    public ChessSlot getElement(Position where) {
        checkPosition(where);
        return contents[(y - 1) - where.y()][where.x()];
    }

    /**
     * Getter method for the current color. The current color is the color of the chesspiece
     * that should move when this color is set.
     *
     * @return current color
     */
    public ChessPieceColor getCurrentColor() {
        return currentColor;
    }

    /**
     * Private method that copies the contents of this ChessBoard object
     *
     * @return copy of contents of this chessboard
     */
    private ChessSlot[][] getContents() {
        ChessSlot[][] copy = new ChessSlot[y][x];
        for (int i = 0; i < y; i++) {
            copy[i] = Arrays.copyOf(contents[i], x);
        }
        return copy;
    }

    /**
     * The method returns the chessboard almost like this one
     * except with a chesspiece at the given position set to the given chesspiece
     *
     * @param where   position where the chesspiece should change
     * @param toValue new chesspiece in a new chessboard
     * @return modified chessboard
     */
    public ChessBoard getChessBoardWithChangedChessPiece(Position where, ChessPiece toValue) {
        checkPosition(where);
        ChessSlot[][] copy = getContents();
        copy[(y - 1) - where.y()][where.x()] = new ChessSlot(getElement(where).tile(), toValue);
        return new ChessBoard(getCurrentColor(), copy);
    }

    /**
     * The method returns the chessboard almost like this one
     * except with a color flipped from to the opposite one(from white to black or from black to white)
     *
     * @return chessboard with a flipped color
     */
    public ChessBoard getChessBoardWithFlippedColor() {
        return new ChessBoard(currentColor.oppositeColor(), contents);
    }

    /**
     * The method returns contents of the board
     * together with the inscriptions on the sides of the board
     * as a string
     *
     * @return string representing this object
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("*abcdefgh*\n");
        int rowNumber;
        for (int i = 0; i < y; i++) {
            rowNumber = y - i;
            builder
                    .append(rowNumber);
            for (int j = 0; j < contents[i].length; j++) {
                builder.append(contents[i][j].character());
            }
            builder
                    .append(rowNumber)
                    .append('\n');
        }
        builder.append("*abcdefgh*\n");
        return builder.toString();
    }

    /**
     * The method that checks if two objects are equal
     *
     * @param o a tested object
     * @return true if the object are equal otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(contents, that.contents);
    }

    /**
     * The method returns hash code of an object
     *
     * @return hashcode of this object
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(contents);
    }
}
