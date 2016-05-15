package life;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import kro.frame.KFrame;
import kro.frame.Paintable;

public class Main implements Paintable{
	final int WIDTH = 100, HEIGHT = 80;

	Cell[][] cells = new Cell[WIDTH][HEIGHT];
	Cell[][] futureCells = new Cell[WIDTH][HEIGHT];

	int cellSize = 9;

	KFrame kFrame = new KFrame(WIDTH * cellSize, HEIGHT * cellSize, "Жизнь", this);


	volatile boolean move = false;



	public static void main(String[] args){
		new Main();
	}

	public Main(){
		init();
		initFrame();

		run();
	}

	int button = 0;

	private void initFrame(){

		kFrame.setVisible(true);

		new Thread(new Runnable(){
			public void run(){
				while(true){
					kFrame.paint();
					try{
						Thread.sleep(1000 / 100);
					}catch(Exception ex){
					}
				}
			}
		}).start();


		kFrame.getKPanel().addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){

			}

			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_F4){
					clearField();
				}
				if(e.getKeyCode() == KeyEvent.VK_F5){
					move = false;
					cells = new Cell[WIDTH][HEIGHT];
					futureCells = new Cell[WIDTH][HEIGHT];
					init();
					move = true;
				}
			}


			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_F6){
					move = !move;
				}
			}
		});



		kFrame.getKPanel().addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e){
				button = 0;
			}

			public void mousePressed(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){
					setIsLiveC(e.getX() / cellSize, e.getY() / cellSize, true);

					button = 1;
				}
				if(e.getButton() == MouseEvent.BUTTON3){
					setIsLiveC(e.getX() / cellSize, e.getY() / cellSize, false);

					button = 2;
				}



				/*for ships
				setIsLiveC(e.getX() / cellSize, e.getY() / cellSize, true);
				setIsLiveC(e.getX() / cellSize + 1, e.getY() / cellSize, true);
				setIsLiveC(e.getX() / cellSize, e.getY() / cellSize + 1, true);
				setIsLiveC(e.getX() / cellSize - 1, e.getY() / cellSize + 1, true);
				setIsLiveC(e.getX() / cellSize - 1, e.getY() / cellSize - 1, true);*/
			}

			public void mouseExited(MouseEvent e){

			}

			public void mouseEntered(MouseEvent e){

			}

			public void mouseClicked(MouseEvent e){

			}
		});

		kFrame.getKPanel().addMouseMotionListener(new MouseMotionListener(){
			int x = -1, y = -1;

			public void mouseMoved(MouseEvent e){
				x = -1;
				y = -1;
			}

			public void mouseDragged(MouseEvent e){
				if(button == 1){
					int _x = e.getX() / cellSize;
					int _y = e.getY() / cellSize;
					if(!(_x == x && _y == y)){
						setIsLiveC(_x, _y, true);
					}
					x = _x;
					y = _y;
				}
				if(button == 2){
					int _x = e.getX() / cellSize;
					int _y = e.getY() / cellSize;
					if(!(_x == x && _y == y)){
						setIsLiveC(_x, _y, false);
					}
					x = _x;
					y = _y;
				}
			}
		});
	}

	private void init(){
		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){
				cells[xx][yy] = new Cell();
				futureCells[xx][yy] = new Cell();
			}
		}

		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){
				cells[xx][yy].isLive = Math.random() >= 0.9 ? true : false;
			}
		}
	}

	private void clearField(){
		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){
				cells[xx][yy].isLive = false;
				futureCells[xx][yy].isLive = false;
			}
		}
	}

	private void run(){
		move = true;
		while(true){
			// step
			makeFutureCells();
			
			
			while(!move);
			try{
				Thread.sleep(10);
			}catch(Exception ex){
			}
		}

	}


	private void makeFutureCells(){
		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){

				if(isLiveC(xx, yy)){
					if(getNeighborsCountC(xx, yy) == 2 || (getNeighborsCountC(xx, yy) == 3)){
						setIsLiveF(xx, yy, true);
					}else{
						setIsLiveF(xx, yy, false);
					}
				}else{
					if(getNeighborsCountC(xx, yy) == 3){
						setIsLiveF(xx, yy, true);
					}
				}
			}
		}

		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){
				setIsLiveC(xx, yy, isLiveF(xx, yy));
			}
		}
	}

	private int getNeighborsCountC(int x, int y){
		int n = 0;

		for(int xx = -1; xx < 2; xx++){
			for(int yy = -1; yy < 2; yy++){
				if(!(xx == 0 && yy == 0)){

					if(isLiveC(x + xx, y + yy)){
						n++;
					}

				}
			}
		}

		return n;
	}

	//current
	private boolean isLiveC(int x, int y){
		int _x = x, _y = y;
		if(x == -1){
			_x = WIDTH - 1;
		}
		if(x == WIDTH){
			_x = 0;
		}

		if(y == -1){
			_y = HEIGHT - 1;
		}
		if(y == HEIGHT){
			_y = 0;
		}

		return cells[_x][_y].isLive;
	}

	//future
	private boolean isLiveF(int x, int y){
		int _x = x, _y = y;
		if(x == -1){
			_x = WIDTH - 1;
		}
		if(x == WIDTH){
			_x = 0;
		}

		if(y == -1){
			_y = HEIGHT - 1;
		}
		if(y == HEIGHT){
			_y = 0;
		}

		return futureCells[_x][_y].isLive;
	}

	//current
	private void setIsLiveC(int x, int y, boolean isLive){
		int _x = x, _y = y;
		if(x == -1){
			_x = WIDTH - 1;
		}
		if(x == WIDTH){
			_x = 0;
		}

		if(y == -1){
			_y = HEIGHT - 1;
		}
		if(y == HEIGHT){
			_y = 0;
		}

		cells[_x][_y].isLive = isLive;
	}

	//future
	private void setIsLiveF(int x, int y, boolean isLive){
		int _x = x, _y = y;
		if(x == -1){
			_x = WIDTH - 1;
		}
		if(x == WIDTH){
			_x = 0;
		}

		if(y == -1){
			_y = HEIGHT - 1;
		}
		if(y == HEIGHT){
			_y = 0;
		}

		futureCells[_x][_y].isLive = isLive;
	}

	int c = 16777215;

	public void paint(Graphics2D gr){
		for(int xx = 0; xx < WIDTH; xx++){
			for(int yy = 0; yy < HEIGHT; yy++){
				try{
					gr.setColor(new Color(cells[xx][yy].isLive ? c : 0x000000));
				}catch(Exception ex){
					gr.setColor(new Color(0x000000));
				}

				gr.fillRect(xx * cellSize, yy * cellSize, cellSize, cellSize);
			}
		}

		try{
			Thread.sleep(1000 / 60);
		}catch(Exception ex){
		}

		/*if (c == 16711680) {
			c = 255;
		} else {
			//c *= 256;
		}
		System.out.println(c);*/
	}

}
