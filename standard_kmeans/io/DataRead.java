package com.research.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.research.pojo.ClusterData;
import com.research.pojo.TimeSeries;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataRead 
{
	private String dataPath;
	
	public DataRead(String dataPath)
	{
		this.dataPath = dataPath;
	}
	
	//read data from xlsx to array
	public void readDataToArray(ClusterData kmeansData)
	{
		Iterator<Row> itr ; //��ñ����� �ĵ�����
		Row row ; //�ж���
		int index ;//������
		String[] item_tag;
		double[][] items;
		
		item_tag = kmeansData.getItemTag();
		items = kmeansData.getItems();
		
		itr = readIterator();
		row = null;
		index = 0;
		
		//the first row is ommited for it stores column index
		if(itr.hasNext()) 
		{
       	 	row = itr.next();
		}
		// other rows stores time series data
		while (itr.hasNext()) 
		{
			int j ;
			Cell cell;
			
			j = 0;
			row = itr.next(); 
			Iterator<Cell> cellIterator = row.cellIterator(); //����ÿ�е�Ԫ��ĵ�����
			cell = cellIterator.next(); 
			
			item_tag[index] = cell.getStringCellValue(); // �� xlsx �г�ȡ�б�ǩ 
			while (cellIterator.hasNext()) 
			{
                cell = cellIterator.next();
                items[index][j++] = cell.getNumericCellValue(); // ���δ�xlsx �г�ȡһ���е�ÿ����Ԫ��
            }//һ�����ݶ�ȡ���			
			
			index++;
		} // �����ж�ȡ���
	}
	
	//��ȡ������ ������
	public Iterator<Row> readIterator()
	{
		Iterator<Row> itr = null;
		try {
			File excel = new File(this.dataPath);
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
