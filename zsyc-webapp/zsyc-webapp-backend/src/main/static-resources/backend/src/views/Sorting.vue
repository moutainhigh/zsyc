<template>
<div id="app">
 <div>
     <div>统一分拣条件：
     <span>
         <el-input v-model="beihuo" style="width: 200px;" placeholder="请输入备货条件"></el-input>
     </span>
         <span> <el-button size="mini" round style="margin-left: 50px">修改</el-button></span>
     </div>
 </div>
    <div style="margin-top: 30px;">
        <div>自动刷新频率：
            <span>


         <el-input v-model="timenum" style="width: 200px;"  @keyup.native="handleClick" placeholder="请输入自动刷新频率"></el-input>
     </span>
            <span> <el-button size="mini" round style="margin-left: 50px" @click="timeupdate()">修改</el-button></span>
        </div>
    </div>
    <div style="margin-top: 30px;">
        <el-radio-group v-model="radio3"  size="medium" @change="followup">
            <el-radio-button label="待分拣订单"></el-radio-button>
            <el-radio-button label="已分拣订单"></el-radio-button>
        </el-radio-group>
    </div>


<div v-for="item in list">
        <div   style="margin-top: 10px;" >
        <span></span><span style="margin-left: 10px;"> 订单号</span><font color="#00bfff" style="margin-left: 10px">{{item.orderNo}}</font>
        <span style="margin-left: 50px">下单时间</span>
        <span style="margin-left: 10px">{{item.createTime}}</span>
        <span style="margin-left: 50px">买家姓名：</span>
        <span style="margin-left: 10px">{{item.consignee}}</span>
        <span style="margin-left: 80px">联系方式：</span>
        <span style="margin-left: 5px">{{item.consigneePhone}}</span>

        <span > <el-button style="margin-left: 250px" type="primary">点击完成</el-button></span>
        <span></span>
        </div>

<div v-for="item2 in item.list">
<img :src="item2.img" style="margin-left:30px;margin-top:20px; width:100px;height: 100px;">
                <div style="margin-left: 150px; margin-top:-80px;width: 300px ;">{{item2.goodsName}} 500g/份</div>
                    <div style="margin-top: -40px;margin-left: 330px">
                <div style="margin-left: 190px;width: 100px;margin-top: -22px;">{{item2.attrValuer}}/份</div>


                    <div style="margin-left: 480px;margin-top: -21px;width: 100px;">{{item2.num}}{{item2.unit}}份</div>

                       <el-button  style="margin-left: 700px;margin-top: -50px" type="primary">缺货登记</el-button>
                    </div>
</div>

</div>
</div>

</template>

<script>
export default {

    timer:"",
    TIME:2000,

  data () {
    return {visible: false,
        timenum:"",
        beihuo:"",
        form:{ order:"",
            commodity:"",
            datetime:"",
            servicename:"",
            servicephone:"",
            Accountnumber:"",},
      list:[],

        value6: '',
        value3: '',
        value4: '',
        radio3: '待分拣订单',
        tableData: [{
            selectAll:"0QY4Q(I%7B)%7BU)%25FD5FTT@B1D.png",
            date: '2016-05-02',
            name: '中国有机芹菜西兰花新鲜蔬菜凉拌菜叶菜火锅食材500g/份',
            proice: '500'
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

  methods:{


      handleClick() {

          this.timenum=this.timenum.replace(/[^\.\d]/g,'');
          this.timenum=this.timenum.replace('.','');

      },


          timeupdate(){
              if(this.timenum==""){
                  this.$message.error('你还没有输入时间');
              }else{
                  if(this.timer){
                      clearInterval(this.timer);
                      this.timenum=this.timenum*1000
                      this.timer=setInterval( () => {

                          this.time()

                      },  this.timenum)
                      this.$message({
                          message: '修改成功自动刷新的频率为'+this.timenum/1000+"秒",
                          type: 'success'
                      });
                      this.timenum="";
                  }
              }
      },


    time(){
      console.log("time");
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

  },
    created() {
        this.timer=setInterval( () => {

            this.time()

        }, 1000)

        this.$http
            .get("http://localhost:8080/api/BackendWarehouseController/returnSelectBackendWarehousePackOrder").then(response => {
            this.list =response.data.bizContent;
            console.log(this.list);
        })





    },
    destroyed() {
      clearInterval(this.timer)
    }


}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
