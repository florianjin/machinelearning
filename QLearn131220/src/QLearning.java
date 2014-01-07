import java.util.ArrayList;


public class QLearning {
	public States st;
	double gama = 0.9;//默认系数
	static final double eps = 1e-5;//更新控制精度
	int totalScan;
	int success;
	int Qup,Dup;//Q值更新次数和方向更新次数初始为0。
	int TotalQup,TotalDup;
	ArrayList<Integer> QupHis=new ArrayList<Integer>();
	ArrayList<Integer> DupHis=new ArrayList<Integer>();
	ArrayList<Double> VDisHis=new ArrayList<Double>();
	public QLearning(){
		st = new States();
	}
	
	boolean oneRouteTrain(){
		Qup=0;Dup=0;//重新计数
		int l = st.route.size();
		if(l<2) {
			System.out.println("training：程序不正确！路径长度为0或1");
			return false;
		}
		int i=l;
		while(i>1) {
			totalScan++;
			int start = st.route.get(i-2);
			int end = st.route.get(i-1);
			double r = st.trans[start][end].r;//即时回报
			double qAscend = gama * st.maxQ[end];//累积回报
			double q = r+qAscend;
			if(q<st.trans[start][end].q) {	System.out.println("注意：本次Q值递减");}
			if(Math.abs(q-st.trans[start][end].q)<eps){
				//System.out.println("本次状态转移Q值更新很少");
			}else{Qup++;}
			//System.out.println("本次状态转移Q值更新：trans["+	start+"]["+end+"]从"+st.trans[start][end].q+	"更新到"+q);
			st.trans[start][end].q = q;
			if(st.maxQ[start]>q-eps ){
				//System.out.println("本次无方向性更新，maxQ["+	start+"]的值已经是"+st.maxQ[start]);
			}
			else {
				if(Math.abs(q-st.maxQ[start])<eps){
					//System.out.println("本次方向性更新很少，maxQ["+start+"]的值已经是"+st.maxQ[start]);
				} else {Dup++;}
				//System.out.println("本次方向性更新:maxQ["+start+"]的值从"+st.maxQ[start]+"到"	+ q);
				st.maxQ[start]=q;
				st.maxNextS[start]=end;
			}
			i--;
		}
		return true;
	}
	void runNormal(){
		for(int i=0;i<1000;i++){
			if(st.getRoute()) {
				success++;
				oneRouteTrain();
				TotalQup+=Qup;
				TotalDup+=Dup;
				QupHis.add(TotalQup);
				DupHis.add(TotalDup);
				VDisHis.add(st.calcVDis());
				if(Qup>0 || Dup>0){
					System.out.println("尝试次数："+i+"次数"+success+"route length:"+st.route.size()+" Qup:"+Qup+" Dup:"+Dup);
					//System.out.println(ql.route);
				}
			}
		}
		System.out.println(success);
		System.out.println(totalScan);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QLearning ql= new QLearning();
		//System.out.println(ql.st.usedL);
		ql.runNormal();
		System.out.println(ql.QupHis);
		System.out.println(ql.DupHis);
		System.out.println(ql.VDisHis);
	}

}
