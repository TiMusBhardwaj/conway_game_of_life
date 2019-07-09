package com.example.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//TODO: This code needs a lot of refactoring : ::Refactor CODe

public class DynamicWorld {

	
	public int tick;
	private int width;
	private int height;
	//TODO: Refactor code to use Position as Key instead of position identifier
	private Map<String, DynamicCell> aliveCells;
	

	public DynamicWorld(int width, int height) {
		this.width = width;
		this.height = height;
		this.tick = 0;
		this.aliveCells = new ConcurrentHashMap<String, DynamicCell>();

		populate_cells();

	}

	/**
	 * 3 Step Process
	 * ----------------------------
	 * 1. Find Next State of All alive cells
	 * 2. Find Neighbor dead cell that will be alive in next cycle.
	 * 		Add these dead cells , as live cell to aliveCellMap with next state Null
	 * 3. After Next state to all cells in aliveCellMap.
	 *  
	 * 
	 */
	public void _tick() {
		// Alive Neigbhor count of dead cells
		Map<String, Integer> aliveCountOfDeadCell = new HashMap<String, Integer>();
		// First determine the action for all cells
		for (DynamicCell cell : aliveCells.values()) {
			long alive_neighbours = aliveNeighborsAround(cell);
			updateAliveNeigborCount(aliveCountOfDeadCell, cell);
			
			// TODO : Move this logic to Cell
			if (!cell.alive && alive_neighbours == 3) {
				cell.next_state = 1;
			} else if (alive_neighbours < 2 || alive_neighbours > 3) {
				cell.next_state = 0;
			}
		}
		
		//if neighbor count of dead cells is 3 make them alive and add to aliveCells
		aliveCountOfDeadCell.entrySet().stream().filter(entry -> entry.getValue() == 3)
			.forEach(entry -> aliveCells.put(entry.getKey(), 
					new DynamicCell(Position2D.getPositionForIdentifier(entry.getKey()), true)));

		// Then execute the determined action for all cells
		Iterator<Entry<String, DynamicCell>> it = aliveCells.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, DynamicCell> entry =  it.next();
			DynamicCell cell = entry.getValue();
			if (cell.next_state != null && cell.next_state == 1) {
				cell.alive = true;
			} else if (cell.next_state != null && cell.next_state == 0) {
				//Remove it from Alive Band..
				it.remove();
			}
			
		}
		
		tick++;
	}

	/**
	 * Populate world with alive cells and keep a track
	 */
	private void populate_cells() {
		for (int y = 0; y <= height; y++) {
			for (int x = 0; x <= width; x++) {
				if (Math.random() <= 0.2)
					add_cell(x, y, true);
			}
		}
	}

	private DynamicCell add_cell(int x, int y, boolean args) {

		DynamicCell cell = new DynamicCell(new Position2D(x, y), args);
		aliveCells.put(cell.getPositionId(), cell);

		return cell;
	}
	
	/**
	 * This Method updates the alive neighbor count of neighboring dead cells
	 * 
	 * @param deadNeigbhbourCells
	 * @param cell
	 */
	private void updateAliveNeigborCount(final Map<String, Integer> aliveCountOfDeadCell
			, DynamicCell cell) {
		deadNeighboursAround(cell).stream()
		.forEach(positionId -> aliveCountOfDeadCell.merge(positionId, 1, Integer::sum));
	}

	
	//TODO: Move Below Methods To Dynamic Cell If Possible.
	//Think about it
	/**
	 * @param cell
	 * @return List of alive + dead(newly created cells)
	 */
	private List<String> deadNeighboursAround(DynamicCell cell) {

		return cell.getneigbourPositions()
				.stream().filter(posStr -> !aliveCells.containsKey(posStr))
				.collect(Collectors.toList());
	}

	/**
	 * @param cell
	 * @return count of live neighbor
	 */
	private long aliveNeighborsAround(DynamicCell cell) {

		return cell.getneigbourPositions()
				.stream().filter(posStr -> aliveCells.containsKey(posStr))
				.count();
	}

}
