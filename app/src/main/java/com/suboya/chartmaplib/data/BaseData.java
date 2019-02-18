package com.suboya.chartmaplib.data;

import android.graphics.Path;
import android.graphics.Region;

import java.util.List;

public class BaseData {

    private String name;
    private int color;
    private int lineColor;
    private List<Path> listPath;
    private List<Region> regionList;
    private boolean isSelect;
    public boolean isSelect() {
        return isSelect;
    }
    public void setSelect(boolean select) {
        isSelect = select;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public List<Path> getListPath() {
        return listPath;
    }

    public void setListPath(List<Path> listPath) {
        this.listPath = listPath;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
