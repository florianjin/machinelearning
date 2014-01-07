import java.util.ArrayList;
import java.util.Arrays;


public class States {
	public Transition[][] trans;
	//null表示不可以走，有值表示可以走
	public boolean[] isFinal;//true表示吸收状态
	public double maxQ[];
	public double maxV[];
	public int maxNextS[];
	public int scale=10;//10*10的状态规模
	public int usedL;//用过的状态个数
	static int maxTry=400;//最大尝试次数
	ArrayList<Integer> route = new ArrayList<Integer>();
	ArrayList<Double> routeRs = new ArrayList<Double>();//考虑一路上的即时回报
	double routeR;
	States(){
		trans = new Transition[1000][1000];
		isFinal = new boolean[1000];
		maxQ = new double[1000];
		maxV = new double[1000];
		maxNextS = new int[1000];
		initialize();
		calcMaxV();
	}
	States(int num){
		trans = new Transition[num][num];
		isFinal = new boolean[num];
		maxQ = new double[num];
		maxV = new double[num];
		maxNextS = new int[num];
		initialize();
	}
	public double getMaxQofS(int s){
		double result = 0;
		for(int i=0;i<usedL;i++){
			result=Math.max(result, trans[s][i].q);
		}
		maxQ[s]=result;
		return result;
	}
	public boolean newTrans(int start, int end, double r){
		if(trans[start][end]!=null) {
			System.out.println("newTrans:已存在转换");
			return false;
		}
		int maxs = Math.max(start, end)+1;
		if(maxs>isFinal.length) {
			System.out.println("newTrans:超出trans容量");
			return false;
		}
		usedL=Math.max(maxs, usedL);
		trans[start][end] = new Transition(r); 
		return true;
	}
	public void empty(){
		for(int i=0;i<usedL;i++ ){
			for(int j=0;j<usedL;j++) {
				trans[i][j]=null;
			}
		}
		usedL=0;
		Arrays.fill(maxQ, 0);
		Arrays.fill(maxNextS, 0);
		Arrays.fill(isFinal, false);
		route.clear();
		routeRs.clear();
		routeR=0;
	}
	public void clearQ(){
		empty();
		initialize();
	}
	public void initialize(){
		int i,j;
		for(i=0;i<scale;i++){
			for(j=1;j<scale;j++){
				newTrans(scale*i+j-1, scale*i+j, 0);
				newTrans(scale*i+j, scale*i+j-1, 0);
				newTrans(scale*(j-1)+i, scale*j+i, 0);
				newTrans(scale*j+i, scale*(j-1)+i, 0);
			}
		}
		trans[scale*scale-2][scale*scale-1].r=100;
		trans[scale*scale-scale-1][scale*scale-1].r=100;
		isFinal[scale*scale-1]=true;
		/*st.newTrans(0, 1, 0);
		st.newTrans(1, 0, 0);
		st.newTrans(1, 2, 100);
		st.newTrans(0, 3, 0);
		st.newTrans(3, 0, 0);
		st.newTrans(3, 4, 0);
		st.newTrans(4, 3, 0);
		st.newTrans(1, 4, 0);
		st.newTrans(4, 1, 0);
		st.newTrans(4, 5, 0);
		st.newTrans(5, 4, 0);
		st.newTrans(5, 2, 100);*/
	}
	public void calcMaxV(){
		for(int i=0;i<scale;i++){
			for(int j=0;j<scale;j++){
				int distance = scale*2-i-j-3;
				if(distance==-1)	maxV[scale*i+j]=0;
				else maxV[scale*i+j]=100*Math.pow(0.9,distance);
			}
		}
	}
	double calcVDis(){
		double[] V = maxQ;
		int MaxL = scale*scale;
		double vDis=0;
		for(int i=0;i<MaxL;i++){
			vDis+=(V[i]-maxV[i])*(V[i]-maxV[i]);
		}
		return vDis;
	}
	int getRouteSt(){
		int trys;
		for(trys=0;trys<maxTry;trys++) {
			int start = (int) (Math.random()*usedL);
			if(isFinal[start]) continue;
			return start;
		}
		return -1;
	}
	int getNextSt(int start){
		if(start>usedL) return -1;
		if(isFinal[start]) return -2;
		ArrayList<Integer> possible = new ArrayList<Integer>();
		int i;
		for(i=0;i<usedL;i++){
			Transition t=trans[start][i];
			if (t!=null) {
				if(!route.contains(i)) possible.add(i); 
				//else System.out.println(route+"重复点："+i);
			}
		}
		int l = possible.size();
		if(l==0) return -3;
		int suf = (int) (Math.random()*l);
		int x = possible.get(suf);
		if(trans[start][x]==null){	System.out.println(start+""+ x+"异常1！");}
		return x;
	}
	boolean getRoute(){
		route.clear();routeRs.clear();routeR=0;
		int start = getRouteSt();
		//start = 0;//路径是否规定起始点
		if(start==-1) {
			System.out.println("getRoute: 找不到开始点");
			return false;
		}
		route.add(start);
		for(int i=0;i<maxTry;i++) {
			int x = getNextSt(start);
			if(x==-1) {
				System.out.println("getRoute: 开始点超越了的范围:"+start);
				return false;
			}
			if(x==-2){
				//System.out.println("getRoute: 到达最终点:"+start);
				//System.out.println(route);
				routeR = trans[route.get(route.size()-2)][start].r;
				return true;
			}
			if(x==-3){
				//System.out.println("getRoute: 走进了死路"+start);
				//System.out.println(route);
				return false;
			}
			//System.out.println("找到下一个节点："+x);
			route.add(x);
			routeRs.add(trans[start][x].r);
			start=x;
		}
		System.out.println("尝试超过了"+maxTry+"次");
		return false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		States st = new States(100);
		System.out.println(st.isFinal[1]);
		System.out.println(st.trans[1][1]);
		System.out.println(st.maxQ[1]);
		System.out.println((int) (Math.random()*5));
		ArrayList<Integer> s = new ArrayList<Integer>();
		s.add(1);
		s.add(2);
		System.out.println(s.contains(1));
	}

}
