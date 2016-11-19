package com.wfe.astar;

public class Table<T extends Cell> {
	
	public int width;
    public int height;
    private Cell[][] table;
    
    public Table(int width, int height) {
    	this.width = width;
    	this.height = height;
    	this.table = new Cell[width][height];
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    			table[i][j] = new Cell(0, 0, false);
    }
	
    public void add(Cell node) {
    	table[node.x][node.y] = node;
    }
    
    @SuppressWarnings("unchecked")
	public T get(int x, int y) {
    	if(x < width && x >= 0 && y < height && y >= 0)
    		return (T)table[x][y];
    	
    	return (T)(new Cell(0, 0, true));
    }
    
    public void printp() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(this.get(j, i) + " ");
            }
            System.out.println();
        }
    }
}
