package life;

import java.awt.Color;
import java.awt.Graphics2D;

import kro.frame.KFrame;
import kro.frame.Paintable;

public class Main implements Paintable {
	final int WIDTH = 140, HEIGHT = 140;

	Cell[][] cells = new Cell[WIDTH][HEIGHT];
	Cell[][] futureCells = new Cell[WIDTH][HEIGHT];

	int cellSize = 5;

	KFrame kFrame = new KFrame(WIDTH * cellSize, HEIGHT * cellSize, "ַûחםü", this);

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		init();
		initFrame();

		run();
	}

	private void initFrame() {
		kFrame.setVisible(true);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					kFrame.paint();
					try {
						Thread.sleep(1000 / 100);
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}

	private void init() {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				cells[xx][yy] = new Cell();
				futureCells[xx][yy] = new Cell();
			}
		}

		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				cells[xx][yy].isLive = Math.random() >= 0.5 ? true : false;
			}
		}
	}

	private void run() {

		while (true) {
			// step
			makeFutureCells();

			cells = futureCells;
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}

	}

	private void makeFutureCells() {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {

				if (isLive(xx, yy)) {
					if (!(getNeighborsCount(xx, yy) == 2 || (getNeighborsCount(xx, yy) == 3))) {
						setIsLive(xx, yy, false);
					}
				} else {
					if (getNeighborsCount(xx, yy) == 3) {
						setIsLive(xx, yy, true);
					}
				}

			}
		}
	}

	private int getNeighborsCount(int x, int y) {
		int n = 0;

		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy < 2; yy++) {
				if (!(xx == 0 && yy == 0)) {

					if (isLive(x + xx, y + yy)) {
						n++;
					}

				}
			}
		}

		return n;
	}

	private boolean isLive(int x, int y) {
		int _x = x, _y = y;
		if (x == -1) {
			_x = WIDTH - 1;
		}
		if (x == WIDTH) {
			_x = 0;
		}

		if (y == -1) {
			_y = HEIGHT - 1;
		}
		if (y == HEIGHT) {
			_y = 0;
		}

		return cells[_x][_y].isLive;
	}

	private void setIsLive(int x, int y, boolean isLive) {
		futureCells[x][y].isLive = isLive;
	}

	int c = 16777215;

	public void paint(Graphics2D gr) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				try {
					gr.setColor(new Color(cells[xx][yy].isLive ? c : 0x000000));
				} catch (Exception ex) {
					gr.setColor(new Color(0x000000));
				}

				gr.fillRect(xx * cellSize, yy * cellSize, cellSize, cellSize);
			}
		}

		try {
			Thread.sleep(1000 / 60);
		} catch (Exception ex) {
		}

		if (c == 16711680) {
			c = 255;
		} else {
			//c *= 256;
		}
		System.out.println(c);
	}

}
