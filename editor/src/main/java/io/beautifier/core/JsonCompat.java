package io.beautifier.core;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Small compatibility shim for {@code org.json}.
 *
 * <p>Android ships its own copy of {@code org.json} baked into the framework
 * ({@code android.jar}), and it is an old, frozen snapshot of the upstream "json-java"
 * project. Because it lives on the boot classpath, Android's version always wins over any
 * {@code org.json:json} Maven/Gradle dependency you add to your app &mdash; the framework
 * class loader resolves {@code org.json.*} before your app's classes ever get a look in, no
 * matter what version you declare as a dependency. In practice this means:
 *
 * <ul>
 *   <li>Newer convenience methods added to the desktop library over the years &mdash;
 *       {@code JSONObject#keySet()}, {@code JSONObject#toMap()}, {@code JSONObject#append},
 *       and similar &mdash; simply do not exist at runtime on Android, even if your code
 *       compiled fine against the desktop jar. Calling them throws
 *       {@code NoSuchMethodError} on a real device/emulator.</li>
 *   <li>{@code org.json.JSONException} is a <em>checked</em> exception on Android
 *       (extends {@code Exception}), whereas the desktop library made it unchecked
 *       (extends {@code RuntimeException}) years ago. Code written against the desktop jar
 *       without try/catch around {@code getInt}/{@code getString}/etc. will fail to compile
 *       against the Android SDK with "unreported exception" errors.</li>
 * </ul>
 *
 * <p>The fix is simply: don't add {@code org.json:json} as a Gradle dependency at all (Android
 * already provides {@code org.json} at runtime), and only call methods that exist in Android's
 * version. This class fills in the handful of newer methods this codebase or its callers might
 * otherwise reach for.
 */
public final class JsonCompat {

	private JsonCompat() {
	}

	/**
	 * Equivalent of the desktop org.json {@code JSONObject#keySet()}, implemented using only
	 * {@code JSONObject#keys()} (an {@code Iterator<String>}), which IS available on Android.
	 * Preserves iteration order.
	 */
	public static Set<String> keySet(JSONObject json) {
		Set<String> keys = new LinkedHashSet<>();
		if (json == null) {
			return keys;
		}
		Iterator<String> it = json.keys();
		while (it.hasNext()) {
			keys.add(it.next());
		}
		return keys;
	}

	/**
	 * Equivalent of the desktop org.json {@code JSONObject#toMap()}. Converts simple scalar
	 * values, nested {@code JSONObject}s (recursively, as a {@code Map}), and nested
	 * {@code JSONArray}s (recursively, as a {@code java.util.List}).
	 */
	public static Map<String, Object> toMap(JSONObject json) {
		Map<String, Object> map = new TreeMap<>();
		if (json == null) {
			return map;
		}
		for (String key : keySet(json)) {
			Object value;
			try {
				value = json.get(key);
			} catch (JSONException e) {
				// Can't happen: key came from json.keys(), so it's guaranteed present.
				throw new IllegalStateException(e);
			}
			map.put(key, unwrap(value));
		}
		return map;
	}

	private static Object unwrap(Object value) {
		if (value instanceof JSONObject) {
			return toMap((JSONObject) value);
		}
		if (value instanceof JSONArray) {
			JSONArray array = (JSONArray) value;
			java.util.List<Object> list = new java.util.ArrayList<>(array.length());
			for (int i = 0; i < array.length(); i++) {
				Object item;
				try {
					item = array.get(i);
				} catch (JSONException e) {
					throw new IllegalStateException(e);
				}
				list.add(unwrap(item));
			}
			return list;
		}
		if (value == JSONObject.NULL) {
			return null;
		}
		return value;
	}

	/**
	 * Runs a {@code JSONObject} accessor without forcing callers to deal with the
	 * checked-on-Android/unchecked-on-desktop split in {@code JSONException}. Wraps any
	 * {@code JSONException} as an unchecked {@link IllegalArgumentException}.
	 */
	public static <T> T getUnchecked(JsonGetter<T> getter) {
		try {
			return getter.get();
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public interface JsonGetter<T> {
		T get() throws JSONException;
	}
}
