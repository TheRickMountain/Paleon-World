package com.wfe.astar;

public class Cell {

	public int x = -1;
	public int y = -1;
	public Cell parent = this;
	public boolean blocked = false;
	public boolean start = false;
	public boolean finish = false;
	public boolean road = false;
	public int F = 0;
	public int G = 0;
	public int H = 0;
	
	public Cell(int x, int y, boolean blocked) {
		this.x = x;
		this.y = y;
		this.blocked = blocked;
	}
	
	public int mandist(Cell finish) {
		return 10 * (Math.abs(this.x - finish.x) + Math.abs(this.y - finish.y));
	}
	
	public int price(Cell finish) {
		if(this.x == finish.x || this.y == finish.y) {
			return 10;
		} else {
			return 14;
		}
	}
	
	public void setAsStart() {
		this.start = true;
	}
	
	public void setAsFinish() {
		this.finish = true;
	}
	
	public boolean equals(Cell second) {
		return (this.x == second.x) && (this.y == second.y);
	}
	
	public String toString() {
		if(this.road) 
			return "*";
		
		if(this.start)
			return "+";
		
		if(this.finish)
			return "=";
		
		if(this.blocked)
			return "#";
					
		return "$";
	}
	
}
