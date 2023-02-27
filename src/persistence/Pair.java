package persistence;

import java.io.Serializable;

public record Pair (
    String key,
    Object val
) implements Serializable {
    private final static long serialVersionUID = 114514L;
};
