package com.suboya.chartmaplib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;

import com.suboya.chartmaplib.data.BaseData;
import com.suboya.chartmaplib.data.ChartData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SvgUtil {
    private String path;
    private Context context;
    private float Max_X,Min_x,Max_y,Min_y;
    public SvgUtil(String path, Context context){
        this.context=context;
        this.path=path;
    }
    public SvgUtil(Context context){
        this.context=context;
    }
    public ChartData getProvinces(){
        ChartData map=new ChartData();
        try {
            InputStream inputStream= context.getResources().getAssets().open("china.svg");
            DocumentBuilder mybuilder= DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document myDoc=mybuilder.parse(inputStream);
            //找到根Element
            Element root=myDoc.getDocumentElement();
            NodeList items1 = root.getElementsByTagName("g");
            Element groot=(Element)items1.item(0);
            NodeList items2 = groot.getElementsByTagName("path");
            //遍历每一个省份
            if (items2.getLength()>0){
                List<BaseData> list=new ArrayList<>();
                SvgPathToAndroidPath svg=new SvgPathToAndroidPath();
                for (int i=0;i<items2.getLength();i++){
                    BaseData provinceModel =new BaseData();
                    Element ele_Province=(Element)items2.item(i);
                    String PathPoints=ele_Province.getAttribute("d");
                    String name=ele_Province.getAttribute("title");
                    List<Path> listpath=new ArrayList<>();
                    //拿到每个省的path集合
                    String s[]=PathPoints.split("z");
                    for(String ss:s){
                        ss+="z";
                        listpath.add(svg.parsePath(ss));
//                        listpath.add(PathParser.createPathFromPathData(ss));
                    }
                    //拿到name和path
                    provinceModel.setName(name);
                    provinceModel.setListPath(listpath);
                    provinceModel.setColor(Color.WHITE);
                    provinceModel.setLineColor(Color.GRAY);
                    if (svg.getMax_X()>=Max_X){
                        Max_X=svg.getMax_X();
                    }
                    if (svg.getMax_Y()>=Max_y){
                        Max_y=svg.getMax_Y();
                    }
                    if (svg.getMin_X()<=Min_x){
                        Min_x=svg.getMin_X();
                    }
                    if (svg.getMin_Y()<=Min_y){
                        Min_y=svg.getMin_Y();
                    }
                    list.add(provinceModel);
                }
                map.setChartData(list);
                map.setMax_x(Max_X);
                map.setMax_y(Max_y);
                map.setMin_x(Min_x);
                map.setMin_y(Min_y);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

}