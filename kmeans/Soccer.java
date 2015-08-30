package com.zhongqi.kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Soccer {

	private String inpath = "E:/bench-cluster/temp-resource/soccer.xlsx";
	
	// �������
	static int cluster = 3;
	// �������飨3 ��3ά��
	static double[][] centroid = new double[3][3];
	// �������
	static String[] team = new String[15];
	//���������飬 ���һ�б�ʾ���� ���
	double[][] items_data = new double[15][cluster + 1];
			
	public static void main(String[] args) {

		Soccer soccer = new Soccer();
		soccer.firstKmeans();
	}

	public void firstKmeans(){
		
		//��ñ����� �ĵ�����
		Iterator<Row> itr = readIterator();
		Row row = null;
		
		// ǰ3�����ݣ��������
		for (int i = 0; i < cluster; i++) 
			items_data[i][cluster] = i;
		
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
		//������
		//iran(3), Japan(1), Bahlin(12)
		//assignment(items_data[3]);
		
		
		//��ʼ ���� item
		for (int i = 0; i < items_data.length; i++)  
			items_data[i][cluster] = assignment(items_data[i]);
		
		printResult(1);
		
		// ת�� �������� ���� kmeans(1.�������� + 2.����ָ�ɴ�)
		for (int i = 2; i < 4; i++) 
			refinementAndAssignment(i);
	}
	
	public void refinementAndAssignment(int ith){
		
		//1.�������� (refinement-��������)
		setOtherCentroid();
		
		//2.ָ�ɷ��� item(Assignment-ָ�ɷ������)
		for (int i = 0; i < items_data.length; i++)  
			items_data[i][cluster] = assignment(items_data[i]);
		//��ӡ������
		printResult(ith);
	}
	
	//��item ����ָ��
	public int assignment(double[] item){
		
		double sum = 0, minSum = 0;
		int minimal = 0;
		
		for (int j = 0; j < 3; j++) {
			sum += Math.pow(item[j] - centroid[0][j], 2.0);
		}
		minSum = sum;
		
		for (int i = 1; i < 3; i++) {
			sum = 0;
			for (int j = 0; j < 3; j++) {
				sum += Math.pow(item[j] - centroid[i][j], 2.0);
			}
			if(sum < minSum) {
				minSum = sum;
				minimal = i;
			}
		}
		return minimal;
	}
	
	// ����ǳ�ʼ����
	public void setOtherCentroid() {

		int[] classnum = new int[3]; 
		int temp = 0;
		
		//������������
		resetCentroid();
		for (int i = 0; i < items_data.length; i++) {
			
			temp = (int)items_data[i][3];
			centroid[temp][0] += items_data[i][0];
			centroid[temp][1] += items_data[i][1];
			centroid[temp][2] += items_data[i][2];
			classnum[temp] += 1;  
		}
		
		//��������
		for (int i = 0; i < cluster; i++) {
			
			centroid[i][0] /= classnum[i];
			centroid[i][1] /= classnum[i];
			centroid[i][2] /= classnum[i];
		}
	}
	
	//��ӡ ĳ�ξ���� �Ľ��
	public void printResult(int ith){
		
		System.out.println("==== ��" + ith + "�ξ�����Ч�� ====");
		for (int i = 0; i < cluster; i++) {
			
			System.out.print("���䵽��" + (i+1) + " ������У� ");
			for (int j = 0; j < items_data.length; j++) {
				
				if(items_data[j][cluster] == i)
					System.out.print(team[j] + "  ");
			}
			System.out.println("");
		}
		System.out.println("==== ��" + ith + "�ξ��� over====\n");
	}
	
	//���������� ����Ϊ 0
	public void resetCentroid(){
		
		for (int i = 0; i < centroid.length; i++) {
			centroid[i][0] = 0;
			centroid[i][1] = 0;
			centroid[i][2] = 0;
		}
	}
	
	//�����ʼ����
	public void setFirstCentroid(){
		
		Random rand = new Random();
		int[] init = {1, 12, 9};
		
		for (int i = 0; i < cluster; i++) {
			//init[i] = rand.nextInt(15);
			centroid[i][0] = items_data[init[i]][0];
			centroid[i][1] = items_data[init[i]][1];
			centroid[i][2] = items_data[init[i]][2];
		}
		System.out.println("====  ��Һã� ��ʼ����Ϊ��" + team[init[0]] + "  " + team[init[1]] + "  " + team[init[2]] + "  ====");
		System.out.println("============================================================================");
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
