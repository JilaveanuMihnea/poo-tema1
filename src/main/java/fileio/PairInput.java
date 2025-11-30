package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class PairInput {
    private int x;
    private int y;

    public PairInput(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}
