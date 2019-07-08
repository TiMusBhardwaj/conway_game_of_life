package com.example.game;

import java.util.List;

class DynamicCell {

  private Position position;
  public boolean alive;
  public Integer next_state; 
  
  public DynamicCell(Position position, boolean args) {
    this.position = position;    
	this.alive = args;
    this.next_state = null;
    
  }
  
  public static DynamicCell getDeadCell(Position position) {
	  return new DynamicCell(position, false);
  }
  
  public String getPositionId() {
	  return position.getIdentifier();
  }

  public List<String> getneigbourPositions() {
	  return position.getNeigbhourPositionsIdentifiers();
  }
  
  public char to_char() {
    return this.alive ? 'o' : ' ';
  }

}