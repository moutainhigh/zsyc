<template>
<div id="app">
  <el-form ref="form" :model="form" label-width="80px">
    <div style="margin-left:200px;width: 2000px">
        <div id=“DD” style="margin-left: 60px;margin-top: 60px;">

        <span>订单编号：<el-input v-model="form.order" style="width: 200px;" placeholder="请输入订单编号"></el-input>
            </span>

            <span style="margin-left: 50px;"> 商品名称：<el-input v-model="form.commodity" style="width: 200px;"
                                                             placeholder="请输入商品名称"></el-input></span>

    <span class="demonstration" style="margin-left: 50px;">
    下单时间：
    <el-date-picker
            v-model="form.datetime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
    </el-date-picker>
        </span>

        </div>
        <div style="margin-left: 60px;margin-top: 60px;">
            <span>
                客户姓名：<el-input v-model="form.servicename" style="width: 200px;"  placeholder="请输入客户姓名"></el-input>
            </span>
            <span style="margin-left: 50px;">
                客户电话：<el-input v-model="form.servicephone" style="width: 200px;" placeholder="请输入客户电话"></el-input>
            </span >
            <span style="margin-left: 50px;">
               下单账号：<el-input v-model="form.Accountnumber" style="width: 360px;"  placeholder="请输入下单账号"></el-input>
            </span>

        </div>
        <div style="margin-left: 350px;margin-top: 30px;">
        <el-button type="primary">查询</el-button>
        <el-button style="margin-left: 20px" type="primary" @click="chongzhi">重置</el-button>
        <el-button style="margin-left: 20px" type="primary">批量导出</el-button>
    </div>

    </div>
  </el-form>
    <div style="margin-top: 30px;">
        <el-radio-group v-model="radio3"  size="medium" @change="followup">
            <el-radio-button label="待跟进"></el-radio-button>
            <el-radio-button label="已跟进"></el-radio-button>
        </el-radio-group>
    </div>
    <div style="width: 3000px;">
    <hr color="darkgrey"  />
    <input id="quan" type="checkbox" @click="checkAll($event)"> 全选
    <font style="font-family: 'STHeiti';margin-left: 100px;">商品信息</font>
    <font style="font-family: 'STHeiti';margin-left: 300px;">单价</font>
    <font style="font-family: 'STHeiti';margin-left: 50px;">数量</font>
    <font style="font-family: 'STHeiti';margin-left: 50px;">商品金额</font>
    <font style="font-family: 'STHeiti';margin-left: 50px;">是否有货</font>
    <font style="font-family: 'STHeiti';margin-left: 50px;">配送时间</font>
    <hr color="darkgrey"  />
    </div>
    <div v-for="(item,index) in list" v-if="index<3"  :key='index' style="  margin-top:10px;width: 1600px;height: 350px;border:solid 1px;border-color: rgba(121,121,121,0.56)">
    <div style="margin-top: 10px;">
        <input class="checkItem" type="checkbox" value="apple" v-model="checkData"><span style="margin-left: 10px;"> 订单号</span><font color="#00bfff" style="margin-left: 10px">{{item.orderNo}}</font>
        <span style="margin-left: 50px">下单时间</span>
        <span style="margin-left: 10px">{{item.createTime}}</span>
        <span style="margin-left: 50px">买家姓名：</span>
        <span style="margin-left: 10px">{{item.consignee}}</span>
        <span style="margin-left: 80px">联系方式：</span>
        <span style="margin-left: 5px">{{item.consigneePhone}}</span>

    </div>
        <el-button type="text" style="margin-left: 1110px" @click="dialogTableVisible = true">订单详情</el-button>

        <div v-for="item1 in item.list" style="margin-top: 20px;">
<img :src="item1.img" style="margin-left:30px;margin-top:20px; width:100px;height: 100px;">
                <div style="margin-left: 150px; margin-top:-80px;width: 300px ;">{{item1.goodsName}}</div>
                    <div style="margin-top: -40px;margin-left: 330px">
                <div style="margin-left: 190px;width: 50px;margin-top: -22px;">{{item1.price}}元/份</div>
                <div style="margin-left: 280px;margin-top: -19px;width: 50px;">{{item1.num}}份</div>
                        <div style="margin-left: 380px;margin-top: -18px;width: 100px;">{{item1.priceAll}}元</div>
                    <div style="margin-left: 480px;margin-top: -18px;width: 100px;">{{item1.status}}</div>
                    <div style="margin-left: 590px;margin-top: -21px;width: 100px;">{{item1.bookStarTime}}</div>
                    </div>
            <div style="margin-left: 1100px;margin-top: -30px;height: 100px">
                   <div style="margin-top: -60px;margin-left: 10px; "> <el-button type="primary" >退款</el-button></div>
                    <div style="margin-left: 10px;margin-top: 10px"> <el-button type="primary">换货</el-button></div>
                </div>
        </div>

        <el-dialog title="订单详情" :visible.sync="dialogTableVisible">
            <el-table :data="item.list" :cell-style="cellStyle" :header-cell-style="rowClass">
                <el-table-column property="xinxi" label="商品信息" width="150"></el-table-column>
                <el-table-column property="porce" label="单价" width="100"></el-table-column>
                <el-table-column property="number" label="数量"></el-table-column>
                <el-table-column property="univalent" label="商品金额"></el-table-column>
                <el-table-column property="state" label="是否有货"></el-table-column>
                <el-table-column property="time" label="配送时间"></el-table-column>
            </el-table>
        </el-dialog>

    </div>

</div>

</template>

<script>
export default {
  name: 'HelloWorld2',

  data () {
    return {visible: false,
        dialogTableVisible: false,
        dialogFormVisible: false,
        form1: {
            name: '',
            region: '',
            date1: '',
            date2: '',
            delivery: false,
            type: [],
            resource: '',
            desc: ''
        },
        formLabelWidth: '120px',
        form:{ order:"",
            commodity:"",
            datetime:"",
            servicename:"",
            servicephone:"",
            Accountnumber:"",},
      list:[

        ],
        fi:[{
            xinxi:"中国有机芹菜西兰花新鲜蔬菜凉拌叶菜火锅食材 500g/份",
            img: require("../../static/img/cai.png"),
            porce:2,
            number:2,
            univalent:4,
            state:"缺货",
            time:"11;22",
        },{
            xinxi:"中国有机芹菜西兰花新鲜蔬菜凉拌叶菜火锅食材 500g/份",
        img: require("../../static/img/cai.png"),porce:2,
            number:2,
            univalent:4,
            state:"缺货",
            time:"11;22",
    }],

        value6: '',
        value3: '',
        value4: '',
        radio3: '待跟进',
        tableData: [{

            selectAll:"0QY4Q(I%7B)%7BU)%25FD5FTT@B1D.png",
            date: '2016-05-02',
            name: '中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材500g/份',
            proice: '2元一份'
        }],
        checkData: [] // 双向绑定checkbox数据数组




                        }
    },
    watch: { // 监视双向绑定的数据数组
                checkData: {
                    handler(){ // 数据数组有变化将触发此函数
                        if(this.checkData.length == 3){
                            document.querySelector('#quan').checked = true;
                        }else {
                            document.querySelector('#quan').checked = false;
                        }
                    },
                    deep: true // 深度监视
                }
            },
    created(){

        this.$http
            .get("http://localhost:8080/api/AfterSaleController/selectAfterSaleOrderGoodsAll").then(response => {
            this.list =response.data.bizContent;
        })
    },
  methods:{
      cellStyle({row,column,rowIndex,columIndex}){
          return "text-align:center";
      },
      rowClass({row,rowIndex}){
          return "text-align:center;";
      },

    chongzhi(){
    this.form={}
    },
    send(){
      axios.get('/user?ID=123456')
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        })
    },
    followup:function(){
      alert("待跟进页面")
    },
      checkAll(e){ // 点击全选事件函数
          var checkObj = document.querySelectorAll('.checkItem'); // 获取所有checkbox项
          if(e.target.checked){ // 判定全选checkbox的勾选状态
              for(var i=0;i<checkObj.length;i++){
                  if(!checkObj[i].checked){ // 将未勾选的checkbox选项push到绑定数组中
                      this.checkData.push(checkObj[i].value);
                  }
              }
          }else { // 如果是去掉全选则清空checkbox选项绑定数组
              this.checkData = [];
          }
      }

  }



  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
