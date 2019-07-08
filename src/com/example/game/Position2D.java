package com.example.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Position2D implements Position{
	
	private final int x;
	private final int y;
	private final List <String> neighborIdentifiers;
	private final String identifier;
	
	public Position2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		int [][] cached_directions = new int[][]{
		      {-1, 1},  {0, 1},  {1, 1},  // above
		      {-1, 0},           {1, 0},  // sides
		      {-1, -1}, {0, -1}, {1, -1}, // below
		    };
		this.neighborIdentifiers = Arrays.stream(cached_directions).map(dir -> (x + dir[0]) + "-" + (y + dir[1])).collect(Collectors.toList());    
		this.identifier = x +"-"+y;    
	}

	@Override
	public List<String> getNeigbhourPositionsIdentifiers(){
		
		return neighborIdentifiers;
	}

	@Override
	public String getIdentifier() {
		
		return identifier;
	}
	
	public static Position getPositionForIdentifier(String identifier) {
		String[] arr = identifier.split("-");
		return new Position2D(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
	}
	

}
