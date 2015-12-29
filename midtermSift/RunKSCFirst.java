package com.zhongqi.running;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * @author lenovo
 *
 * ֻ������ һ�� ���࣬��ʼ����
 */
public class RunKSCFirst {
	
	private String inpath = "E:/bench-cluster/temp-resource/timeSeries.xlsx";
		
	// �������
	static final int cluster = 3;
	//����ά��
	static final int dimension = 128;
	//��������
	static final int rowno = 14;
	//��λ��
	static final int[][] unit = new int[dimension][dimension];
	//�ݴ��ά�������
	// static final double[][] tempDouble = new double[dimension][dimension];
	
	// �������飨3 ��3ά��
	static double[][] centroid = new double[cluster][dimension];
	// �������
	static final String[] team = new String[rowno];
	//���������飬 ���һ�б�ʾ���� ���
	final double[][] items_data = new double[rowno][dimension + 1];
		
	
	public static void main(String[] args) {

		RunKSCFirst rk = new RunKSCFirst();
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
			
			int j = 0;
			row = itr.next(); 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			//��һ����Ԫ�񣨵�һ�У� �� ���ڴ洢�������
			Cell cell = cellIterator.next(); 
			team[index] = cell.getStringCellValue();
			
			while (cellIterator.hasNext()) {
            	
                cell = cellIterator.next();
                items_data[index][j++] = cell.getNumericCellValue();
            }//һ�����ݶ�ȡ���
			
			index++;
		}

		setFirstCentroid();
		
		//��ʼ ���� item
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		
		printResult(1);
		
		/*// ת�� �������� ���� kmeans(1.�������� + 2.����ָ�ɴ�)
		for (int i = 2; i < 6; i++) 
			refinementAndAssignment(i);*/
	}
	
	//����KSC�㷨�� ������ ָ�� 
	public void refinementAndAssignment(int ith){
		
		//1.�������� (refinement-��������)
//		setOtherCentroid();
		
		//2.ָ�ɷ��� item(Assignment-ָ�ɷ������)
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		//��ӡ������
		printResult(ith);
	}
	
	// ���·ǳ�ʼ����
	 
	//�� item ����ָ��
	public int assignment(double[][] data){
		
		double[] temp = new double[dimension];
		double[] centroid_norm = new double[3];
		double sum = 0, minSum = 0;
		int minimal = 0;
		int index = 0; //��ѡcluster������
		
		// 0.compute centroid norm
		for (int j = 0; j < cluster; j++)  
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
		for (index = 1; index < cluster; index++) { 
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
//			double[][] tempDouble = new double[dimension][dimension];
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
		
		int[] init = {1, 12, 9};
		
		for (int i = 0; i < cluster; i++)  
			//init[i] = rand.nextInt(15);
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = items_data[init[i]][j];
		 
		System.out.println("====  ��Һã� ��ʼ����Ϊ��" + team[init[0]] + "  " + team[init[1]] + "  " + team[init[2]] + "  ====");
		System.out.println("============================================================================");
	}
	
	//���������� ����Ϊ 0
	public void resetCentroid(){
		
		for (int i = 0; i < cluster; i++)  
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = 0;
	}
	
	//��ӡ ĳ�ξ���� �Ľ��
	public void printResult(int ith){
		
		System.out.println("==== ��" + ith + "�ξ�����Ч�� ====");
		for (int i = 0; i < cluster; i++) {
			
			System.out.print("���䵽��" + (i+1) + " ������У� ");
			for (int j = 0; j < rowno; j++) {
				
				if(items_data[j][dimension] == i)
					System.out.print(team[j] + "  ");
			}
			System.out.println("");
		}
		
		System.out.println("==== ��" + ith + "�ξ��� over====\n");
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