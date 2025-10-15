package com.imp.all.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.imp.all.easypoi.ExcelExportStylerHeaderImpl;
import com.imp.all.easypoi.MemberExcelDataHandler;
import com.imp.all.easypoi.vo.CpfrFcstReportMonthly;
import com.imp.all.easypoi.vo.Member;
import com.imp.all.easypoi.vo.Order;
import com.imp.all.easypoi.vo.Product;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.util.LocalJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Longlin
 * @date 2021/12/6 14:25
 * @description 文档地址：http://doc.wupaas.com/docs/easypoi/easypoi-1c0u96flii98v
 */
@RestController
@Api(value = "EasyPoiController", tags = "EasyPoi导入导出测试")
@RequestMapping("/easypoi")
@Slf4j
public class EasyPoiController {

    @ApiOperation(value = "导出会员列表Excel")
    @GetMapping("/exportMemberList")
    public void exportMemberList(ModelMap map,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        List<Member> memberList = LocalJsonUtil.getListFromJson("excel/members.json", Member.class);
        ExportParams params = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        //对导出结果进行自定义处理
        MemberExcelDataHandler handler = new MemberExcelDataHandler();
        handler.setNeedHandlerFields(new String[]{"昵称"});
        params.setDataHandler(handler);
        map.put(NormalExcelConstants.DATA_LIST, memberList);
        map.put(NormalExcelConstants.CLASS, Member.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "memberList");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation("从Excel导入会员列表")
    @PostMapping(value = "/importMemberList")
    @ResponseBody
    public CommonResult<?> importMemberList(@RequestPart("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try {
            List<Member> list = ExcelImportUtil.importExcel(
                    file.getInputStream(),
                    Member.class, params);
            return CommonResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("导入失败！");
        }
    }

    @ApiOperation(value = "导出订单列表Excel")
    @GetMapping(value = "/exportOrderList")
    public void exportOrderList(ModelMap map,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        List<Order> orderList = getOrderList();
        ExportParams params = new ExportParams("订单列表", "订单列表", ExcelType.XSSF);
        //导出时排除一些字段
        params.setExclusions(new String[]{"ID", "出生日期", "性别"});
        params.setStyle(ExcelExportStylerHeaderImpl.class);
        map.put(NormalExcelConstants.DATA_LIST, orderList);
        map.put(NormalExcelConstants.CLASS, Order.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "orderList");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "导出订单列表Excel")
    @GetMapping(value = "/exportBigDataList")
    public void exportBigDataList(HttpServletResponse response) throws IOException {
        List<Order> orderList = getOrderList();
        ExportParams params = new ExportParams(null, "excel1", ExcelType.XSSF);
        params.setStyle(ExcelExportStylerHeaderImpl.class);

        Workbook workbook = ExcelExportUtil.exportBigExcel(params, Order.class, new IExcelExportServer() {
            @Override
            public List selectListForExcelExport(Object o, int i) {
                // o值为queryParams
                // i为page，初始值为1，每次进来都加1
                if ((int) o == i) {
                    return orderList;
                }
                return null;
            }
        }, 1);

//        response.setContentType("application/ms-excel;charset=UTF-8");
        response.addHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode("exportList", "UTF-8") + ".xlsx\";");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

    private List<Order> getOrderList() {
        List<Order> orderList = LocalJsonUtil.getListFromJson("excel/orders.json", Order.class);
        List<Product> productList = LocalJsonUtil.getListFromJson("excel/products.json", Product.class);
        List<Member> memberList = LocalJsonUtil.getListFromJson("excel/members.json", Member.class);
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            order.setMember(memberList.get(i));
            order.setProductList(productList);
        }
        return orderList;
    }

    @ApiOperation(value = "导出功能动态表头(XXX部门xxxx年x月排班表)")
    @GetMapping(value = "/exportTest2")
    public void exportTest2(HttpServletResponse response) throws IOException {
        String[][] strArray = {{"MONDAY", "一"}, {"TUESDAY", "二"}, {"WEDNESDAY", "三"}, {"THURSDAY", "四"}, {"FRIDAY", "五"}, {"SATURDAY", "六"}, {"SUNDAY", "日"}};

        // 排班状态
        String[] constantStr = {"休", "班"};

        int length = constantStr.length;

        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate firstday = LocalDate.of(today.getYear(),today.getMonth(),1);
        //本月的最后一天
        LocalDate lastDay =today.with(TemporalAdjusters.lastDayOfMonth());
        log.info("本月的第一天" + firstday);
        log.info("本月的最后一天" + lastDay);

        List<LocalDate> dateList = new ArrayList<>();

        long distance = ChronoUnit.DAYS.between(firstday, lastDay);
        Stream.iterate(firstday, d -> d.plusDays(1)).limit(distance + 1).forEach(dateList::add);

        List<ExcelExportEntity> colList = new ArrayList<>();
        //第一列
        ExcelExportEntity colEntity1 = new ExcelExportEntity("序号", "code");
        colEntity1.setNeedMerge(true);
//        colEntity1.setOrderNum(3); // 写之后就是第三列
        colList.add(colEntity1);
        //第二列
        ExcelExportEntity colEntity2 = new ExcelExportEntity("工号", "jobNo");
        colEntity2.setNeedMerge(true);
        colList.add(colEntity2);
        //第三列
        ExcelExportEntity colEntity3 = new ExcelExportEntity("姓名\\日期", "name");
        colEntity3.setNeedMerge(true);
        colList.add(colEntity3);


        for(LocalDate localDate:dateList){
            ExcelExportEntity dateColGroup = new ExcelExportEntity(""+localDate.getDayOfMonth(), "dayOfMouth"+localDate.getDayOfMonth());
            List<ExcelExportEntity> dateColList = new ArrayList<ExcelExportEntity>();
            String dayOfWeek = String.valueOf(localDate.getDayOfWeek());
            for (String[] strings : strArray) {
                if (dayOfWeek.equals(strings[0])) {
                    dayOfWeek = strings[1];
                    break;
                }
            }
            ExcelExportEntity tempExcelExportEntity = new ExcelExportEntity(dayOfWeek, "dayOfWeek"+localDate.getDayOfMonth());
            dateColList.add(tempExcelExportEntity);
            dateColGroup.setList(dateColList);
            colList.add(dateColGroup);
        }

        // 根据部门id查询人员
        List<Member> memberList = LocalJsonUtil.getListFromJson("excel/members.json", Member.class);

        List<Map<String, Object>> list = new ArrayList<>();


        for(int i=0;i < memberList.size();i++){
            Map<String, Object> valMap = new HashMap<>();
            //添加序号
            valMap.put("code", i+1);
            //添加工号
            valMap.put("jobNo",memberList.get(i).getId());
            //添加姓名
            valMap.put("name",memberList.get(i).getNickname());

            for(LocalDate localDate:dateList){
                List<Map<String, Object>> dayOfWeekList = new ArrayList<Map<String, Object>>();
                Map<String, Object> dayOfWeekMap = new HashMap<String, Object>();
                dayOfWeekMap.put("dayOfWeek"+ localDate.getDayOfMonth(), constantStr[(int)(Math.random()*length)]);
                dayOfWeekList.add(dayOfWeekMap);
                valMap.put("dayOfMouth"+ localDate.getDayOfMonth(), dayOfWeekList);
            }

            list.add(valMap);
        }
        ExportParams params = new ExportParams("XXX部门"+today.getYear()+"年"+today.getMonthValue()+"月排班表", today.getYear()+"年"+today.getMonthValue()+"月(安排)");
        // 设置标题样式(ExcelExportStyleBigHeaderImpl重写了easypoi的AbstractExcelExportStyler方法)
        params.setStyle(ExcelExportStylerHeaderImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, colList,
                list);
        response.addHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode("exportList", "UTF-8") + ".xlsx\";");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

    @ApiOperation(value = "导出", notes = "CPFR主页面")
    @GetMapping("/exportResult")
    public void exportResult(HttpServletResponse response) throws IOException {
        List<CpfrFcstReportMonthly> reportMonthlyList = LocalJsonUtil.getListFromJson("excel/reportMonthly.json", CpfrFcstReportMonthly.class);
        // 根据model分类
        Map<String, List<CpfrFcstReportMonthly>> listMap = reportMonthlyList.stream().collect(Collectors.groupingBy(CpfrFcstReportMonthly::getModel));
        List<Map<String, Object>> dataRows = getDataRows(listMap);

        ExportParams params = new ExportParams(null, "CpfrMonthly", ExcelType.XSSF);
        params.setStyle(ExcelExportStylerHeaderImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(
                params,
                getHeadRows(),
                dataRows
        );
        response.addHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode("exportList", "UTF-8") + ".xlsx\";");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

    /********************************************************** 导出 ****************************************************************/
    public List<Map<String, Object>> getDataRows(Map<String, List<CpfrFcstReportMonthly>> listMap) {

        List<Map<String, Object>> list = new ArrayList<>();

        List<String> keyFigures = Arrays.asList("P", "S", "I", "MOH(DM)");
        List<String> keyFiguresP = Arrays.asList("Inbound Inv.-DM", "Inbound Inv.-DI", "Intransit PR-DM", "Intransit PR-DI", "Open PR-DM", "Open PR-DI", " Suggest replenishment-DM", " Suggest replenishment-DI");
        List<String> keyFiguresS = Arrays.asList("PO QTY(DM&DF)", "SI(DM)", "In-transit(DM)", "PO QTY(DI)", "SI(DI)", "POS/ST FCST");
        List<String> keyFiguresI = Collections.singletonList("Ending Inv.(BU)");
        List<String> keyFiguresMOH = Arrays.asList("MOH(Actual)", "MOH(Target)", "Target Stock");

        for (Map.Entry<String, List<CpfrFcstReportMonthly>> entry : listMap.entrySet()) {
            String key = entry.getKey();
            List<CpfrFcstReportMonthly> monthlyList = entry.getValue();
            monthlyList = monthlyList.stream().sorted(Comparator.comparingInt(CpfrFcstReportMonthly::getFcstReportMonth)).collect(Collectors.toList());
            if (monthlyList.size() == 19) {
                for (int i = 0; i < 18; i++) {
                    Map<String, Object> valMap = new HashMap<>();
                    valMap.put("model", key);
                    if (i<8) {
                        valMap.put("keyFigure", keyFigures.get(0));
                        valMap.put("figureDetail", keyFiguresP.get(i));
                    }
                    else if (i<14) {
                        valMap.put("keyFigure", keyFigures.get(1));
                        valMap.put("figureDetail", keyFiguresS.get(i-8));
                    }
                    else if (i<15) {
                        valMap.put("keyFigure", keyFigures.get(2));
                        valMap.put("figureDetail", keyFiguresI.get(0));
                    }
                    else {
                        valMap.put("keyFigure", keyFigures.get(3));
                        valMap.put("figureDetail", keyFiguresMOH.get(i-15));
                    }
                    // past
                    List<Map<String, Object>> pastMonthList = new ArrayList<>();
                    valMap.put("pastMonth", pastMonthList);
                    Map<String, Object> pastValMap = new HashMap<>();
                    pastMonthList.add(pastValMap);
                    for (int j = 0; j < 6; j++) {
                        String pastMonth = DatePattern.NORM_MONTH_FORMAT.format(DateUtil.offsetMonth(new Date(), j-6));
                        CpfrFcstReportMonthly lastMonthly = monthlyList.get(j);
                        pastValMap.put(pastMonth, getValueFormModelWithRow(lastMonthly, i));
                    }
                    // overdue
                    CpfrFcstReportMonthly overdueMonthly = monthlyList.get(6);
                    valMap.put("overdue", getValueFormModelWithRow(overdueMonthly, i));
                    // currentMonth
                    List<Map<String, Object>> currentMonthList = new ArrayList<>();
                    valMap.put("currentMonth", currentMonthList);
                    CpfrFcstReportMonthly currentMonthly = monthlyList.get(7);
                    Map<String, Object> currentValMap = new HashMap<>();
                    String currentMonth = DatePattern.NORM_MONTH_FORMAT.format(new Date());
                    currentValMap.put(currentMonth, getValueFormModelWithRow(currentMonthly, i));
                    currentMonthList.add(currentValMap);
                    // future
                    List<Map<String, Object>> futureMonthList = new ArrayList<>();
                    valMap.put("futureMonth", futureMonthList);
                    Map<String, Object> futureValMap = new HashMap<>();
                    futureMonthList.add(futureValMap);
                    for (int j = 0; j < 11; j++) {
                        String futureMonth = DatePattern.NORM_MONTH_FORMAT.format(DateUtil.offsetMonth(new Date(), j+1));
                        CpfrFcstReportMonthly futureMonthly = monthlyList.get(j+8);
                        futureValMap.put(futureMonth, getValueFormModelWithRow(futureMonthly, i));
                    }
                    list.add(valMap);
                }
            }
        }
        return list;
    }

    public List<ExcelExportEntity> getHeadRows() {
        List<ExcelExportEntity> colList = new ArrayList<>();
        //第1列
        ExcelExportEntity colEntity1 = new ExcelExportEntity("model", "model");
        colEntity1.setMergeVertical(true);
        colEntity1.setWrap(true); // 支持 \n 换行
        colList.add(colEntity1);
        //第2列
        ExcelExportEntity colEntity2 = new ExcelExportEntity("Key Figure", "keyFigure");
        colEntity2.setMergeVertical(true);
        colList.add(colEntity2);
        //第3列
        ExcelExportEntity colEntity3 = new ExcelExportEntity("Figure Detail", "figureDetail");
//        colEntity3.setNeedMerge(true);
        colList.add(colEntity3);
        //第4列
        ExcelExportEntity colEntity4 = new ExcelExportEntity("Past Month", "pastMonth");
        List<ExcelExportEntity> colEntity4Sub = new ArrayList<>();
        for (int i = -6; i < 0; i++) {
            String pastMonth = DatePattern.NORM_MONTH_FORMAT.format(DateUtil.offsetMonth(new Date(), i));
            colEntity4Sub.add(new ExcelExportEntity(pastMonth, pastMonth));
        }
        colEntity4.setList(colEntity4Sub);
        colList.add(colEntity4);
        //第5列
        ExcelExportEntity colEntity5 = new ExcelExportEntity("Overdue", "overdue");
//        colEntity5.setNeedMerge(true);
        colList.add(colEntity5);
        //第6列
        ExcelExportEntity colEntity6 = new ExcelExportEntity("Current Month", "currentMonth");
        List<ExcelExportEntity> colEntity6Sub = new ArrayList<>();
        String currentMonth = DatePattern.NORM_MONTH_FORMAT.format(new Date());
        colEntity6Sub.add(new ExcelExportEntity(currentMonth, currentMonth));
        colEntity6.setList(colEntity6Sub);
        colList.add(colEntity6);
        //第7列
        ExcelExportEntity colEntity7 = new ExcelExportEntity("Future Month", "futureMonth");
        List<ExcelExportEntity> colEntity7Sub = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            String pastMonth = DatePattern.NORM_MONTH_FORMAT.format(DateUtil.offsetMonth(new Date(), i));
            colEntity7Sub.add(new ExcelExportEntity(pastMonth, pastMonth));
        }
        colEntity7.setList(colEntity7Sub);
        colList.add(colEntity7);
        //第8列
        ExcelExportEntity colEntity8 = new ExcelExportEntity("remarks", "remarks");
//        colEntity8.setNeedMerge(true);
        colList.add(colEntity8);
        return colList;
    }

    private Object getValueFormModelWithRow(CpfrFcstReportMonthly reportMonthly, Integer row) {
        if (row == 0) {
            return reportMonthly.getInboundDm();
        }
        else if (row == 1) {
            return reportMonthly.getInboundDi();
        }
        else if (row == 2) {
            return reportMonthly.getIntansitPrDm();
        }
        else if (row == 3) {
            return reportMonthly.getIntansitPrDi();
        }
        else if (row == 4) {
            return reportMonthly.getOpenPrDm();
        }
        else if (row == 5) {
            return reportMonthly.getOpenPrDi();
        }
        else if (row == 6) {
            return reportMonthly.getSuggestReplenishmentDm();
        }
        else if (row == 7) {
            return reportMonthly.getSuggestReplenishmentDi();
        }
        else if (row == 8) {
            return reportMonthly.getSalesQty();
        }
        else if (row == 9) {
            return reportMonthly.getSellInFcst();
        }
        else if (row == 10) {
            return reportMonthly.getPlfDmStock();
        }
        else if (row == 11) {
            return reportMonthly.getSalesInQty();
        }
        else if (row == 12) {
            return reportMonthly.getSiDi();
        }
        else if (row == 13) {
            return reportMonthly.getPosStFcst();
        }
        else if (row == 14) {
            return reportMonthly.getEndingInv();
        }
        else if (row == 15) {
            return reportMonthly.getMohActual();
        }
        else if (row == 16) {
            return reportMonthly.getMohTarget();
        }
        else if (row == 17) {
            return reportMonthly.getTargetStock();
        }
        return null;
    }
}
