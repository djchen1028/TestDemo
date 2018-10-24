package com.bc;

import java.util.Iterator;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class myCrawler {
    public static int countDocumentElements(DomElement domElement){
            int count=0;
            System.out.println(domElement);
            Iterator<DomElement> iteratorElements = domElement.getChildElements().iterator();//获取domElement的子节点
            while (iteratorElements.hasNext()){
                DomElement next = iteratorElements.next();
                count = count +countDocumentElements(next);//递归统计标签数
            }
            return count+1;
    }

    public static void main(String[] args) throws Exception {
        int sumLables = 0; //统计标签数量
        String baiduUrl = "https://www.baidu.com";

        WebClient webClient = new WebClient();//模拟浏览器
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(50000);
            HtmlPage htmlPage = webClient.getPage(baiduUrl);//模拟打开百度页面

            webClient.waitForBackgroundJavaScript(10000);//等待10秒加载JavaScript
            webClient.close();
            sumLables = myCrawler.countDocumentElements(htmlPage.getDocumentElement());//统计标签总数.
            System.out.println("标签数"+sumLables);
//                while((buffer = br.readLine())!=null){
//                    pw.println(buffer);
//                    //System.out.println(buffer);
//                    while(buffer.indexOf("<")!=-1){//获取每次读取页面的标签数量累加
//                        if(buffer.startsWith("//")==true){
//                            break;
//                        }
//                        int i = buffer.indexOf("<");
//                        labels++;
//                        buffer=buffer.substring(i+1);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            } finally {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                pw.close();
//                System.out.print(labels/2);
//            }
    }
}
