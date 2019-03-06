package io.git.zjoker.zcache.utils;


public final class Optional {

    public static <T> T checkNotNull(T ref) {
        return checkNotNull(ref, "ref is null!!!");
    }

    public static <T> T checkNotNull(T ref, String errorMsg) {
        if (ref == null) {
            throw new NullPointerException(errorMsg);
        }
        return ref;
    }
}
