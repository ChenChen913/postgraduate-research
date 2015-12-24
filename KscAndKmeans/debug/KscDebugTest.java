package com.research.debug;

import com.research.Alg.KscAlg;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

public class KscDebugTest 
{
	private static KscAlg alg;
	
	public static void main(String[] args) 
	{
		int cluster; //�������
		int dimension; // ����ά��
		int rowno; // ��������
		int clusterExeNum; // �����㷨ִ�д���
		double[][] items; // �ݴ����ݵĶ�ά����
		String[] itemTag; // �ݴ������б�ǩ��һά����
		double[][] centroid;//�ݴ����ݵľ�������
		int[] clusterMemberNum; //�ݴ���������еĳ�Ա����
		String directory; // �����ļ�·��
		DataRead dataRead; //��ȡ���ݶ���
		DataWrite dataWrite; //д�����ݵ��ļ�
		TimeSeries timeSeries; // ʱ���������ݶ���
		KscAlg alg; //kmeans �����㷨����
		ClusterParam kp; //kmeans�����㷨�� ������װ��	
		
		cluster = 6;
		dimension = 3;
		rowno = 1000;
		clusterExeNum = 50;
		kp = new ClusterParam(cluster, clusterExeNum);
		items = new double[rowno][dimension + 1];// the last field holds the cluster tag
		itemTag = new String[rowno];
		centroid = new double[cluster][dimension];
		clusterMemberNum = new int[cluster];		
		//directory = "E:/bench-cluster/temp-resource/QiaoGuide/originalTimeSeries.xlsx";
		directory = "E://bench-cluster//temp-resource//QiaoGuide//151231_machine_learning//";
		//dataRead = new DataRead(directory + "originalTimeSeries.xlsx");
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory);
		test_distanceOfSampleToSample();
		
	}
	
	public static void test_distanceOfSampleToSample()
	{
		TimeSeriesSilhouette tss;
		double[] xVector;
		double[] yVector;
		
		tss = new TimeSeriesSilhouette();
		xVector = new double[]{1, 2, 3};
		yVector = new double[]{3, 1, 2};
		
		tss.distanceOfSampleToSample(xVector, yVector);
	}
}
 











