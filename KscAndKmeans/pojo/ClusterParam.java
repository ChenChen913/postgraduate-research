package com.research.pojo;

/**
 * ���ڴ洢kmeans�㷨�������������
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class ClusterParam {
	
	public static int cluster; //�������
	public static int clusterExeNum; // �����㷨ִ�д���
	
	/**
	 * KmeansAlg ������
	 * @param cluster
	 * @param clusterExeNum
	 */
	public ClusterParam(int cluster, int clusterExeNum)
	{
		this.cluster = cluster;
		this.clusterExeNum = clusterExeNum;
	}
}
