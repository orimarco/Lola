package il.ac.technion.cs.ssdl.lola.parser;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.experimental.dag.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.*;
import org.jgrapht.graph.*;
/** The symbol table of categories and token names. */
public enum CategoriesHierarchy {
	;
	private static final Map<String, Category> name2category = new LinkedHashMap<>();

	/**
	 * Add category to hierarchy
	 * 
	 * @param name - name of category
	 */
	public static void addCategory(final String name) {
		Hierarchy.addCategory(new Category(name));
	}

	/**
	 * Add classification to hierarchy
	 * 
	 * @param name - name of category
	 * @throws CycleFoundException
	 */
	public static void addClassification(final String category, final String classification) throws CycleFoundException {
		Hierarchy.addDagEdge(name2category.get(category), name2category.get(classification));
	}

	/**
	 * Add category to hierarchy
	 * 
	 * @param name - name of category
	 */
	public static void addKeywordCategory(final String name) {
		Hierarchy.addKeywordCategory(new Category(name));
	}

	public static void addTriviaCategory(final String name) {
		Hierarchy.addTriviaCategory(new Category(name));
	}

	/**
	 * Use this function to get category by name
	 * 
	 * @param name
	 * @return
	 */
	public static Category getCategory(final String name) throws IllegalArgumentException {
		if (!name2category.containsKey(name))
			throw new IllegalArgumentException(name);
		return name2category.get(name);
	}

	public static boolean hasCategory(final String name) {
		return name2category.containsKey(name);
	}

	public static boolean isClassifiedAs(final Category c, final Category classification) {
		return Hierarchy.isClassifiedAs(c, classification);
	}

	public static boolean isClassifiedAs(final String c, final String classification) {
		return Hierarchy.isClassifiedAs(getCategory(c), getCategory(classification));
	}

	/**
	 * Instantiates this class
	 * 
	 * @param name name of the newly created instance
	 * @throws IllegalArgumentException in case the name was already used, is
	 *           null, or is empty
	 */
	public static Iterable<Category> leaves() {
		return new ArrayList<>();
	}

	public static Iterable<Category> roots() {
		return new ArrayList<>();
	}
	public static class Category {
		public final String name;
		public boolean isTrivia;
		public boolean isKeyword;

		public Category(final String name) {
			if (name2category.containsKey(name))
				throw new IllegalArgumentException(name);
			this.name = name;
			name2category.put(name, this);
		}

		@Override
		public boolean equals(final Object o) {
			if (o == this)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			final Category other = (Category) o;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			return 31 + (name == null ? 0 : name.hashCode());
		}

		@Override
		public String toString() {
			return "Category [name=" + name + "]";
		}
	}
	public static class Edge {
	}
	public static class Hierarchy {
		static DirectedAcyclicGraph<Category, Edge> inner = new DirectedAcyclicGraph<>(Edge.class);

		public static boolean addCategory(final Category ¢) {
			return inner.addVertex(¢);
		}

		public static Edge addDagEdge(final Category fromVertex, final Category toVertex) throws CycleFoundException {
			return inner.addDagEdge(fromVertex, toVertex);
		}

		public static void addKeywordCategory(final Category ¢) {
			¢.isKeyword = true;
			inner.addVertex(¢);
		}

		public static void addTriviaCategory(final Category ¢) {
			¢.isTrivia = true;
			inner.addVertex(¢);
		}

		public static boolean isClassifiedAs(final Category c, final Category classification) {
			final Set<Edge> set = inner.outgoingEdgesOf(c);
			for (final Edge ¢ : set)
				if (inner.getEdgeTarget(¢).equals(classification) || isClassifiedAs(inner.getEdgeTarget(¢), classification))
					return true;
			return false;
		}

		public boolean addCategory(final Category v, final boolean addToTop) {
			return inner.addVertex(v, addToTop);
		}

		public Edge addEdge(final Category sourceVertex, final Category targetVertex) {
			return inner.addEdge(sourceVertex, targetVertex);
		}

		public boolean containsEdge(final Category sourceVertex, final Category targetVertex) {
			return inner.containsEdge(sourceVertex, targetVertex);
		}

		public boolean containsVertex(final Category v) {
			return inner.containsVertex(v);
		}

		public int degreeOf(final Category vertex) {
			return inner.degreeOf(vertex);
		}

		public Set<Edge> edgeSet() {
			return inner.edgeSet();
		}

		public Set<Edge> edgesOf(final Category vertex) {
			return inner.edgesOf(vertex);
		}

		public Edge getEdge(final Category sourceVertex, final Category targetVertex) {
			return inner.getEdge(sourceVertex, targetVertex);
		}

		public EdgeFactory<Category, Edge> getEdgeFactory() {
			return inner.getEdgeFactory();
		}

		public Category getEdgeSource(final Edge ¢) {
			return inner.getEdgeSource(¢);
		}

		public Category getEdgeTarget(final Edge ¢) {
			return inner.getEdgeTarget(¢);
		}

		public double getEdgeWeight(final Edge ¢) {
			return inner.getEdgeWeight(¢);
		}

		@Override
		public int hashCode() {
			return inner.hashCode();
		}

		public boolean immediateGeneralizer(final Category sourceVertex, final Category targetVertex) {
			return inner.containsEdge(sourceVertex, targetVertex);
		}

		public Set<Edge> incomingEdgesOf(final Category vertex) {
			return inner.incomingEdgesOf(vertex);
		}

		public int inDegreeOf(final Category vertex) {
			return inner.inDegreeOf(vertex);
		}

		public boolean isAllowingLoops() {
			return inner.isAllowingLoops();
		}

		public boolean isAllowingMultipleEdges() {
			return inner.isAllowingMultipleEdges();
		}

		public Iterator<Category> iterator() {
			return inner.iterator();
		}

		public int outDegreeOf(final Category vertex) {
			return inner.outDegreeOf(vertex);
		}

		public Set<Edge> outgoingEdgesOf(final Category vertex) {
			return inner.outgoingEdgesOf(vertex);
		}

		public Set<Edge> removeAllEdges(final Category sourceVertex, final Category targetVertex) {
			return inner.removeAllEdges(sourceVertex, targetVertex);
		}

		public boolean removeAllEdges(final Collection<? extends Edge> ¢) {
			return inner.removeAllEdges(¢);
		}

		public boolean removeAllVertices(final Collection<? extends Category> arg0) {
			return inner.removeAllVertices(arg0);
		}

		public Edge removeEdge(final Category sourceVertex, final Category targetVertex) {
			return inner.removeEdge(sourceVertex, targetVertex);
		}

		public boolean removeEdge(final Edge ¢) {
			return inner.removeEdge(¢);
		}

		public boolean removeVertex(final Category v) {
			return inner.removeVertex(v);
		}

		public void setEdgeSetFactory(final EdgeSetFactory<Category, Edge> ¢) {
			inner.setEdgeSetFactory(¢);
		}

		public void setEdgeWeight(final Edge e, final double weight) {
			inner.setEdgeWeight(e, weight);
		}

		@Override
		public String toString() {
			return inner + "";
		}

		public Set<Category> vertexSet() {
			return inner.vertexSet();
		}
	}
}