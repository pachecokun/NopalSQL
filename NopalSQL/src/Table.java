
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class Table {
    String name;
    Class c;
    LinkedList<Object> rows = new LinkedList<>();
    String[] cols;
    
    public static void main(String[] args) {
        Table t = new Table("ola", new String[][]{
            new String[]{"int","edad"},
            new String[]{"char","genero"},
            new String[]{"float","estatura"}
        });
        
        try{
            t.insertRow(new String[][]{
                new String[]{"edad","20"},
                new String[]{"genero","H"},
                new String[]{"estatura","1.70"}
            });
            t.insertRow(new String[][]{
                new String[]{"edad","18"},
                new String[]{"genero","M"},
                new String[]{"estatura","1.50"}
            });
            
            t.update(new String[][]{
                new String[]{"genero","M"},
                new String[]{"edad","30"},
            }, "genero", "H");
            
            int del = t.delete("genero", "H");
            
            String[][][] rows = t.select("genero", "H");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Table(String name,String[][]fields){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("public class "+name+" {");
        cols = new String[fields.length];
        
        for(int i=0;i<fields.length;i++){
            String[] field = fields[i];
            cols[i] = field[0];
            field[1] = field[1].toLowerCase();
            out.println("private "+field[0]+" "+field[1]+";");
            out.println("public void set"+field[1]+"("+field[0]+" value){this."+field[1]+" = value;}");
            out.println("public "+field[0]+" get"+field[1]+"(){return this."+field[1]+";}");
        }
        out.println("}");
        out.close();
        JavaFileObject file = new JavaSourceFromString(name, writer.toString());
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        for(Diagnostic diagnostic : diagnostics.getDiagnostics()) {
          System.out.println(diagnostic.getCode());
          System.out.println(diagnostic.getKind());
          System.out.println(diagnostic.getPosition());
          System.out.println(diagnostic.getStartPosition());
          System.out.println(diagnostic.getEndPosition());
          System.out.println(diagnostic.getSource());
          System.out.println(diagnostic.getMessage(null));

        }
        System.out.println("Success: " + success);
        
        if(success){
            try {

                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
                c = Class.forName(name, true, classLoader);
                
            }catch(Exception e){e.printStackTrace();}
        }
    }
    
    
    public void insertRow(String[][] fields) throws Exception{
        
        Object o = c.newInstance();
         
        set(o,fields);
        
        rows.add(o);
    }
    
    public Object[] where(String field,String value) throws Exception{        
        if(field==null && value==null){
            return rows.toArray(new Object[0]);
        }
        
        ArrayList<Object> res = new ArrayList<>();
        
        for(Object o:rows){
            for(Method m:c.getDeclaredMethods()){
                if(m.getName().equals("get"+field)){
                    Class type = m.getReturnType();
                    
                    if(type==int.class){
                        Integer data = (Integer)m.invoke(o);
                        if(data==Integer.parseInt(value))
                        res.add(o);
                    }
                    else if(type==float.class){
                        Float data = (Float)m.invoke(o);
                        if(data == Float.parseFloat(value))
                        res.add(o);
                    }
                    else if(type==long.class){
                        Long data = (Long)m.invoke(o);
                        if(data == Long.parseLong(value))
                        res.add(o);
                    }
                    else if(type==boolean.class){
                        Boolean data = (Boolean)m.invoke(o);
                        if(data==Boolean.parseBoolean(value))
                        res.add(o);
                    }
                    else if(type==byte.class){
                        Byte data = (Byte)m.invoke(o);
                        if(data == Byte.parseByte(value))
                        res.add(o);
                    }
                    else if(type==char.class){
                        Character data = (Character)m.invoke(o);
                        if(data==value.charAt(0))
                        res.add(o);
                    }
                }
            }
        }
               
        
        return res.toArray();
    }
    
    public String[][] rowToString(Object o)throws Exception{
        ArrayList<String[]> res = new ArrayList<>();
        
        for(Method m:c.getDeclaredMethods()){
            if(m.getName().startsWith("get")){
                String[] field = new String[]{m.getName().substring(3),""+m.invoke(o)};
                res.add(field);
            }
        }
        return res.toArray(new String[0][0]);
    }
    
    public String[][][] rowsToString(Object[] rows) throws Exception{
        String[][][] res= new String[rows.length][][];
        for(int i=0;i<rows.length;i++){
            res[i] = rowToString(rows[i]);
        }
        return res;
    }
    
    public String[][][] select(String field,String value) throws Exception{
        return rowsToString(where(field,value));
    }
    
    public int delete(String field,String value) throws Exception{
        Object[] res = where(field,value);
        for(Object o:res){
            rows.remove(o);
        }
        return res.length;
    }
    
    public void set(Object o,String[][]fields) throws Exception{
        for(String[] f:fields){
            
            for(Method m:c.getDeclaredMethods()){
                if(m.getName().equals("set"+f[0])){
                    Class type = m.getParameterTypes()[0];
                    
                    if(type==int.class){
                        Integer data = Integer.parseInt(f[1]);
                        m.invoke(o,data);
                    }
                    else if(type==float.class){
                        Float data = Float.parseFloat(f[1]);
                        m.invoke(o,data);
                    }
                    else if(type==long.class){
                        Long data = Long.parseLong(f[1]);
                        m.invoke(o,data);
                    }
                    else if(type==boolean.class){
                        Boolean data = Boolean.parseBoolean(f[1]);
                        m.invoke(o,data);
                    }
                    else if(type==byte.class){
                        Byte data = Byte.parseByte(f[1]);
                        m.invoke(o,data);
                    }
                    else if(type==char.class){
                        Character data = f[1].charAt(0);
                        m.invoke(o,data);
                    }
                    
                }
            }
        }
    }
    
    public int update(String[][]fields,String field,String value) throws Exception{
        Object[] res = where(field,value);
        
        for(int i=0;i<res.length;i++){
            Object o = res[i];
            set(o,fields);
            rows.set(rows.indexOf(res[i]), o);
        }
        return res.length;
    }
    
}

class JavaSourceFromString extends SimpleJavaFileObject {
  final String code;

  JavaSourceFromString(String name, String code) {
    super(URI.create("string:///" + name.replace('.','/') + JavaFileObject.Kind.SOURCE.extension),JavaFileObject.Kind.SOURCE);
    this.code = code;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return code;
  }
}