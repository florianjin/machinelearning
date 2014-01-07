import java.util.ArrayList;


public class AscenQLearning {
	public States st;
	double gama = 0.9;//Ĭ��ϵ��
	static final double eps = 1e-5;//���¿��ƾ���
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
		System.out.println("Ϊ��·�����������̽��������"+totalTries);
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
			if(newQ<st.trans[start][end].q) System.out.println("������ԭQֵ�ϴ�");
			rec.updateRevertQ();//RevertQup++;
			st.trans[start][end].q=newQ;
			if(st.maxQ[start]>newQ-eps ){
				//System.out.println("������ɨ����޷����Ը��£�maxQ["+	start+"]��ֵ�Ѿ���"+st.maxQ[start]);
			}
			else {
				if(Math.abs(newQ-st.maxQ[start])<eps){
					//System.out.println("������ɨ��㷽���Ը��º��٣�maxQ["+start+"]��ֵ�Ѿ���"+st.maxQ[start]);
				} else {rec.updateRevertV();}//RevertDup++;}
				//System.out.println("������ɨ��㷽���Ը���:maxQ["+start+"]��ֵ��"+st.maxQ[start]+"��"	+ q);
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
					//System.out.println("������"+i+"route length:"+routePool.get(i).size()+" Qup:"+rec.inRouteQup+" Vup:"+rec.inRouteVup);
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
				double r = routeRPool.get(index).get(i-2);//��ʱ�ر�
				if(st.trans[start][end]==null) {
					st.newTrans(start, end, r);
				}
				double qAscend = gama * st.maxQ[end];//�ۻ��ر�
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("ע�⣺����Qֵ�ݼ�");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("����״̬ת��Qֵ���º���");
				}else{rec.updateQ();}
				//System.out.println("����״̬ת��Qֵ���£�trans["+	start+"]["+end+"]��"+st.trans[start][end].q+	"���µ�"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("�����޷����Ը��£�maxQ["+	start+"]��ֵ�Ѿ���"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("���η����Ը��º��٣�maxQ["+start+"]��ֵ�Ѿ���"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("���η����Ը���:maxQ["+start+"]��ֵ��"+st.maxQ[start]+"��"	+ q);
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
				double r = routeRPool.get(index).get(i-2);//��ʱ�ر�
				if(st.trans[start][end]==null) {
					st.newTrans(start, end, r);
				}
				double qAscend = gama * st.maxQ[end];//�ۻ��ر�
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("ע�⣺����Qֵ�ݼ�");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("����״̬ת��Qֵ���º���");
				}else{rec.updateQ();}
				//System.out.println("����״̬ת��Qֵ���£�trans["+	start+"]["+end+"]��"+st.trans[start][end].q+	"���µ�"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("�����޷����Ը��£�maxQ["+	start+"]��ֵ�Ѿ���"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("���η����Ը��º��٣�maxQ["+start+"]��ֵ�Ѿ���"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("���η����Ը���:maxQ["+start+"]��ֵ��"+st.maxQ[start]+"��"	+ q);
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
				double r = routeRPool.get(index).get(i-2);//��ʱ�ر�
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
				double r = routeRPool.get(index).get(i-2);//��ʱ�ر�
				double qAscend = gama * st.maxQ[end];//�ۻ��ر�
				double q = r+qAscend;
				if(q<st.trans[start][end].q) {	System.out.println("ע�⣺����Qֵ�ݼ�");}
				if(Math.abs(q-st.trans[start][end].q)<eps){
					//System.out.println("����״̬ת��Qֵ���º���");
				}else{rec.updateQ();}
				//System.out.println("����״̬ת��Qֵ���£�trans["+	start+"]["+end+"]��"+st.trans[start][end].q+	"���µ�"+q);
				st.trans[start][end].q = q;
				if(st.maxQ[start]>q-eps ){
					//System.out.println("�����޷����Ը��£�maxQ["+	start+"]��ֵ�Ѿ���"+st.maxQ[start]);
				}
				else {
					if(Math.abs(q-st.maxQ[start])<eps){
						//System.out.println("���η����Ը��º��٣�maxQ["+start+"]��ֵ�Ѿ���"+st.maxQ[start]);
					} else {rec.updateV();}
					//System.out.println("���η����Ը���:maxQ["+start+"]��ֵ��"+st.maxQ[start]+"��"	+ q);
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
