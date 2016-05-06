package life;

import java.awt.Color;
import java.awt.Graphics2D;

import kro.frame.KFrame;
import kro.frame.Paintable;


public class Main implements Paintable {


	Cell[][] cells = new Cell[70][60];
	int cellSize = 10;

	KFrame kFrame = new KFrame(cells.length * cellSize, cells[0].length * cellSize, "ַûחםü", this);
	int n = 0;

	public static void main(String[] args) {
		new Main();
	}


	public Main() {
		init();
		initFrame();
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
		for (int xx = 0; xx < cells.length; xx++) {
			for (int yy = 0; yy < cells[0].length; yy++) {
				cells[xx][yy] = new Cell();
			}
		}
	}

	public void paint(Graphics2D gr) {
		for (int xx = 0; xx < cells.length; xx++) {
			for (int yy = 0; yy < cells[0].length; yy++) {
				try {
					gr.setColor(new Color(cells[xx][yy].isLive ? 0x00ff00 : 0x000000));
				} catch (Exception ex) {
					gr.setColor(new Color(0x000000));
				}

				gr.fillRect(xx * cellSize, yy * cellSize, cellSize, cellSize);
			}
		}
	}

}
