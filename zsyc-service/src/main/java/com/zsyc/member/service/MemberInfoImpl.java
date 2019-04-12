package com.zsyc.member.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.account.entity.Account;
import com.zsyc.account.entity.AccountBind;
import com.zsyc.account.mapper.AccountBindMapper;
import com.zsyc.account.mapper.AccountMapper;
import com.zsyc.admin.service.BaseService;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.member.entity.*;
import com.zsyc.member.mapper.*;
import com.zsyc.member.vo.MemberAddressStoreVo;
import com.zsyc.member.vo.MemberInfoVo;
import com.zsyc.order.vo.StockVo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.zsyc.common.LocationUtils.getmeter;

@Service
public class MemberInfoImpl extends BaseService implements MemberInfoService{

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private MemberAddressMapper memberAddressMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private MemberAddressStoreMapper memberAddressStoreMapper;

    @Autowired
    private MemberAddressStoreCreditMapper memberAddressStoreCreditMapper;

    @Autowired
    private MemberAddressStoreCreditLogMapper memberAddressStoreCreditLogMapper;

    @Autowired
    private MemberBalanceMapper memberBalanceMapper;

    @Autowired
    private MemberBalanceLogMapper memberBalanceLogMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountBindMapper accountBindMapper;

    @Override
    public MemberInfo getmemberByAccount(Long accountId) {
        AssertExt.hasId(accountId,"无效accountID");
        return this.memberInfoMapper.selectOne(newQueryWrapper(MemberInfo.class).eq("account_id", accountId));
    }

    @Override
    public IPage<MemberInfo> getMemberList(String name, String beginTime, Integer currentPage, Integer pageSize,
                                           String endTime, String telephone, String status, Long storeId) {

        if(StringUtils.isBlank(telephone))telephone=null;
        if(StringUtils.isBlank(status))status=null;
        if(StringUtils.isBlank(name))name=null;
        if(StringUtils.isBlank(beginTime))beginTime=null;
        if(StringUtils.isBlank(endTime))endTime=null;
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<MemberInfo> page = new Page<>(currentPage, pageSize);
        IPage<MemberInfo> memberInfoIPage = memberInfoMapper.getMemberList(page, name, beginTime, endTime, telephone, status, storeId);
        return memberInfoIPage;
    }

    @Override
    public Map getLoginMember(MemberInfo memberInfo) {

        Map<String, Object> map = new HashMap<>();

        //查询出默认地址
        QueryWrapper<MemberAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberInfo.getId());
        queryWrapper.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        queryWrapper.eq("is_default", 1);
        MemberAddress memberAddress = memberAddressMapper.selectOne(queryWrapper);

        if (memberAddress != null){
            map.put("memberAddress", memberAddress);
            map = getAddressStore(memberAddress.getId(), map);
        }

        //拿出openId
        Account account = accountMapper.selectById(memberInfo.getAccountId());
        QueryWrapper<AccountBind> queryWrapper1= new QueryWrapper<>();
        queryWrapper1.eq("account_id", account.getId());
        queryWrapper1.eq("type", AccountBind.AccountBindType.PROGRAM_OPENID);
        AccountBind accountBind = accountBindMapper.selectOne(queryWrapper1);
        map.put("openId", accountBind.getBindAccount());

        return map;
    }

    @Override
    public List<MemberAddress> getMemberAddressList(Long memberId) {
        AssertExt.notNull(memberId, "memberId为空");
        return memberAddressMapper.getMemberAddressList(memberId);
    }

    @Override
    public List<MemberAddress> getMemberAddressList2(Long memberId) {
        AssertExt.notNull(memberId, "memberId为空");
        return memberAddressMapper.getMemberAddressList(memberId);
    }

    @Override
    public MemberAddress getMemberAddressById(Long addressId) {
        AssertExt.notNull(addressId, "addressId为空");
        return memberAddressMapper.selectById(addressId);
    }

    @Override
    public MemberAddress getMemberAddressById2(Long addressId) {
        AssertExt.notNull(addressId, "addressId为空");
        return memberAddressMapper.selectById(addressId);
    }

    @Override
    @Transactional
    public Object addAddress(Integer isElevator, String tag, String locationAddress, String consignee, Integer sex, String phone, String address,
                          Integer storey, Long memberId,  Double lng, Double lat, Long adCode, Integer isDefault){

        AssertExt.notBlank(tag, "tag为空");
        AssertExt.notBlank(locationAddress, "locationAddress为空");
        AssertExt.notBlank(consignee, "consignee为空");
        AssertExt.notNull(sex, "sex为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notNull(storey, "storey为空");
        AssertExt.notNull(memberId, "memberId为空");
        AssertExt.notNull(lng, "lng为空");
        AssertExt.notNull(lat, "lat为空");
        AssertExt.notNull(adCode, "adCode为空");
        AssertExt.notNull(isDefault, "isDefault为空");

        if(isElevator == -1){
            storey = -1;
        }

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setIsDefault(2);
        memberAddress.setMemberId(memberId);
        memberAddress.setTag(tag);
        memberAddress.setLocationAddress(locationAddress);
        memberAddress.setAddress(address);
        memberAddress.setAdcode(adCode);
        memberAddress.setCardImg("");
        memberAddress.setConsignee(consignee);
        memberAddress.setCreateTime(now);
        memberAddress.setUpdateTime(now);
        memberAddress.setLat(lat);
        memberAddress.setLng(lng);
        memberAddress.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        memberAddress.setCreateUserId(0l);
        memberAddress.setTelephone(phone);
        memberAddress.setSex(sex);
        memberAddress.setStorey(storey);
        memberAddress.setUpdateUserId(0l);
        memberAddress.setAreaId(0l);
        memberAddress.setIsDefault(isDefault);

        //添加默认地址，把其他地址改为非默认
        int num = 0;
        if(isDefault == 1){
            UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();
            MemberAddress memberAddress2 = new MemberAddress();
            updateWrapper.eq("member_id", memberId);
            updateWrapper.set("is_default", 2);
            memberAddressMapper.update(memberAddress2, updateWrapper);
        }else {
            //添加非默认地址，只有一个地址，自动设置为首页默认地址
            QueryWrapper<MemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("member_id", memberId);
            queryWrapper.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
            num = memberAddressMapper.selectCount(queryWrapper);
            if (num == 0){
                memberAddress.setIsDefault(1);
            }
        }


        memberAddressMapper.insert(memberAddress);


        //查询门店,绑定距离最近的门店类型
        StoreInfo gas = saveMemberAddressStore(0l, now, memberAddress);
        StoreInfo water = saveMemberAddressStore(1l, now, memberAddress);

        //如果有菜市场和快捷菜组合的门店，则不需要绑定菜市场和快捷菜的了
        StoreInfo marketAndFastfood = saveMemberAddressStore(4l, now, memberAddress);
        StoreInfo market = new StoreInfo();
        StoreInfo fastfood = new StoreInfo();
        if(marketAndFastfood.getId() == null){
            market = saveMemberAddressStore(3l, now, memberAddress);
            fastfood = saveMemberAddressStore(2l, now, memberAddress);
        }

        //返回默认地址
        if(isDefault == 1){
            map.put("code","0");
            map.put("memberAddress", memberAddress);
            map.put("msg","添加默认地址成功，返回默认地址");

            //装在map里
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> gasMap = new HashMap<>();
            if(gas.getId() != null){
                gasMap.put("id", gas.getId());
                gasMap.put("address", gas.getAddress());
                gasMap.put("phone", gas.getPhone());
                gasMap.put("storeName", gas.getStoreName());
            }


            Map<String, Object> marketMap = new HashMap<>();
            if(market.getId() != null){
                marketMap.put("id", market.getId());
                marketMap.put("address", market.getAddress());
                marketMap.put("phone", market.getPhone());
                marketMap.put("storeName", market.getStoreName());
            }


            Map<String, Object> waterMap = new HashMap<>();
            if(water.getId() != null){
                waterMap.put("id", water.getId());
                waterMap.put("address", water.getAddress());
                waterMap.put("phone", water.getPhone());
                waterMap.put("storeName", water.getStoreName());
            }


            Map<String, Object> fastfoodMap = new HashMap<>();
            if(fastfood.getId() != null){
                fastfoodMap.put("id", fastfood.getId());
                fastfoodMap.put("address", fastfood.getAddress());
                fastfoodMap.put("phone", fastfood.getPhone());
                fastfoodMap.put("storeName", fastfood.getStoreName());
            }


            Map<String, Object> marketAndFastfoodMap = new HashMap<>();
            if(marketAndFastfood.getId() != null){
                marketAndFastfoodMap.put("id", marketAndFastfood.getId());
                marketAndFastfoodMap.put("address", marketAndFastfood.getAddress());
                marketAndFastfoodMap.put("phone", marketAndFastfood.getPhone());
                marketAndFastfoodMap.put("storeName", marketAndFastfood.getStoreName());
            }


            if(gas.getId() != null)map1.put("gas", gasMap);
            if(market.getId() != null)map1.put("market", marketMap);
            if(fastfood.getId() != null)map1.put("fastfood", fastfoodMap);
            if(water.getId() != null)map1.put("water", waterMap);
            if(marketAndFastfood.getId() != null)map1.put("marketAndFastfood",  marketAndFastfoodMap);

            if(!map1.isEmpty())map.put("store", map1);
            return map;
        }else {

            //添加非默认地址，只有一个地址，自动设置为首页默认地址
            if (num == 0){
                memberAddress.setIsDefault(1);
                map.put("memberAddress", memberAddress);
                map.put("code","2");
                map.put("msg","只有一条地址，非默认也变成默认");
            }else {
                map.put("code","1");
                map.put("msg","添加非默认地址成功");
            }
            return map;
        }
    }

    @Override
    public Object addAddress2(Integer isElevator, String tag, String locationAddress, String consignee, Integer sex, String phone,
                              String address, Integer storey,  Double lng, Double lat, Long adCode, Long loginUserId, Long storeId) {

        AssertExt.notBlank(tag, "tag为空");
        AssertExt.notBlank(locationAddress, "locationAddress为空");
        AssertExt.notBlank(consignee, "consignee为空");
        AssertExt.notNull(sex, "sex为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notNull(storey, "storey为空");
        AssertExt.notNull(lng, "lng为空");
        AssertExt.notNull(lat, "lat为空");
        AssertExt.notNull(adCode, "adCode为空");
        AssertExt.notNull(storeId, "storeId为空");

        if(isElevator == -1){
            storey = -1;
        }

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setIsDefault(2);
        memberAddress.setTag(tag);
        memberAddress.setLocationAddress(locationAddress);
        memberAddress.setAddress(address);
        memberAddress.setAdcode(adCode);
        memberAddress.setCardImg("");
        memberAddress.setConsignee(consignee);
        memberAddress.setCreateTime(now);
        memberAddress.setUpdateTime(now);
        memberAddress.setLat(lat);
        memberAddress.setLng(lng);
        memberAddress.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        memberAddress.setCreateUserId(0l);
        memberAddress.setTelephone(phone);
        memberAddress.setSex(sex);
        memberAddress.setStorey(storey);
        memberAddress.setUpdateUserId(0l);
        memberAddress.setAreaId(0l);

        //没有在小程序注册过的,插入地址和用户信息
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telephone", phone);
        MemberInfo memberInfo2 = memberInfoMapper.selectOne(queryWrapper);

        if(memberInfo2 == null){
            //插入account
            Account account = new Account();
            account.setAccount(phone);
            account.setPassword("");
            account.setSalt("");
            account.setStatus(1);
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            accountMapper.insert(account);


            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setAccountId(account.getId());
            memberInfo.setTelephone(phone);
            memberInfo.setBirthday(null);
            memberInfo.setCreateTime(now);
            memberInfo.setUpdateTime(now);
            memberInfo.setStatus("normal");
            memberInfo.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
            memberInfo.setNickName("后台下单的用户");
            memberInfo.setUpdateUserId(loginUserId);
            memberInfoMapper.insert(memberInfo);


            memberAddress.setMemberId(memberInfo.getId());
            memberAddress.setIsDefault(2);
            memberAddressMapper.insert(memberAddress);
        }else {
            //注册过的，直接生成非默认地址
            memberAddress.setMemberId(memberInfo2.getId());
            memberAddress.setIsDefault(2);
            memberAddressMapper.insert(memberAddress);
        }

        //关联门店地址表
        MemberAddressStore memberAddressStore = new MemberAddressStore();
        memberAddressStore.setAddressId(memberAddress.getId());
        memberAddressStore.setStoreId(storeId);
        memberAddressStore.setCreateTime(now);
        memberAddressStore.setAddressType(MemberAddressStore.AddressType.NORMAL.name());
        memberAddressStore.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        memberAddressStore.setCreateUserId(loginUserId);
        memberAddressStore.setUpdateTime(now);
        memberAddressStore.setUpdateUserId(loginUserId);
        memberAddressStore.setIsCustom(0);
        memberAddressStoreMapper.insert(memberAddressStore);

        map.put("addressId", memberAddress.getId());
        map.put("msg", "添加成功");
        return map;
    }

    @Override
    public List<MemberAddress> getMemberAddressByPhone(String phone) {
        QueryWrapper<MemberAddress> memberAddressQueryWrapper = new QueryWrapper<>();
        memberAddressQueryWrapper.eq("telephone", phone);
        memberAddressQueryWrapper.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        return memberAddressMapper.selectList(memberAddressQueryWrapper);
    }

    public StoreInfo saveMemberAddressStore(Long storeType, LocalDateTime now,  MemberAddress memberAddress){
        QueryWrapper<StoreInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("store_type_id", storeType);
        wrapper.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);

        StoreInfo storeInfo2 = new StoreInfo();
        List<StoreInfo> storeInfoList = storeInfoMapper.selectList(wrapper);
        //门店和地址距离
        int distant = 300000;

        if(storeInfoList.size() > 0){
            MemberAddressStore memberAddressStore =  null;

            for(StoreInfo storeInfo: storeInfoList){
                double radius = getmeter(memberAddress.getLat(), memberAddress.getLng(), storeInfo.getLatitude(), storeInfo.getLongitude());

                if(storeInfo.getRadius() >= (int)radius){

                    //保存距离最近的门店
                    if(distant > (int)radius){
                        memberAddressStore = new MemberAddressStore();
                        memberAddressStore.setAddressId(memberAddress.getId());
                        memberAddressStore.setStoreId(storeInfo.getId());
                        memberAddressStore.setCreateTime(now);
                        memberAddressStore.setAddressType(MemberAddressStore.AddressType.NORMAL.name());
                        memberAddressStore.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
                        memberAddressStore.setUpdateTime(now);
                        memberAddressStore.setUpdateUserId(0l);
                        memberAddressStore.setCreateUserId(0l);
                        memberAddressStore.setIsCustom(0);

                        storeInfo2 = storeInfo;
                    }
                    distant = (int)radius;
                }

            }

            if(memberAddressStore != null){
                memberAddressStoreMapper.insert(memberAddressStore);
                return storeInfo2;
            }else {
                return storeInfo2;
            }
        }else {
            return storeInfo2;
        }
    }



    @Override
    @Transactional
    public Object updateAddress(Integer isElevator, String consignee, Integer sex, String phone, String address, Integer storey, Long addressId,
                             Integer isDefault, double lng, double lat, Long adcode, String tag, String locationAddress) {

        AssertExt.notBlank(tag, "tag为空");
        AssertExt.notBlank(locationAddress, "locationAddress为空");
        AssertExt.notBlank(consignee, "consignee为空");
        AssertExt.notNull(sex, "sex为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notNull(storey, "storey为空");
        AssertExt.notNull(addressId, "addressId为空");
        AssertExt.notNull(lng, "lng为空");
        AssertExt.notNull(lat, "lat为空");
        AssertExt.notNull(adcode, "adcode为空");
        AssertExt.notNull(isDefault, "isDefault为空");

        if(isElevator == -1){
            storey = -1;
        }

        Map<String, Object> map = new HashMap<>();
        MemberAddress memberAddress = new MemberAddress();

        UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", addressId);
        updateWrapper.set("consignee", consignee);
        updateWrapper.set("sex", sex);
        updateWrapper.set("telephone", phone);
        updateWrapper.set("address", address);
        updateWrapper.set("storey", storey);
        updateWrapper.set("lng", lng);
        updateWrapper.set("lat", lat);
        updateWrapper.set("adcode", adcode);
        updateWrapper.set("tag", tag);
        updateWrapper.set("location_address", locationAddress);

        //查询出该地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper3 = new QueryWrapper<>();
        memberAddressQueryWrapper3.eq("id", addressId);
        MemberAddress memberAddress3 = memberAddressMapper.selectOne(memberAddressQueryWrapper3);
        //判断有多少条地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper2 = new QueryWrapper<>();
        memberAddressQueryWrapper2.eq("member_id", memberAddress3.getMemberId());
        memberAddressQueryWrapper2.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        List<MemberAddress> memberAddressList = memberAddressMapper.selectList(memberAddressQueryWrapper2);

        if(memberAddressList.size() > 1 ){
            if(isDefault == 2){
                for(MemberAddress memberAddress1: memberAddressList){
                        //1.先把所有地址改为非默认
                        UpdateWrapper<MemberAddress> updateWrapper3 = new UpdateWrapper<>();
                        updateWrapper3.eq("member_id", memberAddress3.getMemberId());
                        updateWrapper3.set("is_default", 2);
                        memberAddressMapper.update(memberAddress, updateWrapper3);

                        if(!memberAddress1.getId().equals(addressId)){
                            //2.再把第另外1条改为默认
                            UpdateWrapper<MemberAddress> updateWrapper2 = new UpdateWrapper<>();
                            updateWrapper2.eq("id", memberAddress1.getId());
                            updateWrapper2.set("is_default", 1);
                            memberAddressMapper.update(memberAddress1, updateWrapper2);
                            map.put("code", 1);
                            map.put("msg", "地址改为非默认成功，返回其他默认地址");
                            memberAddress1.setIsDefault(1);
                            map.put("memberAddress", memberAddress1);
                            break;
                        }
                }
                //最后把这条地址修改所有字段
                updateWrapper.set("is_default", 2);
                memberAddressMapper.update(memberAddress3, updateWrapper);

                //返回默认地址的门店id
                MemberAddress memberAddress1 = (MemberAddress)map.get("memberAddress");
                return getAddressStore(memberAddress1.getId(), map);
            }else {
                //两条以上地址，全改为非默认
                memberAddressMapper.updateNotDeafult(memberAddressList);
                //再改这条为默认
                updateWrapper.set("is_default", 1);
                memberAddressMapper.update(memberAddress3, updateWrapper);

                map.put("code","0");
                map.put("msg","地址编辑改为默认成功");
                map.put("memberAddress", memberAddressMapper.selectOne(memberAddressQueryWrapper3));

                //返回默认地址的店铺
                return getAddressStore(addressId, map);
            }
        }else {
            if(isDefault == 2){
                map.put("code","2");
                map.put("msg","地址编辑改为非默认，只有一条用户地址必须是默认的");
                return map;
            }else {
                updateWrapper.set("is_default", isDefault);
                memberAddressMapper.update(memberAddress, updateWrapper);

                map.put("code","0");
                map.put("msg","地址编辑改为默认成功");
                map.put("memberAddress", memberAddressMapper.selectOne(memberAddressQueryWrapper3));

                //返回默认地址的店铺
                return getAddressStore(addressId, map);
            }
        }
    }


    @Override
    @Transactional
    public Object updateAddress2(Integer isElevator, String consignee, Integer sex, String phone, String address, Integer storey, Long addressId,
                                 Integer isDefault, double lng, double lat, Long adcode, String tag, String locationAddress, Long loginUserId) {

        AssertExt.notBlank(tag, "tag为空");
        AssertExt.notBlank(locationAddress, "locationAddress为空");
        AssertExt.notBlank(consignee, "consignee为空");
        AssertExt.notNull(sex, "sex为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notNull(storey, "storey为空");
        AssertExt.notNull(addressId, "addressId为空");
        AssertExt.notNull(lng, "lng为空");
        AssertExt.notNull(lat, "lat为空");
        AssertExt.notNull(adcode, "adcode为空");
        AssertExt.notNull(isDefault, "isDefault为空");

        if(isElevator == -1){
            storey = -1;
        }

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        MemberAddress memberAddress = new MemberAddress();

        UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", addressId);
        updateWrapper.set("consignee", consignee);
        updateWrapper.set("sex", sex);
        updateWrapper.set("telephone", phone);
        updateWrapper.set("address", address);
        updateWrapper.set("storey", storey);
        updateWrapper.set("lng", lng);
        updateWrapper.set("lat", lat);
        updateWrapper.set("adcode", adcode);
        updateWrapper.set("update_user_id", loginUserId);
        updateWrapper.set("update_time", now);
        updateWrapper.set("tag", tag);
        updateWrapper.set("location_address", locationAddress);

        //查询出该地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper3 = new QueryWrapper<>();
        memberAddressQueryWrapper3.eq("id", addressId);
        MemberAddress memberAddress3 = memberAddressMapper.selectOne(memberAddressQueryWrapper3);
        //判断有多少条地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper2 = new QueryWrapper<>();
        memberAddressQueryWrapper2.eq("member_id", memberAddress3.getMemberId());
        memberAddressQueryWrapper2.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        List<MemberAddress> memberAddressList = memberAddressMapper.selectList(memberAddressQueryWrapper2);

        if(memberAddressList.size() > 1 ){
            if(isDefault == 2){
                for(MemberAddress memberAddress1: memberAddressList){
                    //1.先把所有地址改为非默认
                    UpdateWrapper<MemberAddress> updateWrapper3 = new UpdateWrapper<>();
                    updateWrapper3.eq("member_id", memberAddress3.getMemberId());
                    updateWrapper3.set("is_default", 2);
                    memberAddressMapper.update(memberAddress, updateWrapper3);

                    if(!memberAddress1.getId().equals(addressId)){
                        //2.再把第另外1条改为默认
                        UpdateWrapper<MemberAddress> updateWrapper2 = new UpdateWrapper<>();
                        updateWrapper2.eq("id", memberAddress1.getId());
                        updateWrapper2.set("is_default", 1);
                        memberAddressMapper.update(memberAddress1, updateWrapper2);
                        break;
                    }
                }
                //最后把这条地址修改所有字段
                updateWrapper.set("is_default", 2);
                memberAddressMapper.update(memberAddress3, updateWrapper);

                return map;
            }else {
                //两条以上地址，全改为非默认
                memberAddressMapper.updateNotDeafult(memberAddressList);
                //再改这条为默认
                updateWrapper.set("is_default", 1);
                memberAddressMapper.update(memberAddress3, updateWrapper);

                map.put("code","0");
                map.put("msg","地址编辑改为默认成功");

                //返回默认地址的店铺
                return map;
            }
        }else {
            if(isDefault == 2){
                map.put("code","2");
                map.put("msg","地址编辑改为非默认，只有一条用户地址必须是默认的");
                return map;
            }else {
                updateWrapper.set("is_default", isDefault);
                memberAddressMapper.update(memberAddress, updateWrapper);

                map.put("code","0");
                map.put("msg","地址编辑改为默认成功");

                return map;
            }
        }
    }


    @Override
    @Transactional
    public Object deleteMemberAddress2(List<Long> ids, Long loginUserId){

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        //循环对每个地址处理
        for(int i = 0; i < ids.size(); i ++){

            //查询删除的地址
            QueryWrapper<MemberAddress> memberAddressQueryWrapper = new QueryWrapper<>();
            memberAddressQueryWrapper.eq("id", ids.get(i));
            MemberAddress memberAddress = memberAddressMapper.selectOne(memberAddressQueryWrapper);

            //把地址关联表删除
            MemberAddressStore memberAddressStore = new MemberAddressStore();
            UpdateWrapper<MemberAddressStore> updateWrapper5 = new UpdateWrapper<>();
            updateWrapper5.eq("address_id", ids.get(i));
            updateWrapper5.set("is_del", CommonConstant.IsDel.IS_DEL);
            updateWrapper5.set("update_time", now);
            updateWrapper5.set("update_user_id", loginUserId);

            memberAddressStoreMapper.update(memberAddressStore, updateWrapper5);

            if(memberAddress.getIsDefault() == 1){
                //删除默认地址
                UpdateWrapper<MemberAddress> memberAddressQueryWrapper2 = new UpdateWrapper<>();
                memberAddressQueryWrapper2.eq("id", ids.get(i));
                memberAddressQueryWrapper2.set("is_del", CommonConstant.IsDel.IS_DEL);
                memberAddressQueryWrapper2.set("is_default", 2);
                memberAddressMapper.update(memberAddress, memberAddressQueryWrapper2);
                //查询所有没删除的地址
                QueryWrapper<MemberAddress> memberAddressQueryWrapper1 = new QueryWrapper<>();
                memberAddressQueryWrapper1.eq("member_id", memberAddress.getMemberId());
                memberAddressQueryWrapper1.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
                List<MemberAddress> list = memberAddressMapper.selectList(memberAddressQueryWrapper1);

                //把第一条改为默认地址
                if(list.size() > 0){
                    UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id", list.get(0).getId());
                    updateWrapper.set("is_default", 1);
                    memberAddressMapper.update(list.get(0), updateWrapper);

                }
            }else {
                UpdateWrapper<MemberAddress> memberAddressQueryWrapper2 = new UpdateWrapper<>();
                memberAddressQueryWrapper2.eq("id", ids.get(i));
                memberAddressQueryWrapper2.set("is_del", CommonConstant.IsDel.IS_DEL);
                memberAddressQueryWrapper2.set("is_default", 2);
                memberAddressMapper.update(memberAddress, memberAddressQueryWrapper2);


            }
        }


        map.put("code","0");
        map.put("msg","批量删除成功");
        return map;

    }


    @Override
    @Transactional
    public Object deleteAddress(Long addressId) {

        Map<String, Object> map = new HashMap<>();
        AssertExt.notNull(addressId, "addressId为空");

        //查询删除的地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper = new QueryWrapper<>();
        memberAddressQueryWrapper.eq("id", addressId);
        MemberAddress memberAddress = memberAddressMapper.selectOne(memberAddressQueryWrapper);
        if(memberAddress == null){
            map.put("code", 3);
            map.put("msg", ",地址不存在");
            return map;
        }

        //把地址关联表删除
        MemberAddressStore memberAddressStore = new MemberAddressStore();
        UpdateWrapper<MemberAddressStore> updateWrapper5 = new UpdateWrapper<>();
        updateWrapper5.eq("address_id", addressId);
        updateWrapper5.set("is_del", CommonConstant.IsDel.IS_DEL);
        memberAddressStoreMapper.update(memberAddressStore, updateWrapper5);

        if(memberAddress.getIsDefault() == 1){
            //删除默认地址
            UpdateWrapper<MemberAddress> memberAddressQueryWrapper2 = new UpdateWrapper<>();
            memberAddressQueryWrapper2.eq("id", addressId);
            memberAddressQueryWrapper2.set("is_del", CommonConstant.IsDel.IS_DEL);
            memberAddressQueryWrapper2.set("is_default", 2);
            memberAddressMapper.update(memberAddress, memberAddressQueryWrapper2);
            //查询所有没删除的地址
            QueryWrapper<MemberAddress> memberAddressQueryWrapper1 = new QueryWrapper<>();
            memberAddressQueryWrapper1.eq("member_id", memberAddress.getMemberId());
            memberAddressQueryWrapper1.eq("is_del", CommonConstant.IsDel.IS_NOT_DEL);
            List<MemberAddress> list = memberAddressMapper.selectList(memberAddressQueryWrapper1);

            //把第一条改为默认地址
            if(list.size() > 0){
                UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", list.get(0).getId());
                updateWrapper.set("is_default", 1);
                memberAddressMapper.update(list.get(0), updateWrapper);

                //返回默认地址
                map.put("code", 0);
                map.put("msg", "默认地址删除成功，返回其他默认地址");
                list.get(0).setIsDefault(1);
                map.put("memberAddress", list.get(0));

                //返回默认地址的店铺
                return getAddressStore(list.get(0).getId(), map);
            }else {
                map.put("code", 1);
                map.put("msg", "默认地址删除成功，用户没有地址了");
                return map;
            }
        }else {
            UpdateWrapper<MemberAddress> memberAddressQueryWrapper2 = new UpdateWrapper<>();
            memberAddressQueryWrapper2.eq("id", addressId);
            memberAddressQueryWrapper2.set("is_del", CommonConstant.IsDel.IS_DEL);
            memberAddressQueryWrapper2.set("is_default", 2);
            memberAddressMapper.update(memberAddress, memberAddressQueryWrapper2);

            map.put("code", 2);
            map.put("msg", "非默认地址删除成功");
            return map;

        }

    }

    @Override
    public IPage<MemberAddressStoreCredit> getMemberStoreCreditList(Long storeId, Integer currentPage, Integer pageSize,
                                                                    String name, String telephone, String status){
        AssertExt.notNull(storeId, "storeId为空");
        if(StringUtils.isBlank(telephone))telephone=null;
        if(StringUtils.isBlank(status))status=null;
        if(StringUtils.isBlank(name))name=null;

        IPage<MemberAddressStoreCredit> page = new Page<MemberAddressStoreCredit>(currentPage, pageSize);
        IPage<MemberAddressStoreCredit> memberInfoIPage = memberAddressStoreCreditMapper.getMemberStoreCreditList(page, storeId, name, telephone, status);
        return memberInfoIPage;
    }

    @Override
    public MemberAddressStoreCredit getMemberStoreCreditById(Long creditId) {
        AssertExt.notNull(creditId, "creditId为空");
        return memberAddressStoreCreditMapper.selectById(creditId);
    }

    @Override
    @Transactional
    public int recharge(Long money, Long balanceId, Long memberId, Integer payType) {

        AssertExt.notNull(balanceId, "balanceId为空");
        AssertExt.notNull(memberId, "memberId为空");
        AssertExt.notNull(money, "bouns为空");
        AssertExt.notNull(payType, "payType为空");

        LocalDateTime now = LocalDateTime.now();

        //充值
        memberBalanceMapper.recharge(money, balanceId, 0l, now);

        //生成充值记录
        MemberBalanceLog memberBalanceLog = new MemberBalanceLog();
        memberBalanceLog.setCreateTime(now);
        memberBalanceLog.setCreateUserId(0l);
        memberBalanceLog.setMemberId(memberId);
        memberBalanceLog.setOperate("ADD");
        memberBalanceLog.setRmb(money);
        memberBalanceLog.setPayType(payType);

        memberBalanceLogMapper.insert(memberBalanceLog);
        return 1;
    }

    @Override
    public int cash(Integer bouns, Long balanceId, Long memberId) {
        AssertExt.notNull(balanceId, "balanceId为空");
        AssertExt.notNull(memberId, "memberId为空");
        AssertExt.notNull(bouns, "bouns为空");

        LocalDateTime now = LocalDateTime.now();

        MemberBalance memberBalance = memberBalanceMapper.selectById(balanceId);
        AssertExt.isFalse(memberBalance.getBouns().longValue() < bouns, "红包余额不足");

        UpdateWrapper<MemberBalance> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", balanceId);
        updateWrapper.set("create_time", now);
        updateWrapper.set("bouns", memberBalance.getBouns() - bouns);
        memberBalanceMapper.update(memberBalance, updateWrapper);


        //生成充值记录
        MemberBalanceLog memberBalanceLog = new MemberBalanceLog();
        memberBalanceLog.setCreateTime(now);
        memberBalanceLog.setCreateUserId(0l);
        memberBalanceLog.setMemberId(memberId);
        memberBalanceLog.setOperate("CASH");
        memberBalanceLog.setRmb(Long.valueOf(bouns));
        memberBalanceLog.setPayType(null);
        memberBalanceLogMapper.insert(memberBalanceLog);

        return 1;
    }

    @Override
    public int bouns(Integer bouns, Long memberId) {
        AssertExt.notNull(memberId, "memberId为空");
        AssertExt.notNull(bouns, "money为空");

        LocalDateTime now = LocalDateTime.now();

        QueryWrapper<MemberBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);
        MemberBalance memberBalance = memberBalanceMapper.selectOne(queryWrapper);

        UpdateWrapper<MemberBalance> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", memberBalance.getId());
        updateWrapper.set("create_time", now);
        updateWrapper.set("bouns", memberBalance.getBouns() + bouns);
        memberBalanceMapper.update(memberBalance, updateWrapper);


        //生成充值记录
        MemberBalanceLog memberBalanceLog = new MemberBalanceLog();
        memberBalanceLog.setCreateTime(now);
        memberBalanceLog.setCreateUserId(0l);
        memberBalanceLog.setMemberId(memberId);
        memberBalanceLog.setOperate("BOUNS");
        memberBalanceLog.setRmb(Long.valueOf(bouns));
        memberBalanceLog.setPayType(null);
        memberBalanceLogMapper.insert(memberBalanceLog);

        return 1;
    }

    @Override
    public MemberBalance getBalance(Long memberId) {

        LocalDateTime now = LocalDateTime.now();

        QueryWrapper<MemberBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);

        MemberBalance memberBalance = memberBalanceMapper.selectOne(queryWrapper);
        if(memberBalance == null){
            memberBalance = new MemberBalance();

            memberBalance.setBalance(0l);
            memberBalance.setMemberId(memberId);
            memberBalance.setCreateTime(now);
            memberBalance.setUpdateTime(now);
            memberBalance.setCreateUserId(0l);
            memberBalance.setUpdateUserId(0l);

            memberBalanceMapper.insert(memberBalance);
        }
        return memberBalance;
    }


    @Override
    public MemberBalance getBalance2(Long memberId) {

        LocalDateTime now = LocalDateTime.now();

        QueryWrapper<MemberBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);

        MemberBalance memberBalance = memberBalanceMapper.selectOne(queryWrapper);
        if(memberBalance == null){
            memberBalance = new MemberBalance();

            memberBalance.setBalance(0l);
            memberBalance.setMemberId(memberId);
            memberBalance.setCreateTime(now);
            memberBalance.setUpdateTime(now);
            memberBalance.setCreateUserId(0l);
            memberBalance.setUpdateUserId(0l);

            memberBalanceMapper.insert(memberBalance);
        }
        return memberBalance;
    }


    @Override
    public IPage<MemberBalance> getBalanceList(Integer currentPage, Integer pageSize, String telephone, String name) {
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<MemberBalance> page = new Page<>(currentPage, pageSize);
        return memberBalanceMapper.getBalanceList(page ,telephone, name);
    }

    @Override
    public IPage<MemberBalanceLog> getBalanceLogList(Long memberId, Integer currentPage, Integer pageSize, String name, String telephone) {
        AssertExt.notNull(memberId, "memberId为空");
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<MemberBalanceLog> page = new Page<>(currentPage, pageSize);
        return memberBalanceLogMapper.getBalanceLogList(page, memberId, name, telephone);
    }

    @Override
    public IPage<MemberBalanceLog> getBalanceLogList2(Long memberId, Integer currentPage, Integer pageSize, String name, String telephone) {
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<MemberBalanceLog> page = new Page<>(currentPage, pageSize);
        return memberBalanceLogMapper.getBalanceLogList(page, memberId, name, telephone);
    }


    @Override
    public List<MemberAddressStoreCreditLog> getMemberStoreCreditLog(Long creditId) {
        AssertExt.notNull(creditId, "creditId为空");
        QueryWrapper<MemberAddressStoreCreditLog> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_address_store_credit_id", creditId);
        return memberAddressStoreCreditLogMapper.selectList(queryWrapper);
    }

    @Override
    public List<MemberAddressStoreCreditLog> getMemberStoreCreditLog2(Long storeId) {
        AssertExt.notNull(storeId, "storeId为空");
        return memberAddressStoreCreditLogMapper.getMemberAddressStoreCreditLog(storeId);
    }

    @Override
    @Transactional
    public int updateUserStatus(String status, Long memberId, Long loginUserId){

        AssertExt.notNull(memberId, "memberId为空");
        AssertExt.notBlank(status, "status为空");

        MemberInfo memberInfo = new MemberInfo();

        UpdateWrapper<MemberInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", memberId);
        updateWrapper.set("status", status);

        memberInfoMapper.update(memberInfo, updateWrapper);
        return 1;
    }

    @Override
    @Transactional
    public Map updateAddressStore(Long addressId, Long storeId, Long loginUserId){

        AssertExt.notNull(addressId, "addressId为空");
        AssertExt.notNull(storeId, "storeId为空");

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        //查出门店
        StoreInfo storeInfo = storeInfoMapper.selectById(storeId);

        //查询出该地址的门店关联表
        List<MemberAddressStoreVo> memberAddressStoreList = memberAddressStoreMapper.getMemberAddressStoreAndSoreType(addressId);

        Long addressStoreId = 0l;
        for (MemberAddressStoreVo memberAddressStoreVo: memberAddressStoreList){
            if(memberAddressStoreVo.getStoreTypeId().longValue() == storeInfo.getStoreTypeId().longValue()){
                addressStoreId = memberAddressStoreVo.getId();
            }
        }

        if(addressStoreId.longValue() != 0l){
            //修改关联表
            MemberAddressStore memberAddressStore = new MemberAddressStore();
            UpdateWrapper<MemberAddressStore> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", addressStoreId);
            updateWrapper.set("store_id", storeId);
            updateWrapper.set("update_time", now);
            updateWrapper.set("update_user_id", loginUserId);
            memberAddressStoreMapper.update(memberAddressStore, updateWrapper);

            map.put("code", 1);
            map.put("msg", "修改成功");
        }else {
            map.put("code", 2);
            map.put("msg", "地址没有该类型关联门店，不能修改");
        }
        return map;
    }



    @Override
    @Transactional
    public Object updateDeafultAddress(Long addressId) {

        Map<String, Object> map = new HashMap<>();

        //查询出该地址
        MemberAddress memberAddress3 = memberAddressMapper.selectById(addressId);

        //判断有多少条地址
        QueryWrapper<MemberAddress> memberAddressQueryWrapper = new QueryWrapper<>();
        memberAddressQueryWrapper.eq("member_id", memberAddress3.getMemberId());
        List<MemberAddress> memberAddressList = memberAddressMapper.selectList(memberAddressQueryWrapper);

        UpdateWrapper<MemberAddress> updateWrapper = new UpdateWrapper<>();

        //两条以上地址
        if(memberAddressList.size() > 1){
                //先全部改为非默认
                memberAddressMapper.updateNotDeafult(memberAddressList);

                //改这条为默认
                updateWrapper.eq("id", addressId);
                updateWrapper.set("is_default", 1);
                memberAddressMapper.update(memberAddress3, updateWrapper);

                map.put("code","0");
                map.put("msg","地址编辑改为默认成功");
                memberAddress3.setIsDefault(1);
                map.put("memberAddress", memberAddress3);

                //返回默认地址的店铺
                return getAddressStore(addressId, map);
        }else {
                map.put("code","1");
                map.put("msg","只有一条地址不能切换");
                return map;
        }
    }

    /**
     * 获取门店和类型
     * @param addressId
     * @param map
     * @return
     */
    public Map getAddressStore(Long addressId, Map<String, Object> map){

        List<MemberAddressStoreVo> memberAddressStoreList = memberAddressStoreMapper.getMemberAddressStoreAndSoreType(addressId);
        Map<String, Object> store = new HashMap<>();
        Map<String, Object> gas = null;
        Map<String, Object> market = null;
        Map<String, Object> fastfood = null;
        Map<String, Object> water = null;
        Map<String, Object> marketAndFastfood = null;

        if(memberAddressStoreList != null){
            for (MemberAddressStoreVo memberAddressStore: memberAddressStoreList){
                if(memberAddressStore.getStoreTypeId().longValue() == 0){
                    gas = new HashMap<>();
                    gas.put("id", memberAddressStore.getStoreId());
                    gas.put("storeName", memberAddressStore.getStoreName());
                    gas.put("phone", memberAddressStore.getPhone());
                    gas.put("address",memberAddressStore.getAddress());
                }else if (memberAddressStore.getStoreTypeId().longValue() == 3){
                    market = new HashMap<>();
                    market.put("id", memberAddressStore.getStoreId());
                    market.put("storeName", memberAddressStore.getStoreName());
                    market.put("phone", memberAddressStore.getPhone());
                    market.put("address",memberAddressStore.getAddress());
                }else if(memberAddressStore.getStoreTypeId() == 2){
                    fastfood = new HashMap<>();
                    fastfood.put("id", memberAddressStore.getStoreId());
                    fastfood.put("storeName", memberAddressStore.getStoreName());
                    fastfood.put("phone", memberAddressStore.getPhone());
                    fastfood.put("address",memberAddressStore.getAddress());
                }else if(memberAddressStore.getStoreTypeId() == 1){
                    water = new HashMap<>();
                    water.put("id", memberAddressStore.getStoreId());
                    water.put("storeName", memberAddressStore.getStoreName());
                    water.put("phone", memberAddressStore.getPhone());
                    water.put("address",memberAddressStore.getAddress());
                }else if(memberAddressStore.getStoreTypeId() == 4){
                    marketAndFastfood = new HashMap<>();
                    marketAndFastfood.put("id", memberAddressStore.getStoreId());
                    marketAndFastfood.put("storeName", memberAddressStore.getStoreName());
                    marketAndFastfood.put("phone", memberAddressStore.getPhone());
                    marketAndFastfood.put("address",memberAddressStore.getAddress());
                }
            }
        }else {

        }
        if(gas != null)store.put("gas", gas);
        if(market != null)store.put("market", market);
        if(fastfood != null)store.put("fastfood", fastfood);
        if(water != null)store.put("water", water);
        if(marketAndFastfood != null)store.put("marketAndFastfood", marketAndFastfood);

        if(!store.isEmpty())map.put("store", store);
        return map;
    }



}
