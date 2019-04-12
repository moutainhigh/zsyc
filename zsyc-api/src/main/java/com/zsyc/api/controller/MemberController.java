package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.api.AccountHelper;
import com.zsyc.member.entity.*;
import com.zsyc.member.po.MemberAddressPo;
import com.zsyc.member.po.MemberBalancePo;
import com.zsyc.member.po.MemberInfoPo;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.member.vo.MemberInfoVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/member")
@Api
public class MemberController {

    @Reference
    private MemberInfoService memberInfoService;
    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取当前小程序登陆人信息")
    @GetMapping(value = "getLoginMember")
    public Map getLoginMember() {
        Map map = memberInfoService.getLoginMember(accountHelper.getMember());
        map.put("memberInfo", accountHelper.getMember());
        return map;
    }

    @ApiOperation("用户查询接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "门店id(不传就是所有)", required = true, dataType = "long"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态（NORMAL正常,LOCK锁定）", required = true, dataType = "string")})
    @GetMapping(value = "getMemberList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<MemberInfo> getMemberList(MemberInfoPo memberInfoPo) {

        return memberInfoService.getMemberList(memberInfoPo.getName(),
                                                memberInfoPo.getBeginTime(),
                                                memberInfoPo.getCurrentPage(),
                                                memberInfoPo.getPageSize(),
                                                memberInfoPo.getEndTime(),
                                                memberInfoPo.getTelephone(),
                                                memberInfoPo.getStatus(),
                                                memberInfoPo.getStoreId());
    }

    @ApiOperation("用户地址查询接口---小程序")
//    @ApiImplicitParams({
//    @ApiImplicitParam(name = "memberId", value = "会员id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberAddressList",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberAddress> getMemberAddressList() {
        return memberInfoService.getMemberAddressList(accountHelper.getMember().getId());
    }


    @ApiOperation("用户地址查询接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "会员id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberAddressList2",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberAddress> getMemberAddressList2(Long memberId) {
        return memberInfoService.getMemberAddressList2(memberId);
    }


    @ApiOperation("单个用户地址查询接口---小程序")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberAddressById",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberAddress getMemberAddressById(Long addressId) {
        return memberInfoService.getMemberAddressById(addressId);
    }

    @ApiOperation("查看会员地址详情---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberAddressById2",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberAddress getMemberAddressById2(Long addressId) {
        return memberInfoService.getMemberAddressById2(addressId);
    }

    @ApiOperation("添加用户地址接口---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isElevator", value = "是否电梯房：大于0非电梯房，-1电梯房", required = true, dataType = "int"),
            @ApiImplicitParam(name = "tag", value = "标签", required = true, dataType = "string"),
            @ApiImplicitParam(name = "locationAddress", value = "定位地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "consignee", value = "收货人", required = true, dataType = "string"),
            @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "int"),
//            @ApiImplicitParam(name = "memberId", value = "会员id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "storey", value = "楼层(负数表示电梯户)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "isDefault", value = "是否默认,1:默认,2:非默认", required = true, dataType = "int"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地址编码", required = true, dataType = "long")})
    @PostMapping(value = "addMemberAddress",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addMemberAddress(@RequestBody MemberAddressPo memberAddressPo) {
        return memberInfoService.addAddress(memberAddressPo.getIsElevator(),
                                            memberAddressPo.getTag(),
                                            memberAddressPo.getLocationAddress(),
                                            memberAddressPo.getConsignee(),
                                            memberAddressPo.getSex(),
                                            memberAddressPo.getTelephone(),
                                            memberAddressPo.getAddress(),
                                            memberAddressPo.getStorey(),
                                            accountHelper.getMember().getId(),
                                            memberAddressPo.getLng(),
                                            memberAddressPo.getLat(),
                                            memberAddressPo.getAdcode(),
                                            memberAddressPo.getIsDefault());
    }


    @ApiOperation("后台下单地址查询接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),})
    @GetMapping(value = "getMemberAddressByPhone",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberAddress> getMemberAddressByPhone(String telephone){
        return memberInfoService.getMemberAddressByPhone(telephone);
    }


    @ApiOperation("添加后台下单地址接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isElevator", value = "是否电梯房：大于0非电梯房，-1电梯房", required = true, dataType = "int"),
            @ApiImplicitParam(name = "tag", value = "标签", required = true, dataType = "string"),
            @ApiImplicitParam(name = "locationAddress", value = "定位地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "consignee", value = "收货人", required = true, dataType = "string"),
            @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "storey", value = "楼层(负数表示电梯户)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地址编码", required = true, dataType = "long")})
    @PostMapping(value = "addMemberAddress2",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addMemberAddress2(@RequestBody MemberAddressPo memberAddressPo) {
        return memberInfoService.addAddress2(memberAddressPo.getIsElevator(),
                memberAddressPo.getTag(),
                memberAddressPo.getLocationAddress(),
                memberAddressPo.getConsignee(),
                memberAddressPo.getSex(),
                memberAddressPo.getTelephone(),
                memberAddressPo.getAddress(),
                memberAddressPo.getStorey(),
                memberAddressPo.getLng(),
                memberAddressPo.getLat(),
                memberAddressPo.getAdcode(),
                accountHelper.getUser().getId(),
                memberAddressPo.getStoreId());
    }


    @ApiOperation("编辑用户地址接口---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isElevator", value = "是否电梯房：大于0非电梯房，-1电梯房", required = true, dataType = "string"),
            @ApiImplicitParam(name = "tag", value = "标签", required = true, dataType = "string"),
            @ApiImplicitParam(name = "locationAddress", value = "定位地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "consignee", value = "收货人", required = true, dataType = "string"),
            @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "int"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storey", value = "楼层", required = true, dataType = "string"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "storey", value = "楼层(负数表示电梯户)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "isDeafult", value = "是否默认,1:默认,2:非默认", required = true, dataType = "string"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地址编码", required = true, dataType = "long")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "地址编辑改为默认成功"),
            @ApiResponse(code = 1, message = "地址改为非默认成功，返回其他默认地址"),
            @ApiResponse(code = 2, message = "地址编辑改为非默认，只有一条用户地址必须是默认的")})
    @PostMapping(value = "updateMemberAddress",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateMemberAddress(@RequestBody MemberAddressPo memberAddressPo) {
        return memberInfoService.updateAddress(
                memberAddressPo.getIsElevator(),
                memberAddressPo.getConsignee(),
                memberAddressPo.getSex(),
                memberAddressPo.getTelephone(),
                memberAddressPo.getAddress(),
                memberAddressPo.getStorey(),
                memberAddressPo.getAddressId(),
                memberAddressPo.getIsDefault(),
                memberAddressPo.getLng(),
                memberAddressPo.getLat(),
                memberAddressPo.getAdcode(),
                memberAddressPo.getTag(),
                memberAddressPo.getLocationAddress());
    }

//    @ApiOperation("编辑用户地址接口---后台")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "isElevator", value = "是否电梯房：大于0非电梯房，-1电梯房", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "tag", value = "标签", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "locationAddress", value = "定位地址", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "consignee", value = "收货人", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "int"),
//            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
//            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "storey", value = "楼层", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "double"),
//            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "double"),
//            @ApiImplicitParam(name = "storey", value = "楼层(负数表示电梯户)", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "isDeafult", value = "是否默认,1:默认,2:非默认", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "adcode", value = "最后一级地址编码", required = true, dataType = "long")})
//    @ApiResponses(value = {
//            @ApiResponse(code = 0, message = "地址编辑改为默认成功"),
//            @ApiResponse(code = 1, message = "地址改为非默认成功"),
//            @ApiResponse(code = 2, message = "地址编辑改为非默认，只有一条用户地址必须是默认的")})
//    @PostMapping(value = "updateMemberAddress2",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object updateMemberAddress2(@RequestBody MemberAddressPo memberAddressPo) {
//        return memberInfoService.updateAddress2(
//                memberAddressPo.getIsElevator(),
//                memberAddressPo.getConsignee(),
//                memberAddressPo.getSex(),
//                memberAddressPo.getTelephone(),
//                memberAddressPo.getAddress(),
//                memberAddressPo.getStorey(),
//                memberAddressPo.getAddressId(),
//                memberAddressPo.getIsDefault(),
//                memberAddressPo.getLng(),
//                memberAddressPo.getLat(),
//                memberAddressPo.getAdcode(),
//                memberAddressPo.getTag(),
//                memberAddressPo.getLocationAddress(),
//                accountHelper.getUser().getId());
//    }


    @ApiOperation("单个用户地址删除接口---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "默认地址删除成功，返回其他默认地址"),
            @ApiResponse(code = 1, message = "默认地址删除成功，用户没有地址了"),
            @ApiResponse(code = 2, message = "非默认地址删除成功")})
    @GetMapping(value = "deleteMemberAddress",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object deleteMemberAddress(Long addressId) {
        return memberInfoService.deleteAddress(addressId);
    }


//    @ApiOperation("多个用户地址删除接口---后台")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "ids", value = "地址ids", required = true, dataType = "long")})
//    @ApiResponses(value = {
//            @ApiResponse(code = 0, message = "批量删除成功")})
//    @PostMapping(value = "deleteMemberAddress2",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object deleteMemberAddress2(@RequestBody Map<String, Object> map) {
//        List<Long> ids = (List<Long>)map.get("ids");
//        return memberInfoService.deleteMemberAddress2(ids, accountHelper.getUser().getId());
//    }


    @ApiOperation("门店会员账期余额列表查询---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "会员名称", required = true, dataType = "long"),
            @ApiImplicitParam(name = "telephone", value = "会员电话", required = true, dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态('NORMAL'正常,'BAN'禁止使用账期,'OVERDUE'账期已超期)", required = true, dataType = "long"),
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberStoreCredit",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<MemberAddressStoreCredit> getMemberStoreCredit(MemberInfoPo memberInfoPo){

        return memberInfoService.getMemberStoreCreditList(memberInfoPo.getStoreId(),
                                                            memberInfoPo.getCurrentPage(),
                                                            memberInfoPo.getPageSize(),
                                                            memberInfoPo.getName(),
                                                            memberInfoPo.getTelephone(),
                                                            memberInfoPo.getStatus());
    }

    @ApiOperation("会员账期余额详情查询---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "creditId", value = "用户账期id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberStoreCreditById",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberAddressStoreCredit getMemberStoreCreditById(Long creditId){
        return memberInfoService.getMemberStoreCreditById(creditId);
    }


    @ApiOperation("会员账期操作记录查询---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "creditId", value = "用户账期id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberStoreCreditLog",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberAddressStoreCreditLog> getMemberStoreCreditLog(Long creditId ){
        return memberInfoService.getMemberStoreCreditLog(creditId);
    }


    @ApiOperation("门店会员账期操作记录查询--后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long")})
    @GetMapping(value = "getMemberStoreCreditLog2",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberAddressStoreCreditLog> getMemberStoreCreditLog2(Long storeId ){
        return memberInfoService.getMemberStoreCreditLog2(storeId);
    }

    @ApiOperation("首页切换地址接口---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long")})
    @GetMapping(value = "updateDeafultAddress", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateDeafultAddress(Long addressId){
        return memberInfoService.updateDeafultAddress(addressId);
    }

    @ApiOperation("会员充值接口---小程序")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "payType", value = "充值方式（1微信）", required = true, dataType = "long"),
            @ApiImplicitParam(name = "balanceId", value = "用户余额详情id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "money", value = "充值金额", required = true, dataType = "int")})
    @PostMapping(value = "recharge",produces = MediaType.APPLICATION_JSON_VALUE)
    public int recharge(@RequestBody MemberBalancePo memberBalancePo){
        return memberInfoService.recharge(memberBalancePo.getMoney(),
                                          memberBalancePo.getBalanceId(),
                                          accountHelper.getMember().getId(),
                                          memberBalancePo.getPayType());
    }

    @ApiOperation("会员提现接口---小程序")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "balanceId", value = "用户余额详情id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "bouns", value = "提现金额", required = true, dataType = "int")})
    @PostMapping(value = "cash",produces = MediaType.APPLICATION_JSON_VALUE)
    public int cash(@RequestBody MemberBalancePo memberBalancePo){
        return memberInfoService.cash(memberBalancePo.getBouns(),
                memberBalancePo.getBalanceId(),
                accountHelper.getMember().getId());
    }


    @ApiOperation("会员余额列表查询接口--后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "昵称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string")})
    @GetMapping(value = "getBalanceList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<MemberBalance> getBalanceList(MemberInfoPo memberInfoPo){
        return memberInfoService.getBalanceList(memberInfoPo.getCurrentPage(),
                                                memberInfoPo.getPageSize(),
                                                memberInfoPo.getName(),
                                                memberInfoPo.getTelephone());
    }



    @ApiOperation("会员余额查询接口---小程序")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, dataType = "long")})
    @GetMapping(value = "getBalance",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberBalance getBalance(){
        return memberInfoService.getBalance(accountHelper.getMember().getId());
    }


    @ApiOperation("会员余额查询接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, dataType = "long")})
    @GetMapping(value = "getBalance2",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberBalance getBalance2(Long memberId){
        return memberInfoService.getBalance2(memberId);
    }



    @ApiOperation("会员充值记录列表查询接口---小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "昵称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
//            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string")})
    @GetMapping(value = "getBalanceLogList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<MemberBalanceLog> getBalanceLogList(MemberInfoPo memberInfoPo){
        return memberInfoService.getBalanceLogList(accountHelper.getMember().getId(), memberInfoPo.getCurrentPage(), memberInfoPo.getPageSize(),
                memberInfoPo.getName(), memberInfoPo.getTelephone());
    }

    @ApiOperation("会员充值记录列表查询接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "昵称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "用户id(不传就是查所有用户的充值记录，超级管理员权限)", required = true, dataType = "long"),
            @ApiImplicitParam(name = "telephone", value = "电话", required = true, dataType = "string")})
    @GetMapping(value = "getBalanceLogList2",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<MemberBalanceLog> getBalanceLogList2(MemberInfoPo memberInfoPo){
        return memberInfoService.getBalanceLogList2(memberInfoPo.getId(), memberInfoPo.getCurrentPage(), memberInfoPo.getPageSize(),
                                    memberInfoPo.getName(), memberInfoPo.getTelephone());
    }


    @ApiOperation("会员禁止/允许下单接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "用户状态('NORMAL':正常,'LOCK':锁定)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long"),})
    @PostMapping(value = "updateUserStatus",produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateUserStatus(@RequestBody MemberInfoPo memberInfoPo){
        return memberInfoService.updateUserStatus(memberInfoPo.getStatus(), memberInfoPo.getId(), accountHelper.getUser().getId());
    }

    @ApiOperation("修改用户地址所属门店接口---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "修改后的门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "addressId", value = "用户地址id", required = true, dataType = "long"),})
    @ApiResponses(value = {
            @ApiResponse(code = 1, message = "修改成功"),
            @ApiResponse(code = 2, message = "地址没有该类型关联门店，不能修改")})
    @PostMapping(value = "updateAddressStore",produces = MediaType.APPLICATION_JSON_VALUE)
    public Map updateAddressStore(@RequestBody MemberAddressPo memberAddressPo){
        return memberInfoService.updateAddressStore(memberAddressPo.getAddressId(), memberAddressPo.getStoreId(), accountHelper.getUser().getId());
    }
}
