import java.util.Scanner;

public class Main {
    /*
    This is a static 2D array of characters that represents a chessboard.
    Space character represents chess tile with no chess piece on it.
    Other characters are unicode symbols representing various chess pieces.
    It's initialized with chess pieces placed, where they should be at the beginning of the chess game.
     */
    static char[][] chessArray = {
            {'♖', '♘', '♗', '♕', '♔', '♗', '♘', '♖'},
            {'♙', '♙', '♙', '♙', '♙', '♙', '♙', '♙'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'♟', '♟', '♟', '♟', '♟', '♟', '♟', '♟'},
            {'♜', '♞', '♝', '♛', '♚', '♝', '♞', '♜'}
    };

    /*
    This is an initialization of 12 character constants representing various chess pieces.
    The intention was to use those named constants instead of actual character constants
    for easier understanding of the code below it.
     */
    final static char
            blackKing = '♔', blackQueen = '♕', blackRook = '♖', blackBishop = '♗', blackKnight = '♘', blackPawn = '♙',
            whiteKing = '♚', whiteQueen = '♛', whiteRook = '♜', whiteBishop = '♝', whiteKnight = '♞', whitePawn = '♟';

    static String theStateOfTheGame = "none wins";
    static int turn = 1;

    /**
     * This method checks whether chessPiece is a character representing 1 of 12 chess pieces
     *
     * @param chessPiece an argument that will be tested
     * @return true if character is a unicode chess symbol, otherwise false
     */
    static boolean isChessPiece(char chessPiece) {
        return chessPiece >= blackKing && chessPiece <= whitePawn;
    }

    /**
     * This method checks whether chessPiece is a character representing 1 of 6 black chess pieces
     *
     * @param chessPiece an argument that will be tested
     * @return true if character is a unicode symbol representing black chess piece, otherwise false
     */
    static boolean isBlackChessPiece(char chessPiece) {
        return chessPiece >= blackKing && chessPiece <= blackPawn;
    }

    /**
     * This method checks whether chessPiece is a character representing 1 of 6 white chess pieces
     *
     * @param chessPiece an argument that will be tested
     * @return true if character is a unicode symbol representing white chess piece, otherwise false
     */
    static boolean isWhiteChessPiece(char chessPiece) {
        return chessPiece >= whiteKing && chessPiece <= whitePawn;
    }
/*
INFO ABOUT CHESSBOARDS FOR CONTEXT
Letters from a to h (according to English alphabet)
and digits from 1 to 8 (according to the order of natural numbers)
are standard symbols placed on the edges of chessboards.
Letters from a to h are placed on one edge of the chessboard and the edge opposite to it.
Digits from 1 to 8 are placed on another adjacent edge of the chessboard and the edge opposite to it.
*//*
POSITION IN CHESS COORDINATE SYSTEM
Position in this coordinate system can be written
as mX, which is a CHARACTER NOTATION,
where m is letter from a to h (according to the order of the English alphabet)
and where X is digit from 1 to 8 (according to the order of the natural numbers)
or {x, y}, which is an ARRAY NOTATION,
where both x and y are integers from 0 to 7 (according to the order of the natural numbers)
and where {x, y} is integer array of length 2,
where x element is placed at index 0 and y element is placed at index 1.
*//*
CHARACTER NOTATION will be represented in code as an ordinary String object in Java.
ARRAY NOTATION will be represented in code as an ordinary int[] object in Java.
Both notations of position are equivalent and can be mapped to one another.
*//*
CHESS COORDINATE SYSTEM
From the point of view of the player playing with white chess pieces
the start of the coordinate system in chess is placed in the lower left corner of the chessboard,
it would be a1 position on a chessboard or {0, 0},
the x coordinate increases on AX axis from left to right, from a to h on a chessboard or from 0 to 7 as x in {x, y}
and the y coordinate increases on AY axis from bottom to top, from 1 to 8 on a chessboard or from 0 to 7 as y in {x, y}.
*/



/*
BOUND VECTOR describes origin of movement and its destination.
It can be written
in CHARACTER NOTATION as od,
where o and d are positions in CHARACTER NOTATION and od is concatenation of those 2 strings,
for example when o is "c1" and d is "d2" then od is "c1d2",
o is position the movement starts at and d is position the movement ends at,
for example "c1d2" means movement from "c1" to "d2"
or in ARRAY NOTATION as {O, D},
where O and D are positions in ARRAY NOTATION and {O, D} is their array with O at index 0 and D at index 1,
for example when O is {1, 2} and D is {0, 1} then {O, D} is {{1, 2}, {0, 1}},
{1, 2} is position the movement starts at and {0, 1} is position the movement ends at,
for example "c1d2" means movement from "c1" to "d2".
*/

    /**
     * The method creates BOUND VECTOR in ARRAY NOTATION from BOUND VECTOR in CHARACTER NOTATION
     *
     * @param move String object representing BOUND VECTOR in CHARACTER NOTATION
     * @return int[][] object representing BOUND VECTOR in ARRAY NOTATION
     */
    static int[][] getBoundChessMoveVector(String move) {
        return new int[][]{
                new int[]{move.charAt(0) - 'a', move.charAt(1) - '1'},
                new int[]{move.charAt(2) - 'a', move.charAt(3) - '1'}
        };
    }
    /*
    FREE VECTOR describes magnitude, direction and orientation of movement
    It can be written as {x, y} where x is movement on AX axis and y is movement on AY axis.
    Both x and y have values between -7 and 7 inclusive (according to the order of whole numbers).
    It means movement:
    a) if x is positive, towards greater values on AX axis
    b) if y is positive, towards greater values on AY axis
    c) if x is negative, towards smaller values on AX axis
    d) if y is negative, towards smaller values on AY axis
    It means no movement:
    a) if x is 0, on AX axis
    b) if y is 0, on AY axis
    It is represented as an ordinary int[] object in Java.
    */

    /**
     * The method creates FREE VECTOR in ARRAY NOTATION from BOUND VECTOR in ARRAY NOTATION
     *
     * @param boundVector int[][] object representing BOUND VECTOR in ARRAY NOTATION
     * @return int[] object representing FREE VECTOR in ARRAY NOTATION
     */
    static int[] getFreeChessMoveVector(int[][] boundVector) {
        return new int[]{boundVector[1][0] - boundVector[0][0], boundVector[1][1] - boundVector[0][1]};
    }
    /*
    ORTHODIAGONAL ITERATION VECTOR describes directions of orthogonal or diagonal movement
    It can be written as {x, y} where x is movement on AX axis and y is movement on AY axis.
    Both x and y have values of -1, 0 or 1.
    It means movement:
    a) if x is 1, towards greater values on AX axis
    b) if y is 1, towards greater values on AY axis
    c) if x is -1, towards smaller values on AX axis
    d) if y is -1, towards smaller values on AY axis
    It means no movement:
    a) if x is 0, on AX axis
    b) if y is 0, on AY axis
    It is represented as an ordinary int[] object in Java.
    */

    /**
     * The method creates ORTHODIAGONAL ITERATION VECTOR from FREE VECTOR
     * that represents orthogonal or diagonal movement.
     * In either orthogonal or diagonal movement,
     * adding vector that is returned by this method
     * to vector used to represent the initial position on a chessboard
     * will produce vector representing
     * the next position in either orthogonal or diagonal movement.
     *
     * @param freeVector int[] object representing FREE VECTOR
     * @return int[] object representing ORTHODIAGONAL ITERATION VECTOR
     */
    static int[] getOrthodiagonalIterationVector(int[] freeVector) {
        int[] iterationVector = new int[2];
        iterationVector[0] = (freeVector[0] != 0) ? freeVector[0] / Math.abs(freeVector[0]) : 0;
        iterationVector[1] = (freeVector[1] != 0) ? freeVector[1] / Math.abs(freeVector[1]) : 0;
        return iterationVector;
    }

    /**
     * The method is adding 2 vectors.
     * It's creating new vector c from 2 vectors a and b,
     * where a, b and c are each composed of x and y coordinates,
     * where x coordinate is at index 0 and y coordinate is at index 1 of each array representing vector,
     * and x coordinate of c is the sum of x coordinates of a and b,
     * and y coordinate of c is the sum of y coordinates of a and b.
     *
     * @param a int[] object representing first vector to add
     * @param b int[] object representing second vector to add
     * @return int[] object representing vector that is a result of addition of vectors a and b
     */
    static int[] getVectorSum(int[] a, int[] b) {
        return new int[]{a[0] + b[0], a[1] + b[1]};
    }

    /**
     * It's checking whether 2 vectors a and b
     * where a and b are each composed of x and y coordinates,
     * where x is coordinate at index 0 and y coordinate is at index 1 of each array representing vector,
     * have x coordinates equal to one another and y coordinates equal to one another
     *
     * @param a int[] object representing first vector to check for equality
     * @param b int[] object representing second vector to check for equality
     * @return true if vectors are equal, false otherwise
     */
    static boolean areVectorsEqual(int[] a, int[] b) {
        return a[0] == b[0] && a[1] == b[1];
    }

    /**
     * This method returns character from a chessboard at the position given by argument.
     *
     * @param position int[] object representing position on a chessboard in ARRAY NOTATION
     * @return character on the chessboard at the given position
     */
    static char getChessElement(int[] position) {
        return chessArray[7 - position[1]][position[0]];
    }

    /**
     * This method sets given character value to a chessboard at the position given by argument position.
     *
     * @param position int[] object representing position on a chessboard in ARRAY NOTATION
     * @param value    character to set given position to
     */
    static void setChessElement(int[] position, char value) {
        chessArray[7 - position[1]][position[0]] = value;
    }

    /**
     * The method checks whether the chess piece is moved to an empty tile on a chessboard
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if chess piece is moved to an empty tile, otherwise false
     */
    static boolean isChessMoveToEmptyTile(int[][] chessMoveVector){
        char firstElement = getChessElement(chessMoveVector[0]);
        char secondElement = getChessElement(chessMoveVector[1]);
        return isChessPiece(firstElement) && !isChessPiece(secondElement);
    }

    /**
     * The method checks whether the chess piece is moved to a tile with a chess piece of the opposite color
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if chess piece is moved to a tile with a chess piece of the opposite color, otherwise false
     */
    static boolean isChessMoveToOppositeColor(int[][] chessMoveVector) {
        char firstElement = getChessElement(chessMoveVector[0]);
        char secondElement = getChessElement(chessMoveVector[1]);
        boolean attackingABlackChessPieceWithAWhiteChessPiece = isWhiteChessPiece(firstElement) && isBlackChessPiece(secondElement);
        boolean attackingAWhiteChessPieceWithABlackChessPiece = isBlackChessPiece(firstElement) && isWhiteChessPiece(secondElement);
        return attackingABlackChessPieceWithAWhiteChessPiece || attackingAWhiteChessPieceWithABlackChessPiece;
    }

    /**
     * The method checks whether chesspiece moving either orthogonally or diagonally has all the tiles
     * on its path from origin to destination not including the tile of origin and the tile of destination
     * empty of any chess pieces. The method is going to work only for diagonal or orthogonal movement.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the path is empty, otherwise false
     */
    static boolean isOrthodiagonalPathEmpty(int[][] chessMoveVector) {
        int[] endVector = getFreeChessMoveVector(chessMoveVector);
        int[] iteration = getOrthodiagonalIterationVector(endVector);
        for (int[] vector = iteration;
             !areVectorsEqual(vector, endVector);
             vector = getVectorSum(vector, iteration)) {
            if (isChessPiece(getChessElement(getVectorSum(chessMoveVector[0], vector)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method checks whether a chess piece moves at most one tile if any direction
     * @param vector int[] object representing FREE VECTOR
     * @return true if chess piece moves at most one tile in any direction, otherwise false
     */
    static boolean isChessMoveOneTiled(int[] vector) {
        return Math.abs(vector[0]) <= 1 && Math.abs(vector[1]) <= 1;
    }

    /**
     * The method checks whether a chess piece moves orthogonally
     * @param vector int[] object representing FREE VECTOR
     * @return true if chess piece moves orthogonally, otherwise false
     */
    static boolean isChessMoveOrthogonal(int[] vector) {
        return vector[0] == 0 || vector[1] == 0;
    }

    /**
     * The method checks whether a chess piece moves diagonally
     * @param vector int[] object representing FREE VECTOR
     * @return true if chess piece moves diagonally, otherwise false
     */
    static boolean isChessMoveDiagonal(int[] vector) {
        return Math.abs(vector[0]) == Math.abs(vector[1]);
    }

    /**
     * The method checks whether a chess piece movement is L-shaped
     * @param vector int[] object representing FREE VECTOR
     * @return true if chess piece movement is L-shaped, otherwise false
     */
    static boolean isChessMoveLShaped(int[] vector) {
        return (Math.abs(vector[0]) == 2 && Math.abs(vector[1]) == 1)
                || (Math.abs(vector[0]) == 1 && Math.abs(vector[1]) == 2);
    }

    /**
     * The method checks whether a chess piece moves diagonally like a pawn.
     * That is in 1 of 2 directions on AY axis depending on whether it is supposed
     * to be a black pawn or a white pawn, because they move differently,
     * by exactly 1 tile in that direction
     * and 1 tile in any dircetion on AX axis
     * @param vector vector int[] object representing FREE VECTOR
     * @param isBlackPawn true means the chess piece is supposed to be a black pawn,
     *                    false means the chess piece is supposed to be a white pawn
     * @return true if a chesspiece moves diagonally like a pawn of the given color, otherwise false
     */
    static boolean isDiagonalPawnMove(int[] vector, boolean isBlackPawn) {
        int displacement = isBlackPawn ? -1 : 1;
        return Math.abs(vector[0]) == 1 && vector[1] == displacement;
    }

    /**
     * The method checks whether a chess piece moves forward like a pawn.
     * That is in 1 of 2 directions on AY axis depending on whether it is supposed
     * to be a black pawn or a white pawn, because they move differently,
     * by exactly 1 tile in that direction
     * and 0 tiles on AX axis
     * @param vector vector int[] object representing FREE VECTOR
     * @param isBlackPawn true means the chess piece is supposed to be a black pawn,
     *                    false means the chess piece is supposed to be a white pawn
     * @return true if a chesspiece moves forard like a pawn of the given color, otherwise false
     */
    static boolean isForwardPawnMove(int[] vector, boolean isBlackPawn) {
        int displacement = isBlackPawn ? -1 : 1;
        return  vector[0] == 0 && vector[1] == displacement;
    }

    /**
     * The method whether the chess piece moves how the king in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the chess piece moves how the king in chess should move, otherwise false
     */
    static boolean isCorrectKingMove(int[][] chessMoveVector) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isChessMoveToEmptyTile(chessMoveVector) || isChessMoveToOppositeColor(chessMoveVector))
                && isChessMoveOneTiled(vector)
                && isOrthodiagonalPathEmpty(chessMoveVector);
    }

    /**
     * The method whether the chess piece moves how the queen in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the chess piece moves how the queen in chess should move, otherwise false
     */
    static boolean isCorrectQueenMove(int[][] chessMoveVector) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isChessMoveToEmptyTile(chessMoveVector) || isChessMoveToOppositeColor(chessMoveVector))
                && (isChessMoveOrthogonal(vector) || isChessMoveDiagonal(vector))
                && isOrthodiagonalPathEmpty(chessMoveVector);
    }

    /**
     * The method whether the chess piece moves how the rook in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the chess piece moves how the rook in chess should move, otherwise false
     */
    static boolean isCorrectRookMove(int[][] chessMoveVector) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isChessMoveToEmptyTile(chessMoveVector) || isChessMoveToOppositeColor(chessMoveVector))
                && isChessMoveOrthogonal(vector)
                && isOrthodiagonalPathEmpty(chessMoveVector);
    }

    /**
     * The method whether the chess piece moves how the bishop in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the chess piece moves how the bishop in chess should move, otherwise false
     */
    static boolean isCorrectBishopMove(int[][] chessMoveVector) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isChessMoveToEmptyTile(chessMoveVector) || isChessMoveToOppositeColor(chessMoveVector))
                && isChessMoveDiagonal(vector)
                && isOrthodiagonalPathEmpty(chessMoveVector);
    }

    /**
     * The method whether the chess piece moves how the knight in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @return true if the chess piece moves how the knight in chess should move, otherwise false
     */
    static boolean isCorrectKnightMove(int[][] chessMoveVector) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isChessMoveToEmptyTile(chessMoveVector) || isChessMoveToOppositeColor(chessMoveVector))
                && isChessMoveLShaped(vector);
    }

    /**
     * The method whether the chess piece moves how the pawn of the particular color in chess should move.
     * @param chessMoveVector int[][] object which is BOUND VECTOR in ARRAY NOTATION
     * @param isBlackPawn true means the chess piece is supposed to be a black pawn,
     * false means the chess piece is supposed to be a white pawn
     * @return true if the chess piece moves how the pawn of the particular color should move, otherwise false
     */
    static boolean isCorrectPawnMove(int[][] chessMoveVector, boolean isBlackPawn) {
        int[] vector = getFreeChessMoveVector(chessMoveVector);
        return (isDiagonalPawnMove(vector, isBlackPawn) && isChessMoveToOppositeColor(chessMoveVector))
                || (isForwardPawnMove(vector, isBlackPawn) && isChessMoveToEmptyTile(chessMoveVector));
    }

    /**
     * The method checks whether there is whites turn
     * @return true if it's whites turn, otherwise false
     */
    static boolean isWhitesTurn(){
        return turn%2!=0;
    }

    /**
     * The method checks whether there is blacks turn
     * @return true if it's blacks turn, otherwise false
     */
    static boolean isBlacksTurn(){
        return turn%2==0;
    }

    /**
     * The method checks whether a color of a given chess piece has its turn
     * @param chessPiece the chesspiece which color's turn will be checked
     * @return true if its turn of the color of the given chess piece,
     * false when it's not a chesspiece or when it's not a turn of the color of the given chess piece
     */
    static boolean isCorrectTurn(char chessPiece){
      return (isWhiteChessPiece(chessPiece)&&isWhitesTurn())
                ||(isBlackChessPiece(chessPiece)&&isBlacksTurn());
    }

    /**
     * The method check whether a given move is correct
     * @param move String object representing BOUND VECTOR in CHARACTER NOTATION
     * @return true if given BOUND VECTOR is correct in CHESS COORDINATE SYSTEM
     * and represents a correct movement in chess
     */
    static boolean isChessMoveCorrect(String move) {
        if (move == null || !move.matches("([a-h][1-8]){2}"))
            return false;
        int[][] chessMoveVector = getBoundChessMoveVector(move);
        char chessElement = getChessElement(chessMoveVector[0]);
        if(!isCorrectTurn(chessElement))
            return false;
        return switch (chessElement) {
            case blackKing, whiteKing:
                yield isCorrectKingMove(chessMoveVector);
            case blackQueen, whiteQueen:
                yield isCorrectQueenMove(chessMoveVector);
            case blackRook, whiteRook:
                yield isCorrectRookMove(chessMoveVector);
            case blackBishop, whiteBishop:
                yield isCorrectBishopMove(chessMoveVector);
            case blackKnight, whiteKnight:
                yield isCorrectKnightMove(chessMoveVector);
            case blackPawn:
                yield isCorrectPawnMove(chessMoveVector, true);
            case whitePawn:
                yield isCorrectPawnMove(chessMoveVector, false);
            default:
                yield false;
        };
    }

    /**
     * The method checks whether static char[][] chessArray
     * contains correct representation of the chessboard.
     * Which means it's array of 8 subarrays each containg
     * 8 characters with all characters being
     * either spaces or unicode chess symbols
     * @return true if array has the right format, otherwise false
     */
    static boolean isChessArrayCorrect() {
        if (chessArray == null || chessArray.length != 8) return false;
        for (char[] row : chessArray) {
            if (row == null || row.length != 8) return false;
            for (char element : row) {
                if (element != ' ' && !isChessPiece(element)) return false;
            }
        }
        return true;
    }

    /**
     * The method takes static char[][] chessArray
     * and returns its representation as String object
     * with standard chessboard symbols added to the edges
     * @return String object representation of the chessboard
     */
    static String getChessBoardString() {
        if (!isChessArrayCorrect())
            throw new IllegalArgumentException("Incorrect chess array!");
        StringBuilder builder = new StringBuilder(110);
        builder.append("*abcdefgh*").append('\n');
        for (int i = 0, nr = 8; i < chessArray.length; i++, nr--) {
            builder.append(nr);
            for (int j = 0; j < chessArray[i].length; j++) {
                builder.append(chessArray[i][j]);
            }
            builder.append(nr).append('\n');
        }
        builder.append("*abcdefgh*").append('\n');
        return builder.toString();
    }

    /**
     * The method updates characters in static char[][] chessArray representing chessboard.
     * Updated chessboard is chessboard after making a move described by BOUND VECTOR move.
     * The method checks whether BOUND VECTOR move is correct and describes a correct movement
     * in CHESS COORDINATE SYSTEM before updating a chessboard.
     * If the check fails, it throws an exception.
     * @param move String object representing BOUND VECTOR in CHARACTER NOTATION
     */
    static void makeChessMove(String move) {
        if (!isChessMoveCorrect(move))
            throw new IllegalArgumentException("Incorrect chess move");
        int[][] chessMoveVector = getBoundChessMoveVector(move);
        char firstElement = getChessElement(chessMoveVector[0]);
        char secondElement = getChessElement(chessMoveVector[1]);
        setChessElement(chessMoveVector[0], ' ');
        setChessElement(chessMoveVector[1], firstElement);
        turn++;
        switch (secondElement) {
            case whiteKing -> theStateOfTheGame = "blacks win";
            case blackKing -> theStateOfTheGame = "whites win";
            default -> theStateOfTheGame = "none wins";
        }
    }

    /**
     * The method prompts a user to enter a chess move as a BOUND VECTOR in CHARACTER NOTATION
     * If the BOUND VECTOR entered wasn't correct or didn't represent a correct chess move,
     * it keeps prompting the user for a new one, until it gets the right one.
     * @return String object representing BOUND VECTOR in CHARACTER NOTATION describing a correct chess movement
     */
    static String getCorrectChessMove() {
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
     * The method displays the chessboard in the terminal
     * and prompts the user to enter a chess move command,
     * the prompt is first saying to move a white chesspiece,
     * then to move a black chess piece. The method quits
     * after one of sides whites or blacks wins.
     */
    static void playChess(){
        do {
            System.out.println(getChessBoardString());
            String move = getCorrectChessMove();
            makeChessMove(move);
        } while (theStateOfTheGame.equals("none wins"));
        System.out.println(getChessBoardString());
        System.out.println(theStateOfTheGame);
    }

    /**
     * The main method of the program
     * @param args command line arguments
     */
    public static void main(String[] args) {
        playChess();
    }

}
