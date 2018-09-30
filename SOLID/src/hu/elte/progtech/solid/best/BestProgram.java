package hu.elte.progtech.solid.best;

public class BestProgram {

    private static void doTheBestThings(){
        System.out.println("You are the best");
        
        System.out.println(Math.PI);
        
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum+=i;
        }
        
        System.out.println(sum + " is the highest number i could count");
    }
    
    public static void main(String[] args) {
        doTheBestThings();
    }
}
