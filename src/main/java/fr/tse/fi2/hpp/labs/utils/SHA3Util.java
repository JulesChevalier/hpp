package fr.tse.fi2.hpp.labs.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;

public class SHA3Util {

    public static int digest(final String string, final int size) {

        final DigestSHA3 md = new DigestSHA3(size);
        final String text = string != null ? string : "null";
        try {
            md.update(text.getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException ex) {
            // most unlikely
            md.update(text.getBytes());
        }
        final byte[] digest = md.digest();
        return byteArrayToInt(digest);
    }

    public static int byteArrayToInt(final byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
}
