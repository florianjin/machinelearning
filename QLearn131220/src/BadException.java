
public class BadException extends Exception {
	String str;
	BadException(String message){
		str = message;
	}
	BadException(){
	}
	void takerisk() throws BadException {
		boolean ab = true;
		if(ab) {
			throw new BadException("abc");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BadException mv = new BadException();
		try{
			mv.takerisk();
		}	catch (BadException x){
			System.out.println(x.str);
		}	finally	{
			System.out.println(mv.getClass());
		}

	}
}
