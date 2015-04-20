package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Constants;

public class Map {
	boolean showBumpers = true;

	ArrayList<MapVertex> vertices;
	ArrayList<MapEdge> edges;

	public Map() {
		loadMap();
	}

	public void loadMap() {
		vertices = new ArrayList<MapVertex>();
		edges = new ArrayList<MapEdge>();
		
		MapVertex v0 = createMapVertex(new Coordinate(0, 0));
		MapVertex v1 = createMapVertex(new Coordinate(10, 300));
		MapVertex v2 = createMapVertex(new Coordinate(150, 310), true);
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
		for (MapEdge edge : edges) {
			g2d.setColor(Color.GREEN);
			g2d.drawLine(camera.xAdjust(edge.line.coor1.x), camera.yAdjust((int)edge.line.coor1.y), camera.xAdjust((int)edge.line.coor2.x), camera.yAdjust((int)edge.line.coor2.y));
			if (showBumpers) {
				g2d.setColor(Color.PINK);
				g2d.drawLine(camera.xAdjust(edge.frontBumperLine.coor1.x), camera.yAdjust((int)edge.frontBumperLine.coor1.y), camera.xAdjust((int)edge.frontBumperLine.coor2.x), camera.yAdjust((int)edge.frontBumperLine.coor2.y));
				g2d.drawLine(camera.xAdjust(edge.backBumperLine.coor1.x), camera.yAdjust((int)edge.backBumperLine.coor1.y), camera.xAdjust((int)edge.backBumperLine.coor2.x), camera.yAdjust((int)edge.backBumperLine.coor2.y));

				g2d.setColor(Color.MAGENTA);
				g2d.drawLine(camera.xAdjust(edge.leftCatchLine.coor1.x), camera.yAdjust((int)edge.leftCatchLine.coor1.y), camera.xAdjust((int)edge.leftCatchLine.coor2.x), camera.yAdjust((int)edge.leftCatchLine.coor2.y));
				g2d.drawLine(camera.xAdjust(edge.rightCatchLine.coor1.x), camera.yAdjust((int)edge.rightCatchLine.coor1.y), camera.xAdjust((int)edge.rightCatchLine.coor2.x), camera.yAdjust((int)edge.rightCatchLine.coor2.y));
			}
		}
		for (MapVertex vertex : vertices) {
			g2d.setColor(Color.YELLOW);
			g2d.drawOval(camera.xAdjust(vertex.coordinate.x) - 4, camera.yAdjust(vertex.coordinate.y) - 4, 8, 8);
			if (showBumpers) {
				g2d.setColor(Color.MAGENTA);
				g2d.drawOval(camera.xAdjust(vertex.coordinate.x) - Player.trueRadius, camera.yAdjust(vertex.coordinate.y) - Player.trueRadius, Player.trueRadius * 2, Player.trueRadius * 2);
			}
		}
	}
	
	public void updatePlayerPos(Player p, Vector nextVel) {
		boolean edgeNull = (p.currentEdge == null);
		boolean vertexNull = (p.currentVertex == null);
		
		/*if (!edgeNull) nextVel = p.currentEdge.absorbVector(nextVel);
		else if (!vertexNull) nextVel = p.currentVertex.absorbVector(nextVector)*/
		
		for (MapEdge edge : edges) {
			if (edge == p.currentEdge) continue;
			//check for other collisions
		}
		for (MapVertex vertex : vertices) {
			if (vertex == p.currentVertex) continue;
		}
		
		if (p.currentEdge != null) {
			//check edge catches
		} else if (p.currentVertex != null) {
			//dunno yet...
		}
		
		
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
