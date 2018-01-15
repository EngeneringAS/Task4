/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ex4;
//libraries
import Ex1.DataWIFI;
import Ex1.Location;
import Ex1.WIFI;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * read SQL from Boaz database for Assignment 4
 * @author Alexey Titov   and   Shalom Weinberger
 * @version 1
 */
public class readSQL {
    //global variables
    private static String _ip;
    private static String _url;
    private static String _user;
    private static String _password;
    private static Connection _con;
    /**
     * the function return data
     * @param args IP,port, user, password,database,table
     * @return data from sql server
     */
    public static ArrayList<DataWIFI> Connect(String[] args) 
    {
        ArrayList<DataWIFI> dwf_list=new ArrayList<>();
        try{
            _ip=args[0];
            _url="jdbc:mysql://"+_ip+":"+args[1]+"/"+args[4];
            _user=args[2];
            _password=args[3];
            _con = null;
            dwf_list=test_ex4_db(args);
        }catch(Exception e){
            return null;
        }
        return dwf_list;
    }
    /**
     * the function connet to sql database and read data
     * @param args IP,port, user, password,database,table
     * @return data from data base
     */
    @SuppressWarnings("resource")
	public static ArrayList<DataWIFI> test_ex4_db(String[] args)
    {
        //variables
        Statement st = null;
        ResultSet rs = null;
        ArrayList<DataWIFI> tmp=new ArrayList<>();
        try {     
            //connect to SQL database
            _con = DriverManager.getConnection(_url, _user, _password);
            st = _con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
            //print version
            if (rs.next()){
                System.out.println(rs.getString(1));
            }
            //command select all columns
            PreparedStatement pst = _con.prepareStatement("SELECT * FROM "+args[5]);
            rs = pst.executeQuery();
            //read data 
            while (rs.next()) 
            {
                //number columns
                DataWIFI dwf=new DataWIFI();
                Location lla=new Location();
                int size = rs.getInt(7);
                int len = 7+2*size;
                dwf.setTIME(rs.getString(2));
                dwf.setID(rs.getString(3));
                lla.setLat(Double.parseDouble(rs.getString(4)));
                lla.setLon(Double.parseDouble(rs.getString(5)));
                lla.setAlt(Double.parseDouble(rs.getString(6)));
                dwf.setLla(lla);
                for(int i=8;i<=len;i+=2)
                {
                    WIFI wf=new WIFI();
                    wf.setMAC(rs.getString(i));
                    wf.setSignal(Integer.parseInt(rs.getString(i+1)));
                    dwf.setWiFi(wf);
                }
                tmp.add(dwf);
            }
        }catch (SQLException ex){
            Logger lgr = Logger.getLogger(readSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }finally {
            try {
                if (rs != null) 
                    {rs.close();}
                if (st != null) 
                    { st.close();}
                if (_con != null) 
                    { _con.close();}
            }catch(SQLException ex){
                Logger lgr = Logger.getLogger(readSQL.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return tmp;
    }
    /**
     * the function return time of last modification
     * @param args  IP,port,user,password,database,table
     * @return time of last modification
     */
    @SuppressWarnings("resource")
	public static String TimeUPDATE(String[] args)
    {
        //variables
        Statement st = null;
        ResultSet rs = null;
        try{
            _ip=args[0];
            _url="jdbc:mysql://"+_ip+":"+args[1]+"/"+args[4];
            _user=args[2];
            _password=args[3];
            _con = null;
        }catch(NullPointerException e){
            return null;
        }
        String answer="null";
        try {     
            _con = DriverManager.getConnection(_url, _user, _password);
            st = _con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
            //print version
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
            PreparedStatement pst = _con.prepareStatement("SELECT UPDATE_TIME\n" +
                                                          "FROM   INFORMATION_SCHEMA.TABLES\n" +
                                                          "WHERE  TABLE_SCHEMA = '"+args[4]+"'\n" +
                                                          "AND    TABLE_NAME = '"+args[5]+"'");
            rs = pst.executeQuery();
            if (rs.next()) {
                answer=rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(readSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) 
                    {rs.close();}
                if (st != null) 
                    { st.close(); }
                if (_con != null) 
                    { _con.close();  }
            } catch (SQLException ex) {
                
                Logger lgr = Logger.getLogger(readSQL.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return answer;
    }
}