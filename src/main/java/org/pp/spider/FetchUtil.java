package org.pp.spider;

import base.GLog;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pp.http.client.HttpUtil;
import org.pp.model.LotteryDraw;
import org.pp.util.JacksonUtil;
import org.pp.util.NumberUtil;
import practice.util.DataUtils;
import practice.util.json.JsonUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-17
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
public class FetchUtil {
    public static final List<Map<String, Object>> issueList = new ArrayList<>(40);
    private static final String url = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&pageSize=30&isVerify=1&pageNo=1";
    private static final String urlForAll = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&pageSize=300&isVerify=1&pageNo=1";
    //    private static final String url = "http://chart.lottery.gov.cn//dltBasicZongHeTongJi.do?typ=3&issueTop=15&param=0";
    private static final String url2 = "http://chart.lottery.gov.cn//dltBasicZongHeTongJi.do?typ=3&issueTop=5000&param=0";
    static String tzkj = "https://www.lottery.gov.cn/tz_kj.json";
    private static String str = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&pageSize=800&isVerify=1&pageNo=1&startTerm=%s&endTerm=%s";
    private static List<int[]> result = new ArrayList<>();

    public static List<int[]> updateAll() {
        GLog.logger.info("抓取体彩中心数据 start...");
        Connection con = Jsoup.connect(url2);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
//        开奖日期 期号 开奖号码 和数值 跨度值 最大号码 最小号码 和尾值 平均值 合跨和 合跨差 尾数和值 连号个数 连号组数 尾数组数
        // content fromIndex = 15  tailIndex = 27
        String[] array0 = str.split(" |-");
        int len = array0.length - 15 - 27;
        String[] array = new String[len];
        System.arraycopy(array0, 15, array, 0, len);
        int mod = 3;
        List<int[]> list = new ArrayList<>();
        for (int i = 1; i < len; i++) {
            int tempMod = i % 23;
            if (tempMod == mod) {
                int[] intArr = new int[8];
                intArr[0] = DataUtils.asInt(array[i].substring(2, 7));
                for (int j = 1; j < 8; j++) {
                    intArr[j] = DataUtils.asInt(array[i + j]);
                }
                list.add(intArr);
            }
        }
        setResult(list);
        GLog.logger.info("抓取体彩中心数据 end...");
        return list;
    }

    public static List<int[]> update() {
        GLog.logger.info("抓取体彩中心数据 start...");
        Connection con = Jsoup.connect(url);
        con.ignoreContentType(true);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
//        开奖日期 期号 开奖号码 和数值 跨度值 最大号码 最小号码 和尾值 平均值 合跨和 合跨差 尾数和值 连号个数 连号组数 尾数组数
        // content fromIndex = 15  tailIndex = 27
        String[] array0 = str.split(" |-");
        int len = array0.length - 15 - 27;
        String[] array = new String[len];
        System.arraycopy(array0, 15, array, 0, len);
        int mod = 3;
        List<int[]> list = new ArrayList<>();
        for (int i = 1; i < len; i++) {
            int tempMod = i % 23;
            if (tempMod == mod) {
                int[] intArr = new int[8];
                intArr[0] = DataUtils.asInt(array[i].substring(2, 7));
                for (int j = 1; j < 8; j++) {
                    intArr[j] = DataUtils.asInt(array[i + j]);
                }
                list.add(intArr);
            }
        }
        setResult(list);
        GLog.logger.info("抓取体彩中心数据 end...");
        return list;
    }

    public static List<Map<String, Object>> update2() {
        GLog.logger.info("抓取体彩中心数据 start...");
        Connection con = Jsoup.connect(url);
        con.ignoreContentType(true);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
        Map<String, Object> map = JsonUtil.unSerializableMap(String.class, Object.class, str);
        issueList.addAll((List<Map<String, Object>>) ((Map) map.get("value")).get("list"));
        GLog.logger.info("抓取体彩中心数据 end...");
        return issueList;
    }

    public static List<Map<String, Object>> update3() {
        List<String> lines = FileUtil.readLines(NumberUtil.getClassPath() + "lotteryDraw", "UTF8");
        String s1 = lines.get(lines.size() - 1);
        String lotteryDrawNum = s1.split(" ")[0];
        String s = HttpUtil.get(tzkj);
        List<Map<String, Object>> maps = JacksonUtil.jsonToMapList(s);
        String term = (String) ((Map) maps.get(0).get("dlt")).get("term");
        String realUrl = String.format(str, lotteryDrawNum, term);
        String body = HttpUtil.get(realUrl);
        Map<String, Object> map = JacksonUtil.toMap(body, String.class, Object.class);
        List<Map<String, Object>> dataList = (List) ((Map) map.get("value")).get("list");
        List<LotteryDraw> list = new ArrayList<>(dataList.size());
        for (Map<String, Object> map1 : dataList) {
            LotteryDraw lotteryDraw = new LotteryDraw(map1.get("lotteryDrawNum").toString(),
                    map1.get("lotteryDrawResult").toString());
            list.add(lotteryDraw);
        }
        FileUtil.appendLines(list, NumberUtil.getClassPath() + "lotteryDraw", "UTF8");
        return dataList;
    }

    public static List<LotteryDraw> getAllNumber2() {
        GLog.logger.info("获取全部开奖数据 start...");
        String[] arr = {"07001", "12001", "17001", "22001", "27001", "32001", "37001"};
        String s = String.valueOf(LocalDate.now().getYear()).substring(2, 4) + "001";
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1 || Integer.parseInt(s) < Integer.parseInt(arr[i])) {
                break;
            }
            // 请求获取开奖数据
            String format = String.format(str, arr[i], arr[i + 1]);
            String body = HttpUtil.get(format);
            // 得到开奖数据中 彩票期号和中奖数据
            System.out.println(new DateTime());
            Map<String, Object> map = JacksonUtil.toMap(body, String.class, Object.class);
            List<Map> dataList = (List) ((Map) map.get("value")).get("list");
            dataList.remove(0);
            List<LotteryDraw> list = new ArrayList<>(dataList.size());
            for (Map<String, Object> map1 : dataList) {
                LotteryDraw lotteryDraw = new LotteryDraw(map1.get("lotteryDrawNum").toString(),
                        map1.get("lotteryDrawResult").toString(),
                        map1.get("lotteryDrawTime").toString());
                list.add(lotteryDraw);
            }
            Collections.sort(list, Comparator.comparing(LotteryDraw::getLotteryDrawNum));
            System.out.println(new DateTime());
            // 开奖数据写入文件
//            String fileName = String.format("%s-%s", arr[i], arr[i + 1]);
//            File file = FileUtil.writeString(body, new File(classpath + fileName), "UTF8");
            FileUtil.appendLines(list, NumberUtil.getClassPath() + "lotteryDraw", "UTF8");
        }
        GLog.logger.info("获取全部开奖数据 end...");
        return null;
    }

    public static List<LotteryDraw> getAllNumber() {
        GLog.logger.info("获取全部开奖数据 start...");
        Connection con = Jsoup.connect(urlForAll);
        con.ignoreContentType(true);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
            IOUtils.writeChunked(rs.bodyAsBytes(), new FileOutputStream(NumberUtil.getClassPath() + "number.json"));
        } catch (IOException e) {
            throw new RuntimeException("全部开奖数据写入文件出错...");
        }
        Map<String, Object> map = JsonUtil.unSerializableMap(String.class, Object.class, rs.body());
        List<Map<String, Object>> maps = (List<Map<String, Object>>) ((Map) map.get("value")).get("list");
        List<LotteryDraw> list = new ArrayList<>(maps.size());
        for (Map<String, Object> map1 : maps) {
            LotteryDraw lotteryDraw = new LotteryDraw(map1.get("lotteryDrawNum").toString(),
                    map1.get("lotteryDrawResult").toString(),
                    map1.get("lotteryDrawTime").toString());
            list.add(lotteryDraw);
        }
        Collections.sort(list, new Comparator<LotteryDraw>() {
            @Override
            public int compare(LotteryDraw o1, LotteryDraw o2) {
                return Integer.parseInt(o1.getLotteryDrawNum()) - Integer.parseInt(o2.getLotteryDrawNum());
            }
        });

        GLog.logger.info("获取全部开奖数据 end...");
        return list;
    }

    public static List<int[]> getResult() {
        return result;
    }

    public static void setResult(List<int[]> result) {
        FetchUtil.result = new ArrayList<>();
    }

    public static void main(String[] args) {
        getAllNumber2();
    }
}
