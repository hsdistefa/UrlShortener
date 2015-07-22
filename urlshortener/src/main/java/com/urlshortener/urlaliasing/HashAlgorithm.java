package com.urlshortener.urlaliasing;

public enum HashAlgorithm {
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String alg;

    HashAlgorithm(String alg) {
        this.alg = alg;
    }

    @Override
    public String toString() {
        return this.alg;
    }

    // this instead of valueOf() to deal with hyphens
    public static HashAlgorithm getEnum(String value) {
        switch (value) {
            case "SHA-1":
                return HashAlgorithm.SHA1;
            case "SHA-256":
                return HashAlgorithm.SHA256;
            case "SHA-384":
                return HashAlgorithm.SHA384;
            case "SHA-512":
                return HashAlgorithm.SHA512;
            default:
                for (HashAlgorithm alg : values()) {
                    if(alg.toString().equals(value)) return alg;
                }
                throw new IllegalArgumentException();
        }
    }
}
