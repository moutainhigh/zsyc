<template>
    <div>
        <table width="100%" cellpadding="10px">
            <tr>
                <td> 订单编号：<el-input v-model="orderNo" style="width:200px"></el-input></td>
                <td> 商品名称：<el-input v-model="goodName" style="width:200px"></el-input></td>
                <td>
                    下单时间：<el-date-picker v-model="startTime" type="datetime"  placeholder="选择开始时间" value-format="yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    <el-date-picker v-model="endTime" type="datetime"  placeholder="选择结束时间" value-format="yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                </td>
            </tr>
            <tr>
                <td> 客户名称：<el-input v-model="memberName" style="width:200px"></el-input></td>
                <td> 客户电话：<el-input v-model="memberPhone" style="width:200px"></el-input></td>
                <td> 下单账号：<el-input v-model="account" style="width:445px"></el-input></td>
            </tr>
        </table>
        <div style="margin-left: 650px;margin-top: 10px;padding: 20px">
            <el-button type="primary">查询</el-button>
            <el-button type="primary" @click="reset">重置</el-button>
            <el-button type="primary">批量导出</el-button>
        </div>
        <div>
            <el-radio-group v-model="orderStatus" @change="orderStatusChangeHandler">
                <el-radio-button label="allOrder">全部新下订单</el-radio-button>
                <el-radio-button label="inStock">备货中</el-radio-button>
                <el-radio-button label="distribution">配送中</el-radio-button>
                <el-radio-button label="finish">已完成</el-radio-button>
            </el-radio-group>
        </div>

        <div>
            <div style="padding: 25px;color: #303133">
                <input type="checkbox" v-model="checked" @click="checkedAll"/>
                <span style="margin-left: 15px"><b>全选</b></span>
                <span style="margin-left: 110px"><b>商品信息</b></span>
                <span style="margin-left: 280px"><b>单价</b></span>
                <span style="margin-left: 60px"><b>数量</b></span>
                <span style="margin-left: 65px"><b>商品金额</b></span>
                <span style="margin-left: 50px"><b>是否有货</b></span>
                <span style="margin-left: 50px"><b>配送时间</b></span>
            </div>

            <div v-for="(order,index) in tableData" style="margin-bottom: 50px;color: #606266">
                <input type="checkbox" :value='index' v-model="checkList" style="margin-left: 28px"/>
                <span style="margin: 20px;">订单号：GG156446516644414</span>
                <span style="margin: 20px">下单时间：2017-12-12</span>
                <span style="margin: 20px">买家姓名：张三</span>
                <span style="margin: 20px">联系方式：157655554</span>
                <span style="margin: 20px"><a href="">订单详情</a></span>
                <div v-for="order in tableData" style="margin-left: 50px;margin-top: 10px;">
                    <div style="margin: 30px;display: inline-block"><img :src="order.img"/></div>
                    <div style="margin-left: 10px;width: 300px;display: inline-block">{{order.goodInfo}}</div>
                    <div style="margin-left: 30px;width:80px;display: inline-block">{{order.price}}</div>
                    <div style="margin-left: 30px;width:80px;display: inline-block">{{order.number}}</div>
                    <div style="margin-left: 30px;width:80px;display: inline-block">{{order.totalPrice}}</div>
                    <div style="margin-left: 30px;width:80px;display: inline-block">{{order.isHasGood}}</div>
                    <div style="margin-left: 30px;width:80px;display: inline-block">{{order.deliveryTime}}</div>
                </div>
            </div>
            <button @click="aa">dd</button>
        </div>
    </div>
</template>

<script>
export default {
  data() {
    return {
      orderNo: "",
      goodName: "",
      startTime: "",
      endTime: "",
      memberName: "",
      memberPhone: "",
      account: "",

      checked: false,
      orderStatus: "allOrder",

      checkList: [],

      tableData: [
        {
          img: require("../../static/img/cai.png"),
          goodInfo: "中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材 500g/份",
          price: "22元/份",
          number: "2份",
          totalPrice: "4元",
          isHasGood: "有货",
          deliveryTime: "11:22"
        },
        {
          img: require("../../static/img/cai.png"),
          goodInfo:
            "中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材 500g/份ddk大家记得记得记得点击豆浆机打底裤",
          price: "21元/份",
          number: "2份",
          totalPrice: "4元",
          isHasGood: "有货",
          deliveryTime: "11:22"
        },
        {
          img: require("../../static/img/cai.png"),
          goodInfo: "中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材 500g/份",
          price: "211元/份",
          number: "2份",
          totalPrice: "4元",
          isHasGood: "有货",
          deliveryTime: "11:22"
        },
        {
          img: require("../../static/img/cai.png"),
          goodInfo: "中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材 500g/份",
          price: "2元/份",
          number: "2份",
          totalPrice: "4元",
          isHasGood: "有货",
          deliveryTime: "11:22"
        }
      ]
    };
  },
  methods: {
    aa: function() {
      alert(this.checkList);
    },

    checkedAll: function() {
      var _this = this;
      if (_this.checked) {
        //实现反选
        _this.checkList = [];
      } else {
        //实现全选
        _this.checkList = [];
        _this.tableData.forEach(function(order, index) {
          _this.checkList.push(index);
        });
      }
    },
    orderStatusChangeHandler(value) {
      alert(value);
      this.tableData[0].isHasGood = "无货";
    },
    //重置
    reset() {
      (this.orderNo = ""),
        (this.goodName = ""),
        (this.startTime = ""),
        (this.endTime = ""),
        (this.memberName = ""),
        (this.memberPhone = ""),
        (this.account = "");
    },

    init() {
      this.$http
        .get("http://localhost:8080/api/order/get_order_by_id", {params: { id: 1 }})
        .then(response => {
          alert(response.data.bizContent.id);
        })
        .catch(error => {
          alert(error);
          console.log(error);
        });
    }
  },
  created() {
    this.init();
  },
  watch: {
    checkList: {
      handler: function(val, oldVal) {
        if (val.length === this.tableData.length) {
          this.checked = true;
        } else {
          this.checked = false;
        }
      },
      deep: true
    }
  }
};
</script>

<style scoped>
</style>
