package uk.co.caprica.vlcjinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
public class TestBaseline {

	static String baselineFolder;

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.class.path"));
		System.load("C:/Program Files/MediaInfo/MediaInfo.dll");
		String baselineDataFile = "C:\\Users\\nbhushan\\Desktop\\TestAutomation\\BaselineVerification\\baselineData.txt";
		String old_baselineDataFile = "C:\\Users\\nbhushan\\Desktop\\TestAutomation\\BaselineVerification\\old_baselineData.txt";
		String excel = "C:\\Users\\nbhushan\\Desktop\\TestAutomation\\Results.xls";
		String line = null;
		String baselineFilePath;
		String exportedFilePath;
		PrintWriter writer;
		try {
			FileReader fileReader = new FileReader(baselineDataFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				line = line.replace("/", "\\");
				writer = new PrintWriter(old_baselineDataFile);
				writer.print(line);
				writer.close();
				String[] media = line.split("~~");
				baselineFolder = media[2];
				exportedFilePath = media[3];
				baselineFilePath = getBaselineFile(media[0]);

				MediaInfo mediaInfo1 = MediaInfo.mediaInfo(baselineFilePath);
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

				MediaInfo mediaInfo2 = MediaInfo.mediaInfo(exportedFilePath);

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
				baseline_props.remove("Complete name");
				exported_props.remove("Complete name");
				String result;
				
				if(baseline_props.equals(exported_props))
					result = "PASS";
				else
					result = "FAIL";

				FileInputStream fis = null;
				fis = new FileInputStream(excel);
				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				HSSFSheet sheet = null;
				int paramIndex = 10, baselineIndex = 11, exportedIndex = 12, resultIndex = 13;

				if(media[1].contains("Effects.jsx"))
					sheet = workbook.getSheet("Effects");		

				else if(media[1].contains("EffectsKeyframing.jsx"))
				{
					sheet = workbook.getSheet("EffectsKeyframing");
					paramIndex = 8;
					baselineIndex = 10;
					exportedIndex = 11;
					resultIndex = 12;
				}
				else if(media[1].contains("PublishNShare.jsx"))
				{
					sheet = workbook.getSheet("Sharing Center");
					paramIndex = 13;
					baselineIndex = 14;
					exportedIndex = 15;
					resultIndex = 16;
				}
				else if(media[1].contains("TitleSanity.jsx") || media[1].contains("ApplyTextAnimation.jsx") ||  media[1].contains("ApplyTitleTemplate.jsx") || media[1].contains("TextStyles.jsx"))
					sheet = workbook.getSheet("Title Designer");
				else
				{
					bufferedReader.close();  
					writer = new PrintWriter(baselineDataFile);
					writer.print("");
					writer.close();
					fis.close();
					System.exit(0);
				}
				for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					if (row != null) {
						Cell cell = row.getCell(paramIndex);
						if (cell != null) {
							// Found column and there is value in the cell.
							String val = cell.getStringCellValue();
							if(val.contains(media[0]))
							{
								cell = row.createCell(baselineIndex);
								cell.setCellValue(media[2]);
								cell = row.createCell(exportedIndex);
								cell.setCellValue(media[3]);
								cell = row.createCell(resultIndex);
								cell.setCellValue(result);

							}
						}
					}
				}
				fis.close();
				FileOutputStream fileOut = new FileOutputStream("C:\\Users\\nbhushan\\Desktop\\TestAutomation\\Results.xls");  
				workbook.write(fileOut);
				fileOut.close();

			}
			bufferedReader.close();  
			writer = new PrintWriter(baselineDataFile);
			writer.print("");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error occurred while reading/writing data into file");
			e.printStackTrace();
		}

	}

	private static String getBaselineFile(String testcaseID) 
	{
		File folder = new File(baselineFolder);
		String file = "";
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile())
			{
				if(listOfFiles[i].getName().contains(testcaseID))
				{
					file = listOfFiles[i].getName();
					break;
				}
			}
			else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains(testcaseID)) 
			{
				baselineFolder = baselineFolder + "\\" + listOfFiles[i].getName();
				file = getBaselineFile(testcaseID);
			}
		}
		return baselineFolder + "\\" + file;
	}
}


