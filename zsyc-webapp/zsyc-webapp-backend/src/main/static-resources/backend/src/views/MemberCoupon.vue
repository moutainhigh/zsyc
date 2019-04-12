<template>
  <div id = "div" class="div">
    <h4>满减优惠券设置：<el-button  plain class="updateBtn" @click="fullCutDialog.dialogVisible = true">新增</el-button></h4>
    <br>
    <div v-for="(value, index) in responseData.fullCutCoupon">
      <strong>优惠券{{index + 1}}：</strong>订单满<el-input class="input" placeholder="10" :value="value.fullMoney"></el-input>
      减<el-input  placeholder="5" class="input" :value="value.cutMoney"></el-input>元
      <el-button type="primary" class="updateBtn" @click="fullCutBtn(value.id)">修改</el-button>
      <br><br>
    </div>

    <!--添加框-->
    <el-dialog
      title="添加满减券"
      :visible.sync="fullCutDialog.dialogVisible"
      width="400px"
      :before-close="handleClose">

      <el-form  ref="fullCutDialog.form"  :model="fullCutDialog.addData">
        满<el-input class="input" v-model="fullCutDialog.addData.inputFullMoney"></el-input>
        减<el-input class="input" v-model="fullCutDialog.addData.inputCutMoney"></el-input>元
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="fullCutDialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="open">确 定</el-button>
        </span>
    </el-dialog>

    <hr style="height:1px;border:none;border-top:1px solid #555555;" />

    <h4>折扣优惠券设置<el-button  plain class="updateBtn" @click="discountDialog.dialogVisible = true">新增</el-button></h4>
    <br>
    <div v-for="(value, index) in responseData.discountCoupon">
      <strong>优惠券{{index + 1}}：</strong><el-input placeholder="9" class="input"  :value="value.discount"></el-input> 折扣
      <el-button type="primary" class="updateBtn" @click="discountBtn(value.id)">修改</el-button>
      <br><br>
    </div>

    <!--添加框-->
    <el-dialog
      title="添加折扣券"
      :visible.sync="discountDialog.dialogVisible"
      width="400px"
      :before-close="handleClose">

      <el-form  ref="form"  :model="discountDialog.addData">
        打<el-input class="input" v-model="discountDialog.addData.inputDiscount"></el-input>折
      </el-form>


      <span slot="footer" class="dialog-footer">
        <el-button @click="discountDialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="openDiscountDialog">确 定</el-button>
        </span>
    </el-dialog>


    <hr style="height:1px;border:none;border-top:1px solid #555555;" />
    <h4>定向商品优惠券设置<el-button  plain class="updateBtn" @click="goodDisDialog.dialogVisible = true">新增</el-button></h4>
    <br>
    <div v-for="(value, index) in responseData.goodDisCoupon">
      <strong>优惠券{{index + 1}}：</strong>商品<el-input placeholder="蔬菜" class="input"  :value="value.good"></el-input>
      打<el-input placeholder="9" class="input" :value="value.discount"></el-input> 折
      <el-button type="primary" class="updateBtn" @click="goodsDisBtn(value.id)">修改</el-button>
      <br><br>
    </div>

    <!--添加框-->
    <el-dialog
      title="添加定向商品优惠券"
      :visible.sync="goodDisDialog.dialogVisible"
      width="400px"
      :before-close="handleClose">

      <el-form  ref="form"  :model="goodDisDialog.addData">
        商品<el-input class="input" v-model="goodDisDialog.addData.inputGood"></el-input>
        打<el-input class="input" v-model="goodDisDialog.addData.inputDiscount"></el-input>折
      </el-form>


      <span slot="footer" class="dialog-footer">
            <el-button @click="goodDisDialog.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="goodDisCount">确 定</el-button>
        </span>
    </el-dialog>


    <hr style="height:1px;border:none;border-top:1px solid #555555;" />
    <h4>定向会员优惠券设置<el-button  plain class="updateBtn" @click="userDisDialog.dialogVisible = true">新增</el-button></h4>
    <br>
    <div v-for="(value, index) in responseData.userFullCutCoupon">
      <strong>优惠券{{index + 1}}：</strong><el-input placeholder="铁杆会员" class="input" :value="value.level"></el-input>
      全场每满<el-input placeholder="9" class="input" :value="value.fullMoney"></el-input>
      减<el-input placeholder="9" class="input" :value="value.cutMoney"></el-input> 元
      <el-button type="primary" class="updateBtn" @click="userDisBtn(value.id)">修改</el-button>
      <br><br>
    </div>

    <!--添加框-->
    <el-dialog
      title="添加定向会员优惠券"
      :visible.sync="userDisDialog.dialogVisible"
      width="550px"
      :before-close="handleClose">

      <el-form  ref="form"  :model="userDisDialog.addData">
        会员<el-input class="input" v-model="userDisDialog.addData.inputUserLeve"></el-input>
        每满<el-input class="input" v-model="userDisDialog.addData.inputFullMoney"></el-input>
        减<el-input class="input" v-model="userDisDialog.addData.inputCutMoney"></el-input>元
      </el-form>


      <span slot="footer" class="dialog-footer">
        <el-button @click="userDisDialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="userDisCount">确 定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  // el: '#div',
  data () {
    return {
      fullCutDialog:{       //满减券弹框
        dialogVisible: false, //隐藏弹框
        addData: {
          inputFullMoney: '',//输入满减金额
          inputCutMoney: '',//输入减金额
        },
        // form: {},
      },
      discountDialog:{       //折扣券弹框
        dialogVisible: false, //隐藏弹框
        addData: {
          inputDiscount:''
        },
        // form: {},
      },
      goodDisDialog:{       //商品折扣券弹框
        dialogVisible: false, //隐藏弹框
        addData: {
          inputGood:'',
          inputDiscount:''
        },
        // form: {},
      },

      userDisDialog:{     //会员定向满减券弹框
        dialogVisible: false, //隐藏弹框
        addData: {
          inputUserLeve:'',
          inputFullMoney:'',
          inputCutMoney:''
        },
        // form: {},
      },

      responseData: [{}],//返回数据

      }
    },

    created(){
      //满减券
      var fullCutCoupon = [{
        id:'1',
        fullMoney:'10',
        cutMoney:'5'
      },{
        id:'2',
        fullMoney:'50',
        cutMoney:'20'
      },{
        id:'3',
        fullMoney:'100',
        cutMoney:'50'
      }];

      //折扣券
      var discountCoupon = [{
        id:'1',
        discount:'6'
      },{
        id:'2',
        discount:'8'
      },{
        id:'3',
        discount:'9.5'
      }];

      //商品折扣券
      var goodDisCoupon = [{
        id:'1',
        discount:'6',
        good:'牛肉'
      },{
        id:'2',
        discount:'8',
        good:'猪肉'
      },{
        id:'3',
        discount:'9.5',
        good:'白菜'
      }];

      //会员满减券
      var userFullCutCoupon = [{
        id:'1',
        fullMoney:'10',
        cutMoney:'5',
        level:'白金会员'
      },{
        id:'2',
        fullMoney:'50',
        cutMoney:'20',
        level:'超级会员'
      },{
        id:'3',
        fullMoney:'100',
        cutMoney:'50',
        level:'老客户'
      }];


      this.responseData = {
        fullCutCoupon: fullCutCoupon,
        discountCoupon: discountCoupon,
        goodDisCoupon: goodDisCoupon,
        userFullCutCoupon: userFullCutCoupon
      }
    },

    methods:{


      //弹框关闭按钮
      handleClose(done) {
        done();
      },

      //确认添加满减券
      openFullCutDialog( ){

        var fullMoney = this.$refs.form.model.inputFullMoney;
        var cutMoney = this.$refs.form.model.inputCutMoney;

        this.$message({
          message: '添加成功!满' + fullMoney + '减'+ cutMoney,
          type: 'success'
        });

      },
      //修改满减券
      fullCutBtn(id){
        //请求



        this.$message({
          message: '修改成功!',
          type: 'success'
        });
      },




      //确认添加折扣券
      openDiscountDialog( ){
        var discount = this.$refs.form.model.inputDiscount;

        this.$message({
          message: '添加成功!打' + discount + '折',
          type: 'success'
        });
      },
      //修改折扣券
      discountBtn(id){
        //请求



        this.$message({
          message: '修改成功!',
          type: 'success'
        });
      },


      //确认添加商品折扣券
      goodDisCount( ){
        var discount = this.$refs.form.model.inputDiscount;
        var good = this.$refs.form.model.inputGood;
        this.$message({
          message: '添加成功!商品'+ good +'打' + discount + '折',
          type: 'success'
        });
      },
      //修改商品折扣券
      goodsDisBtn(id){
        //请求



        this.$message({
          message: '修改成功!',
          type: 'success'
        });
      },



      //确认添加会员优惠券
      userDisCount( ){
        var userLevel = this.$refs.form.model.inputUserLeve;
        var fullMoney = this.$refs.form.model.inputFullMoney;
        var cutMoeny = this.$refs.form.model.inputCutMoney;
        this.$message({
          message: '添加成功!'+ userLevel+ '满' + fullMoney +'减' + cutMoeny + '元',
          type: 'success'
        });
      },
      //修改会员优惠券
      userDisBtn(id){
        //请求



        this.$message({
          message: '修改成功!',
          type: 'success'
        });
      },

    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .input{
    width: 100px;
    margin-left: 10px;
    margin-right: 10px;
  }
  .updateBtn{
    float: right;
    width: 100px;
  }
  .div{
    width: 100%;
  }
</style>
