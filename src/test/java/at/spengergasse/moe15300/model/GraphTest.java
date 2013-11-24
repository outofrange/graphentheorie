package at.spengergasse.moe15300.model;

import junit.framework.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import at.spengergasse.moe15300.model.matrix.IntegerMatrix;
import at.spengergasse.moe15300.model.matrix.implementation.SquareMatrixArrayList;
import at.spengergasse.moe15300.util.Timer;

public class GraphTest {
	private static final Logger log = LogManager.getLogger(GraphTest.class);

	@Test
	public void testDistanceAndWegMatrix() {
		Graph graph = new Graph(5);
		graph.addEdge(1, 3, true);
		graph.addEdge(1, 4, true);
		graph.addEdge(2, 4, true);

		IntegerMatrix distanceMatrix = new SquareMatrixArrayList(new int[][] {
				{ 0, Graph.INFINITE, Graph.INFINITE, Graph.INFINITE,
						Graph.INFINITE }, { Graph.INFINITE, 0, 2, 1, 1 },
				{ Graph.INFINITE, 2, 0, 3, 1 }, { Graph.INFINITE, 1, 3, 0, 2 },
				{ Graph.INFINITE, 1, 1, 2, 0 } });
		IntegerMatrix wegMatrix = new SquareMatrixArrayList(new int[][] {
				{ 1, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1 },
				{ 0, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1 } });

		Assert.assertEquals(graph.getDistanzMatrix(), distanceMatrix);
		Assert.assertEquals(graph.getWegMatrix(), wegMatrix);
		log.info("----------");
	}

	@Test(timeout = 6000)
	public void testBigEmptyGraph() {
		final int size = 10000;

		Timer.start(0);
		new Graph(size);
		log.info("Building empty graph with size of " + size + " took "
				+ Timer.getTime(0) + " ms");
		log.info("----------");
	}

	@Test(timeout = 1000)
	public void testAddEdgesInBigGraph() {
		final int size = 1000;
		log.debug("Start constructing graph with size of " + size);
		Graph graph = new Graph(size);
		log.debug("Done constructing graph with size of " + size);

		Timer.start(0);
		graph.addEdge(100, 200, true);
		log.info("Adding undirected edge in graph of size " + size + " took "
				+ Timer.getTime(0) + " ms");

		Timer.start(0);
		graph.addEdge(100, 300, true);
		log.info("Adding another undirected edge in graph of size " + size
				+ " took " + Timer.getTime(0) + " ms");

		Timer.start(0);
		graph.addEdge(1, 2, false);
		log.info("Adding a directed edge in graph of size " + size + " took "
				+ Timer.getTime(0) + " ms");
		log.info("----------");
	}

	@Test
	public void testGettingDistanceMatrixForBigGraph() {
		final int size = 1000;
		log.debug("Start constructing graph with size of " + size);
		Graph graph = new Graph(size);
		log.debug("Done constructing graph with size of " + size);

		Timer.start(0);
		graph.addEdge(1, 2, true);
		log.info("Adding undirected edge in graph of size " + size + " took "
				+ Timer.getTime(0) + " ms");

		Timer.start(0);
		graph.addEdge(3, 4, true);
		log.info("Adding another undirected edge in graph of size " + size
				+ " took " + Timer.getTime(0) + " ms");

		Timer.start(0);
		graph.addEdge(5, 6, true);
		log.info("Adding a directed edge in graph of size " + size + " took "
				+ Timer.getTime(0) + " ms");

		log.info("\n" + graph.getDistanzMatrix().toString());

		log.info("----------");
	}

	@Test
	public void testIteratingSpeed() {
		Graph graph = new Graph(10000);

		final int runs = 10;
		long sum = 0;

		for (int i = 0; i < runs; i++) {
			Timer.start("Iterating");
			graph.iterateThroughTheGraph();
			sum += Timer.getTime("Iterating");
		}

		System.out.println("Mean time to iterate " + runs + "x times: " + sum
				/ runs);
	}
}
