package com.research.timeseries;

public class DTWTest {
	
	public static void main(String[] args) {
		// ��������Ϊ�У���������Ϊ��
		double[] long_seq = {1, 1, 1, 10, 2, 3};
		double[] short_seq = {1, 1, 1, 2, 10, 3};
		DTW dtw = new DTW();
		
		dtw.DTW(short_seq, long_seq);
	}
}