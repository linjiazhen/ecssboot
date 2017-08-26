package com.wintoo.model;
import java.util.List;

public class LeakChart {

	private List<Double> totalin;//总进水
    private List<Double> totalout;//总出水
	private List<Double> leak;//漏水
	private List<Double> nocal;//未统计

	private List<String> datatime;//数据时间格式先统一为 YYYY/MM/DD H24:MI，具体需要什么的可以在底层或者js展示时截取

    public void setDatatime(List<String> datatime) {
        this.datatime = datatime;
    }

    public void setLeak(List<Double> leak) {
        this.leak = leak;
    }

    public void setNocal(List<Double> nocal) {
        this.nocal = nocal;
    }

    public void setTotalin(List<Double> totalin) {
        this.totalin = totalin;
    }

    public void setTotalout(List<Double> totalout) {
        this.totalout = totalout;
    }

    public List<Double> getLeak() {
        return leak;
    }

    public List<Double> getNocal() {
        return nocal;
    }

    public List<String> getDatatime() {
        return datatime;
    }

    public List<Double> getTotalin() {
        return totalin;
    }

    public List<Double> getTotalout() {
        return totalout;
    }
}

