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
		MapVertex v2 = createMapVertex(new Coordinate(150, 440), true);
		MapVertex v3 = createMapVertex(new Coordinate(250, 420), true);
		MapVertex v4 = createMapVertex(new Coordinate(400, 350));
		MapVertex v5 = createMapVertex(new Coordinate(450, 375));
		MapVertex v6 = createMapVertex(new Coordinate(500, 350));
		MapVertex v7 = createMapVertex(new Coordinate(800, 400));
		MapVertex v8 = createMapVertex(new Coordinate(750, 0));

		createMapEdge(v0, v1, false);
		createMapEdge(v1, v2, true);
		createMapEdge(v2, v3, true);
		createMapEdge(v3, v4, true);
		createMapEdge(v4, v5, true);
		createMapEdge(v5, v6, true);
		createMapEdge(v6, v7, true);
		createMapEdge(v7, v8, false);
	}
	
	public void update(long counter) {
		//TODO
	}
	
	public void draw(Graphics2D g2d, Camera camera) {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.GREEN);
		for (MapEdge edge : edges) {
			g2d.drawLine(camera.xAdjust(edge.line.coor1.x), camera.yAdjust((int)edge.line.coor1.y), camera.xAdjust((int)edge.line.coor2.x), camera.yAdjust((int)edge.line.coor2.y));
		}
		g2d.setColor(Color.YELLOW);
		for (MapVertex vertex : vertices) {
			g2d.drawOval(camera.xAdjust(vertex.coordinate.x) - 4, camera.yAdjust(vertex.coordinate.y) - 4, 8, 8);
		}
	}
	
	public Collision getCollision(Line line, MapEdge edgeToIgnore, MapVertex vertexToIgnore) {
		
		Collision collisionFinal = null;
		
		for (final MapEdge edge : edges) {
			if (edge == edgeToIgnore) continue;
			
			final Coordinate collisionCoor = edge.line.intersection(line);
			if (collisionCoor != null) {
				collisionFinal = new Collision() {

					@Override
					public void updatePlayer(Player p) {
						p.currentEdge = edge;
						p.center = collisionCoor;
						p.vel.origin = p.center;
						p.vel.xDelta = 0;
						p.vel.yDelta = 0;
					}
				};
				break;
			}
		}
		
		return collisionFinal;//new Vector(vel.origin, vel.origin.x - collisionCoorFinal.x, vel.origin.y - collisionCoorFinal.y);
	}

	private MapVertex createMapVertex(Coordinate coor) {
		return createMapVertex(coor, false);
	}

	private MapVertex createMapVertex(Coordinate coor, boolean isGrabbable) {
		MapVertex mapVertex = new MapVertex(this, coor, isGrabbable);
		vertices.add(mapVertex);
		return mapVertex;
	}

	private MapEdge createMapEdge(MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		MapEdge mapEdge = new MapEdge(this, leftVertex, rightVertex, hookable);
		leftVertex.rightEdge = mapEdge;
		rightVertex.leftEdge = mapEdge;
		edges.add(mapEdge);
		return mapEdge;
	}
}
