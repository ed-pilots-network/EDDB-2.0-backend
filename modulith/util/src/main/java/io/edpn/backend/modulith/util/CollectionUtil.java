package io.edpn.backend.modulith.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CollectionUtil {

    public static <T> List<T> toList(T[] array) {
        return Optional.ofNullable(array)
                .map(ts -> Arrays.stream(ts).toList())
                .orElseGet(Collections::emptyList);
    }
}
