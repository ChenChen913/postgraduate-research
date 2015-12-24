package com.search.alltest;

import java.io.IOException;

import com.research.Alg.KmeansAlg;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class TimesSeriesKmeansTest 
{
	public static void main(String[] args) throws IOException 
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
		KmeansAlg kmeansAlg; //kmeans �����㷨����
		ClusterParam kp; //kmeans�����㷨�� ������װ��	
		
		cluster = 6;
		dimension = 128;
		rowno = 1000;
		clusterExeNum = 20;
		kp = new ClusterParam(cluster, clusterExeNum);
		items = new double[rowno][dimension + 1];// the last field holds the cluster tag
		itemTag = new String[rowno];
		centroid = new double[cluster][dimension];
		clusterMemberNum = new int[cluster];		
		//directory = "E:/bench-cluster/temp-resource/QiaoGuide/originalTimeSeries.xlsx";
		directory = "E://bench-cluster//temp-resource//QiaoGuide//151231_machine_learning//";
		dataRead = new DataRead(directory + "originalTimeSeries.xlsx");
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory);
		kmeansAlg = new KmeansAlg();
		
		dataRead.readDataToArray(timeSeries); // reading data from xlsx to array over
		kmeansAlg.clusterAlg(); //����kmeans����;
		
		System.out.println("kmeans alg over");
		
		//����������� д����������ļ� + д������������Ϊ�ļ�
//		directory += "\\151231_machine_learning\\";
		dataWrite = new DataWrite(directory);
		dataWrite.writeCentroid(ClusterData.centroid);
		
		System.out.println("===time series kmeans test over===");
	}
}
