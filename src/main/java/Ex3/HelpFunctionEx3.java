/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ex3;
//libraries
import Ex1.DataWIFI;
import Ex1.Location;
import Ex1.WIFI;
import Ex2.ALGOtwoCLASS;
import Ex2.ClassOfAlgorithm1;
import Ex2.HelpFunctions;
import Ex4.readSQL;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * help function for Assignment 3
 * @author Alexey Titov   and   Shalom Weinberger
 * @version 2
 */
public class HelpFunctionEx3 
{
    //variables
    private static String pathDataBase;     //path to database
    private static ReadFunctions rf;
    private static WriteFunctions wf;
    /**
     * the function add sql data to location database
     * @param dwf data from sql
     */
    public void AddSQL(ArrayList<DataWIFI> dwf)
    {
        try{
            ArrayList<DataWIFI> fileData= DeleteDuplicate(dwf);
            wf.WriteCSV(fileData);
        }catch(Exception e){
            System.out.println("Exception Ex3.HelpFunctionEx3.AddSQL");
        }
    }
    /**
     * the function clears the database
     */
    public void Clear()
    {
        File myFile = new File(pathDataBase);
        if (myFile.exists()){
            myFile.delete();
            try {
                // use FileWriter constructor that specifies open for appending
                CsvWriter tmp = new CsvWriter(new FileWriter(myFile, false), ',');	
                //headers for first row
                tmp.write("TIME");          tmp.write("ID");
                tmp.write("latitude");      tmp.write("longitude");         tmp.write("altitude");
                tmp.write("#WiFi networks");
		for (int i=1;i<11;i++)
		{
                    tmp.write("SSID"+i);
                    tmp.write("MAC"+i);
                    tmp.write("Frequency"+i);
                    tmp.write("Signal"+i);
		}
		tmp.endRecord();
		tmp.close();
            }catch (IOException e){
                JOptionPane.showMessageDialog(null,"database not cleared");
                return;
            }
        }
        JOptionPane.showMessageDialog(null,"database cleared");
    }
    /**
     * the function writes database in kml format
     * @return save file path 
     */
    public String SaveKML()
    {
        ArrayList<DataWIFI> tmp=rf.ReadCSV(pathDataBase);
        try {
            return wf.WriteKML(tmp);
	}catch (IOException e) {
            return "KML File&";
        }
    }
    /**
     * the function browse a folder with the Wigle files
     * @return path
     */
    public String BrowseFolder()
    {
        //variable
        int ret;
        //File chooser
        JFileChooser fileopen = new JFileChooser();
        //only directory
        fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileopen.setAcceptAllFileFilterUsed(false);
        //dialog box for determining the desired directory
        ret=fileopen.showDialog(null, "Open directory");
        //check if directory is not selected
        if (!(ret == JFileChooser.APPROVE_OPTION))
        {
            JOptionPane.showMessageDialog(null, "Directory is not selected\ngoodbye");
            return "Name Folder";
        }
        return fileopen.getSelectedFile().getAbsolutePath();
    }
    /**
     * the function browse the Wigle file
     * @return path
     */
    public String BrowseFile()
    {
        //variables
        int ret;                                        //flag for check a directory is selection
        JFileChooser fileopen = new JFileChooser();
        String csvFile ="";				//input csv file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WIGLE-WIFI CSV FILES","csv");
        //open window
        fileopen.setFileFilter(filter);
        ret=fileopen.showDialog(null, "Open csv file");                
        if (ret == JFileChooser.APPROVE_OPTION &&
        fileopen.getSelectedFile().getAbsolutePath().substring(fileopen.getSelectedFile().getAbsolutePath().length()-3).equals("csv"))
        {
            csvFile=fileopen.getSelectedFile().getAbsolutePath();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "file is not selected or the file type is not valid,\ngoodbye");
            return "Name File";
        }
        return csvFile;
    }
    /**
     * the functon return data about number of WiFi and rows in database
     * @return data about number of WiFi and rows in database
     */
    public String ShowData()
    {
    	try {
    	return rf.ReadShowData();
    	}catch(NullPointerException e) {
            return "Data: file not found";
        }
    }
    /**
     * the function saves a special file
     * @return special file path
     */
    public String SaveFile()
    {
        //variables
        File source = new File(pathDataBase);   //database
        File dest;                              //printing way where to copy
	String OUTcsvFile="";			//output csv file
	//select the location of the file
	FileNameExtensionFilter filter = new FileNameExtensionFilter("*.CSV","*.*");
	JFileChooser fc = new JFileChooser();
	fc.setFileFilter(filter);
	if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
            try {
            //user wrote at the end csv
            if (fc.getSelectedFile().getAbsolutePath().substring(fc.getSelectedFile().getAbsolutePath().length()-4).equals(".csv"))
                OUTcsvFile=fc.getSelectedFile().getAbsolutePath();
            else 	//user did not wrote at the end csv
		OUTcsvFile=fc.getSelectedFile().getAbsolutePath()+".csv";
            }catch (Exception e ) {
                JOptionPane.showMessageDialog(null, "an error occurred, the file was not saved.\ngoodbye");
		return "Save File";
            }
	}
	else	//user did not select a file to save
	{
            JOptionPane.showMessageDialog(null, "you did not select a file to save,\ngoodbye");
            return "Save File";
        }
        dest=new File(OUTcsvFile);
        try {
            Files.copy(source.toPath(), dest.toPath());
            return dest.toString();
        }catch(IOException ex){
            return "Save File";
        }
        }
    //the function delete duplicate from new data
    private ArrayList<DataWIFI> DeleteDuplicate(ArrayList<DataWIFI> newdata)
    {
        ArrayList<DataWIFI> result =ReadFunctions.ReadDataBase();
        for(int i=0;i<newdata.size();i++)
        {    
            for(int j=0;j<result.size();j++)
                if (result.get(j).getLla().compareLLA(newdata.get(i).getLla())==0 &&
                    result.get(j).getID().equals(newdata.get(i).getID()) &&
                    result.get(j).getTIME().equals(newdata.get(i).getTIME()))
                {    
                    newdata.remove(i);
                    i--;
                    break;
                }
        }
        return newdata;
    }
    //the function add data from the wigle file to database
    public synchronized void AddFile(String filepath)
    {
        ArrayList<String> csvFiles=new ArrayList<>();
        csvFiles.add(filepath);
        ArrayList<DataWIFI> fileData= DeleteDuplicate(rf.ReadCSV(csvFiles));
        wf.WriteCSV(fileData);
    }
    //the function add data from wigle folder to database
    public synchronized void AddFolder(String folderpath)
    {
        //variables
        ArrayList<String> files=new ArrayList<>();	//list csv files
        ArrayList<DataWIFI> fileData=new ArrayList<>();	//data of WiFiNetwork
        File []fList;        
        File F = new File(folderpath);                          //the path to the directory
        //all files that are in the folder     
        fList = F.listFiles();
        //runs at the folder.
        for(int i=0; i<fList.length; i++)           
        {
            String mark=fList[i].getName();  
            //check if name is csv file
            if(fList[i].isFile() && mark.substring(mark.length()-3).equals("csv"))
                files.add(folderpath+"\\"+fList[i].getName());
        }
        //no files
        if (files.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "in the folder there are no csv files");
        }
        else
        {
            fileData= DeleteDuplicate(rf.ReadCSV(files));
            wf.WriteCSV(fileData);
        }
    }
    /**
     * the function makes the first algorithm
     * @param mac for first algorithm
     * @return location
     */
    public Location FirstAlgorithm(String mac)
    {
        //variables
        ClassOfAlgorithm1 coaFirst=new ClassOfAlgorithm1();
        HelpFunctions hlp=new HelpFunctions();
        Location place=new Location();
        try{
        	coaFirst=rf.ReadFirstAlgo(mac);
        	place=hlp.WriteWeight(coaFirst);
        	if (place==null)
        		return new Location();
        	return place;
        }catch(NullPointerException e) {
        	return new Location();
        }
    }
    /**
    * the function returns a place for the three MACs according to the second algorithm 
    * @param DWF-three MACs
    * @param three data of three MACs
    * @return location of three MACs
    */
    public Location ThreeMac(ArrayList<DataWIFI> DWF,DataWIFI three)
    {
        try{
        //variables
        Location place=new Location();
        HelpFunctions hlp=new HelpFunctions();
        ArrayList<ALGOtwoCLASS> data=hlp.Find(DWF,three.getWiFi());
        place= hlp.WeightAlgo2(three.getWiFi(),data);
        if (place!=null)
            return place;
        return new Location();
        }catch(NullPointerException e){
            return new Location();
        }
    }
    /**
    * the function returns a place for row as comb_no_gps_ts1.csv according to the second algorithm 
    * @param DWF-database
    * @param rowTS-row as comb_no_gps_ts1.csv
    * @return location of row as comb_no_gps_ts1.csv
    */
    public Location RowTS(ArrayList<DataWIFI> DWF,String rowTS)
    {
        //variables
        Location place=new Location();
        HelpFunctions hlp=new HelpFunctions();
        String[] row=rowTS.split(",");
        if (row.length<10 || row.length>46)
            return place;
        DataWIFI tmpWIFI=new DataWIFI();
        int cnt=0;                              //variables for check MAC,Signal
        int max=0;                              //number of networks in the list
        boolean flagtime;                       //flag for check time
        //#WIFI network
        try{
            max=Integer.parseInt(row[5]);
        }catch(NumberFormatException e){
            System.out.println("Err: #WiFi network is no correct");	
            return place;
        }
        tmpWIFI.setLla(place);
        flagtime=tmpWIFI.setTIME(row[0]);
        //check if time is correct
        if (!flagtime)
            return place;
        tmpWIFI.setID(row[1]);
        //read WiFi data
        for (int i=0;i<max;cnt=0,i++)
        {
            WIFI tmpWF=new WIFI();
            try {
                cnt+=tmpWF.setMAC(row[7+i*4]);
                tmpWF.setSSID(row[6+i*4]);
                cnt+=tmpWF.setSignal((int) Double.parseDouble(row[9+i*4]));
                cnt+=tmpWF.setFrequency(Integer.parseInt(row[8+i*4]));
                if (cnt!=3)
                    continue;
                tmpWIFI.setWiFi(tmpWF);
            }catch(NumberFormatException e){
                System.out.println("Err,WIFI");
                continue;
            }
        }
        ArrayList<ALGOtwoCLASS> data=hlp.Find(DWF,tmpWIFI.getWiFi());
        place= hlp.WeightAlgo2(tmpWIFI.getWiFi(),data);
        if (place!=null)
            return place;
        return new Location();
    }
    /**
     * the function return filter
     * @return filter
     */
    public String ShowFilter()
    {
    	try{
    		 return rf.ReadShowFilter();
        }catch(NullPointerException e) {
        	return "file not found";
        }
    }
    /**
     * update database with filter
     * @param _filter for filtering database
     */
    public void WriteFilter(Filter _filter)
    {
        ArrayList<DataWIFI> dwf=ReadFunctions.ReadDataBase();
        Iterator<DataWIFI> it = dwf.iterator();
        while (it.hasNext())
        {
            DataWIFI tmp = it.next();
            if (!_filter.Compare(tmp))
                it.remove();
        }
        Clear();
        wf.WriteCSV(dwf);
        wf.WriteFilter(_filter);
    }
    /**
     * add, clear
     */
    public void WriteFilter()
    {
        Filter tmp=null;
        wf.WriteFilter(tmp);
    }
    /**
     * back to old database
     */
    public void UNDO()
    {
       wf.WriteClearCSV(rf.ReadUNDO());
       wf.WriteFilter(rf.ReadOldFilter());
    }
    /**
     * write old data to UNDO
     */
    public void WriteUNDO()
    {
        wf.WriteUNDO(ReadFunctions.ReadDataBase());
        wf.WriteOldFilter(rf.ReadShowFilter());
    }
    /**
     * the function return time of last modification for table
     * @param args  IP,port,user,password,database,table
     * @return time of last modification
     */
    public String TimeUpdate(String[] args)
    {
        return readSQL.TimeUPDATE(args);
    }
    /**
     * the function create database
     * @param path location where run application
     */
	public static void initDatabase(String path) {
		System.out.println(path);
		File dir = new File(path+"\\database");
		dir.mkdirs();
		pathDataBase=path+"\\database\\database.csv";
		rf=new ReadFunctions(path+"\\database");
		wf=new WriteFunctions(path+"\\database");
	}
}