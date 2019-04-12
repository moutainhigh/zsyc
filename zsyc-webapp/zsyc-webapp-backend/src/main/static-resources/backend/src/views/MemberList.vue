<template>
  <div id="query">

    <div>
      会员名称:
      <el-input  v-model = "name" placeholder="请输入名称" class="input" ></el-input>

      会员价值:
      <el-select v-model="value" clearable placeholder="请选择" class="input">
        <el-option
          v-for="item in options"
          :key="item.key"
          :value="item.value">

        </el-option>
      </el-select>

      数据统计周期:
      <el-date-picker
        v-model="beginTime"
        type="datetime"
        format="yyyy-MM-dd HH:mm"
        value-format="yyyy-MM-dd HH:mm"
        :editable = "false"
        placeholder="选择开始时间">
      </el-date-picker>
      至
      <el-date-picker
        v-model="endTime"
        type="datetime"
        format="yyyy-MM-dd HH:mm"
        value-format="yyyy-MM-dd HH:mm"
        :editable = "false"
        placeholder="选择结束时间">
      </el-date-picker>
    </div>

    <div class="queDiv">
      <el-button  plain class = "queBtn"  @click="query">查询</el-button>
      <el-button plain class = "queBtn" @click="reset">重置</el-button>
    </div>


    <div class="CouponBtn">
      <el-button  size="small" type="info" round @click="Coupon">批量发放优惠券</el-button>
    </div>

    <div>
      <el-table :data="tableData" style="width: auto" @selection-change="changeFun">
        :default-sort = "{prop: 'tableData', order: 'descending'}"
        <el-table-column width="30px" type="selection">
        </el-table-column>
        <el-table-column width="100px"
                         label="全选">
          <!--<template slot-scope="{row,$index}">-->
          <!--<span> {{$index + 1}} </span>-->
          <!--</template>-->
        </el-table-column>
        <el-table-column
          prop="nickName"
          label="会员名称"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="telephone"
          label="手机号码"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="value"
          label="会员价值"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="totalNum"
          label="交易总笔数"
          sortable
          width="auto">
        </el-table-column>

        <el-table-column
          prop="totalFee"
          label="交易总额"
          sortable
          width="auto">
        </el-table-column>

        <el-table-column
          prop="avePrice"
          label="平均客单价"
          sortable
          width="auto">
        </el-table-column>

        <el-table-column
          prop="weekNum"
          label="周频次"
          sortable
          width="auto">
        </el-table-column>

        <el-table-column
          label="操作"
          width="auto"
          header-align="center">
          <template slot-scope="{row,$index}">
            <el-button type="text" size="small" @click="detail(row.id)">查看</el-button>
            <el-button type="text" size="small" @click="shield(row.id)">屏蔽</el-button>
            <el-button type="text" size="small" @click="costDetail(row.id)">消费明细</el-button>
          </template>
        </el-table-column>
      </el-table>

    <div style="float: right">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="1"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    </div>
  </div>
</template>

<script>
export default {
    // el:"#query",
    data () {
      return {
        name: '',//会员名称
        beginTime: '',//开始时间
        options: [{ }],//下拉框数据
        value:'',//会员价值
        endTime: '',//结束时间
        checked:'',//多选
        tableData: [{}],//列表数据
        CouponData:'',//优惠券数据
        currentPage: 1,//默认1页
        total:'',//总页数
        pageSize:'10',//每页数目
      }
    },
  //自动加载方法
  created(){
    //自动请求 页数：  页码：
    var pageSize = this.pageSize;
    var pageNum = this.currentPage;


    var _this = this;
    //   this.$http.post('http://localhost:8089/api/member/getMemberList',
    // {     "name":this.name,
    //       "beginTime":this.beginTime,
    //       "endTime":this.endTime
    //   }).then(res=>{
    //       //返回列表数据
    //       _this.tableData = res.data.bizContent;
    //
    //       //总条数
    //       _this.total = _this.tableData.length;
    //   });
      _this.tableData =[{nickName:'sb', telephone: '110', id: '1'},{nickName:'sbb', telephone: '1210', id: '2'}];
      _this.total = 100;

    //返回下拉框数据
    this.options = [{
      key: 'vip',
      value:'vip会员'
    },{
      key: 'normal',
      value:'普通会员'
    }];




    },
    methods:{
      //查询方法
        query(){
          //赋值下拉框的key
          var valueKey = '';
          for(var i = 0; i < this.options.length; i ++){
            if(this.options[i].value == this.value){
              valueKey = this.options[i].key;
            }
          }
          //请求数据
          var reqData = {
            name: this.name,
            value: valueKey,
            beginTime: this.beginTime,
            endTime: this.endTime
          }
          //请求页数：  页码：
          var pageSize = this.pageSize;
          var pageNum = this.currentPage;

            // this.$http.post('http://localhost:8089/api/member/getMemberList',
            //     {     "name":reqData.name,
            //         "beginTime":reqData.beginTime,
            //         "endTime":reqData.endTime
            //     }).then(res=>{
            //     //返回列表数据
            //     this.tableData = res.data.bizContent;
            // });


        },
        //重置方法
        reset() {
          this.name = '',
            this.beginTime = '',
            this.value = '',
            this.endTime = ''
        },
        //发送优惠券方法
        Coupon() {
          if(this.CouponData.length == 0){
            this.$message('请勾选数据');
          }else {
            //发送



            this.$message({
              message: '已经发送优惠券',
              type: 'success'
            });
          }
        },
        //勾选方法
        changeFun(userList) {
          var array = new Array();
          for(var i = 0; i < userList.length; i ++){
            array.push(userList[i].id)
          }
          this.CouponData=array;
        },
        //查看方法
        detail(id) {
            //查看


            this.$message({
            message: 'id:' + id,
            type: 'success'
            });
        },
        //屏蔽方法
        shield(id){
            //屏蔽



            this.$message({
                message: 'id:' + id + '已屏蔽',
                type: 'success'
            });
        },
        //消费明细
        costDetail(id){
            //明细





            this.$message({
                message: 'id' + id + '总共消费多少元',
                type: 'success'
            });
        },

        //排序
        // formatter(row, column) {
        //     return row.phone;
        // },


        //分页
        handleSizeChange(val) {
          this.pageSize = val;
          //每页 ${val} 条

          this.query();
        },
        handleCurrentChange(val) {
          this.currentPage = val;
          //当前页: ${val}

          this.query();
        },
      }

  }
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .input{
    width: 150px;
    margin-right:50px;
  }
  .queBtn{
    width: 100px;
    height: 40px;
  }
  .queDiv{
    text-align: center;
    margin-top: 30px;
  }
  .CouponBtn{
    margin-left: 92%;
  }
  #query{
    width: auto;
  }
</style>
