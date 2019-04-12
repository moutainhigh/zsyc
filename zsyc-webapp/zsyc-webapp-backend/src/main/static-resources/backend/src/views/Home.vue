<template>
  <el-container class="home-container">
    <!-- 侧边栏 -->
    <el-aside :width="collapse ? '0' : '250px'">
      <!-- 左侧的menu菜单 开始 -->
      <el-scrollbar style="height:100%">
        <div class="aside-header">
          <h3 class="title">柴米油盐后台</h3>
          <div class="user">
            <img src alt>
            <!-- <el-select size="mini"></el-select> -->
          </div>
        </div>
        <el-menu
                background-color="#333744"
                text-color="#fff"
                active-text-color="#409EFF"
                :router="true"
                :unique-opened="true"
                v-for="item in list"
                :default-active="activePath"
                :key="item.name"
                style="border:none;"
        >
          <el-submenu  v-if="item.children" :index="item.name">
            <template slot="title">
              <i :class="item.icon"></i>
              <span>{{item.name}}</span>
            </template>
            <el-menu-item  v-for="subItem in item.children" :index="subItem.path" :key="subItem.name" @click="saveNavState(subItem.path)">
              <i :class="subItem.icon"></i>
              <span>{{subItem.name}}</span>
            </el-menu-item>
          </el-submenu>
          <el-menu-item v-if="!item.children" :index="item.path"  @click="saveNavState(item.path)">
            <i :class="item.icon"></i>
            <span>{{item.name}}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>


    <!-- 头部区域 -->
    <el-container>
      <el-header height="50px">
        <div class="col" @click="collapse=!collapse">缩小</div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>











<script>
    export default {
        data() {
            return {
                collapse: false,
                list: [
                    {
                        name: "经营大数据",
                        icon: "el-icon-bell",
                        path: "/datas"
                    },
                    {
                        name: "经营中心",
                        icon: "el-icon-service",
                        children: [
                            { path: "/StoreManager", name: "店铺管理", icon: "el-icon-service" },
                            { path: "/Test", name: "团队管理", icon: "el-icon-service" },
                            { path: "/1", name: "报表统计", icon: "el-icon-service" }
                        ]
                    },
                    {
                        name: "会员中心",
                        icon: "el-icon-view",
                        children: [
                            { path: "/MemberList", name: "会员列表", icon: "el-icon-view" },
                            { path: "/MemberGroup", name: "会员分群", icon: "el-icon-view" },
                            { path: "/MemberLeve", name: "消费统计", icon: "el-icon-view" },
                            { path: "/MemberCoupon", name: "优惠体系", icon: "el-icon-view" }
                        ]
                    },
                    {
                        name: "商品中心",
                        icon: "el-icon-news",
                        children: [
                          { path: "/Goods", name: "商品列表", icon: "el-icon-news" },
                          { path: "/GoodsPriceManage", name: "商品价格管理", icon: "el-icon-news" },
                          { path: "/GoodsInsert", name: "新增商品", icon: "el-icon-news" },
                          { path: "/GoodsCategoryManage", name: "商品属性管理", icon: "el-icon-news" }
                        ]
                    },
                    {
                        name: "交易中心",
                        icon: "el-icon-upload",
                        children: [
                            { path: "/new_order", name: "新订单追踪", icon: "el-icon-news" },
                            { path: "/booking_order", name: "预约订单", icon: "el-icon-upload" },
                            { path: "/pay_type", name: "支付类型", icon: "el-icon-upload" }

                        ]
                    },
                    {
                        name: "备货中心",
                        icon: "el-icon-rank",
                        children: [
                            { path: "/Sweep", name: "统一扫货", icon: "el-icon-rank" },
                            { path: "/Sorting", name: "统一分拣", icon: "el-icon-rank" },
                            { path: "/Production", name: "统一生产", icon: "el-icon-rank" }
                        ]
                    },
                    {
                        name: "配送中心",
                        icon: "el-icon-bell",
                        children: [
                            { path: "/6", name: "送货订单", icon: "el-icon-bell" },
                            { path: "/6", name: "自提订单", icon: "el-icon-bell" },
                            { path: "/6", name: "运力池", icon: "el-icon-bell" }
                        ]
                    },
                    {
                        name: "客服中心",
                        icon: "el-icon-bell",
                        children: [
                            { path: "/Followup", name: "缺货退换货跟进", icon: "el-icon-bell" },
                            { path: "/CustomerService", name: "过期未取跟进", icon: "el-icon-bell" }
                        ]
                    },
                    {
                        name: "账号信息",
                        icon: "el-icon-bell",
                        path: "/8"
                    },
                    {
                        name: "帮助与反馈",
                        icon: "el-icon-bell",
                        path: "/9"
                    }
                ]
            };
        },
        methods: {
            saveNavState(activePath) {
                // 把当前激活的路由地址保存到sessionStorage
                sessionStorage.setItem("activePath", activePath);
            }
        },
        created() {
            if (!sessionStorage.getItem("activePath")) {
                sessionStorage.setItem("activePath", "/datas");
            }
            this.activePath = sessionStorage.getItem("activePath");
        }
    };
</script>
<style lang="less" scoped>
  .home-container {
    height: 100%;
    .el-header {
      background-color: #373d41;
      height: 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0;
      padding-right: 20px;
      .col {
        margin-left: 20px;
        width: 50px;
        background: yellow;
        text-align: center;
      }
    }
    .el-aside {
      background-color: #333744;
      user-select: none;
      height: 100%;
      overflow: hidden;
      .aside-header {
        .title {
          font-size: 16px;
          color: #fff;
          text-align: center;
          line-height: 50px;
          width: 200px;
          margin: 0;
        }
        .user {
          display: flex;
          height: 150px;
          background: #140d0d;
          flex-direction: column;
          align-items: center;
          justify-content: space-around;
          img {
            width: 70px;
            height: 70px;
            background: yellow;
          }
          .el-select {
            width: 150px;
          }
        }
      }
    }
    .el-main {
      background-color: #eaedf1;
    }
  }
</style>
