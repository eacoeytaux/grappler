package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Constants;

public class Map {
	boolean showBumpers = false;

	ArrayList<AbsMapElement> elements;
	ArrayList<MapVertex> vertices;
	ArrayList<MapEdge> edges;

	public Map() {
		loadMap();
	}

	public void loadMap() {
		elements = new ArrayList<AbsMapElement>();
		vertices = new ArrayList<MapVertex>();
		edges = new ArrayList<MapEdge>();
		
		MapVertex v0 = createMapVertex(new Coordinate(0, 0));
		MapVertex v1 = createMapVertex(new Coordinate(10, 300));
		MapVertex v2 = createMapVertex(new Coordinate(150, 310));
		MapVertex v3 = createMapVertex(new Coordinate(250, 420));
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
			g2d.setColor(Color.WHITE);
			g2d.drawLine(camera.xAdjust(edge.line.coor1.x), camera.yAdjust((int)edge.line.coor1.y), camera.xAdjust((int)edge.line.coor2.x), camera.yAdjust((int)edge.line.coor2.y));
			if (showBumpers) {
				g2d.setColor(Color.GREEN);
				g2d.drawLine(camera.xAdjust(edge.bumper.coor1.x), camera.yAdjust((int)edge.bumper.coor1.y), camera.xAdjust((int)edge.bumper.coor2.x), camera.yAdjust((int)edge.bumper.coor2.y));
				//g2d.drawLine(camera.xAdjust(edge.backBumperLine.coor1.x), camera.yAdjust((int)edge.backBumperLine.coor1.y), camera.xAdjust((int)edge.backBumperLine.coor2.x), camera.yAdjust((int)edge.backBumperLine.coor2.y));
				g2d.setColor(Color.RED);
				g2d.drawLine(camera.xAdjust(edge.leftCatchLine.coor1.x), camera.yAdjust((int)edge.leftCatchLine.coor1.y), camera.xAdjust((int)edge.leftCatchLine.coor2.x), camera.yAdjust((int)edge.leftCatchLine.coor2.y));
				g2d.drawLine(camera.xAdjust(edge.rightCatchLine.coor1.x), camera.yAdjust((int)edge.rightCatchLine.coor1.y), camera.xAdjust((int)edge.rightCatchLine.coor2.x), camera.yAdjust((int)edge.rightCatchLine.coor2.y));
			}
		}
		for (MapVertex vertex : vertices) {
			//g2d.setColor(Color.YELLOW);
			//g2d.drawOval(camera.xAdjust(vertex.coordinate.x) - 4, camera.yAdjust(vertex.coordinate.y) - 4, 8, 8);
			if (showBumpers) {
				g2d.setColor(Color.GREEN);
				g2d.drawOval(camera.xAdjust(vertex.coordinate.x) - Player.trueRadius, camera.yAdjust(vertex.coordinate.y) - Player.trueRadius, Player.trueRadius * 2, Player.trueRadius * 2);
			}
		}
	}
	
	public AbsMapElement getCollision(Vector vel, AbsMapElement currentElement) {
		AbsMapElement closestElement = null;
		double distanceToElement = 0;
		
		
		Line vectorLine = vel.toLine();
		
		for( AbsMapElement element: elements){
			if (element != currentElement){
				Coordinate coor = element.findCollision(vectorLine);
				if(coor != null){
					double dist =  Constants.distance(coor, vel.origin);
					if(closestElement == null){
						closestElement = element;
						distanceToElement = dist;
					}else{
						if( dist < distanceToElement){
							closestElement = element;
							distanceToElement = dist;
						}
					}
				}
			}
		}
		
		return closestElement;
	}

	private MapVertex createMapVertex(Coordinate coor) {
		MapVertex mapVertex = new MapVertex(this, coor);
		vertices.add(mapVertex);
		elements.add(mapVertex);
		return mapVertex;
	}

	private MapEdge createMapEdge(MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		MapEdge mapEdge = new MapEdge(this, leftVertex, rightVertex, hookable);
		leftVertex.rightEdge = mapEdge;
		rightVertex.leftEdge = mapEdge;
		edges.add(mapEdge);
		elements.add(mapEdge);
		return mapEdge;
	}
}
