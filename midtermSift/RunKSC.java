package com.zhongqi.running;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhongqi.processed.DataForDrawing;
import com.zhongqi.processed.Silhouette;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author lenovo
 * 
 * ���ж�ξ��࣬���� KSC�㷨
 */

public class RunKSC {
	
	private String inpath = "E:/bench-cluster/temp-resource/TimeSeries.xlsx";
		
	//��������
	static final int rowno = 1000;
	// �������
	static final int clusterno = 6;
	//����ά��
	public static final int dimension = 128;
	//��λ��
	static final int[][] unit = new int[dimension][dimension];
	//�ݴ��ά�������
	static final double[][] tempDouble = new double[dimension][dimension];
	
	// �������飨3 ��3ά��
	static double[][] centroid = new double[clusterno][dimension];
	// �������
	static final String[] team = new String[rowno];
	//���������飬 ���һ�б�ʾ���� ���
	final double[][] items_data = new double[rowno][dimension + 1];
	//�����о���Ĵ���
	static final int clusteringno = 6;
	
	public static void main(String[] args) {

		RunKSC rk = new RunKSC();
		rk.firstKsc();
	}

	public void firstKsc(){
		
		//��ñ����� �ĵ�����
		Iterator<Row> itr = readIterator();
		Row row = null;
		
		int index = 0;//������
		
		//double[] unit = new double[128];
		if(itr.hasNext()) 
			//��һ�б� ����
       	 	row = itr.next();
		while (itr.hasNext()) {
			
			if (index == rowno)
				break;
			int j = 0;
			row = itr.next(); 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			//��һ����Ԫ�񣨵�һ�У� �� ���ڴ洢�������
			Cell cell = cellIterator.next(); 
			team[index] = cell.getStringCellValue();
			
			while (cellIterator.hasNext()) {
            	
                cell = cellIterator.next();
                items_data[index][j++] = cell.getNumericCellValue();
                if (j == dimension - 1)
                	break;
            }//һ�����ݶ�ȡ���
			
			index++;
		} // �������ݶ�ȡ���

		setFirstCentroid();
		
		//��ʼ ���� item
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		
		printResult(1);
		
		// ת�� �������� ���� kmeans(1.�������� + 2.����ָ�ɴ�)
		for (int i = 2; i <= clusteringno; i++) 
			refinementAndAssignment(i);
	}
	
	//����KSC�㷨�� ������ ָ�� 
	public void refinementAndAssignment(int ith){
		
		//1.�������� (refinement-��������)
		setOtherCentroid();
		
		//2.ָ�ɷ��� item(Assignment-ָ�ɷ������)
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		//��ӡ������
		printResult(ith);
	}
	
	// ���·ǳ�ʼ����
	public void setOtherCentroid() {

		int temp = 0;
		//ÿ��cluster �� M����
		double[][][] matrixM = new double[clusterno][dimension][dimension];
		
		//������������Ϊ��
		resetCentroid();
		
		//��������(���� M ����С��������)
		for (int i = 0; i < rowno; i++) {
			
			//temp == ��item�������
			temp = (int)items_data[i][dimension];
			
			//����ÿ��cluster �� M ����
			computeTransM(matrixM[temp], items_data[i]);
		}// �Դˣ�ÿ��cluster ��M���� ������ϣ�
		 
		//�����������ģ� ��M�� ��С��������, ���µ� double[][] centroid
		for (int i = 0; i < clusterno; i++) 			
			computeSmallestEigenVector(i, matrixM[i]);
		
	}

	/**
	 * ����Ϊindex cluster ����������
	 * @param index ����Ϊindex cluster ����������
	 * @param data  ԭʼ����
	 */
	public double[] computeSmallestEigenVector(int index, double[][] data) { 
		
		double temp = 0, minimal = 0;
		int[] column = {0};
		int i = 0;  
		
		Matrix matrix2d = new Matrix(data);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(matrix2d);
		
		//minimal == smallest eigenValue , column == column with smallest eigenValue 
		minimal = ed.getD().get(i, i); 
		for (i = 1; i < dimension; i++) {
			temp = ed.getD().get(i, i);
			if (temp < minimal) {
				minimal = temp;
				column[0] = i;
			}
		}
		// ͨ����С����ֵ �� �����У� �ҳ� ��Ӧ�� ��С��������
		centroid[index] = ed.getV().getMatrix(0, dimension - 1, column).getColumnPackedCopy();
		//������������������
		temp = centroid[index][0]; 
		for (int j = 0; j < dimension; j++)    
			centroid[index][j] /= temp;
		 
		return centroid[index]; 
	}
	
	//�� item ����ָ��
	public int assignment(double[][] data){
		
		double[] temp = new double[dimension];
		double[] centroid_norm = new double[clusterno];
		double sum = 0, minSum = 0;
		int minimal = 0;
		int index = 0; //��ѡcluster������
		
		// 0.compute centroid norm
		for (int j = 0; j < clusterno; j++)  
			centroid_norm[j] = l2norm(centroid[j]);
		
		// 1.mul ut and M ; 2. (ut*M) * u
		
		// 1.1  �ȸ�ֵ minSum (index == 0), ����֤������˷� �㷨��ȷ		
		for (int j = 0, k = 0; j < dimension; j++, k++)    
			for (int j2 = 0; j2 < dimension; j2++)  
				temp[j] += centroid[index][j2] * data[j2][k];
		
		for (int j = 0; j < dimension; j++)  
			sum += temp[j] * centroid[index][j];
		sum /= centroid_norm[index];
		minSum = sum;
		minimal = index;
		
		// 1.2 �ټ��� �� �������ǳ�ʼ����� ���(index > 0)
		for (index = 1; index < clusterno; index++) { 
			sum = 0;
			for (int j = 0, k = 0; j < dimension; j++, k++)    
				for (int j2 = 0; j2 < dimension; j2++)  
					temp[j] += centroid[index][j2] * data[j2][k];
			
			for (int j = 0; j < dimension; j++) 
				sum += temp[j] * centroid[index][j]; 
			sum /= centroid_norm[index];
			if (sum < minSum) {
				minSum = sum;
				minimal = index;
			}
		}
		
		return minimal;
	}
	
	//���� �ۺϾ��� M
	public double[][] computeTransM(double[][] tempDouble, double[] data){
		
		double norm = -l2norm(data);
		double temp = 0;
//		double[][] tempDouble = new double[dimension][dimension];
		//���´����ԭ�������� mulVectorAndTransposeAndDivNormAndSubUnit
		// 1.1 ����������ת�������ĳ˻�; 1.2Ȼ����� ����; 1.3 �͵�λ�����ӷ��������������㣩
		
		for (int i = 0; i < dimension; i++)  
			for (int j = 0; j < dimension; j++) {  
				temp = data[i] * data[j];
				temp /= norm;
				if (i == j)
					temp += 1;
				tempDouble[i][j] += temp;
			}
		return tempDouble;
	}
	
	// 0.���������ĵ�2����
	public double l2norm(double[] data){
		
		double result = 0;
		
		for (int i = 0; i < dimension; i++)  
			result += Math.pow(data[i], 2.0); 
		return result;
	}
	
	//������λ��
	public void generateUnitMatrix(){
		
		for (int i = 0; i < dimension; i++)  
			unit[i][i] = 1;
	}
	
	//�����ʼ����
	public void setFirstCentroid(){
		
		Random rand = new Random();
		int[] init = {1, 199, 399, 599, 799, 999};
		
		for (int i = 0; i < clusterno; i++)  
			//init[i] = rand.nextInt(15);
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = items_data[init[i]][j];
		 
		System.out.println("====  ��Һã� ��ʼ����Ϊ��");
		for (int i = 0; i < init.length; i++)  
			System.out.print(team[init[i]]);
		System.out.println("====");		
		System.out.println("============================================================================");
	}
	
	//���������� ����Ϊ 0
	public void resetCentroid(){
		
		for (int i = 0; i < clusterno; i++)  
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = 0;
	}
	
	//��ӡ ĳ�ξ���� �Ľ��
	public void printResult(int ith) {
		
		DataForDrawing dfd = null;
		Silhouette sil = new Silhouette();
		int[] result = new int[rowno];
		int k = 0;
		
		System.out.println("==== ��" + ith + "�ξ�����Ч�� ====");
		for (int i = 0; i < clusterno; i++) {
			k = 0;
			System.out.print("���䵽��" + (i+1) + " ������У� ");
			for (int j = 0; j < rowno; j++)  
				if(items_data[j][dimension] == i) {
					System.out.print(team[j] + "  ");
					result[k++] = j;
				}
			
			// ���������е����һ�Σ� �Ͱѽ��д�뵽�µ�xlsx
			try {
				if(ith == clusteringno) {					
					dfd = new DataForDrawing("ksc");	
					dfd.centroidFinalToXls(centroid);
					for (int i2 = 0; i2 < clusterno; i2++)   
						dfd.readyToXls(result, i);						 					 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("");
		}
		
		System.out.println("==== ��" + ith + "�ξ��� over====\n");
		
		//���� ƽ������ϵ��, д������ computeSilhouette ����ɵ�
		if(ith == clusteringno) {
			try {
				sil = new Silhouette();
				sil.computeSilhouette(items_data, "ksc");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("=== ƽ������ϵ��д����� over ===");
		}
	}
	
	//��ȡ������ ������
	public Iterator<Row> readIterator(){

		Iterator<Row> itr = null;
		try {
			File excel = new File(this.inpath);
		    FileInputStream fis = new FileInputStream(excel);
		    //����������
		    XSSFWorkbook book = new XSSFWorkbook(fis);
		    //�����������µĵ�һҳֽ��
		    XSSFSheet sheet = book.getSheetAt(0);
		        
		    // ֽ�ŵĵ����������ڱ�����
		    itr = sheet.iterator();
		    // Iterating over Excel file in Java
		    
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return itr;
	}
}