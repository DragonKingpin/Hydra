package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.util.Arrays;

public abstract class ArchGuidAllocator128 implements GuidAllocator128 {
    public static final int GUID_CHARS = 36;

    @Override
    public GUID parse(String hexId ) {
        return Parser.parse( hexId );
    }

    static final class Parser {

        private static final byte[] MAP;
        static {
            byte[] mapping = new byte[256];
            Arrays.fill(mapping, (byte) -1);
            mapping['0'] = 0;
            mapping['1'] = 1;
            mapping['2'] = 2;
            mapping['3'] = 3;
            mapping['4'] = 4;
            mapping['5'] = 5;
            mapping['6'] = 6;
            mapping['7'] = 7;
            mapping['8'] = 8;
            mapping['9'] = 9;
            mapping['a'] = 10;
            mapping['b'] = 11;
            mapping['c'] = 12;
            mapping['d'] = 13;
            mapping['e'] = 14;
            mapping['f'] = 15;
            mapping['A'] = 10;
            mapping['B'] = 11;
            mapping['C'] = 12;
            mapping['D'] = 13;
            mapping['E'] = 14;
            mapping['F'] = 15;
            MAP = mapping;
        }

        private static final int DASH_POSITION_1 = 8;
        private static final int DASH_POSITION_2 = 13;
        private static final int DASH_POSITION_3 = 18;
        private static final int DASH_POSITION_4 = 23;


        public static GUID parse(final String string) {

            validate(string);

            long msb = 0;
            long lsb = 0;

            for (int i = 0; i < 8; i++) {
                msb = (msb << 4) | get(string, i);
            }

            for (int i = 9; i < 13; i++) {
                msb = (msb << 4) | get(string, i);
            }

            for (int i = 14; i < 18; i++) {
                msb = (msb << 4) | get(string, i);
            }

            for (int i = 19; i < 23; i++) {
                lsb = (lsb << 4) | get(string, i);
            }

            for (int i = 24; i < 36; i++) {
                lsb = (lsb << 4) | get(string, i);
            }

            return new UUID128(msb, lsb);
        }

        public static boolean valid(final String guid) {
            try {
                parse(guid);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        private static long get(final String string, int i) {

            final int chr = string.charAt(i);
            if (chr > 255) {
                throw exception(string);
            }

            final byte value = MAP[chr];
            if (value < 0) {
                throw exception(string);
            }
            return value & 0xffL;
        }

        private static RuntimeException exception(final String str) {
            return new IllegalArgumentException("Invalid UUID: " + str);
        }

        private static void validate(final String string) {
            if (string == null || string.length() != GUID_CHARS) {
                throw exception(string);
            }
            if (string.charAt(DASH_POSITION_1) != '-' || string.charAt(DASH_POSITION_2) != '-'
                    || string.charAt(DASH_POSITION_3) != '-' || string.charAt(DASH_POSITION_4) != '-') {
                throw exception(string);
            }
        }
    }

}
