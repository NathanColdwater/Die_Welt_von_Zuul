package character;

import java.util.LinkedList;

import item.Item;
import javafx.scene.image.Image;
import ort.Raum;

public class Enemy extends Character {
	public Enemy(String name, int maxTraglast, Raum raum, int x, int y, Image image,
			LinkedList<Item> gegenstaende) {
		super(name, maxTraglast, raum, x, y, image, gegenstaende);
	}

	@Override
	public void interagieren(Player spieler) {

	}
}