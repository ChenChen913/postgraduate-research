package com.research.debug;

import java.util.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import com.research.Alg.KscAlg;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

import static java.lang.System.*;

public class KscDebugTest 
{
	private static KscAlg alg = new KscAlg();
	
	public static void main1(String[] args) 
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
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory, new int[3]);
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
		
	}
	
	public static void main2(String[] args)
	{
		double[][] x;
		double[][] y;
		
		x = new double[][]{{1.0, 2.0}, {}};
		y = new double[][]{{1,2},{3,4}};
		// xT * y * x = 27
	}
	
	public static void main3(String[] args)
	{
		double[] a = new double[]{1.0 ,2.0 ,3.0};
		modify(a);
		for(double t : a)
			out.print(t + " ");
	}
	
	public static void modify(double[] data)
	{
		Arrays.fill(data, 0);
	}
	
	public static void main(String[] args)
	{
		double[][] data = new double[][]{{-1, -4, 1}, {1, 3, 0}, {0, 0, 2}};
		computeSmallestEigenVector(data);
	}
	
	/**
	 * ����Ϊindex cluster ����������
	 * @param index ����Ϊindex cluster ����������
	 * @param data  ԭʼ����
	 */
	public static double[] computeSmallestEigenVector(double[][] data) { 
		
		double temp = 0, minimal = 0;
		int[] column = {0};
		int i = 0;  
		double[] array;
		
		array = new double[data.length];
		Matrix matrix2d = new Matrix(data);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(matrix2d);
		
		//minimal == smallest eigenValue , column == column with smallest eigenValue 
		minimal = ed.getD().get(i, i); 
		for (i = 1; i < data.length; i++) {
			temp = ed.getD().get(i, i);
			if (temp < minimal) {
				minimal = temp;
				column[0] = i;
			}
		}
		// ͨ����С����ֵ �� �����У� �ҳ� ��Ӧ�� ��С��������
		array = ed.getV().getMatrix(0, data.length - 1, column).getColumnPackedCopy();
		//������������������
		temp = array[0]; 
		for (int j = 0; j < data.length; j++)    
			array[j] /= temp;
		 
		return array; 
	}
}
 
