package com.research.pojo;

public class ClusterData {
	
	public static String directory;  // the path input and output files corresponding to
	public static double[][] items; // �ݴ����ݵĶ�ά���� 
	public static String[] itemTag; // �ݴ������б�ǩ��һά����
	public static double[][] centroid; //�ݴ����ݵľ�������
	public static int[] clusterMemberNum; // ÿ�������еĳ�Ա����
	public static int rowno; // ��������
	public static int dimension; // ����ά��
	public static int[] rownoWithClusterid; // item �к� ��Ӧ �� clusterid ��ʶ
	
	/**
	 * ������
	 * @param items xlsx����ӳ���Ķ�ά����
	 * @param itemTag �����е�������� or ��ǩ
	 * @param centroid �ݴ�����ִ��
	 * @param clusterMemberNum ���������еĳ�Ա����
	 * @param rowno ��������
	 * @param dimension ÿ������ά��
	 */
	public ClusterData(double[][] items, String[] itemTag, double[][] centroid, int[] clusterMemberNum, int rowno, int dimension, String directory, int[] rwc)
	{
		this.items = items;
		this.itemTag = itemTag;
		this.centroid = centroid; 
		this.rowno = rowno;
		this.dimension = dimension;
		this.clusterMemberNum = clusterMemberNum;
		this.directory = directory;
		ClusterData.rownoWithClusterid = rwc;
	}
	
	/**
	 * constructor of ClusterData
	 * @param dir the directory accessing the input and output files
	 * @param rowno num of items
	 * @param clusterNum num of given cluster
	 * @param dimension a property of given time series
	 */
	public ClusterData(String dir, int rowno, int clusterNum, int dimension)
	{
		this.directory = dir;
		this.items = new double[rowno][dimension]; 
		this.itemTag = new String[rowno];
		this.centroid = new double[clusterNum][dimension];
		this.clusterMemberNum = new int[clusterNum];
		this.rowno = rowno;
		this.dimension = dimension;
		this.rownoWithClusterid = new int[rowno];
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
