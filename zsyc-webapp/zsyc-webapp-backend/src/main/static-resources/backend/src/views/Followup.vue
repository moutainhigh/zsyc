<template>
  <div id="app2"  >
    <div  v-for="item1 in num" :key=item1.num>
    <div style="margin-top: 50px;" >
      <el-radio-group v-model="radio3"  size="medium" @change="followup">
        <el-radio-button label="待跟进" ></el-radio-button>
        <el-radio-button label="已跟进" ></el-radio-button>
      </el-radio-group>
    </div>

    <div style="width: 3000px" :class="(item1.num===2)?'xianshi':'yincang'">
      <el-table
              :data="tableData"
              border
              style="width: 100%">
        <el-table-column
                prop="date"
                label="序号"
                width="80">
        </el-table-column>
        <el-table-column
                prop="Ordernumber"
                label="订单编号"
                width="100"
        >
        </el-table-column>
        <el-table-column
                prop="Ordertime"
                label="下单时间"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="Name"
                label="买家姓名"
                width="80"
        >
        </el-table-column>
        <el-table-column
                prop="information"
                label="联系方式"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="address"
                label="地址"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="extractingtime"
                label="自提时间"
                width="120"
        >
        </el-table-column>
        <el-table-column

                prop="Self"
                label="自提状态"
                width="100"
        >
          <template scope="scope">
            <span  v-if="scope.row.Self===1" style="color: aqua;text-align: center">已自提</span>
            <span  v-else style="color: crimson">没自提</span>
          </template>
        </el-table-column>

        <el-table-column
                prop="information"
                label="电话通知"
                width="120"
        >
          <template scope="scope">
            <el-button size="small" v-if="scope.row.Telephone===0" style="text-align: center" type="primary">未通知</el-button>
            <span v-else style="color: lightslategray" >已通知</span>

          </template>
        </el-table-column>
        <el-table-column
                prop="result"
                label="处理结果"
                width="120"
        >
        </el-table-column>
      </el-table>

</div>


      <div style="width: 3000px" :class="(item1.num===1)?'xianshi':'yincang'">
      <el-table
              :data="tableData2"
              border
              style="width: 100%">
        <el-table-column
                prop="id"
                label="序号"
                width="80">
        </el-table-column>
        <el-table-column
                prop="orderNo"
                label="订单编号"
                width="100"
        >
        </el-table-column>
        <el-table-column
                prop="createTime"
                label="下单时间"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="consignee"
                label="买家姓名"
                width="80"
        >
        </el-table-column>
        <el-table-column
                prop="consigneePhone"
                label="联系方式"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="consigneeAddress"
                label="地址"
                width="120"
        >
        </el-table-column>
        <el-table-column
                prop="urgeTime"
                label="自提时间"
                width="120"
        >
        </el-table-column>
        <el-table-column

                prop="Self"
                label="自提状态"
                width="100"
        >
          <template scope="scope">
            <span  v-if="scope.row.Self===1" style="color: aqua;text-align: center">已自提</span>
            <span  v-else style="color: crimson">没自提</span>
          </template>
        </el-table-column>

        <el-table-column
                prop="information"
                label="电话通知"
                width="120"
        >


          <template scope="scope">
            <el-button size="small" style="text-align: center" type="primary">未通知</el-button>


          </template>
        </el-table-column>
        <el-table-column
                prop="backendRemark"
                label="处理结果"
                width="120"
        >
        </el-table-column>
      </el-table>
      </div>

  </div>
  </div>

</template>
<style>
  .xianshi{
    display: block;
  }
  .yincang{
    display: none;
  }
  .Selffalse{

    color: red;

  }
  .Selftrue{

    color: forestgreen;
  }
</style>
<script>
  export default {
    name: 'followup',

    methods:{

      followup:function(){
        if( this.num[0].num===1){

          this.num[0].num=2


        }else{

          this.num[0].num=1

        }

      },
      weitongzhi:function(){
        alert("未通知页面")
      }

    },

    data: function () {
      return {visible: false,
        num:[{num:1}],


        radio3: '待跟进',
        tableData: [
        ],
        tableData2: [{
          date: '1',
          Ordernumber: 'dd2018991201',
          Ordertime: '2018-12-11 9:30',
          Name:'张三',
          information:'13918221732',
          address:'太古仓公寓201',
          extractingtime:'2018-12-11 10：30',
          Self:0,
          Telephone:0,
          result:'客户忘记，以过来取'
        },
          {
            date: '2',
            Ordernumber: 'dd2018991202',
            Ordertime: '2018-12-11 9:30',
            Name:'李四',
            information:'13918221732',
            address:'太古仓公寓201',
            extractingtime:'2018-12-11 10：30',
            Self:0,
            Telephone:0,
            result:'一直未接电话'
          }
        ]


      }
    },
    created(){

      this.$http
              .get("http://localhost:8080/api/AfterSaleController/selectAfterSaleOrderAll").then(response => {
        this.tableData2 =response.data.bizContent;
        console.log(this.list);
      })


    },

  }
</script>

<style scoped>

</style>
