/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * 对每一个句子去停用词、去符号、去空格
 *  
 * ***/

public class FileExcludeStopWord{


	/**
	 * 1：按句子读取（按行） 
         * 2：<span style="font-family: Arial, Helvetica, sans-serif;">去标点去空格</span> 
         * 3：<span style="font-family: Arial, Helvetica, sans-serif;">去停用词（去“and”“to””he“....）</span>

	 * */
	public String doEx(String text) {

		// TODO 1：按句子读取（按行）
		// 读文件，文件是否存在
		List<String> stringList = new ArrayList<String>();
		List<String> stopWordsList = new ArrayList<String>();
		// 读取文件
		//File file = new File("/Users/zhangcong/dev/corpus/gvm_data.txt");
		File stopwords=new File("/Users/zhangcong/dev/corpus/StopWordTable2.txt");
                /*
		if (!file.exists()&&!stopwords.exists()){
			System.out.println("文件不存在");
			return;
		}*/
                
                String[] lines=text.toLowerCase().split("\n\r");
                for(String tl : lines){
                    stringList.add(tl);
                }
                /*
		// 循环，取一句，一行
		BufferedReader reader = null;
		try {
			//System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				tempString=tempString.toLowerCase();
				//System.out.println("line " + line + ": " + tempString);
				// 存入List<String>
				stringList.add(tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}*/

                
		
		
		
		// TODO 3：去停用词（去“and”“to””he“....）
		// 停用词 转化成 扫描用数组
		// 调用以前写好的代码过滤

		BufferedReader stops = null;
		try {
			String tempString = null;
			stops = new BufferedReader(new FileReader(stopwords));
			tempString = stops.readLine();
			while ((tempString = stops.readLine()) != null) {
				stopWordsList.add(tempString);
			}
			//如果你已经把停用词变成集合了,接下来对每个做循环作比较,但是现在是要去除字符串
			
			//用来保存去除后的集合
			List<String> tempStringList = new ArrayList<String>();
			for (String string : stringList) {//取出每一行句子，对每一句分别与每个停用词做replace
				for (String stopWord : stopWordsList) {//循环停用词
					string = string.replace(" "+stopWord+" ", " ");
				}
				tempStringList.add(string);
			}
			stringList = tempStringList;//新集合赋值回去
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
                // TODO 2：去标点去空格
		// 2.1.去掉英文标点符号后的字符串
		String[] firstSubStr = new String[] { "[", "]", ".", ",", ":", "\\",
				"/", "?", "!", ";", "\"", "'", "\n", "\r", "<", ">",
                                "="};
		// 循环List ,对每一个元素替换
		List<String> firstStringList = new ArrayList<String>();
		for (String contance : stringList) {
			// 替换
			for (String str : firstSubStr) {
				contance = contance.replace(str, ""); // 循环把英文标点符号替换成空，即去掉英文标点符号
			}
			firstStringList.add(" " + contance + " ");
		}
		stringList = firstStringList;
		
		
		// TODO 3：去标点去空格
                /*
		// 3.2.去除多余空格
		String[] secondSubStr = new String[] { " " };
		// 循环List ,对每一个元素替换
		List<String> secondStringList = new ArrayList<String>();
		for (String contance : stringList) {// 使用上一步生成的数据
			// 替换
			for (String str : secondSubStr) {
				contance = contance.replace(str, ""); // 循环把英文标点符号替换成空，即去掉英文标点符号
			}
			secondStringList.add(contance);
		}
		stringList = secondStringList;
                */
		
                List<String> allWords=new ArrayList<String>();
		// 把字符串变成字符数组，循环计数
		int num=0;
		for (String contance : stringList) {
                    String[] words=contance.split(" ");
                    for(String t : words){
                        allWords.add(t);
                    }
		}
                
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String w=i.next();
                    if(w==null || w.equals("") || w.equals(" ")){
                        i.remove();
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    for (String stopWord : stopWordsList) {
                        if(stopWord.equals(wd) || wd.length()<2){
                            i.remove();
                            break;
                        }
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    if(wd.startsWith("http")){
                        i.remove();
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    if(wd.endsWith("\'s")){
                        int index=allWords.indexOf(wd);
                        wd=wd.substring(0, wd.length()-3);
                        allWords.set(index, wd);
                    }
                }
                
                String[] tweetStr = new String[] { "#", "@", "(", ")"};
		// 循环List ,对每一个元素替换
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    int index=allWords.indexOf(wd);
                    for (String str : tweetStr) {
                        wd = wd.replace(str, "");
		    }
                    allWords.set(index, wd);
                }
                
                StringBuffer sb=new StringBuffer();
                for (String t : allWords) {
                    sb.append(" "+t);
		}
                System.out.println("Words are "+ sb.toString());
                return sb.toString();
                
	}
        
        public String doExForTweet(String text) {

		Set<String> stopWordsList = new CopyOnWriteArraySet<String>();
		// 读取文件
		File stopwords=new File("/Users/zhangcong/dev/corpus/StopWordTable2.txt");
		BufferedReader stops = null;
		try {
                    String tempString = null;
                    stops = new BufferedReader(new FileReader(stopwords));
                    tempString = stops.readLine();
                    while ((tempString = stops.readLine()) != null) {
                        stopWordsList.add(tempString.toLowerCase().trim());
                    }
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
                List<String> allWords=new ArrayList<String>();
                
                String[] lines=text.toLowerCase().split("\n");
                for(String tl : lines){
                    String[] stringList=tl.split(" ");
                    for (String t : stringList) {
                        allWords.add(t);
                    }
                }
		
                
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String w=i.next();
                    if(w==null || w.equals("") || w.equals(" ")){
                        i.remove();
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    for (String stopWord : stopWordsList) {
                        if(stopWord.equals(wd) || wd.length()<2){
                            i.remove();
                            break;
                        }
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    if(wd.startsWith("http")){
                        i.remove();
                    }
                }
                
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    if(wd.endsWith("\'s") || wd.endsWith("’s")){
                        int index=allWords.indexOf(wd);
                        wd=wd.substring(0, wd.length()-2);
                        allWords.set(index, wd);
                    }
                }
                
                String[] tweetStr = new String[] { "[", "]", ".", ",", ":", "\\",
				"/", "?", "!", ";", "\"", "'", "\n", "\r", "<", ">",
                                "=", "#", "@", "(", ")", "*", "‘", "’", "“","”"};
		// 循环List ,对每一个元素替换
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    int index=allWords.indexOf(wd);
                    for (String str : tweetStr) {
                        wd = wd.replace(str, "");
		    }
                    allWords.set(index, wd);
                }
                
                //再过一遍停用词
                for(Iterator<String> i=allWords.iterator();i.hasNext();){
                    String wd=i.next();
                    for (String stopWord : stopWordsList) {
                        if(stopWord.equals(wd) || wd.length()<2){
                            i.remove();
                            break;
                        }
                    }
                }
                
                StringBuffer sb=new StringBuffer();
                for (String t : allWords) {
                    sb.append(" "+t);
		}
                System.out.println("Words are "+ sb.toString());
                return sb.toString();
                
	}
}
