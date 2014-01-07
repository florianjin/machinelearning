import java.util.ArrayList;


public class LearningRecord {
	int nowScan;
	int inRouteQup;
	int inRouteVup;
	int inRouteRevertQup;
	int inRouteRevertVup;
	int oneRouteQup;
	int oneRouteVup;
	int totalQup;
	int totalVup;
	int totalTries;
	int finnishV;
	int finnishQ;
	ArrayList<Integer> myHis=new ArrayList<Integer>();
	ArrayList<Integer> QupHis=new ArrayList<Integer>();
	ArrayList<Integer> DupHis=new ArrayList<Integer>();
	ArrayList<Double> VDisHis=new ArrayList<Double>();
	public void Big(int num){
		myHis.add(num);
	}
	public void newTry(){
		totalTries++;
	}
	public void newScan(){
		nowScan++;
	}
	public void updateQ(){
		inRouteQup++;
	}
	public void updateV(){
		inRouteVup++;
	}
	public void updateRevertQ(){
		inRouteRevertQup++;
	}
	public void updateRevertV(){
		inRouteRevertVup++;
	}
	public void finnishV(int num){
		if (finnishV==0) finnishV=num;
	}
	public void oneRouteEnd(double newVDis){
		inRouteQup+=inRouteRevertQup;
		inRouteVup+=inRouteRevertVup;
		totalQup+=inRouteQup;
		totalVup+=inRouteVup;
		QupHis.add(totalQup);
		DupHis.add(totalVup);
		VDisHis.add(newVDis);
		inRouteQup=0;
		inRouteVup=0;
		inRouteRevertQup=0;
		inRouteRevertVup=0;
	}
	
	public String toString(){
		return "��ɨ�����:"+nowScan+"\n"+
				"���Vֵ��·���ţ�"+finnishV+"\n"+
				"���һ�δ���µ�·���ţ�"+myHis.get(myHis.size()-1)+"\n"+
				"Qֵ�仯��ʷ��"+QupHis+"\n"+
				"maxQֵ�仯��ʷ"+DupHis+"\n"+
				"V����仯��ʷ"+VDisHis;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
