
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class Database {
    private String name;
    
    HashMap<String, Table> tables = new HashMap<>();
    
    public Database(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public String[] showTables(){
        return tables.keySet().toArray(new String[0]);
    }
    
    public void createTable(String name,String[][]fields){
        tables.put(name, new Table(name,fields));
    }
    
    public void dropTable(String name){
        tables.remove(name);
    }
    
    public void insert(String name,String[] values) throws Exception{
        Table t = tables.get(name);
        String[][]fields = new String[t.cols.length][];
        for(int i = 0;i<fields.length;i++){
            fields[i] = new String[]{t.cols[i],values[i]};
        }
        t.insertRow(fields);
    }
    
    public int update(String name,String[][]fields,String field,String value)throws Exception{
        Table t = tables.get(name);
        return t.update(fields, field, value);
    }
    
    public String[][][] select(String name,String[] fields,String field, String value) throws Exception{
        Table t = tables.get(name);
        return t.select(fields,field, value);
    }
    
    public int delete(String name,String field,String value) throws Exception{
        Table t = tables.get(name);
        return t.delete(field, value);
    }
    
}
