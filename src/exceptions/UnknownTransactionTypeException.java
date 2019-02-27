package exceptions;

public class UnknownTransactionTypeException extends Exception {

	String transactionType;
	
	public UnknownTransactionTypeException(String s){
		transactionType = s;
	}
	
	@Override
	public void printStackTrace(){
		System.out.println("Unknown Transaction Type: " + this.transactionType);
		super.printStackTrace();
	}
}
