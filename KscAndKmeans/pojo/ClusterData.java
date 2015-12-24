package com.research.pojo;

public class ClusterData {
	
	public static String directory;
	public static double[][] items; // �ݴ����ݵĶ�ά���� 
	public static String[] itemTag; // �ݴ������б�ǩ��һά����
	public static double[][] centroid; //�ݴ����ݵľ�������
	public static int[] clusterMemberNum; // ÿ�������еĳ�Ա����
	public static int rowno; // ��������
	public static int dimension; // ����ά��
	
	/**
	 * ������
	 * @param items xlsx����ӳ���Ķ�ά����
	 * @param itemTag �����е�������� or ��ǩ
	 * @param centroid �ݴ�����ִ��
	 * @param clusterMemberNum ���������еĳ�Ա����
	 * @param rowno ��������
	 * @param dimension ÿ������ά��
	 */
	public ClusterData(double[][] items, String[] itemTag, double[][] centroid, int[] clusterMemberNum, int rowno, int dimension, String directory)
	{
		this.items = items;
		this.itemTag = itemTag;
		this.centroid = centroid; 
		this.rowno = rowno;
		this.dimension = dimension;
		this.clusterMemberNum = clusterMemberNum;
		this.directory = directory;
	}

	public static double[][] getItems()
	{
		return items;
	}

	public static String[] getItemTag()
	{
		return itemTag;
	}
}
