package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Map {
	private ArrayList<MapVertex> vertices;
	private ArrayList<MapEdge> edges;

	public Map() {
		loadMap();
	}

	public void loadMap() {
		vertices = new ArrayList<MapVertex>();
		edges = new ArrayList<MapEdge>();

		MapVertex v0 = createMapVertex(new Coordinate(0, 0));
		MapVertex v1 = createMapVertex(new Coordinate(10, 300));
		MapVertex v2 = createMapVertex(new Coordinate(150, 420), true);
		MapVertex v3 = createMapVertex(new Coordinate(250, 420), true);
		MapVertex v4 = createMapVertex(new Coordinate(400, 350));
		MapVertex v5 = createMapVertex(new Coordinate(450, 375));
		MapVertex v6 = createMapVertex(new Coordinate(500, 350));
		MapVertex v7 = createMapVertex(new Coordinate(800, 400));
		MapVertex v8 = createMapVertex(new Coordinate(750, 0));

		createMapEdge(v0, v1, false, 1);
		createMapEdge(v1, v2, true, 2);
		createMapEdge(v2, v3, true, 2);
		createMapEdge(v3, v4, true, 2);
		createMapEdge(v4, v5, true, 2);
		createMapEdge(v5, v6, true, 2);
		createMapEdge(v6, v7, true, 2);
		createMapEdge(v7, v8, false, 1);
	}
	
	public void update() {
		//TODO
	}
	
	public void draw(Graphics2D g, Camera camera) {
		for (MapEdge edge : edges) {
			g.setColor(edge.isFloor ? Color.GREEN : Color.RED);
			g.setStroke(new BasicStroke(((edge.strength - 1) * 3) + 1));
			g.drawLine(camera.xAdjust(edge.line.coor1.x), camera.yAdjust((int)edge.line.coor1.y), camera.xAdjust((int)edge.line.coor2.x), camera.yAdjust((int)edge.line.coor2.y));
		}
		g.setStroke(new BasicStroke(1));
		for (MapVertex vertex : vertices) {
			g.setColor(vertex.isGrabbable ? Color.YELLOW : Color.BLUE);
			g.drawOval(camera.xAdjust(vertex.coordinate.x) - 4, camera.yAdjust(vertex.coordinate.y) - 4, 8, 8);
		}
	}

	/*public Collision getClosestCollision(Player player, Velocity vel, MapEdge edgeIgnore, MapVertex vertexIgnore) {
		Collision finalCollision = null;
		for (MapEdge edge : edges) {
			if (edge == edgeIgnore) continue;
			Collision collision = edge.checkCollision(player.hitbox, vel);
			if (collision != null) {
				if (finalCollision == null) finalCollision = collision;
				else if (collision.newVel.distance < finalCollision.newVel.distance) finalCollision = collision;
			}
		} //TODO add vertices
		return finalCollision;
	}*/

	private MapVertex createMapVertex(Coordinate coor) {
		return createMapVertex(coor, false);
	}

	private MapVertex createMapVertex(Coordinate coor, boolean isGrabbable) {
		MapVertex mapVertex = new MapVertex(this, coor, isGrabbable);
		vertices.add(mapVertex);
		return mapVertex;
	}

	private MapEdge createMapEdge(MapVertex leftVertex, MapVertex rightVertex, boolean isFloor, int strength) {
		MapEdge mapEdge = new MapEdge(this, leftVertex, rightVertex, isFloor, strength);
		leftVertex.rightEdge = mapEdge;
		rightVertex.leftEdge = mapEdge;
		edges.add(mapEdge);
		return mapEdge;
	}
}
