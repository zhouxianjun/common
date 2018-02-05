package com.alone.common.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2016/3/21 15:55
 */
public class CheckIdCardUtils {

    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 每位加权因子 */
    public static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
            10, 5, 8, 4, 2 };

    /** 第18位校检码 */
    public static final String verifyCode[] = { "1", "0", "X", "9", "8", "7",
            "6", "5", "4", "3", "2" };
    /** 最低年限 */
    public static final int MIN = 1930;
    public static Map<String, String> provinceCodes = new HashMap<String, String>();
    public static Map<String, String> cityCodes = new HashMap<String, String>();
    static {
        provinceCodes.put("11", "北京");
        provinceCodes.put("12", "天津");
        provinceCodes.put("13", "河北");
        provinceCodes.put("14", "山西");
        provinceCodes.put("15", "内蒙古");
        provinceCodes.put("21", "辽宁");
        provinceCodes.put("22", "吉林");
        provinceCodes.put("23", "黑龙江");
        provinceCodes.put("31", "上海");
        provinceCodes.put("32", "江苏");
        provinceCodes.put("33", "浙江");
        provinceCodes.put("34", "安徽");
        provinceCodes.put("35", "福建");
        provinceCodes.put("36", "江西");
        provinceCodes.put("37", "山东");
        provinceCodes.put("41", "河南");
        provinceCodes.put("42", "湖北");
        provinceCodes.put("43", "湖南");
        provinceCodes.put("44", "广东");
        provinceCodes.put("45", "广西");
        provinceCodes.put("46", "海南");
        provinceCodes.put("50", "重庆");
        provinceCodes.put("51", "四川");
        provinceCodes.put("52", "贵州");
        provinceCodes.put("53", "云南");
        provinceCodes.put("54", "西藏");
        provinceCodes.put("61", "陕西");
        provinceCodes.put("62", "甘肃");
        provinceCodes.put("63", "青海");
        provinceCodes.put("64", "宁夏");
        provinceCodes.put("65", "新疆");
        provinceCodes.put("71", "台湾");
        provinceCodes.put("81", "香港");
        provinceCodes.put("82", "澳门");
        provinceCodes.put("91", "国外");

        cityCodes.put("1301", "石家庄市");
        cityCodes.put("1302", "唐山市");
        cityCodes.put("1303", "秦皇岛市");
        cityCodes.put("1304", "邯郸市");
        cityCodes.put("1305", "邢台市");
        cityCodes.put("1306", "保定市");
        cityCodes.put("1307", "张家口市");
        cityCodes.put("1308", "承德市");
        cityCodes.put("1309", "沧州市");
        cityCodes.put("1310", "廊坊市");
        cityCodes.put("1311", "衡水市");
        cityCodes.put("1401", "太原市");
        cityCodes.put("1402", "大同市");
        cityCodes.put("1403", "阳泉市");
        cityCodes.put("1404", "长治市");
        cityCodes.put("1405", "晋城市");
        cityCodes.put("1406", "朔州市");
        cityCodes.put("1407", "晋中市");
        cityCodes.put("1408", "运城市");
        cityCodes.put("1409", "忻州市");
        cityCodes.put("1410", "临汾市");
        cityCodes.put("1411", "吕梁市");
        cityCodes.put("1501", "呼和浩特市");
        cityCodes.put("1502", "包头市");
        cityCodes.put("1503", "乌海市");
        cityCodes.put("1504", "赤峰市");
        cityCodes.put("1505", "通辽市");
        cityCodes.put("1506", "鄂尔多斯市");
        cityCodes.put("1507", "呼伦贝尔市");
        cityCodes.put("1508", "巴彦淖尔市");
        cityCodes.put("1509", "乌兰察布市");
        cityCodes.put("1522", "兴安盟");
        cityCodes.put("1525", "锡林郭勒盟");
        cityCodes.put("1529", "阿拉善盟");
        cityCodes.put("2101", "沈阳市");
        cityCodes.put("2102", "大连市");
        cityCodes.put("2103", "鞍山市");
        cityCodes.put("2104", "抚顺市");
        cityCodes.put("2105", "本溪市");
        cityCodes.put("2106", "丹东市");
        cityCodes.put("2107", "锦州市");
        cityCodes.put("2108", "营口市");
        cityCodes.put("2109", "阜新市");
        cityCodes.put("2110", "辽阳市");
        cityCodes.put("2111", "盘锦市");
        cityCodes.put("2112", "铁岭市");
        cityCodes.put("2113", "朝阳市");
        cityCodes.put("2114", "葫芦岛市");
        cityCodes.put("2201", "长春市");
        cityCodes.put("2202", "吉林市");
        cityCodes.put("2203", "四平市");
        cityCodes.put("2204", "辽源市");
        cityCodes.put("2205", "通化市");
        cityCodes.put("2206", "白山市");
        cityCodes.put("2207", "松原市");
        cityCodes.put("2208", "白城市");
        cityCodes.put("2224", "延边朝鲜族自治州");
        cityCodes.put("2301", "哈尔滨市");
        cityCodes.put("2302", "齐齐哈尔市");
        cityCodes.put("2303", "鸡西市");
        cityCodes.put("2304", "鹤岗市");
        cityCodes.put("2305", "双鸭山市");
        cityCodes.put("2306", "大庆市");
        cityCodes.put("2307", "伊春市");
        cityCodes.put("2308", "佳木斯市");
        cityCodes.put("2309", "七台河市");
        cityCodes.put("2310", "牡丹江市");
        cityCodes.put("2311", "黑河市");
        cityCodes.put("2312", "绥化市");
        cityCodes.put("2327", "大兴安岭地区");
        cityCodes.put("3201", "南京市");
        cityCodes.put("3202", "无锡市");
        cityCodes.put("3203", "徐州市");
        cityCodes.put("3204", "常州市");
        cityCodes.put("3205", "苏州市");
        cityCodes.put("3206", "南通市");
        cityCodes.put("3207", "连云港市");
        cityCodes.put("3208", "淮安市");
        cityCodes.put("3209", "盐城市");
        cityCodes.put("3210", "扬州市");
        cityCodes.put("3211", "镇江市");
        cityCodes.put("3212", "泰州市");
        cityCodes.put("3213", "宿迁市");
        cityCodes.put("3301", "杭州市");
        cityCodes.put("3302", "宁波市");
        cityCodes.put("3303", "温州市");
        cityCodes.put("3304", "嘉兴市");
        cityCodes.put("3305", "湖州市");
        cityCodes.put("3306", "绍兴市");
        cityCodes.put("3307", "金华市");
        cityCodes.put("3308", "衢州市");
        cityCodes.put("3309", "舟山市");
        cityCodes.put("3310", "台州市");
        cityCodes.put("3311", "丽水市");
        cityCodes.put("3401", "合肥市");
        cityCodes.put("3402", "芜湖市");
        cityCodes.put("3403", "蚌埠市");
        cityCodes.put("3404", "淮南市");
        cityCodes.put("3405", "马鞍山市");
        cityCodes.put("3406", "淮北市");
        cityCodes.put("3407", "铜陵市");
        cityCodes.put("3408", "安庆市");
        cityCodes.put("3410", "黄山市");
        cityCodes.put("3411", "滁州市");
        cityCodes.put("3412", "阜阳市");
        cityCodes.put("3413", "宿州市");
        cityCodes.put("3415", "六安市");
        cityCodes.put("3416", "亳州市");
        cityCodes.put("3417", "池州市");
        cityCodes.put("3418", "宣城市");
        cityCodes.put("3501", "福州市");
        cityCodes.put("3502", "厦门市");
        cityCodes.put("3503", "莆田市");
        cityCodes.put("3504", "三明市");
        cityCodes.put("3505", "泉州市");
        cityCodes.put("3506", "漳州市");
        cityCodes.put("3507", "南平市");
        cityCodes.put("3508", "龙岩市");
        cityCodes.put("3509", "宁德市");
        cityCodes.put("3601", "南昌市");
        cityCodes.put("3602", "景德镇市");
        cityCodes.put("3603", "萍乡市");
        cityCodes.put("3604", "九江市");
        cityCodes.put("3605", "新余市");
        cityCodes.put("3606", "鹰潭市");
        cityCodes.put("3607", "赣州市");
        cityCodes.put("3608", "吉安市");
        cityCodes.put("3609", "宜春市");
        cityCodes.put("3610", "抚州市");
        cityCodes.put("3611", "上饶市");
        cityCodes.put("3701", "济南市");
        cityCodes.put("3702", "青岛市");
        cityCodes.put("3703", "淄博市");
        cityCodes.put("3704", "枣庄市");
        cityCodes.put("3705", "东营市");
        cityCodes.put("3706", "烟台市");
        cityCodes.put("3707", "潍坊市");
        cityCodes.put("3708", "济宁市");
        cityCodes.put("3709", "泰安市");
        cityCodes.put("3710", "威海市");
        cityCodes.put("3711", "日照市");
        cityCodes.put("3712", "莱芜市");
        cityCodes.put("3713", "临沂市");
        cityCodes.put("3714", "德州市");
        cityCodes.put("3715", "聊城市");
        cityCodes.put("3716", "滨州市");
        cityCodes.put("3717", "菏泽市");
        cityCodes.put("4101", "郑州市");
        cityCodes.put("4102", "开封市");
        cityCodes.put("4103", "洛阳市");
        cityCodes.put("4104", "平顶山市");
        cityCodes.put("4105", "安阳市");
        cityCodes.put("4106", "鹤壁市");
        cityCodes.put("4107", "新乡市");
        cityCodes.put("4108", "焦作市");
        cityCodes.put("4109", "濮阳市");
        cityCodes.put("4110", "许昌市");
        cityCodes.put("4111", "漯河市");
        cityCodes.put("4112", "三门峡市");
        cityCodes.put("4113", "南阳市");
        cityCodes.put("4114", "商丘市");
        cityCodes.put("4115", "信阳市");
        cityCodes.put("4116", "周口市");
        cityCodes.put("4117", "驻马店市");
        cityCodes.put("4190", "省直辖县级行政区划");
        cityCodes.put("4201", "武汉市");
        cityCodes.put("4202", "黄石市");
        cityCodes.put("4203", "十堰市");
        cityCodes.put("4205", "宜昌市");
        cityCodes.put("4206", "襄阳市");
        cityCodes.put("4207", "鄂州市");
        cityCodes.put("4208", "荆门市");
        cityCodes.put("4209", "孝感市");
        cityCodes.put("4210", "荆州市");
        cityCodes.put("4211", "黄冈市");
        cityCodes.put("4212", "咸宁市");
        cityCodes.put("4213", "随州市");
        cityCodes.put("4228", "恩施土家族苗族自治州");
        cityCodes.put("4290", "省直辖县级行政区划");
        cityCodes.put("4301", "长沙市");
        cityCodes.put("4302", "株洲市");
        cityCodes.put("4303", "湘潭市");
        cityCodes.put("4304", "衡阳市");
        cityCodes.put("4305", "邵阳市");
        cityCodes.put("4306", "岳阳市");
        cityCodes.put("4307", "常德市");
        cityCodes.put("4308", "张家界市");
        cityCodes.put("4309", "益阳市");
        cityCodes.put("4310", "郴州市");
        cityCodes.put("4311", "永州市");
        cityCodes.put("4312", "怀化市");
        cityCodes.put("4313", "娄底市");
        cityCodes.put("4331", "湘西土家族苗族自治州");
        cityCodes.put("4401", "广州市");
        cityCodes.put("4402", "韶关市");
        cityCodes.put("4403", "深圳市");
        cityCodes.put("4404", "珠海市");
        cityCodes.put("4405", "汕头市");
        cityCodes.put("4406", "佛山市");
        cityCodes.put("4407", "江门市");
        cityCodes.put("4408", "湛江市");
        cityCodes.put("4409", "茂名市");
        cityCodes.put("4412", "肇庆市");
        cityCodes.put("4413", "惠州市");
        cityCodes.put("4414", "梅州市");
        cityCodes.put("4415", "汕尾市");
        cityCodes.put("4416", "河源市");
        cityCodes.put("4417", "阳江市");
        cityCodes.put("4418", "清远市");
        cityCodes.put("4419", "东莞市");
        cityCodes.put("4420", "中山市");
        cityCodes.put("4451", "潮州市");
        cityCodes.put("4452", "揭阳市");
        cityCodes.put("4453", "云浮市");
        cityCodes.put("4501", "南宁市");
        cityCodes.put("4502", "柳州市");
        cityCodes.put("4503", "桂林市");
        cityCodes.put("4504", "梧州市");
        cityCodes.put("4505", "北海市");
        cityCodes.put("4506", "防城港市");
        cityCodes.put("4507", "钦州市");
        cityCodes.put("4508", "贵港市");
        cityCodes.put("4509", "玉林市");
        cityCodes.put("4510", "百色市");
        cityCodes.put("4511", "贺州市");
        cityCodes.put("4512", "河池市");
        cityCodes.put("4513", "来宾市");
        cityCodes.put("4514", "崇左市");
        cityCodes.put("4601", "海口市");
        cityCodes.put("4602", "三亚市");
        cityCodes.put("4603", "三沙市");
        cityCodes.put("4690", "省直辖县级行政区划");
        cityCodes.put("5101", "成都市");
        cityCodes.put("5103", "自贡市");
        cityCodes.put("5104", "攀枝花市");
        cityCodes.put("5105", "泸州市");
        cityCodes.put("5106", "德阳市");
        cityCodes.put("5107", "绵阳市");
        cityCodes.put("5108", "广元市");
        cityCodes.put("5109", "遂宁市");
        cityCodes.put("5110", "内江市");
        cityCodes.put("5111", "乐山市");
        cityCodes.put("5113", "南充市");
        cityCodes.put("5114", "眉山市");
        cityCodes.put("5115", "宜宾市");
        cityCodes.put("5116", "广安市");
        cityCodes.put("5117", "达州市");
        cityCodes.put("5118", "雅安市");
        cityCodes.put("5119", "巴中市");
        cityCodes.put("5120", "资阳市");
        cityCodes.put("5132", "阿坝藏族羌族自治州");
        cityCodes.put("5133", "甘孜藏族自治州");
        cityCodes.put("5134", "凉山彝族自治州");
        cityCodes.put("5201", "贵阳市");
        cityCodes.put("5202", "六盘水市");
        cityCodes.put("5203", "遵义市");
        cityCodes.put("5204", "安顺市");
        cityCodes.put("5205", "毕节市");
        cityCodes.put("5206", "铜仁市");
        cityCodes.put("5223", "黔西南布依族苗族自治州");
        cityCodes.put("5226", "黔东南苗族侗族自治州");
        cityCodes.put("5227", "黔南布依族苗族自治州");
        cityCodes.put("5301", "昆明市");
        cityCodes.put("5303", "曲靖市");
        cityCodes.put("5304", "玉溪市");
        cityCodes.put("5305", "保山市");
        cityCodes.put("5306", "昭通市");
        cityCodes.put("5307", "丽江市");
        cityCodes.put("5308", "普洱市");
        cityCodes.put("5309", "临沧市");
        cityCodes.put("5323", "楚雄彝族自治州");
        cityCodes.put("5325", "红河哈尼族彝族自治州");
        cityCodes.put("5326", "文山壮族苗族自治州");
        cityCodes.put("5328", "西双版纳傣族自治州");
        cityCodes.put("5329", "大理白族自治州");
        cityCodes.put("5331", "德宏傣族景颇族自治州");
        cityCodes.put("5333", "怒江傈僳族自治州");
        cityCodes.put("5334", "迪庆藏族自治州");
        cityCodes.put("5401", "拉萨市");
        cityCodes.put("5421", "昌都地区");
        cityCodes.put("5422", "山南地区");
        cityCodes.put("5423", "日喀则地区");
        cityCodes.put("5424", "那曲地区");
        cityCodes.put("5425", "阿里地区");
        cityCodes.put("5426", "林芝地区");
        cityCodes.put("6101", "西安市");
        cityCodes.put("6102", "铜川市");
        cityCodes.put("6103", "宝鸡市");
        cityCodes.put("6104", "咸阳市");
        cityCodes.put("6105", "渭南市");
        cityCodes.put("6106", "延安市");
        cityCodes.put("6107", "汉中市");
        cityCodes.put("6108", "榆林市");
        cityCodes.put("6109", "安康市");
        cityCodes.put("6110", "商洛市");
        cityCodes.put("6201", "兰州市");
        cityCodes.put("6202", "嘉峪关市");
        cityCodes.put("6203", "金昌市");
        cityCodes.put("6204", "白银市");
        cityCodes.put("6205", "天水市");
        cityCodes.put("6206", "武威市");
        cityCodes.put("6207", "张掖市");
        cityCodes.put("6208", "平凉市");
        cityCodes.put("6209", "酒泉市");
        cityCodes.put("6210", "庆阳市");
        cityCodes.put("6211", "定西市");
        cityCodes.put("6212", "陇南市");
        cityCodes.put("6229", "临夏回族自治州");
        cityCodes.put("6230", "甘南藏族自治州");
        cityCodes.put("6301", "西宁市");
        cityCodes.put("6302", "海东市");
        cityCodes.put("6322", "海北藏族自治州");
        cityCodes.put("6323", "黄南藏族自治州");
        cityCodes.put("6325", "海南藏族自治州");
        cityCodes.put("6326", "果洛藏族自治州");
        cityCodes.put("6327", "玉树藏族自治州");
        cityCodes.put("6328", "海西蒙古族藏族自治州");
        cityCodes.put("6401", "银川市");
        cityCodes.put("6402", "石嘴山市");
        cityCodes.put("6403", "吴忠市");
        cityCodes.put("6404", "固原市");
        cityCodes.put("6405", "中卫市");
        cityCodes.put("6501", "乌鲁木齐市");
        cityCodes.put("6502", "克拉玛依市");
        cityCodes.put("6521", "吐鲁番地区");
        cityCodes.put("6522", "哈密地区");
        cityCodes.put("6523", "昌吉回族自治州");
        cityCodes.put("6527", "博尔塔拉蒙古自治州");
        cityCodes.put("6528", "巴音郭楞蒙古自治州");
        cityCodes.put("6529", "阿克苏地区");
        cityCodes.put("6530", "克孜勒苏柯尔克孜自治州");
        cityCodes.put("6531", "喀什地区");
        cityCodes.put("6532", "和田地区");
        cityCodes.put("6540", "伊犁哈萨克自治州");
        cityCodes.put("6542", "塔城地区");
        cityCodes.put("6543", "阿勒泰地区");
        cityCodes.put("6590", "自治区直辖县级行政区划");
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard
     *            15位身份编码
     * @return 18位身份编码
     */
    public static String conver15CardTo18(String idCard) {
        String idCard18 = "";
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            if (cArr != null) {
                int[] iCard = converCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String sVal = getCheckCode18(iSum17);
                if (sVal.length() > 0) {
                    idCard18 += sVal;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return idCard18;
    }

    /**
     * 验证身份证是否合法
     */
    public static boolean validateCard(String idCard) {
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        }
        if (validateIdCard15(card)) {
            return true;
        }
        return false;
    }

    /**
     * 验证18位身份编码是否合法
     *
     * @param idCard
     *            身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            // 前17位
            String code17 = idCard.substring(0, 17);
            // 第18位
            String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                if (cArr != null) {
                    int[] iCard = converCharToInt(cArr);
                    int iSum17 = getPowerSum(iCard);
                    // 获取校验位
                    String val = getCheckCode18(iSum17);
                    if (val.length() > 0) {
                        if (val.equalsIgnoreCase(code18)) {
                            bTrue = true;
                        }
                    }
                }
            }
        }
        return bTrue;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard
     *            身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (provinceCodes.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode
                        .substring(0, 2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            if (!valiDate(cal.get(Calendar.YEAR),
                    Integer.valueOf(birthCode.substring(2, 4)),
                    Integer.valueOf(birthCode.substring(4, 6)))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 将字符数组转换成数字数组
     *
     * @param ca
     *            字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr
     * @return 身份证编码。
     */
    public static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (power.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < power.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * power[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     */
    public static String getCheckCode18(int iSum) {
        String sCode = "";
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
        }
        return sCode;
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String year = idCard.substring(6, 10);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard
     *            身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard
     *            身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard
     *            身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "N";
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "M";
        } else {
            sGender = "F";
        }
        return sGender;
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idCard
     *            身份编码
     * @return 省级编码。
     */
    public static String getProvinceByIdCard(String idCard) {
        int len = idCard.length();
        String sProvince = null;
        String sProvinNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sProvinNum = idCard.substring(0, 2);
        }
        sProvince = provinceCodes.get(sProvinNum);
        return sProvince;
    }

    /**
     * 根据身份编号获取户籍地级市
     * @param idCard
     * @return
     */
    public static String getCityByIdCard(String idCard){
        int len = idCard.length();
        String sCity = null;
        String sCityNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sCityNum = idCard.substring(0, 4);
        }
        sCity = cityCodes.get(sCityNum);
        return sCity;
    }

    /**
     * 数字验证
     *
     * @param val
     * @return 提取的数字。
     */
    public static boolean isNum(String val) {
        return val == null || "".equals(val) ? false : val
                .matches("^[0-9]*{1}");
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear
     *            待验证日期(年)
     * @param iMonth
     *            待验证日期(月 1-12)
     * @param iDate
     *            待验证日期(日)
     * @return 是否有效
     */
    public static boolean valiDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                datePerMonth = 30;
                break;
            case 2:
                boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
                        && (iYear > MIN && iYear < year);
                datePerMonth = dm ? 29 : 28;
                break;
            default:
                datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }


    /**
     *根据身份证号，自动获取对应的星座
     *
     * @param idCard
     *            身份证号码
     * @return 星座
     */
    public static String getConstellationById(String idCard) {
        if (!validateCard(idCard))
            return "";
        int month = CheckIdCardUtils.getMonthByIdCard(idCard);
        int day = CheckIdCardUtils.getDateByIdCard(idCard);
        String strValue = "";

        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            strValue = "水瓶座";
        } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
            strValue = "双鱼座";
        } else if ((month == 3 && day > 20) || (month == 4 && day <= 19)) {
            strValue = "白羊座";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            strValue = "金牛座";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
            strValue = "双子座";
        } else if ((month == 6 && day > 21) || (month == 7 && day <= 22)) {
            strValue = "巨蟹座";
        } else if ((month == 7 && day > 22) || (month == 8 && day <= 22)) {
            strValue = "狮子座";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            strValue = "处女座";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
            strValue = "天秤座";
        } else if ((month == 10 && day > 23) || (month == 11 && day <= 22)) {
            strValue = "天蝎座";
        } else if ((month == 11 && day > 22) || (month == 12 && day <= 21)) {
            strValue = "射手座";
        } else if ((month == 12 && day > 21) || (month == 1 && day <= 19)) {
            strValue = "魔羯座";
        }

        return strValue;
    }


    /**
     *根据身份证号，自动获取对应的生肖
     *
     * @param idCard
     *            身份证号码
     * @return 生肖
     */
    public static String getZodiacById(String idCard) { // 根据身份证号，自动返回对应的生肖
        if (!validateCard(idCard))
            return "";

        String sSX[] = { "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗" };
        int year = CheckIdCardUtils.getYearByIdCard(idCard);
        int end = 3;
        int x = (year - end) % 12;

        String retValue = "";
        retValue = sSX[x];

        return retValue;
    }


    /**
     *根据身份证号，自动获取对应的天干地支
     *
     * @param idCard
     *            身份证号码
     * @return 天干地支
     */
    public static String getChineseEraById(String idCard) { // 根据身份证号，自动返回对应的生肖
        if (!validateCard(idCard))
            return "";

        String sTG[] = { "癸", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "任" };
        String sDZ[] = { "亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌" };

        int year = CheckIdCardUtils.getYearByIdCard(idCard);
        int i = (year - 3) % 10;
        int j = (year - 3) % 12;

        String retValue = "";
        retValue = sTG[i] + sDZ[j];

        return retValue;
    }

    private static final ExecutorService FIXED_EXECUTOR = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws ParseException {
        String idCard = "432624430505662";
        System.out.println(CheckIdCardUtils.getGenderByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getBirthByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getMonthByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getDateByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getConstellationById(idCard));
        System.out.println(CheckIdCardUtils.getZodiacById(idCard));
        System.out.println(CheckIdCardUtils.getChineseEraById(idCard));
        System.out.println(CheckIdCardUtils.getProvinceByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getCityByIdCard(idCard));
        System.out.println(CheckIdCardUtils.getAgeByIdCard(idCard));
    }
}
