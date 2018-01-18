/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.Ex4;
//libraries
import Ex1.*;
import Ex4.readSQL;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * test for readSQL class
 * @author Alexey Titov   and   Shalom Weinberger
 * @version 1
 */
public class readSQLTest {
    /**
     * Test of Connect method, of class readSQL.
     */
    @Test
    public void testConnect() {
        String[] args = null;
        ArrayList<DataWIFI> expResult = null;
        ArrayList<DataWIFI> result = readSQL.Connect(args);
        assertEquals(expResult, result);
    }
    /**
     * Test of test_ex4_db method, of class readSQL.
     */
    @Test
    public void testTest_ex4_db() {
        ArrayList<DataWIFI> result = readSQL.test_ex4_db(null);
        System.out.println(result.toString());
        assertEquals(new ArrayList<>(), result);
    }
    /**
     * Test of TimeUPDATE method, of class readSQL.
     */
    @Test
    public void testTimeUPDATE() {
        String[] args = null;
        String result = readSQL.TimeUPDATE(args);
        assertEquals(null, result);
    }
    
}
