package com.imp.all.easypoi.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Longlin
 * @date 2022/3/7 11:18
 * @description
 */

@Data
public class CpfrFcstReportMonthly {

    private Long fcstReportMonthlyId;   //预测月度报表ID
    private String fcstReportMonthlyVersion;   //月度版本，一周一个版本
    private Integer fcstReportMonth;   //-6 ~ -1为过去月，0：overdue，1~12 为当前月到未来月
    private String originMarkingVersion;   //数据来源的周度版本
    private Integer orgId;   //组织ID
    private String customerCode;   //客户编码
    private String model;   //产品型号
    private Date monthStartDate;   //月起始日期
    private Date monthEndDate;   //月起始日期
    private Date reportWeekDate;   //当前report的周时间
    private String monthCode;   //月编码
    private Integer inboundDm;   //Inbound Inv.-DM
    private Integer inboundDi;   //Inbound Inv.-DI
    private Integer intansitPrDm;   //Intansit PR-DM
    private Integer intansitPrDi;   //Intansit PR-DI
    private Integer openPrDm;   //Open PR-DM
    private Integer openPrDi;   //Open PR-DI
    private BigDecimal suggestReplenishmentDm;   //预测用户（BU）平台总采购量TTL Suggest replenishment-DM
    private BigDecimal suggestReplenishmentDi;   //预测用户（BU）平台总采购量TTL Suggest replenishment-DI
    private Integer salesQty;   //销售数量 PO QTY(DM+DF)
    private BigDecimal sellInFcst;   //预测客户采购量 SI(DM)
    private BigDecimal plfDmStock;   //平台预计在途DM入库库存 In-transit(DM)
    private BigDecimal salesInQty;   //预测用户（BU）平台DI订单的采购量PO QTY(DI)
    private BigDecimal siDi;   //BU实际发货的订单销量SI(DI)
    private BigDecimal posStFcst;   //cpfr表里周维度的POS值、ST FCST值 POS/ST FCST
    private BigDecimal endingInv;   //Ending Inv.（BU)
    private BigDecimal mohActual;   //该月库存可以支持销售多少周=该月期末库存 I / Avg(sum（未来3个月SI(DM)值）
    private Integer mohTarget;   //默认 8weeks
    private BigDecimal targetStock;   //Target Stock=该月对应的未来8周的SI FCST（SI(DM)） 值总和
}
