package com.research.pojo;

/**
 * ���ڴ洢kmeans�㷨�������������
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class ClusterParam {
	
	public static int clusterNum; //�������
	public static int clusterExeNum; // �����㷨ִ�д���
	
	/**
	 * KmeansAlg ������
	 * @param cluster
	 * @param clusterExeNum
	 */
	public ClusterParam(int cluster, int clusterExeNum)
	{
		this.clusterNum = cluster;
		this.clusterExeNum = clusterExeNum;
	}
}
