
public class Transition {
	public String Action;
	public double q;//Qֵ
	public double r;//��ʱ�ر�
	Transition(){
		Action="";
	}
	Transition(double r){
		Action="";
		this.r=r;
	}
	Transition(String Action, double r){
		this.Action=Action;
		this.r=r;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transition t = new Transition();
		System.out.println(t.Action+"|"+t.q+"|"+t.r);
	}

}
