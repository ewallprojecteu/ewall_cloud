package eu.ewall.platform.reasoner.sleep.psqi;

import org.springframework.beans.factory.annotation.Value;

public class PSQI {
	
	protected long Q1;
	protected int Q2;
	protected long Q3;
	protected int Q4;
	protected class Q5 {
		protected int a;
		protected int b;
		protected int c;
		protected int d;
		protected int e;
		protected int f;
		protected int g;
		protected int h;
		protected int i;
		protected int j;
		
		@Value("${psqi.q5.a}")
		public void setA(int a) {
			this.a = a;
		}
		
		@Value("${psqi.q5.b}")
		public void setB(int b) {
			this.b = b;
		}
		
		@Value("${psqi.q5.c}")
		public void setC(int c) {
			this.c = c;
		}
		
		@Value("${psqi.q5.d}")
		public void setD(int d) {
			this.d = d;
		}
		
		@Value("${psqi.q5.e}")
		public void setE(int e) {
			this.e = e;
		}
		
		@Value("${psqi.q5.f}")
		public void setF(int f) {
			this.f = f;
		}
		
		@Value("${psqi.q5.g}")
		public void setG(int g) {
			this.g = g;
		}
		
		@Value("${psqi.q5.h}")
		public void setH(int h) {
			this.h = h;
		}
		
		@Value("${psqi.q5.i}")
		public void setI(int i) {
			this.i = i;
		}
		
		@Value("${psqi.q5.j}")
		public void setJ(int j) {
			this.j = j;
		}
	}
	protected Q5 q5;
	protected int Q6;
	protected int Q7;
	protected int Q8;
	protected int Q9;
	
	@Value("${psqi.q6}")
	public void setQ6(int q) {
		this.Q6 = q;
	}
	
	@Value("${psqi.q7}")
	public void setQ7(int q) {
		this.Q7 = q;
	}
	
	@Value("${psqi.q8}")
	public void setQ8(int q) {
		this.Q8 = q;
	}
	
	@Value("${psqi.q9}")
	public void setQ9(int q) {
		this.Q9 = q;
	}
		 
	public PSQI() {
		this.q5 = new Q5();
	}

	public int OverallSleepQuality() {
		if(Q9 > 3) {
			return (3);
		} else {
			return (Q9);
		}
	}
	 
	public int SleepLatency() {
		int Q2New, Q5aNew, Aux;
		
		if(Q2 <= 15) {
			Q2New = 0;
		} else if(Q2 <= 30) {
			Q2New = 1;
		} else if(Q2 <= 60) {
			Q2New = 2;
		} else {
			Q2New = 3;
		}
	
		if(q5.a > 3) {
			Q5aNew = 3;
		} else {
			Q5aNew = q5.a;
		}
		
		Aux = Q5aNew + Q2New;
		
		if(Aux == 0) {
			return (0);
		} else if(Aux <= 2) {
			return (1);
		} else if(Aux <= 4) {
			return (2);
		} else {
			return (3);
		}
	}
	
	public int SleepDuration() {
		if(Q4 < 5) {
			return (3);
		} else if(Q4 < 6) {
			return (2);
		} else if(Q4 < 7) {
			return (1);
		} else {
			return (0);
		}
	}
	
	public int SleepEfficiency() {
		long DiffQ3Q1;
		double TiB;
		double Ratio;
		
		if(Q1 > Q3) {
			DiffQ3Q1 = 0;
		} else {
			DiffQ3Q1 = Q3 - Q1;
		}
		
		TiB = (double)(DiffQ3Q1) / 3600;
		Ratio = (double)(Q4) / TiB;
		
		if(Ratio < 0.65) {
			return (3);
		} else if(Ratio < 0.75) {
			return (2);
		} else if(Ratio  < 0.85) {
			return (1);
		} else {
			return (0);
		}
	}
	
	public int SleepDisturbance() {
		int Aux = 0;
		
		if(q5.b > 3) {
			Aux += 3;
		} else {
			Aux += q5.b;
		}
		
		if(q5.c > 3) {
			Aux += 3;
		} else {
			Aux += q5.c;
		}
		
		if(q5.d > 3) {
			Aux += 3;
		} else {
			Aux += q5.d;
		}
		
		if(q5.e > 3) {
			Aux += 3;
		} else {
			Aux += q5.e;
		}
		
		if(q5.f > 3) {
			Aux += 3;
		} else {
			Aux += q5.f;
		}
		
		if(q5.g > 3) {
			Aux += 3;
		} else {
			Aux += q5.g;
		}
		
		if(q5.h > 3) {
			Aux += 3;
		} else {
			Aux += q5.h;
		}
		
		if(q5.i > 3) {
			Aux += 3;
		} else {
			Aux += q5.i;
		}
		
		if(q5.j > 3) {
			Aux += 3;
		} else {
			Aux += q5.j;
		}
		
		if(Aux < 1) {
			return (0);
		} else if(Aux < 10) {
			return (1);
		} else if(Aux < 19) {
			return (2);
		} else {
			return (3);
		}
	}
	
	public int NeedMedicine() {
		if(Q6 > 3) {
			return (3);
		} else {
			return (Q7);
		}
	}
	
	public int DayDysfunction() {
		int Aux;
		
		if(Q7 > 3) {
			Aux = 3;
		} else {
			Aux = Q7;
		}
		
		if(Q8 > 3) {
			Aux += 3;
		} else {
			Aux += Q8;
		}
		
		if(Aux < 1) {
			return (0);
		} else if(Aux < 3) {
			return (1);
		} else if(Aux < 5) {
			return (2);
		} else {
			return (3);
		}
		
	}
	
	public int TotCalc() {
		return (OverallSleepQuality() + SleepLatency() + + SleepDuration() + SleepEfficiency() + SleepDisturbance() + NeedMedicine() + DayDysfunction());
	}
	
}
