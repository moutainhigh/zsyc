package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderProtocolInfoPo;
import com.zsyc.order.vo.OrderProtocolInfoVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 协议 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface OrderProtocolInfoMapper extends BaseMapper<OrderProtocolInfoPo> {

    /**
     * 查询会员协议（已分页）
     *
     * @param page
     * @param ls
     * @return
     */
    IPage<OrderProtocolInfoPo> getOrderProtocolInfo(IPage<OrderProtocolInfoPo> page, @Param("memberAddressList") List<MemberAddress> ls);

    /**
     * 查询会员全部协议（不分页）
     *
     * @param ls
     * @return
     */
    List<OrderProtocolInfoPo> getOrderProtocolInfo(@Param("memberAddressList") List<MemberAddress> ls);


    /**
     * 更新协议当前押金
     *
     * @param protocolNo
     * @param amount
     */
    void updateDepositCurrent(@Param("protocolNo") String protocolNo, @Param("amount") int amount);


    /**
     * 查看店铺所有用户的协议
     *
     * @param orderProtocolInfoVo
     * @return
     */
    IPage<OrderProtocolInfoPo> getAllProtocolByStoreId(IPage<OrderProtocolInfoPo> page, @Param("orderProtocolInfoVo") OrderProtocolInfoVo orderProtocolInfoVo);

    /**
     * 更新最后一次缴纳租金时间
     *
     * @param protocolNo
     * @param now
     */
    void updateOrderProtocolLastRentPaymentTime(@Param("protocolNo") String protocolNo, @Param("now") LocalDateTime now);
}
