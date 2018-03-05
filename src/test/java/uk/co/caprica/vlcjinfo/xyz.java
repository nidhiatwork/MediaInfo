package uk.co.caprica.vlcjinfo;

import java.io.File;

public class xyz {
	static String path = "C:\\Users\\nbhushan\\Desktop\\TestAutomation\\TestData\\Baseline_clips\\Windows\\Effects_old";
	
	public static void main(String[] args) throws Exception {
		File folder = new File(path);
		String getname= getFile(folder);
		System.out.println(getname);
	}
	
	public static String getFile(File folder)
	{
		String file = "";
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
		      if (listOfFiles[i].isFile())
		      {
		    	  if(listOfFiles[i].getName().contains("1009302"))
		        file = listOfFiles[i].getName();
		      }
		      
		      else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains("1009302")) 
		      {
		    	  path = path + "\\" + listOfFiles[i].getName();
		        folder = new File(path);
		        file = getFile(folder);
		      }
		    }
		    return file;
	}
}

