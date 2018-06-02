package parser;
public class SkipToken {
    private int token;

    private boolean consume;

    public SkipToken(int token, boolean consume) {
        this.token = token;
        this.consume = consume;
    }

    public int getToken() {
        return token;
    }

    public boolean isConsumed() {
        return consume;
    }
}
