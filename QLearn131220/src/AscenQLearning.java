import java.util.ArrayList;


public class AscenQLearning {
	public States st;
	double gama = 0.9;//默认系数
	static final double eps = 1e-5;//更新控制精度
	ArrayList<ArrayList<Integer>> routePool;
	ArrayList<ArrayList<Double>> routeRPool=new ArrayList<ArrayList<Double>>();
	ArrayList<Double> rPool=new ArrayList<Double>();
	LearningRecord rec;
	int success=500;
	int totalTries=0;
	
	public AscenQLearning(){
		st = new States();
		routePool = new ArrayList<ArrayList<Integer>>();
	}
	public void fillRoutePool(){
		int i=0;
		while(i<success){
			totalTries++;
			if (st.getRoute()){
				routePool.add((ArrayList<Integer>)st.route.clone());
				routeRPool.add((ArrayList<Double>)st.routeRs.clone());
				rPool.add(st.routeR);
				i++;
			}
		}
		System.out.println("为了路径库而做的总探索次数："+totalTries);
	}
	public void printRoutePool(){
		for(ArrayList<Integer> route:routePool){
			System.out.println(route);
		}
		for(Double r:rPool){
			System.out.println(r);
		}
		for(ArrayList<Double> route:routeRPool){
			System.out.println(route);
		}
	}
	public void RevertScan(int end){
		int start;double newQ;
		for(start=0;start<st.usedL;start++){
			if(st.trans[start][end]==null) continue;
			rec.newScan();//totalScan++;
			newQ=gama*st.maxQ[end]+st.trans[start][end].r;
			if(newQ<st.trans[start][end].q) System.out.println("报警：原Q值较大");
			rec.updateRevertQ();//RevertQup++;
			st.trans[start][end].q=newQ;
			if(st.maxQ[start]>newQ-eps ){
				//System.out.println("本反向扫描点无方向性更新，maxQ["+	start+"]的值已经是"+st.maxQ[start]);
			}
			else {
				if(Math.abs(newQ-st.maxQ[start])<eps){
					//System.out.println("本反向扫描点方向性更新很少，maxQ["+start+"]的值已经是"+st.maxQ[start]);
				} else {rec.updateRevertV();}//RevertDup++;}
				//System.out.println("本反向扫描点方向性更新:maxQ["+start+"]的值从"+st.maxQ[start]+"到"	+ q);
				st.maxQ[start]=newQ;
				st.maxNextS[start]=end;
				RevertScan(start);
			}
		}
	}
	LearningRecord runNormal(TrainMethod method){
		rec = new LearningRecord();
		for(int i=0;i<routePool.size();i++){
				method.oneRouteTrain(i);
				if(rec.inRouteQup>0 ||rec.inRouteVup>0
						||rec.inRouteRevertQup>0 || rec.inRouteRevertVup>0){
					//System.out.println("次数："+i+"route length:"+routePool.get(i).size()+" Qup:"+rec.inRouteQup+" Vup:"+rec.inRouteVup);
					//System.out.println(ql.route);
					rec.Big(i);
				}
				rec.oneRouteEnd(st.calcVDis());
				if (st.calcVDis()<1) rec.finnishV(i);
		}
		System.out.println(rec);
		return rec;
	}
	void clearExceptRoute(){
		st.empty();
	}
	void clearForClassic(){
		st.clearQ();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AscenQLearning ql = new AscenQLearning();
		ql.fillRoutePool();
		ql.clearForClassic();
		ql.runNormal(ql.new TrainMethod1());
		ql.clearExceptRoute();
		ql.runNormal(ql.new TrainMethod1());
		ql.clearExceptRoute();
		ql.runNormal(ql.new TrainMethod2());
		//ql.printRoutePool();
	}
	interface TrainMethod{
		public void oneRouteTrain(int index);
	}
	class TrainMethod1 implements TrainMethod{
		public void oneRouteTrain(int index){
			int l = routePool.get(index).size();
			int i=l;
			while(i>1) {
				rec.newScan();//totalScan++;
				int start = routePool.get(index).get(i-2);
				int end = routePool.get(index).get(i-1);
				double r = routeRPool.get(index).get(i-2);//即时回报
				if(st.trans[start][end]==null) {
					st.newTrans(start, end, r);
				}
				double qAscend = gama * st.maxQ[end];//累积回报
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("注意：本次Q值递减");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("本次状态转移Q值更新很少");
				}else{rec.updateQ();}
				//System.out.println("本次状态转移Q值更新：trans["+	start+"]["+end+"]从"+st.trans[start][end].q+	"更新到"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("本次无方向性更新，maxQ["+	start+"]的值已经是"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("本次方向性更新很少，maxQ["+start+"]的值已经是"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("本次方向性更新:maxQ["+start+"]的值从"+st.maxQ[start]+"到"	+ q);
					st.maxQ[start]=q;
					st.maxNextS[start]=end;
				}
				i--;
			}
		}
	}
	class TrainMethod2 implements TrainMethod{
		public void oneRouteTrain(int index){
			int l = routePool.get(index).size();
			int i=l;
			while(i>1) {
				rec.newScan();//totalScan++;
				int start = routePool.get(index).get(i-2);
				int end = routePool.get(index).get(i-1);
				double r = routeRPool.get(index).get(i-2);//即时回报
				if(st.trans[start][end]==null) {
					st.newTrans(start, end, r);
				}
				double qAscend = gama * st.maxQ[end];//累积回报
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("注意：本次Q值递减");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("本次状态转移Q值更新很少");
				}else{rec.updateQ();}
				//System.out.println("本次状态转移Q值更新：trans["+	start+"]["+end+"]从"+st.trans[start][end].q+	"更新到"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("本次无方向性更新，maxQ["+	start+"]的值已经是"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("本次方向性更新很少，maxQ["+start+"]的值已经是"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("本次方向性更新:maxQ["+start+"]的值从"+st.maxQ[start]+"到"	+ q);
					st.maxQ[start]=q;
					st.maxNextS[start]=end;
					RevertScan(start);
				}
				i--;
			}
		}
	}
	class TrainMethod3 implements TrainMethod{
		public void oneRouteTrain(int index){
			int l = routePool.get(index).size();
			int i=l;
			while(i>1) {
				int start = routePool.get(index).get(i-2);
				int end = routePool.get(index).get(i-1);
				double r = routeRPool.get(index).get(i-2);//即时回报
				if(st.trans[start][end]==null) {
					st.newTrans(start, end, r);
				}
				i--;
			}
			i=l;
			while(i>1) {
				rec.newScan();
				int start = routePool.get(index).get(i-2);
				int end = routePool.get(index).get(i-1);
				double r = routeRPool.get(index).get(i-2);//即时回报
				double qAscend = gama * st.maxQ[end];//累积回报
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("注意：本次Q值递减");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("本次状态转移Q值更新很少");
				}else{rec.updateQ();}
				//System.out.println("本次状态转移Q值更新：trans["+	start+"]["+end+"]从"+st.trans[start][end].q+	"更新到"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("本次无方向性更新，maxQ["+	start+"]的值已经是"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("本次方向性更新很少，maxQ["+start+"]的值已经是"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("本次方向性更新:maxQ["+start+"]的值从"+st.maxQ[start]+"到"	+ q);
					st.maxQ[start]=q;
					st.maxNextS[start]=end;
					RevertScan(start);
					//break;
				}
				i--;
			}
		}
	}
}
