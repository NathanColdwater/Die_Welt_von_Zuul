package Verhalten;

import java.util.Random;

public class SpielerAngriffVerhalten implements AngriffsVerhalten {
	private static SpielerAngriffVerhalten instance = new SpielerAngriffVerhalten(); 
	
	private SpielerAngriffVerhalten(){
	}
	
	public boolean leichtVerletzen(){
		Random randomObj = new Random();
		int randomNumber = randomObj.nextInt(101);
		
		if(randomNumber <= 30){
			return true;
		}
		return false;
	}
	
	public boolean schwerVerletzen(){
		Random randomObj = new Random();
		int randomNumber = randomObj.nextInt(101);
		
		if(randomNumber <= 50){
			return true;
		}
		return false;
	}
	
	public static SpielerAngriffVerhalten getInstance(){
		return instance;
	}
}
