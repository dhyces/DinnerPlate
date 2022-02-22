package dhyces.dinnerplate.util;

import java.util.function.Function;
import java.util.stream.Stream;

public class Util {

	public static <T> Stream<T> streamOf(Function<Integer, T> arrayAccessor, int size) {
		Stream.Builder<T> builder = Stream.builder();
		for (int i = 0; i < size; i++) {
			builder.add(arrayAccessor.apply(i));
		}
		return builder.build();
	}

}
