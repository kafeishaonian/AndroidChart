package com.chart.client.chartmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public class KlineChartModel extends AbstractBaseModel implements Serializable {

    private String code;
    private String type;
    private int count;
    private int startid;
    private int endid;
    private int max;
    private int min;
    private Object prices;
    private Object id;
    private Object timetamp;
    private int forceRefresh;
    private Object dateTime;
    private Object dealCounts;
    private LastPriceInfoBean lastPriceInfo;
    private boolean closed;
    private List<String> time;
    private List<String> open;
    private List<String> close;
    private List<String> hi;
    private List<String> low;
    private List<String> ma5;
    private List<String> ma10;
    private List<String> ma15;
    private List<String> dif;
    private List<String> macd;
    private List<String> dea;
    private List<String> k;
    private List<String> d;
    private List<String> j;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartid() {
        return startid;
    }

    public void setStartid(int startid) {
        this.startid = startid;
    }

    public int getEndid() {
        return endid;
    }

    public void setEndid(int endid) {
        this.endid = endid;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Object getPrices() {
        return prices;
    }

    public void setPrices(Object prices) {
        this.prices = prices;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(Object timetamp) {
        this.timetamp = timetamp;
    }

    public int getForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(int forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    public Object getDateTime() {
        return dateTime;
    }

    public void setDateTime(Object dateTime) {
        this.dateTime = dateTime;
    }

    public Object getDealCounts() {
        return dealCounts;
    }

    public void setDealCounts(Object dealCounts) {
        this.dealCounts = dealCounts;
    }

    public LastPriceInfoBean getLastPriceInfo() {
        return lastPriceInfo;
    }

    public void setLastPriceInfo(LastPriceInfoBean lastPriceInfo) {
        this.lastPriceInfo = lastPriceInfo;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<String> getOpen() {
        return open;
    }

    public void setOpen(List<String> open) {
        this.open = open;
    }

    public List<String> getClose() {
        return close;
    }

    public void setClose(List<String> close) {
        this.close = close;
    }

    public List<String> getHi() {
        return hi;
    }

    public void setHi(List<String> hi) {
        this.hi = hi;
    }

    public List<String> getLow() {
        return low;
    }

    public void setLow(List<String> low) {
        this.low = low;
    }

    public List<String> getMa5() {
        return ma5;
    }

    public void setMa5(List<String> ma5) {
        this.ma5 = ma5;
    }

    public List<String> getMa10() {
        return ma10;
    }

    public void setMa10(List<String> ma10) {
        this.ma10 = ma10;
    }

    public List<String> getMa15() {
        return ma15;
    }

    public void setMa15(List<String> ma15) {
        this.ma15 = ma15;
    }

    public List<String> getDif() {
        return dif;
    }

    public void setDif(List<String> dif) {
        this.dif = dif;
    }

    public List<String> getMacd() {
        return macd;
    }

    public void setMacd(List<String> macd) {
        this.macd = macd;
    }

    public List<String> getDea() {
        return dea;
    }

    public void setDea(List<String> dea) {
        this.dea = dea;
    }

    public List<String> getK() {
        return k;
    }

    public void setK(List<String> k) {
        this.k = k;
    }

    public List<String> getD() {
        return d;
    }

    public void setD(List<String> d) {
        this.d = d;
    }

    public List<String> getJ() {
        return j;
    }

    public void setJ(List<String> j) {
        this.j = j;
    }

    public static class LastPriceInfoBean {
        /**
         * code : Ag
         * codeValue : 0
         * latestUpdate : 1522207940452
         * price : 3500
         * dir : 1
         * lastEnd : 3491
         * todayOpen : 3493
         * highest : 3505
         * lowest : 3492
         * closed : false
         */

        private String code;
        private int codeValue;
        private long latestUpdate;
        private int price;
        private int dir;
        private int lastEnd;
        private int todayOpen;
        private int highest;
        private int lowest;
        private boolean closed;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(int codeValue) {
            this.codeValue = codeValue;
        }

        public long getLatestUpdate() {
            return latestUpdate;
        }

        public void setLatestUpdate(long latestUpdate) {
            this.latestUpdate = latestUpdate;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getDir() {
            return dir;
        }

        public void setDir(int dir) {
            this.dir = dir;
        }

        public int getLastEnd() {
            return lastEnd;
        }

        public void setLastEnd(int lastEnd) {
            this.lastEnd = lastEnd;
        }

        public int getTodayOpen() {
            return todayOpen;
        }

        public void setTodayOpen(int todayOpen) {
            this.todayOpen = todayOpen;
        }

        public int getHighest() {
            return highest;
        }

        public void setHighest(int highest) {
            this.highest = highest;
        }

        public int getLowest() {
            return lowest;
        }

        public void setLowest(int lowest) {
            this.lowest = lowest;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }
    }


//    /**
//     * code : 0
//     * msg : 操作成功
//     * startIndex : 0
//     * total : 0
//     * pageSize : 30
//     * curPage : 0
//     * items : []
//     * item : {"
//     * totalPage : 0
//     */
//
//    /**
//     * closingPrice : 39.98
//     * datas :
//     */
//
//    private ItemBean item;
//
//    public ItemBean getItem() {
//        return item;
//    }
//
//    public void setItem(ItemBean item) {
//        this.item = item;
//    }
//
//    public static class ItemBean implements Serializable{
//        private double closingPrice;
//        private List<List<String>> datas;
//        private List<List<String>> baseLine;
//        private List<List<String>> lineExt;
//
//        public double getClosingPrice() {
//            return closingPrice;
//        }
//
//        public void setClosingPrice(double closingPrice) {
//            this.closingPrice = closingPrice;
//        }
//
//        public List<List<String>> getDatas() {
//            return datas;
//        }
//
//        public void setDatas(List<List<String>> datas) {
//            this.datas = datas;
//        }
//
//        public List<List<String>> getBaseLine() {
//            return baseLine;
//        }
//
//        public void setBaseLine(List<List<String>> baseLine) {
//            this.baseLine = baseLine;
//        }
//
//        public List<List<String>> getLineExt() {
//            return lineExt;
//        }
//
//        public void setLineExt(List<List<String>> lineExt) {
//            this.lineExt = lineExt;
//        }
//
//        @Override
//        public String toString() {
//            return "ItemBean{" +
//                    "closingPrice=" + closingPrice +
//                    ", datas=" + datas +
//                    ", baseLine=" + baseLine +
//                    ", lineExt=" + lineExt +
//                    '}';
//        }
//    }

}
