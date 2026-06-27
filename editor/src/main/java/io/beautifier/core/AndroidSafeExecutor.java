package io.beautifier.core;

/**
 * Runs a unit of work on a dedicated {@link Thread} with a large, explicit stack size.
 *
 * <p>Why this exists: this library's tokenizers rely heavily on {@code java.util.regex}
 * patterns with nested quantifiers and alternation (see {@code Acorn}, the JS/CSS/HTML
 * {@code Tokenizer} classes, etc). Those patterns are matched using ordinary Java regex
 * semantics and are not exotic in any way (no atomic groups, no possessive quantifiers, no
 * unicode property classes, no named groups/back-references) &mdash; they compile and run
 * identically on the JVM and on Android's ICU-backed {@code java.util.regex} implementation.
 *
 * <p>What IS different on Android is the default thread stack size. Desktop JVMs typically
 * give the main thread 512KB&ndash;1MB of stack by default; Android threads (including the
 * main/UI thread) are commonly given a much smaller stack. Java's regex matcher is a
 * backtracking engine implemented recursively, so matching the same pattern against the same
 * input can consume noticeably more native stack on Android than it does in a desktop
 * unit-test run. For small snippets this never matters. For large source files (long minified
 * lines, deeply nested JSON-in-JS, huge CSS files, etc.) it can blow the stack on a device even
 * though the exact same input beautifies fine on a desktop JVM during development &mdash; which
 * matches the "works in my Java tests, breaks specifically on Android" symptom.
 *
 * <p>The fix is not to rewrite the regexes (they're fine), it's to give the beautifier call a
 * thread with enough stack headroom, the same way you'd size a stack for any other deep
 * recursive algorithm. Route any call into {@link io.beautifier.javascript.JavaScriptBeautifier},
 * {@link io.beautifier.css.CSSBeautifier}, or {@link io.beautifier.html.HTMLBeautifier} through
 * this class (or through {@link AndroidBeautify}, which already does this) instead of calling
 * {@code beautify(...)} directly from the calling thread.
 */
public final class AndroidSafeExecutor {

	/** 8MB is comfortably larger than any default Android thread stack and cheap to allocate. */
	public static final long DEFAULT_STACK_SIZE_BYTES = 8L * 1024 * 1024;

	private AndroidSafeExecutor() {
	}

	/** A unit of work that returns a value and may throw any exception. */
	public interface Job<T> {
		T run() throws Exception;
	}

	/** A unit of work that returns a value and never throws a checked exception. */
	public interface UncheckedJob<T> {
		T run();
	}

	/**
	 * Runs {@code job} on a new thread with {@link #DEFAULT_STACK_SIZE_BYTES} of stack and
	 * blocks the calling thread until it completes. Any exception or error thrown inside
	 * {@code job} is rethrown on the calling thread, preserving its original type where possible.
	 */
	public static <T> T run(Job<T> job) throws Exception {
		return run(job, DEFAULT_STACK_SIZE_BYTES);
	}

	public static <T> T run(Job<T> job, long stackSizeBytes) throws Exception {
		final Object[] resultBox = new Object[1];
		final Throwable[] errorBox = new Throwable[1];

		Thread worker = new Thread(null, new Runnable() {
			@Override
			public void run() {
				try {
					resultBox[0] = job.run();
				} catch (Throwable t) {
					errorBox[0] = t;
				}
			}
		}, "beautifier-worker", stackSizeBytes);

		worker.start();
		try {
			worker.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		}

		Throwable error = errorBox[0];
		if (error != null) {
			if (error instanceof RuntimeException) {
				throw (RuntimeException) error;
			}
			if (error instanceof Error) {
				throw (Error) error;
			}
			if (error instanceof Exception) {
				throw (Exception) error;
			}
			throw new RuntimeException(error);
		}

		@SuppressWarnings("unchecked")
		T result = (T) resultBox[0];
		return result;
	}

	/**
	 * Convenience variant for jobs that don't throw checked exceptions (e.g. calls into this
	 * library's {@code beautify(...)} methods, none of which declare checked exceptions).
	 * Any {@link RuntimeException} or {@link Error} thrown inside {@code job} is rethrown as-is.
	 */
	public static <T> T runUnchecked(UncheckedJob<T> job) {
		return runUnchecked(job, DEFAULT_STACK_SIZE_BYTES);
	}

	public static <T> T runUnchecked(UncheckedJob<T> job, long stackSizeBytes) {
		try {
			return run(new Job<T>() {
				@Override
				public T run() {
					return job.run();
				}
			}, stackSizeBytes);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			// Cannot happen: UncheckedJob#run() declares no checked exceptions.
			throw new RuntimeException(e);
		}
	}
}
