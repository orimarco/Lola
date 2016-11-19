package il.ac.technion.cs.ssdl.lola.utils;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.annotation.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
/**
 * Represents a linked list, along with its advanced {@link Chain.Node} and
 * {@link Chain.Interval} and concepts.
 * 
 * @author Yossi Gil
 * @param <T>
 * @since 2016
 */
public class Chain<T, K> implements Iterable<Chain<T, K>.Node> {
	final Map<K, List<Interval>> earmarked = new HashMap<>();
	private Location firstLocation = new Location(null, null);
	private Location lastLocation = firstLocation;
	private Node firstNode;

	public Chain() {
	}

	public Chain(final List<T> li) {
		for (final T t : li)
			add(t);
	}

	/*
	 * Add value to chain. Organizes the locations and nodes so it will be in the
	 * right place.
	 */
	public Chain<T, K> add(final T t) {
		final Node newNode = new Node(t, lastLocation, null);
		final Location newLocation = new Location(newNode, null);
		lastLocation = newNode.after = newLocation;
		if (firstNode == null) // first node ever
			firstNode = newNode;
		return this;
	}

	public Chain<T, K> addFirst(final T t) {
		final Node newNode = new Node(t, null, firstLocation);
		final Location newLocation = new Location(null, newNode);
		firstLocation = newNode.before = newLocation;
		firstNode = newNode;
		return this;
	}

	public Node begin() {
		return firstNode;
	}

	public Node end() {
		return null;
	}

	@Override
	public Iterator<Node> iterator() {
		return new ChainIterator();
	}

	@SuppressWarnings("unused")
	public void printChain() {
		if (true)
			return;
		for (Location l = firstLocation; l != null; l = l.next())
			// if (l.after.t instanceof TriviaBunny)
			// System.out.print((b.text().equals("\n") ? "<\\n>"
			// : b.text().equals("\r") ? "<\\r>"
			// : b.text().equals("\r\n") ? "<\\r\\n>"
			// : b.text().equals("\t") ? "<\\t>" : "<" + b.text() + ">"));
			// else
			System.out.print(" " + l.idx + " " + (l.next() == null
					? ""
					: "\r\n".equals(((Bunny) l.after.t).text())
							? "[\\r\\n]"
							: "[" + ((Bunny) l.after.t).text() + "] "));
		System.out.println("");
	}
	public class Interval implements Content<T, K> {
		private Location from;
		private Location to;
		private K k;

		public Interval(final Location from, final Location to, final K k) {
			assert from.lessThanOrEquals(to);
			this.from = from;
			this.to = to;
			this.k = k;
		}

		Interval(final Location l, final K k) {
			this(l, l, k);
		}

		public void append(final List<T> elements) {
			if (elements.isEmpty())
				return;
			final Pair<Node, Node> pair = elementsToSubChain(elements);
			new Location(pair.y, to.after);
			to.after = pair.x;
			pair.x.before = to;
		}

		@Override
		public boolean containedIn(final Interval i) {
			return i.from.lessThanOrEquals(from) && to.lessThanOrEquals(i.to);
		}

		public void delete() {
			for (final K k : earmarked.keySet())
				earmarked.put(k, earmarked.get(k).stream()
						.filter(i -> !i.containedIn(this)).collect(Collectors.toList()));
			from.after = to.after;
			if (to.after != null)
				to.after.before = from;
		}

		public void earmark() {
			// System.out.println(
			// from.idx + " <-> " + to.idx/* + ":" + (earmarked.containsKey(k)
			// &&
			// * earmarked.get(k).stream().anyMatch(i ->
			// * this.containedIn(i))) */);
			if (!earmarked.containsKey(k))
				earmarked.put(k, new ArrayList<>());
			earmarked.get(k).add(this);
		}

		public boolean earmarked() {
			return earmarked.containsKey(k)
					&& earmarked.get(k).stream().anyMatch(i -> this.containedIn(i));
		}

		public Location from() {
			return from;
		}

		public boolean isEmpty() {
			return size() == 0;
		}

		public Iterable<Location> locations() {
			final List<Location> $ = new ArrayList<>();
			for (Location l = from; from != to; l = l.next())
				$.add(l);
			$.add(to);
			return $;
		}

		public Iterable<Node> nodes() {
			final List<Node> $ = new ArrayList<>();
			for (Location l = from; l != to; l = l.next())
				$.add(l.after());
			return $;
		}

		public void prepend(final List<T> elements) {
			if (elements.isEmpty())
				return;
			final Pair<Node, Node> pair = elementsToSubChain(elements);
			new Location(from.before, pair.x);
			from.before = pair.y;
			pair.y.after = from;
		}

		public void replace(final List<T> elements) {
			printChain();
			if (elements.isEmpty()) {
				from.after = to.after;
				if (to.after != null)
					to.after.before = from;
				return;
			}
			final Pair<Node, Node> pair = elementsToSubChain(elements);
			pair.x.before = from;
			from.after = pair.x;
			pair.y.after = to;
			to.before = pair.y;
			printChain();
		}

		public int size() {
			return 0;
		}

		public boolean strictlyContainedIn(final Interval i) {
			return i.from.lessThan(from) && to.lessThan(i.to);
		}

		public Location to() {
			return to;
		}

		/**
		 * Creates a chain of Nodes which starts with a node and ends with a node
		 * Node - Location - Node - ... - Node - Location - Node
		 */
		private Pair<Node, Node> elementsToSubChain(final List<T> elements) {
			/* first node */
			final double step = (to.idx - from.idx) / (elements.size() + 2);
			double idx = from.idx + step;
			final Node first = new Node(elements.get(0), null, null);
			Node newNode = first;
			Location location = new Location(newNode, null, idx);
			newNode.after = location;
			/* 2 to n-1 nodes */
			for (int i = 1; i < elements.size() - 1; ++i) {
				idx += step;
				newNode = new Node(elements.get(i), location, null);
				location.after = newNode;
				location = new Location(newNode, null, idx);
				newNode.after = location;
			}
			return new Pair<>(first,
					elements.size() <= 1
							? first
							: new Node(elements.get(elements.size() - 1), location, null));
		}
	}
	public class Location implements Content<T, K> {
		private Node before;
		Node after;
		private final double idx;

		public Location(final Node before, final Node after) {
			if (before != null)
				before.after = this;
			if (after != null)
				after.before = this;
			this.before = before;
			this.after = after;
			idx = generateIdx(before, after);
		}

		private Location(final Node before, final Node after, final double idx) {
			if (before != null)
				before.after = this;
			if (after != null)
				after.before = this;
			this.before = before;
			this.after = after;
			this.idx = idx;
		}

		@Nullable
		public Node after() {
			return after;
		}

		@Nullable
		public Node before() {
			return before;
		}

		@Override
		public boolean containedIn(final Interval i) {
			return isAfter(i.from()) && isBefore(i.to());
		}

		public boolean greaterThan(@NonNull final Location l) {
			return idx > l.idx;
		}

		public boolean greaterThanOrEquals(@NonNull final Location l) {
			return idx >= l.idx;
		}

		public boolean isAfter(final Location l) {
			return l.idx <= idx;
		}

		public boolean isBefore(final Location l) {
			return idx <= l.idx;
		}

		public boolean lessThan(@NonNull final Location l) {
			return idx < l.idx;
		}

		public boolean lessThanOrEquals(@NonNull final Location l) {
			return idx <= l.idx;
		};

		@Nullable
		public Location next() {
			return after == null ? null : after.after();
		};

		@Nullable
		public Location prev() {
			return before.before();
		};

		@NonNull
		public Interval union(@NonNull final Interval i) {
			i.to = this;
			return i;
		};

		private double generateIdx(final Node before, final Node after) {
			return (before == null || before.before == null)
					&& (after == null || after.after == null)
							? 0
							: before == null || before.before == null
									? after.after.idx - 1
									: after == null || after.after == null
											? before.before.idx + 1
											: (after.after.idx + before.before.idx) / 2;
		}
	}
	public class Node implements Content<T, K>, Supplier<T> {
		private @NonNull final T t;
		Location before;
		Location after;

		/** instantiates this class */
		public Node(@NonNull final T t, final Location before,
				final Location after) {
			this.t = t;
			this.before = before;
			this.after = after;
			if (before != null)
				before.after = this;
			if (after != null)
				after.before = this;
		}

		@NonNull
		public Location after() {
			return after;
		}

		@NonNull
		public Location before() {
			return before;
		}

		@Override
		public T get() {
			return t;
		}

		@Nullable
		public Node next() {
			return after.after();
		}

		@Nullable
		public Node prev() {
			return before.before();
		}
	}
	class ChainIterator implements Iterator<Node> {
		boolean started = false;
		Node curr = firstNode;

		ChainIterator() {
		}

		@Override
		public boolean hasNext() {
			return curr.next() != null || !started;
		}

		@Override
		public Node next() {
			if (started)
				curr = curr.next();
			started = true;
			return curr;
		}
	}
	interface Content<T, K> {
		default boolean containedIn(final Chain<T, K>.Interval i) {
			return false;
		}
	}
}
