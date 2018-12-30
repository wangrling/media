package com.android.mm.algs.structures.graphs;

import java.util.ArrayList;

public class AdjacencyListGraph<E extends Comparable<E>> {

    ArrayList<Vertex> vertices;

    // 构造函数
    public AdjacencyListGraph() {
        vertices = new ArrayList<>();
    }

    private class Vertex {
        E data;
        ArrayList<Vertex> adjacentVertices;

        public Vertex(E data) {
            adjacentVertices = new ArrayList<>();
            this.data = data;
        }

        public boolean addAdjacentVertex(Vertex to) {
            for (Vertex v : adjacentVertices) {
                if (v.data.compareTo(to.data) == 0) {
                    return false;       // the edge already exists.
                }
            }

            return adjacentVertices.add(to);
        }

        public boolean removeAdjacentVertex(E to) {
            // use indexes here so it is possible to
            // remove easily without implementing
            // equals method that ArrayList.remove(Object o) uses
            for (int i = 0; i < adjacentVertices.size(); i++) {
                if (adjacentVertices.get(i).data.compareTo(to) == 0) {
                    adjacentVertices.remove(i);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * this method removes an edge from the graph between two specified vertices.
     *
     * @param from the data of the vertex the edge is from
     * @param to the data of the vertex the edge is going to
     * @return returns false if the edge doesn't exist, returns true if the edge exists and is removed
     */
    public boolean removeEdge(E from, E to) {
        Vertex fromV = null;
        for (Vertex v : vertices) {
            if (from.compareTo(v.data) == 0) {
                fromV = v;
                break;
            }
        }
        if (fromV == null)
            return false;

        // 把from到to之间的联系移除。
        return fromV.removeAdjacentVertex(to);
    }

    public boolean addEdge(E from, E to) {
        Vertex fromV = null, toV = null;
        for (Vertex v : vertices) {
            if (from.compareTo(v.data) == 0) {
                fromV = v;
            } else if (to.compareTo(v.data) == 0) {
                toV = v;
            }
            if (fromV != null && toV != null)
                break;
        }
        if (fromV == null) {
            fromV = new Vertex(from);
            vertices.add(fromV);
        }
        if (toV == null) {
            toV = new Vertex(to);
            vertices.add(toV);
        }
        return fromV.addAdjacentVertex(toV);
    }

    /**
     * this gives a list of verticies in the graph and their adjacencies
     *
     * @return returns a string describing this graph
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex v: vertices) {
            sb.append("Vertex: ");
            sb.append(v.data);
            sb.append("\n");
            sb.append("Adjacent verticies: ");
            for (Vertex v2: v.adjacentVertices) {
                sb.append(v2.data);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
