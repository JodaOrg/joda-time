package java.io;

public class ObjectOutputStream {

    public ObjectOutputStream(OutputStream out) throws IOException {
        throw objectOutputStreamUnsupportedInGwt();
    }

    public void writeUTF(String iID) {
        throw objectOutputStreamUnsupportedInGwt();
    }

    public final void writeObject(Object obj) throws IOException {
        throw objectOutputStreamUnsupportedInGwt();
    }

    public void close() {
        throw objectOutputStreamUnsupportedInGwt();
    }

    private static UnsupportedOperationException objectOutputStreamUnsupportedInGwt() {
        return new UnsupportedOperationException("ObjectOutputStream not supported in GWT");
    }

}
