import java.util.ArrayList;


public class QLearning {
	public States st;
	double gama = 0.9;//Ĭ��ϵ��
	static final double eps = 1e-5;//���¿��ƾ���
	int totalScan;
	int success;
	int Qup,Dup;//Qֵ���´����ͷ�����´�����ʼΪ0��
	int TotalQup,TotalDup;
	ArrayList<Integer> QupHis=new ArrayList<Integer>();
	ArrayList<Integer> DupHis=new ArrayList<Integer>();
	ArrayList<Double> VDisHis=new ArrayList<Double>();
	public QLearning(){
		st = new States();
	}
	
	boolean oneRouteTrain(){
		Qup=0;Dup=0;//���¼���
		int l = st.route.size();
		if(l<2) {
			System.out.println("training��������ȷ��·������Ϊ0��1");
			return false;
		}
		int i=l;
		while(i>1) {
			totalScan++;
			int start = st.route.get(i-2);
			int end = st.route.get(i-1);
			double r = st.trans[start][end].r;//��ʱ�ر�
			double qAscend = gama * st.maxQ[end];//�ۻ��ر�
			double q = r+qAscend;
			if(q<st.trans[start][end].q) {	System.out.println("ע�⣺����Qֵ�ݼ�");}
			if(Math.abs(q-st.trans[start][end].q)<eps){
				//System.out.println("����״̬ת��Qֵ���º���");
			}else{Qup++;}
			//System.out.println("����״̬ת��Qֵ���£�trans["+	start+"]["+end+"]��"+st.trans[start][end].q+	"���µ�"+q);
			st.trans[start][end].q = q;
			if(st.maxQ[start]>q-eps ){
				//System.out.println("�����޷����Ը��£�maxQ["+	start+"]��ֵ�Ѿ���"+st.maxQ[start]);
			}
			else {
				if(Math.abs(q-st.maxQ[start])<eps){
					//System.out.println("���η����Ը��º��٣�maxQ["+start+"]��ֵ�Ѿ���"+st.maxQ[start]);
				} else {Dup++;}
				//System.out.println("���η����Ը���:maxQ["+start+"]��ֵ��"+st.maxQ[start]+"��"	+ q);
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
					System.out.println("���Դ�����"+i+"����"+success+"route length:"+st.route.size()+" Qup:"+Qup+" Dup:"+Dup);
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
