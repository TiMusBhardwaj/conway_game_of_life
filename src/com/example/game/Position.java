package com.example.game;

import java.util.List;

public interface Position {
	
	
	//TODO: Can we do away with poistion identifer and use Position itself as key.
	public List<String> getNeigbhourPositionsIdentifiers();
	
	public String getIdentifier();
	
	
	
	

}
