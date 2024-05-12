public class errorMessage extends Exception {
    public errorMessage(String message){
        super(message);
    }
    public void displayError(String message){
        System.out.println(message);
    }
    
}
