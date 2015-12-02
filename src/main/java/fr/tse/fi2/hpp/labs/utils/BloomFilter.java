package fr.tse.fi2.hpp.labs.utils;

import java.util.BitSet;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class BloomFilter {

    public static final int SALT_SIZE = 10;
    public static final double ERROR_P = 0.1;

    public final int n;
    public final int m;
    public final int k;

    public final String[] salts;

    public final BitSet bitset;

    @SuppressWarnings("unused")
    private BloomFilter() {
        this.n = 0;
        this.m = this.k = 0;
        this.salts = null;
        this.bitset = null;
    }

    public BloomFilter(final int n) {
        this.n = n;
        this.m = (int) (-(n * Math.log(ERROR_P / 100.)) / (Math.log(2) * Math.log(2)));
        this.k = (int) Math.ceil(this.m / this.n * Math.log(2));

        this.salts = new String[this.k];
        this.bitset = new BitSet(this.m);

        for (int i = 0; i < this.k; i++) {
            this.salts[i] = RandomString.nextString(SALT_SIZE);
        }

        System.out.println("n=" + this.n + " m=" + this.m + " k=" + this.k);
    }

    public void add(final String message) {
        for (final String salt : this.salts) {
            this.bitset.set(this.hash(message + salt));
        }
    }

    public boolean contains(final String message) {
        int i = 0;
        while (i < this.salts.length) {
            final String salt = this.salts[i];
            if (!this.bitset.get(this.hash(message + salt))) {
                return false;
            }
            i++;
        }
        return true;

    }

    public int hash(final String message) {
        final HashFunction hasher = Hashing.murmur3_32();
        return Math.abs(hasher.hashUnencodedChars(message).asInt() % this.m);
        // return Math.abs(SHA3Util.digest(message, 512) % this.m);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        while (i < this.bitset.size() - 1) {
            sb.append(this.bitset.get(i) ? 1 : 0);
            i++;
        }
        sb.append("]");
        return sb.toString();
    }
}
