package com.research.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.research.pojo.ClusterData;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataRead {
	private String dataPath;

	public DataRead(String dataPath) {
		this.dataPath = dataPath;
	}
	
	/**
	 * 
	 * @param row_start is a startup row startup index for reading. 
	 * @param col_start is a startup column index for reading.
	 * @param array is a double array storing the data read from some xlsx. 
	 */
	public final void readDataToArray(int row_start, int col_start, double[][] array) {
		
		Iterator<Row> itr = readIterator(); // ��ñ����� �ĵ�����
		Row row = null; // �ж���
		int row_index = 0;// ������
		int col_index = 0;// ������
		int row_length = array.length; // ��������
		int col_length = array[0].length; // ��������
		
		// the first row is ommited for it stores column index
		if (itr.hasNext()) {
			itr.next();
		}
		
		// ��λ��ָ�뵽 row_start
		while(itr.hasNext()){
			row_index++;
			if(row_index == row_start) {
				row_index = 0;
				break;
			}
			itr.next();
		}// ��λ over
		
		// other rows stores time series data
		while (itr.hasNext() && (row_index<row_length)) {
			col_index = 0;
			row = itr.next();
			Iterator<Cell> cellIterator = row.cellIterator(); // ����ÿ�е�Ԫ��ĵ�����
			
			Cell cell = null; 
			// the first column is ommited for it stores row index
			if(cellIterator.hasNext()) { 
				cellIterator.next();
			}
			
			// ��λ��ָ�뵽 col_start
			while(cellIterator.hasNext()) {
				col_index++;
				if(col_index == col_start) {
					col_index = 0;
					break;
				}
				cellIterator.next();
			}// ��λ over
			
			while (cellIterator.hasNext() && (col_index<col_length)) {
				cell = cellIterator.next();
				array[row_index][col_index++] = cell.getNumericCellValue();
			}// һ�����ݶ�ȡ���

			row_index++;			
		} // �����ж�ȡ���
	}
	
	// read data from xlsx to array
	public final void readDataToArray(int row_start, int row_end) {
		
		Iterator<Row> itr = readIterator(); // ��ñ����� �ĵ�����
		Row row = null; // �ж���
		int index = 0;// ������
		int row_length = row_end-row_start+1; // ��������
		
		// the first row is ommited for it stores column index
		if (itr.hasNext()) {
			row = itr.next();
		}
		
		// ��λ��ָ�뵽 row_start
		while(itr.hasNext()){
			index++;
			if(index == row_start) {
				break;
			}
			row = itr.next();
		}
		
		index -= row_start;
		// other rows stores time series data
		while (itr.hasNext() && (index!=row_length)) {
			int j = 0;
			row = itr.next();
			Iterator<Cell> cellIterator = row.cellIterator(); // ����ÿ�е�Ԫ��ĵ�����
			
			Cell cell = null; 
			// the first column is ommited for it stores row index
			if(cellIterator.hasNext())
				cell = cellIterator.next();

			while (cellIterator.hasNext()) {
				cell = cellIterator.next();
				ClusterData.items[index][j++] = cell.getNumericCellValue();  
			}// һ�����ݶ�ȡ���

			index++;			
		} // �����ж�ȡ���
	}

	// ��ȡ������ ������
	final Iterator<Row> readIterator() {
		Iterator<Row> itr = null;
		try {
			File excel = new File(this.dataPath);
			FileInputStream fis = new FileInputStream(excel);
			// ����������
			XSSFWorkbook book = new XSSFWorkbook(fis);
			// �����������µĵ�һҳֽ��
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
