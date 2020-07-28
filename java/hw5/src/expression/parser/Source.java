package expression.parser;

public class Source {
    private static int OFFSET = 10;
    private final String data;
    private int pos;

    public Source(final String data) {
        this.data = data;
        pos = 0;
    }

    public boolean hasNext() {
        return pos < data.length();
    }

    public boolean test(char ch) {
        return ch == data.charAt(pos - 1);
    }

    public int getPos() {
        return pos;
    }

    public char next() {
        return data.charAt(pos++);
    }

    public String getSuffix(final int shift) {
        return data.substring(Math.max(pos - OFFSET - shift, 0), pos - shift - 1)
                + "---->" + data.substring(pos - shift - 1, Math.min(pos + OFFSET - shift, data.length()));
    }

    public boolean checkEntry(final String prefix) {
        if (data.startsWith(prefix, pos - 1)) {
            pos += prefix.length() - 1;
            return true;
        }
        return false;
    }

    public String substringWithOffset(final int shift) {
        return data.substring(Math.max(pos - OFFSET - shift, 0), Math.max(pos - shift - 2, 0)) + "<HERE>"
                + data.substring(Math.max(pos - shift - 2, 0), Math.min(pos + OFFSET - shift, data.length()));
    }
}