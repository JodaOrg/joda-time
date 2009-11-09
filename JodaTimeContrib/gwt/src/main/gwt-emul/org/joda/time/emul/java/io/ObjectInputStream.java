package java.io;

public class ObjectInputStream {

    public ObjectInputStream(InputStream in) throws IOException {
        throw objectInputStreamUnsupportedInGwt();
    }

    public String readUTF() {
        throw objectInputStreamUnsupportedInGwt();
    }

    public void defaultReadObject() {
        throw objectInputStreamUnsupportedInGwt();
    }

    public Object readObject() {
        throw objectInputStreamUnsupportedInGwt();
    }

    public void close() {
        throw objectInputStreamUnsupportedInGwt();
    }

    private static UnsupportedOperationException objectInputStreamUnsupportedInGwt() {
        return new UnsupportedOperationException("ObjectInputStream not supported in GWT");
    }
}
