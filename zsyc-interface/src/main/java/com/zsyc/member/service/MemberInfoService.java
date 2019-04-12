package com.zsyc.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.admin.entity.User;
import com.zsyc.member.entity.*;
import com.zsyc.member.vo.MemberInfoVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MemberInfoService {

    /**
     * 通过account获取member
     * @param accountId
     * @return
     */
    MemberInfo getmemberByAccount(Long accountId);
    /**
     * 查询会员信息
     * @param name
     * @param beginTime
     * @param endTime
     * @return
     */
    IPage<MemberInfo> getMemberList(String name, String beginTime, Integer currentPage, Integer pageSize, String endTime,
                                    String telephone, String status, Long storeId);

    /**
     * 获取用户信息
     * @param memberInfo
     * @return
     */
    Map getLoginMember(MemberInfo memberInfo);

    /**
     * 查询会员地址--小程序
     * @param memberId
     * @return
     */
    List<MemberAddress> getMemberAddressList(Long memberId);


    /**
     * 查询会员地址--后台
     * @param memberId
     * @return
     */
    List<MemberAddress> getMemberAddressList2(Long memberId);


    /**
     * 查询地址--小程序
     * @param addressId
     * @return
     */
    MemberAddress getMemberAddressById(Long addressId);


    /**
     * 查询地址--后台
     * @param addressId
     * @return
     */
    MemberAddress getMemberAddressById2(Long addressId);

    /**
     * 新增会员地址
     * @param tag
     * @param locationAddress
     * @param consignee
     * @param sex
     * @param phone
     * @param address
     * @param storey
     * @param memberId
     * @param lng
     * @param lat
     * @param adCode
     * @return
     */
    Object addAddress(Integer isElevator, String tag, String locationAddress, String consignee, Integer sex, String phone, String address, Integer storey,
                   Long memberId, Double lng, Double lat, Long adCode, Integer isDefault);


    /**
     * 后台下单添加地址
     * @param isElevator
     * @param tag
     * @param locationAddress
     * @param consignee
     * @param sex
     * @param phone
     * @param address
     * @param storey
     * @param lng
     * @param lat
     * @param adCode
     * @return
     */
    Object addAddress2(Integer isElevator, String tag, String locationAddress, String consignee, Integer sex, String phone, String address, Integer storey,
                       Double lng, Double lat, Long adCode, Long loginUserId, Long storeId);

    /**
     * 后台下单地址查询接口
     * @return
     */
    List<MemberAddress> getMemberAddressByPhone(String phone);

    /**
     * 修改会员地址--小程序
     * @param consignee
     * @param sex
     * @param phone
     * @param address
     * @param storey
     * @param addressId
     * @return
     */
    Object updateAddress(Integer isElevator, String consignee, Integer sex, String phone, String address, Integer storey, Long addressId,
                      Integer isDefault, double lng, double lat, Long adCode, String tag, String locationAddress);


    /**
     * 修改会员地址--后台
     * @param consignee
     * @param sex
     * @param phone
     * @param address
     * @param storey
     * @param addressId
     * @return
     */
    Object updateAddress2(Integer isElevator, String consignee, Integer sex, String phone, String address, Integer storey, Long addressId,
                         Integer isDefault, double lng, double lat, Long adCode, String tag, String locationAddress, Long loginUserId);
    /**
     * 删除会员地址
     * @param addressId
     * @return
     */
    Object deleteAddress(Long addressId);


    /**
    *   多个用户地址删除接口
     */
    Object deleteMemberAddress2(List<Long> ids, Long loginUserId);


    /**
     * 获取门店会员账期额度列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<MemberAddressStoreCredit> getMemberStoreCreditList(Long storeId, Integer currentPage, Integer pageSize,
                                                             String name, String telephone, String status);

    /**
     * 获取会员账期额度详情
     * @param creditId
     * @return
     */
    MemberAddressStoreCredit getMemberStoreCreditById(Long creditId);


    /**
     * 会员充值
     * @param money
     * @param balanceId
     * @return
     */
    int recharge(Long money, Long balanceId, Long memberId, Integer payType);

    /**
     * 用户提现
     * @return
     */
    int cash(Integer bouns, Long balanceId, Long memberId);


    /**
     * 红包提成收入
     * @param bouns
     * @param memberId
     * @return
     */
    int bouns(Integer bouns,  Long memberId);

    /**
     * 会员余额查询接口--小程序
     * @param memberId
     * @return
     */
    MemberBalance getBalance(Long memberId);


    /**
     * 会员余额查询接口--后台
     * @param memberId
     * @return
     */
    MemberBalance getBalance2(Long memberId);

    /**
     *会员余额列表查询接口
     * @return
     */
    IPage<MemberBalance> getBalanceList(Integer currentPage, Integer pageSize, String telephone, String name);

    /**
     * 会员充值记录查询--小程序
     * @param memberId
     * @return
     */
    IPage<MemberBalanceLog> getBalanceLogList(Long memberId, Integer currentPage, Integer pageSize, String name, String telephone);


    /**
     * 会员充值记录查询--后台
     * @param memberId
     * @return
     */
    IPage<MemberBalanceLog> getBalanceLogList2(Long memberId, Integer currentPage, Integer pageSize, String name, String telephone);

    /**
     * 会员账期操作记录查询--后台
     * @param creditId
     * @return
     */
    List<MemberAddressStoreCreditLog> getMemberStoreCreditLog(Long creditId);


    /**
     * 门店用户账期操作记录查询--后台
     * @param storeId
     * @return
     */
    List<MemberAddressStoreCreditLog> getMemberStoreCreditLog2(Long storeId);


    /**
     * 首页更改默认地址--小程序
     * @param addressId
     * @return
     */
    Object updateDeafultAddress(Long addressId);


    /**
     * 会员禁止/允许下单接口--后台
     * @param status
     * @return
     */
    int updateUserStatus(String status, Long memberId, Long loginUserId);


    /**
     * 修改用户地址所属门店接口--后台
     * @return
     */
    Map updateAddressStore(Long addressId, Long storeId, Long loginUserId);
}
