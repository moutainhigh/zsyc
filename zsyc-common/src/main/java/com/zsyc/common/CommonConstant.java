package com.zsyc.common;

public class CommonConstant {

    public interface IsDel{

        //未删除
        public static final Integer IS_NOT_DEL=0;

        //已删除
        public static final Integer IS_DEL=1;

    }

    public interface GoodsInfo{

        //为节点
        public static final Integer IS_LEAF=1;

        //父节点
        public static final Integer IS_NOT_LEAF=0;

        //主节点
        public static final Long IS_PARENT=-1L;

        //小数点后两位
        public static final String DECIMAL_POINT="100";
    }

    public interface Operate{

        //增加
        public static final String ADD="ADD";

        //扣减
        public static final String REDUCE="REDUCE";
    }

    public interface GoodsPriceStatus{

        //上架
        public static final String ON_SALE="ON_SALE";

        //下架
        public static final String OFF_SALE="OFF_SALE";
    }

    public interface GoodsType{

        //普通商品
        public static final Integer NORMAL=1;

        //组合商品
        public static final Integer Group=2;
    }

    public interface AddressStore{

        //NORMAL
        public static final String NORMAL="NORMAL";

        //VIP
        public static final String VIP="VIP";
    }

    public interface CategoryType{

        //菜市场
        public static final String MARKET="MARKET";

        //快捷菜
        public static final String QUICKFOOD="QUICKFOOD";

        //煤气
        public static final String GAS="GAS";

        //订水
        public static final String WATER="WATER";

        //父级节点
        public static final Long PARENT_NODE=-1L;

        //菜市场二级节点
        public static final Long MARKET_CHILD_NODE=1L;
    }

    public interface CategoryName{

        //互换瓶
        public static final String EXCHANGE_BOTTLES="互换瓶";

        //新租瓶
        public static final String RENT_BOTTLES="新租瓶";

        //互换水
        public static final String EXCHANGE_WATER="互换水";

        //桶
        public static final String BUCKET="桶";

        //新租桶
        public static final String RENT_BUCKET="新租桶";
    }

    public interface GoodsStyle{

        //普通

        public static final Integer NORMAL=0;
        //瓶
        public static final Integer BOTTLE=1;

        //气
        public static final Integer GAS=2;

        //水
        public static final Integer WATER=3;

        //配料
        public static final Integer INGREDIENT=4;

        //水桶
        public static final Integer BUCKET=5;
    }

    public interface GroupType{

        //直接生成组合商品
        public static final Integer ONE=1;

        //关联组合商品
        public static final Integer MULTI=2;
    }

    public interface GoodsAttributeType{

        //售卖单位
        public static final Integer IS_SALE=1;

        //非售卖单位
        public static final Integer IS_NOT_SALE=0;
    }

    public interface Display{

        //展示
        public static final Integer IS_DISPLAY=1;

        //不展示
        public static final Integer IS_NOT_DISPLAY=0;
    }

    public interface BargainPrice{

        //特价
        public static final Integer IS_BARGAINPRICE=1;

        //不特价
        public static final Integer IS_NOT_BARGAINPRICE=0;
    }

    public interface Recommend{

        //推荐
        public static final Integer IS_RECOMMEND=1;

        //不推荐
        public static final Integer IS_NOT_RECOMMEND=0;
    }

    public interface GoodsInfoType{

        //菜市场
        public static final Integer ATTRIBUTES=0;

        //快捷菜
        public static final Integer GROUP=1;
    }

    public interface StoreType{

        //0:气店；1：水店；2：快捷菜；3菜市场；4：快捷菜和菜市场组合
        //0:气店
        public static final Long GAS=0L;

        //1：水店
        public static final Long WATER=1L;

        //2：快捷菜
        public static final Long QUICKFOOD=2L;

        //3菜市场
        public static final Long MARKET=3L;

        //4：快捷菜和菜市场组合
        public static final Long MARKETANDQUICKFOOD=4L;

        //水桶
        public static final Integer BUCKET=5;
    }

    public interface GroupGoodsType{

        //没有子商品信息
        public static final Integer IS_NOT_CHILD_LIST=1;

        //包含子商品信息
        public static final Integer IS_CHILD_LIST=2;
    }
}
