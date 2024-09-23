package me.ninjamandalorian.implodusports.handler;

import me.ninjamandalorian.implodusports.object.Port;
import org.bukkit.Location;

import java.util.*;

public class AStarAlgorithm {

	public static ArrayList<Port> findShortestPath(Collection<Port> ports, Port start, Port goal) {
		Set<Port> visited = new HashSet<Port>();
		Map<Port, Double> gScores = new HashMap<Port, Double>();
		Map<Port, Double> fScores = new HashMap<Port, Double>();
		Map<Port, Port> cameFrom = new HashMap<Port, Port>();

		//PriorityQueue<Port> openSet = new PriorityQueue<Port>(Comparator.comparingDouble(fScores::get));
		PriorityQueue<Port> openSet = new PriorityQueue<Port>(1, new Comparator<Port>() {

			@Override
			/**
			 * Compare
			 */
			public int compare(Port o1, Port o2) {
				return Double.compare(fScores.get(o1), fScores.get(o2));
			}

		});
		openSet.add(start);

		gScores.put(start, 0.0);
		fScores.put(start, heuristicCost(start, goal));

		while (!openSet.isEmpty()) {
			Port current = openSet.poll();

			if (current.equals(goal)) {
				return reconstructPath(cameFrom, goal);
			}

			visited.add(current);

			for (Port neighbor : current.getNearby()) {
				if (neighbor == null) continue;

				if (visited.contains(neighbor)) {
					continue;
				}

				double tentativeGScore = gScores.get(current) + distance(current.getSignLocation(), neighbor.getSignLocation());
				double tentativeFScore = tentativeGScore + heuristicCost(neighbor, goal);

				if (!openSet.contains(neighbor)) {
					fScores.put(neighbor, tentativeFScore);
					openSet.add(neighbor);
				} else if (tentativeGScore >= gScores.get(neighbor)) {
					continue;
				}

				cameFrom.put(neighbor, current);
				gScores.put(neighbor, tentativeGScore);
				fScores.put(neighbor, tentativeFScore);
			}
		}

		return null;
	}

	/**
	 * As the A* algorithm does not keep tract of pathing, this goes backward from the end to
	 * find the route which was taken.
	 *
	 * @param cameFrom - Array of backwards ports
	 * @param current  - Current port
	 * @return List of ports from start to finish.
	 */
	private static ArrayList<Port> reconstructPath(Map<Port, Port> cameFrom, Port current) {
		ArrayList<Port> path = new ArrayList<Port>();
		path.add(current);

		while (cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			path.add(0, current);
		}

		return path;
	}

	/**
	 * Distance calculator (Separate in-case of alteration requirements.)
	 *
	 * @param a - Location a
	 * @param b - Location b
	 * @return Distance
	 */
	private static double distance(Location a, Location b) {
		double xDiff = a.getX() - b.getX();
		double yDiff = a.getY() - b.getY();
		double zDiff = a.getZ() - b.getZ();
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
	}

	/**
	 * Estimated cost, must be less than the true costs to function.
	 *
	 * @param a - Port a (current)
	 * @param b - Port b (next/goal)
	 * @return estimated distance
	 */
	private static double heuristicCost(Port a, Port b) {
		double costMultiplier = Math.sqrt(1.0 / Math.min(a.getSize(), b.getSize()));
		return distance(a.getSignLocation(), b.getSignLocation()) * costMultiplier;
	}

}
