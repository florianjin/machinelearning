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
		return "总扫描次数:"+nowScan+"\n"+
				"完成V值的路径号："+finnishV+"\n"+
				"最后一次大更新的路径号："+myHis.get(myHis.size()-1)+"\n"+
				"Q值变化历史："+QupHis+"\n"+
				"maxQ值变化历史"+DupHis+"\n"+
				"V距离变化历史"+VDisHis;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
