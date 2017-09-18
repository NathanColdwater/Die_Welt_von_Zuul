package character;

import java.util.LinkedList;

import gegenstand.Gegenstand;
import javafx.scene.image.Image;
import ort.Raum;

public class NPC extends Character {
	public NPC(String name, int maxTraglast, Raum raum, int x, int y, Image image,
			LinkedList<Gegenstand> gegenstaende) {
		super(name, maxTraglast, raum, x, y, image, gegenstaende);
	}

	protected String text;
	

	@Override
	public void interagieren(Spieler spieler) {
		System.out.println(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
