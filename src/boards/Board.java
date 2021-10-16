package boards;

import java.util.Arrays;

public class Board {
    private final static int xLimit = 8;
    private final static int yLimit = 8;
    private final char[] accepted;
    private final char[][] contents;


    /**
     * This method checks whether array of accepted characters is non-null, non-zero
     * and no characters in the array are repeating
     * @param accepted array of accepted characters
     * @return true if array is valid, otherwise false
     * */
    private boolean isAcceptedValid(char[] accepted){
        boolean validAccepted = true;
        if (accepted == null || accepted.length == 0) {
            validAccepted = false;
        }else {
            for (int i = 0; i < accepted.length && validAccepted; i++) {
                for (int j = i+1; j < accepted.length; j++) {
                    if (accepted[i] == accepted[j]) {
                        validAccepted = false;
                        break;
                    }
                }
            }
        }
        return validAccepted;
    }

    /**
     * The method checks if 2D char array is non-null, regular, 8x8 and composed only from accepted characters
     * @param contents 2D char array representing pieces on the board
     * @return true if array is valid, otherwise false
     */
    private boolean isContentsValid(char[][] contents){
        boolean validContents = true;
        if (contents == null || contents.length != yLimit) {
            validContents = false;
        } else {
            for (int i = 0; i < contents.length && validContents; i++) {
                if (contents[i] == null || contents[i].length != xLimit) {
                    validContents = false;
                } else {
                    for (int j = 0; j < contents[i].length; j++) {
                        boolean validChar = false;
                        for (char element : accepted) {
                            if (contents[i][j] == element) {
                                validChar = true;
                                break;
                            }
                        }
                        if (!validChar) {
                            validContents = false;
                            break;
                        }
                    }
                }
            }
        }
        return validContents;
    }

    /**
     * The Board Constructor takes as an argument array of accepted characters and 2D array of contents
     * @param accepted has to be char array of length 1 or longer and contain only unique characters.
     * @param contents has to be regular 8x8 character array and contain only characters present in accepted array
     */
    public Board(char[] accepted, char[][] contents) {
        if (!isAcceptedValid(accepted)){
            throw new IllegalArgumentException("Invalid array of accepted characters");
        }
        this.accepted = accepted;
        if (!isContentsValid(contents)) {
            throw new IllegalArgumentException("Invalid array of contents");
        }
        this.contents = contents;
    }

    /**
     * The method checks if charaacter ch can be placed in this board
     * @param ch the character checked
     * @return true if it is accepted, otherwise false
     */
    public boolean isAccepted(char ch) {
        boolean test = false;
        for (char c : accepted) {
            if (ch == c) {
                test = true;
                break;
            }
        }
        return test;
    }

    /**
     * The method returns character from position where. Throws exception if position is invalid.
     * @param where the position on the board from which the character should be taken
     * @return the character on the board at the position where
     */
    public char getElement(Position where) {
        if (!where.isValid()) {
            throw new IllegalArgumentException("This position has no corresponding element on the board");
        }
        return contents[7-where.y()][where.x()];
    }

    /**
     * The method sets a character on the board at position where to value toValue.
     * Throws exception if position is invalid or character is not one of accepted characters.
     * @param where the position on the board at which the character should be changed
     * @param toValue the character to which the specified place on the board should be set
     */
    public void setElement(Position where, char toValue) {
        if (!where.isValid()) {
            throw new IllegalArgumentException("This position has no corresponding element on the board");
        }
        if (!this.isAccepted(toValue)) {
            throw new IllegalArgumentException("This value is not accepted on the board");
        }
        contents[7-where.y()][where.x()] = toValue;
    }

    /**
     * The method returns contents of the board
     * together with the inscriptions on the sides of the board
     * as a string
     * @return string representing object of class Board
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("*abcdefgh*\n");
        int rowNumber;
        for (int i = 0; i < 8; i++) {
            rowNumber = 8 - i;
            builder
                    .append(rowNumber)
                    .append(String.valueOf(contents[i]))
                    .append(rowNumber)
                    .append('\n');
        }
        builder.append("*abcdefgh*\n");
        return builder.toString();
    }

    /**
     * The method checks if two objects are equal
     * @param o Object to check for equality
     * @return true if both Objects are of type Board and have the same contents and accepted characters, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.deepEquals(contents, board.contents) && Arrays.equals(accepted, board.accepted);
    }

    /**
     * The method returns hashcode of an object
     * @return hashcode of this object
     */
    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(contents);
        result = 31 * result + Arrays.hashCode(accepted);
        return result;
    }
}
