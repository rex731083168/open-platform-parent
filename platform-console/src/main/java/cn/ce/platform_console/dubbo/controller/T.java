package cn.ce.platform_console.dubbo.controller;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.ce.dfs.DfsFolderClient;
import cn.ce.dfs.thrift.HandleType;


public class T {

    public static void main(String[] args) {
        
      /*  if(true) {
            boolean b = Parameters.readConfig("F:\\JavaProjects\\DfsFolderClient\\src\\main\\java\\cn\\ce\\dfs\\zookeeper.properties");
            System.out.println("读取结果：" + b);
            System.out.println(Parameters.getParameter("FastDfsProjRootNode", "XD"));
            b = Parameters.readConfig("F:\\JavaProjects\\DfsFolderClient\\src\\main\\java\\cn\\ce\\dfs\\zookeeper.properties");
            System.out.println("读取结果：" + b);
            System.out.println(Parameters.getParameter("FastDfsProjRootNode", "XD"));
            b = Parameters.readConfig("F:\\JavaProjects\\DfsFolderClient\\src\\main\\java\\cn\\ce\\dfs\\zookeeper.properties");
            System.out.println("读取结果：" + b);
            System.out.println(Parameters.getParameter("FastDfsProjRootNode", "XD"));
            return;
        }*/
        
        //获取文件操作客户端引用 
        //DfsClient c = new DfsClient();
        DfsFolderClient c = new DfsFolderClient();
        try {
            //初始化读取配置文件的内容
            String p = DfsFolderClient.class.getResource("/").getPath();
            System.out.println(p);
            p = p + "zookeeper.properties";
            System.out.println(p);
            System.out.println();
            boolean b = true;
            byte[] bts  = null;
            if (args.length == 1) {
                bts  = toByteArray("/a.txt");
                b = c.init(p);
            } else {
                bts  = toByteArray("E:\\shuoming.txt");
                b = c.init("C:\\Users\\Administrator\\Desktop\\dfs\\DfsFolderClient\\src\\main\\java\\cn\\ce\\dfs\\zookeeper.properties");
            }
   
            System.out.println("ret is ：" + b);     
            System.out.println();
    
            System.out.println("output result here~!");
            for(int t= 0 ; t<1; ++t) {
                Thread.sleep(1500);
                System.out.println("=======>> *******************" + t);
                try {
                List<String> lt = c.saveFile("/abc/bbb.png", bts, HandleType.AUOT_RENAME);
                for(int i=0;i<lt.size();++i) {
                    System.out.println("------->> " + lt.get(i));
                } } catch(Exception ex) {
                    System.out.println("----------------------");
                    ex.printStackTrace();
                    /** retry to connect again!! */
                    try {
                    c.init("F:\\JavaProjects\\DfsFolderClient\\src\\main\\java\\cn\\ce\\dfs\\zookeeper.properties");
                    } catch(Exception e1sg) {}
                }
                
            }
      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //关闭连接的客户端            
            System.out.println();
            System.out.println("====关闭客户端的连接====");
            c.close(); 
        }
        
        
        
        
        //调用方法完成
        System.out.println("============== END ===============");
    }

    /**
     * 
     * @Title: toByteArray
     * @Description: 读取本地磁盘文件并转换为字节数组
     * @param filename 指定的本地磁盘名称
     * @throws IOException 抛出的异常信息
     * @return byte[]  返回字节数组对象
     * @throws
     */
    public static byte[] toByteArray(String filename) throws IOException{    
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
        BufferedInputStream in = null;  
        try{  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer,0,buf_size))){  
                bos.write(buffer,0,len);  
            }  
            return bos.toByteArray();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                in.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        }  
    } 
    
}
