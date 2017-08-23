package kr.re.etri.kmtk;

import java.io.*;
import java.nio.ByteBuffer;

public class Common {
    public static byte[] bytes(Object x) throws IOException {
        if (x instanceof Integer) {
            ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
            b.putInt((Integer) x).getInt();
            return b.array();
        } else if (x instanceof String)
            return ((String) x).getBytes();
        else {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(x);
            return b.toByteArray();
        }
    }

    public static Integer asInt(Object x) {
        if (x instanceof byte[]) {
            ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
            b.put((byte[]) x);
            b.flip();
            return b.getInt();
        } else
            return null;
    }

    public static String asStr(Object x) {
        if (x instanceof byte[])
            return new String((byte[]) x);
        else
            return null;
    }

    public static Object asObj(Object x) throws IOException, ClassNotFoundException {
        if (x instanceof byte[]) {
            ByteArrayInputStream b = new ByteArrayInputStream((byte[]) x);
            ObjectInputStream i = new ObjectInputStream(b);
            return i.readObject();
        } else
            return x;
    }
}
