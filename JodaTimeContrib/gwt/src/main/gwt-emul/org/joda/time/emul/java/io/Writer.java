package java.io;

public class Writer {

    public void write(int i) {
        throw writerUnsupportedInGwt();
    }

    public void write(String string) {
        throw writerUnsupportedInGwt();
    }

    private static UnsupportedOperationException writerUnsupportedInGwt() {
        return new UnsupportedOperationException("Writer not supported in GWT");
    }
}
