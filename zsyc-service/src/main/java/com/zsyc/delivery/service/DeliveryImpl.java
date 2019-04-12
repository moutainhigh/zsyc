package com.zsyc.delivery.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.CommonConstant;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.delivery.mapper.DeliveryStaffBillMapper;
import com.zsyc.delivery.mapper.DeliveryStaffMapper;
import com.zsyc.delivery.po.DeliVeryStaffAo;
import com.zsyc.delivery.po.DeliverStaffBillBo;
import com.zsyc.delivery.po.DeliverStaffBillCo;
import com.zsyc.delivery.vo.DeliveryStaffVo;
import com.zsyc.order.mapper.OrderSubInfoMapper;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.service.OrderSubInfoService;
import com.zsyc.order.vo.OrderSubInfoVo;
import com.zsyc.store.entity.StoreDeliveryRelation;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DeliveryImpl implements DeliveryService{


    @Autowired
    private DeliveryStaffMapper deliveryStaffMapper;

    @Autowired
    private DeliveryStaffBillMapper deliveryStaffBillMapper;

    @Autowired
    private OrderSubInfoMapper orderSubInfoMapper;

    @Autowired
    private OrderSubInfoService orderSubInfoService;

    @Autowired
    private StoreInfoService storeInfoService;

    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 配送员工资生成
     * 每月1号凌晨2点执行一次
     * @return
     */
//    @Scheduled(cron="0 0 2 1 * ?")
//    @Scheduled(cron="0/20 * * * * ?")
    @Transactional
    public void createDeliverySalary() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取前月的第一天
         Calendar cal_1=Calendar.getInstance();//获取当前日期
         cal_1.add(Calendar.MONTH, -1);
         cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
         String firstDay = format.format(cal_1.getTime()) + " 00:00:00";

         //获取前月的最后一天
         Calendar cale = Calendar.getInstance();
         cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
         String lastDay = format.format(cale.getTime()) + " 23:59:59";


        //查询出该段时间的员工工资（按时间时分秒）
        List<DeliveryStaffVo>  deliveryStaffVos = deliveryStaffMapper.deliverySalary(firstDay, lastDay);

        //生成员工工资信息
        if(deliveryStaffVos.size() > 0){

            //从缓存拿出配送员上交款账单，再加上工资
            List<DeliveryStaffBill> deliveryStaffBills2 = (List<DeliveryStaffBill>)redisTemplate.opsForValue().get("deliveryStaffBills");
            for(DeliveryStaffBill deliveryStaffBill1: deliveryStaffBills2){
                for(DeliveryStaffVo deliveryStaffVo: deliveryStaffVos){
                    if (deliveryStaffBill1.getMasterId().longValue() == deliveryStaffVo.getId().longValue()){
                        deliveryStaffBill1.setBillSalary(deliveryStaffVo.getAmount());
                        deliveryStaffBill1.setBillStatus("UNPAID");
                    }
                }
                deliveryStaffBill1.setBillSalary(deliveryStaffBill1.getBillSalary() == null ? 0 : deliveryStaffBill1.getBillSalary());
                deliveryStaffBill1.setTurnIn(deliveryStaffBill1.getTurnIn() == null ? 0 : deliveryStaffBill1.getTurnIn());
            }


            deliveryStaffBillMapper.insertList(deliveryStaffBills2);
        }
    }


    /**
     * 配送员上交款结算
     * 每月1号凌晨1点执行一次
     * @return
     */
    //    @Scheduled(cron="0 0 1 1 * ?")
//    @Scheduled(cron="0/10 * * * * ?")
    @Transactional
    public void deliveryTurnIn() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取前月的第一天
        Calendar cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime()) + " 00:00:00";

        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        String lastDay = format.format(cale.getTime()) + " 23:59:59";

        DateTimeFormatter fmt24 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginDateTime = LocalDateTime.parse(firstDay, fmt24);
        LocalDateTime endDateTime = LocalDateTime.parse(lastDay, fmt24);

        OrderSubInfoVo orderSubInfoVo = new OrderSubInfoVo();
        orderSubInfoVo.setOrderStartTime(beginDateTime);
        orderSubInfoVo.setOrderEndTime(endDateTime);
        orderSubInfoVo.setOrderStatus("DONE");

        //查询出该时间段内的子订单（按时间时分秒）
        List<OrderSubInfoPo> orderSubInfoServiceList = orderSubInfoService.getOrderinfosByTimeSlot(orderSubInfoVo);
        LocalDateTime now = LocalDateTime.now();
        List<Long> ids = new ArrayList<>();
        for(OrderSubInfoPo orderSubInfoPo: orderSubInfoServiceList){
            if(orderSubInfoPo.getPayType().equals("OFFLINE")){
                ids.add(orderSubInfoPo.getId());
            }
        }

        //把货到付款的子订单加进去
        if(ids.size() == 0){
            ids.add(0l);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        //获取每个配送员的上交款
        List<DeliveryStaffVo>  deliveryStaffVos = deliveryStaffMapper.deliveryTurnIn(map);

        //生成配送员上交款账单
        List<DeliveryStaffBill> deliveryStaffBills = new ArrayList<>();
        if(deliveryStaffVos.size() > 0){
            DeliveryStaffBill deliveryStaffBill = null;
            for(DeliveryStaffVo deliveryStaffVo: deliveryStaffVos){
                deliveryStaffBill = new DeliveryStaffBill();
                deliveryStaffBill.setBillSalary(0);
                deliveryStaffBill.setMasterId(deliveryStaffVo.getId());
                deliveryStaffBill.setBillTime(now);
                deliveryStaffBill.setBillStatus("UNPAID");
                deliveryStaffBill.setTurnIn(deliveryStaffVo.getTurnIn());
                deliveryStaffBill.setTurnInStatus("UNPAID");
                deliveryStaffBill.setCreateTime(now);
                deliveryStaffBill.setCreateUserId(0l);
                deliveryStaffBill.setUpdateUserId(0l);
                deliveryStaffBill.setUpdateTime(now);
                deliveryStaffBill.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
                deliveryStaffBills.add(deliveryStaffBill);
            }

            //先把上缴款账单放入缓存,时间3小时
            redisTemplate.opsForValue().set("deliveryStaffBills", deliveryStaffBills, 3, TimeUnit.HOURS);

            //            deliveryStaffBillMapper.insertList(deliveryStaffBills);
        }
    }

    @Override
    public DeliveryStaff getDeliveryById(Long deliveryId) {
        return deliveryStaffMapper.selectById(deliveryId);
    }


    @Override
    public IPage<DeliveryStaff> getDeliveryList(Long storeId, Integer currentPage, String masterType, Integer pageSize, String masterName, String phone) {
        if(StringUtils.isBlank(masterName))masterName = null;
        if(StringUtils.isBlank(phone))phone = null;
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<DeliveryStaff> page = new Page<>(currentPage, pageSize);
        IPage<DeliveryStaff> storeInfoVoPage = deliveryStaffMapper.getDeliveryList(page, storeId, masterType, masterName, phone);



        return storeInfoVoPage;
    }

    //按照名字或手机或配送员类型查询
    @Override
    public Object selectDeliVeryStaff(String masterName, String phone, String masterType,Integer currentPage,Integer pageSize) {
        if(currentPage==null){
            currentPage=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(masterName))masterName = null;
        if(StringUtils.isBlank(phone))phone = null;
        if(StringUtils.isBlank(masterType))masterType = null;
        IPage<DeliverStaffBillBo> page = new Page<DeliverStaffBillBo>(currentPage, pageSize);
        IPage<DeliverStaffBillBo> deliveryStaffIPage = deliveryStaffMapper.selectDeliVeryStaff(page ,masterName,phone,masterType);
        for(DeliverStaffBillBo deliveryStaffIPage1:deliveryStaffIPage.getRecords()){
             //配送员id
            Long idd=deliveryStaffIPage1.getId();
           //利用配送员id获得配送中间表的店铺id
           List<StoreDeliveryRelation> deliveryRelations= deliveryStaffBillMapper.stroedeliverId(idd);
           String name="";
            if(deliveryRelations.size()>=2){
                for(StoreDeliveryRelation deliveryStaffIPage2:deliveryRelations) {
                    Long storeId = deliveryStaffIPage2.getStoreId();
                    StoreInfo storeById = storeInfoService.getStoreById(storeId);
                    name=name+","+storeById.getStoreName();
                }
                deliveryStaffIPage1.setMasterName(name);

                }else{
                Long storeId =  deliveryRelations.get(0).getStoreId();
                StoreInfo storeById = storeInfoService.getStoreById(storeId);
                 String  name1=storeById.getStoreName();
                deliveryStaffIPage1.setMasterName(name1);

                }
            }





        return deliveryStaffIPage;
    }
    //更改删除状态
    @Override
    public Integer setIsDel(Long id) {
        return deliveryStaffMapper.setIsDel(id);
    }
    //更改配送员名字
    @Override
    public Integer updateMasterName(String newMasterName, Long id) {
        return deliveryStaffMapper.updateMasterName(newMasterName,id);
    }

    @Override
    public Integer updateMasterPhone(String phone, Long id) {
        return deliveryStaffMapper.updateMasterPhone(phone,id);
    }

    @Override
    public Integer updatePapersPapers(String papers, Long id) {
        return deliveryStaffMapper.updatePapersPapers(papers,id);
    }

    @Override
    public Integer updateIsLeader(int isLeader, Long id) {
        return deliveryStaffMapper.updateIsLeader(isLeader,id);
    }
        //后台查询配送员工资列表
    @Override
    public Object deliveryStaffBill(String masterName, String phone, String billStatus, String turnInStatus,Integer currentPage,Integer pageSize,Long storeId) {
        if(currentPage==null){
            currentPage=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(storeId==null){
            storeId=0L;
        }

        IPage<DeliverStaffBillBo> page = new Page<DeliverStaffBillBo>(currentPage, pageSize);
        if(StringUtils.isBlank(masterName))masterName = null;
        if(StringUtils.isBlank(phone))phone = null;
        if(StringUtils.isBlank(billStatus))billStatus = null;
        if(StringUtils.isBlank(turnInStatus))turnInStatus = null;
        IPage<DeliverStaffBillBo> deliveryStaffs=deliveryStaffBillMapper.deliveryStaffBill(page,masterName,  phone,  billStatus, turnInStatus,storeId);
        List<DeliverStaffBillBo> deliverStaffBillBos =new ArrayList<>();
        for(DeliverStaffBillBo  deliveryStaffs1:deliveryStaffs.getRecords()){
            Long deliveryStaffs1Id=deliveryStaffs1.getId();

        if(deliveryStaffs1.getMasterType()!=null){
            if(deliveryStaffs1.getMasterType().equals("STORE")){
                deliveryStaffs1.setMasterType("店铺配送");
            }else{
                deliveryStaffs1.setMasterType("平台配送");
            }
        }


            List<DeliverStaffBillCo>deliveryStaffBills=deliveryStaffBillMapper.selectDeliverStaffBillAll(deliveryStaffs1Id);
            for(DeliverStaffBillCo deliverStaffBillBos1:deliveryStaffBills){

                if(deliverStaffBillBos1.getBillStatus().equals("DONE")){
                    deliverStaffBillBos1.setBillStatus("已结算");
                }else {
                    deliverStaffBillBos1.setBillStatus("未结算");
                }

                if(deliverStaffBillBos1.getTurnInStatus().equals("DONE")){
                    deliverStaffBillBos1.setTurnInStatus("已上缴");
                }else {
                    deliverStaffBillBos1.setTurnInStatus("未上缴");
                }
            }

            deliveryStaffs1.setDeliveryStaffBillList(deliveryStaffBills);
            deliverStaffBillBos.add(deliveryStaffs1);

        }

        return deliverStaffBillBos;
    }

    @Override
    public Integer updateDeliverAll(DeliVeryStaffAo deliVeryStaffAo) {
        Long id=deliVeryStaffAo.getId();
        String newMasterName=deliVeryStaffAo.getNewMasterName();
        String papers=deliVeryStaffAo.getPapers();
        String phone=deliVeryStaffAo.getPhone();
        Integer isLeader=deliVeryStaffAo.getIsLeader();
        if(StringUtils.isBlank(newMasterName))newMasterName = null;
        if(StringUtils.isBlank(phone))phone = null;
        if(StringUtils.isBlank(papers))papers = null;

        return deliveryStaffBillMapper.updateDeliverAll(newMasterName,phone,papers,isLeader,id);
    }


}
