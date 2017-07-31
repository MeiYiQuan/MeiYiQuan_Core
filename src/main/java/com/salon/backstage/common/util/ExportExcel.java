package com.salon.backstage.common.util;

import java.io.File;
import java.io.OutputStream;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;  

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;  
  










import jxl.Workbook;  
import jxl.format.Alignment;  
import jxl.format.Border;  
import jxl.format.BorderLineStyle;  
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;  
import jxl.write.Label;  
import jxl.write.WritableCellFormat;  
import jxl.write.WritableFont;  
import jxl.write.WritableSheet;  
import jxl.write.WritableWorkbook; 
import jxl.write.WriteException;

	/*** 
	 * @author liudan 
	 */  
	public class ExportExcel {  
	 /*************************************************************************** 
	  * @param fileName EXCEL文件名称 
	  * @param listTitle EXCEL文件第一行列标题集合 
	  * @param listContent EXCEL文件正文数据集合 
	  * @return 
	  */  
	 public final  static String exportExcel(String fileName,ExportExcelBean eeb) {  
	  String result="系统提示：Excel文件导出成功！";    
	  // 以下开始输出到EXCEL  
	  try {      
	   /** **********创建工作簿************ */  
	   WritableWorkbook workbook = Workbook.createWorkbook(new File("d:\\"+fileName));  
	  
	   /** **********创建工作表************ */  
	  
	   WritableSheet sheet = workbook.createSheet("Sheet1", 0);  
	  
	   /** **********设置纵横打印（默认为纵打）、打印纸***************** */  
	   jxl.SheetSettings sheetset = sheet.getSettings();  
	   sheetset.setProtected(false);  
	  
	   /** ************设置列的宽度  ************** */  
	   sheet.setColumnView(0, 15);
	   sheet.setColumnView(1, 25); 
	   sheet.setColumnView(2, 35);  
	   sheet.setColumnView(3, 35); 
	 
	 
	 
	  
	  
	   /** ***************以下是EXCEL开头大标题，暂时省略********************* */  
	   
	 
	    sheet.mergeCells(0, 0, 3, 0);  
	  sheet.addCell(new Label(0, 0, "录    音    分    析    表", writableFont1()));  
	  // *//** ***************以下是EXCEL第一行列标题********************* *//*  
	   for (int i = 0; i < eeb.getTitle().length; i++) {  
	    sheet.addCell(new Label(i, 1,eeb.getTitle()[i],writableFont2()));  
	   }     
	   for (int i = 0; i < eeb.getContent().length; i++) {  
			   if(i==2){
				   sheet.addCell(new Label(i, 2,eeb.getContent()[i],writableFont4())); 
			   }else{
				   sheet.addCell(new Label(i, 2,eeb.getContent()[i],writableFont3()));  
			   }  
		   }
	   for (int i = 0; i < eeb.getTitle2().length; i++) {
			   sheet.addCell(new Label(i, 3,eeb.getTitle2()[i],writableFont2()));  
		   }    
	   int index=4;
	  
	   int len=3;
	   for(int i=index;i<3*len;i+=len){
		   sheet.mergeCells(0, i, 0, (i-1)+len);  
		   sheet.mergeCells(2, i, 2, (i-1)+len);  
		   sheet.mergeCells(3, i, 3, (i-1)+len);  
		   sheet.addCell(new Label(0,i,"123",writableFont4()));  
		   sheet.addCell(new Label(2, i,"456",writableFont3()));  
		   sheet.addCell(new Label(3, i,"789",writableFont3()));  
		  for(int j=i;j<3*len+1;j++){
			   sheet.addCell(new Label(1, j,"456",writableFont3()));  
			   index=j+1;
		  }
		
	   }
	   sheet.mergeCells(0, index, 1,index+7);  
	   sheet.mergeCells(2, index, 3,index+7);  
	   sheet.addCell(new Label(0, index,"111111111",writableFont4()));  
	   sheet.addCell(new Label(2, index,"111111111",writableFont3()));  
	   index+=8;
	   sheet.mergeCells(0, index, 1,index+7);  
	   sheet.mergeCells(2, index, 3,index+7); 
	   sheet.addCell(new Label(0, index,"111111111",writableFont4()));  
	   sheet.addCell(new Label(2, index,"111111111",writableFont3()));  
	   /** **********将以上缓存中的内容写到EXCEL文件中******** */  
	   workbook.write();  
	   /** *********关闭文件************* */  
	   workbook.close();     
	  
	  } catch (Exception e) {  
	   result="系统提示：Excel文件导出失败，原因："+ e.toString();  
	   System.out.println(result);   
	   e.printStackTrace();  
	  }  
	  return result;  
	 }  
	 
	 
	 
	 /** 
	  * 设置excel 行格式 主表头
	  * @return 
	 * @throws WriteException 
	  */  
	 public static WritableCellFormat writableFont1() throws WriteException {
		  /** ************设置单元格字体************** */  
		 
		   WritableFont normalFont = new WritableFont(WritableFont.COURIER, 15,WritableFont.BOLD);  
		   WritableCellFormat wcf_title1 = new WritableCellFormat(normalFont);
		   wcf_title1.setAlignment(jxl.format.Alignment.CENTRE); 
		   wcf_title1.setBorder(Border.ALL, BorderLineStyle.MEDIUM,Colour.GRAY_50); // 线条 
		   return wcf_title1;
	}
	 /** 
	  * 设置excel 行格式 主标题
	  * @return 
	 * @throws WriteException 
	  */  
	 public static WritableCellFormat writableFont2() throws WriteException {
		 WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);  
		  /** ************设置单元格字体************** */  
		  WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
		
		   wcf_center.setBorder(Border.ALL, BorderLineStyle.MEDIUM ,Colour.AQUA); // 线条  
		   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
		   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
		   wcf_center.setWrap(true); // 文字是否换行  
		   wcf_center.setBackground(jxl.format.Colour.LIGHT_BLUE);
		   wcf_center.setAlignment(jxl.format.Alignment.CENTRE); 
		   WritableFont font2 = new WritableFont(WritableFont.ARIAL,14,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE);  
		   wcf_center.setFont(font2);  
		   return wcf_center;
	}
	 
	 /** 
	  * 设置excel 行格式 主体1
	  * @return 
	 * @throws WriteException 
	  */  
	 public static WritableCellFormat writableFont3() throws WriteException {
		 WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10);  
		// 用于正文居左  
		   WritableCellFormat wcf_left = new WritableCellFormat(BoldFont);  
		   wcf_left.setBorder(Border.ALL, BorderLineStyle.MEDIUM,Colour.AQUA); // 线条  
		   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
		   wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐  
		   wcf_left.setWrap(true); // 文字是否换行
		   return wcf_left;
	}
	 /** 
	  * 设置excel 行格式 主体1
	  * @return 
	 * @throws WriteException 
	  */  
	 public static WritableCellFormat writableFont4() throws WriteException {
		 WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10);  
		// 用于正文居左  
		   WritableCellFormat wcf_left = new WritableCellFormat(BoldFont);  
		   wcf_left.setBorder(Border.ALL, BorderLineStyle.MEDIUM,Colour.AQUA); // 线条  
		   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
		   wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐  
		   wcf_left.setWrap(true); // 文字是否换行
		   return wcf_left;
	}
	 /** 
	  * 导出excel 
	  * @return 
	  */  
	 public void elPage(){  
	   
	    
	 }  
	 public static void main(String[] args) {

		     /*String[] Title={"辅导日期","TSR姓名","所属城市、项目","录音时长"};  
		     
		     String[] Title2={"阶段","录音提现的内容","客户","TSR的话"}; */ 
		 String[]  content={"6月6号","任盈盈","上海招商银行","16分钟"};
		 List<JSONObject>list=new  ArrayList<JSONObject>();
		     JSONObject json1=new JSONObject();
		     json1.put("value1", "123");
		     json1.put("value3", "456");
		     json1.put("value4", "789");
		     JSONArray jsona1=new JSONArray();
		     for(int i=0;i<10;i++){
		    	 jsona1.add(i);
		     }
		     json1.put("value2", jsona1);
		     
		     JSONObject json2=new JSONObject();
		     json2.put("value1", "123");
		     json2.put("value3", "456");
		     json2.put("value4", "789");
		     JSONArray jsona2=new JSONArray();
		     for(int i=0;i<5;i++){
		    	 jsona2.add(i);
		     }
		     json2.put("value2", jsona2);
		     list.add(json1);
		     list.add(json2);
		     ExportExcelBean eeb=new ExportExcelBean(content,list);
		     ExportExcel.exportExcel("客户资料信息.xls",eeb);  
		    // ExportExcel.exportExcel("客户资料信息.xls",Title,Title2,content, li);   
	}
}
