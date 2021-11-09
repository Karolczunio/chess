package chess.element;

import chess.enums.ChessTileColor;
import chess.enums.PromotionType;

public record ChessTile(ChessTileColor color, PromotionType promotionType) {

    public ChessTile {
        if (color == null) {
            throw new IllegalArgumentException("Chess tile color cannot be null");
        }
        if (promotionType == null) {
            throw new IllegalArgumentException("Promotion type cannot be null");
        }
    }

    /**
     * @return character representing this chess tile
     */
    public char character() {
        return switch (color) {
            case WHITE:
                yield 'w';
            case BLACK:
                yield 'b';
        };
    }
}
