package uk.co.caprica.vlcjinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class copy {

	public static void main(String[] args) throws Exception {
		System.load("C:/Program Files/MediaInfo/MediaInfo.dll");
		String filename = "C:\\Users\\nbhushan\\Desktop\\TestAutomation\\Results.xls";
		FileInputStream fis = null;
	    

		try {

			fis = new FileInputStream(filename);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet("BaselineVerification");
			Iterator rowIter = sheet.rowIterator();
			
			while(rowIter.hasNext()){
			rowIter.next();
			HSSFRow myRow = (HSSFRow) rowIter.next();
			Iterator cellIter = myRow.cellIterator();
			HSSFCell myCell = (HSSFCell) cellIter.next();
			String baselineMedia = myCell.getStringCellValue();
			myCell = (HSSFCell) cellIter.next();
			String exportedMedia = myCell.getStringCellValue();
			
			System.out.println(baselineMedia);
			System.out.println(exportedMedia);
			MediaInfo mediaInfo1 = MediaInfo.mediaInfo(baselineMedia);
			


				HashMap<String, String> baseline_props = mediaInfo1.dump(new OutputStreamWriter(System.out));
				Section video = mediaInfo1.first("Video");
				Integer width = video.integer("Width");
				Integer height = video.integer("Height");
				baseline_props.put("width", width.toString());
				baseline_props.put("height", height.toString());
				baseline_props.put("ID", video.value("ID"));
				baseline_props.put("duration", video.value("Duration"));
				baseline_props.put("framerate", video.value("Frame rate"));
				Section audio = mediaInfo1.first("Audio");
				baseline_props.put("durationlastframe", audio.value("Duration_LastFrame"));
				
				MediaInfo mediaInfo2 = MediaInfo.mediaInfo(exportedMedia);

				HashMap<String, String> exported_props = mediaInfo2.dump(new OutputStreamWriter(System.out));
				video = mediaInfo2.first("Video");
				width = video.integer("Width");
				height = video.integer("Height");
				exported_props.put("width", width.toString());
				exported_props.put("height", height.toString());
				exported_props.put("ID", video.value("ID"));
				exported_props.put("duration", video.value("Duration"));
				exported_props.put("framerate", video.value("Frame rate"));
				audio = mediaInfo1.first("Audio");
				exported_props.put("durationlastframe", audio.value("Duration_LastFrame"));
				myCell = myRow.createCell(3);
				myCell.setCellType(HSSFCell.CELL_TYPE_STRING);  
				myCell.setCellValue("sdfhsdfsdfds");
				//FileOutputStream fileOut = new FileOutputStream("C:\\\\Users\\\\nbhushan\\\\Desktop\\\\TestAutomation\\\\Results.xls");  
				//workbook.write(fileOut);
//				if(baseline_props.equals(exported_props))
//				{
//				myCell.setCellValue("PASS!!");
//				}
//				else
//				{
//				myCell.setCellValue("PASS!!");
//				}
				}

			}
		 catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (fis != null) {

				fis.close();

			}


		}
	}
}
