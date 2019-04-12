<template>
  <div id="div" class="div">
    <div>
        <h4>会员成长值说明：</h4>
        <p>会员成长值是划分会员等级的唯一标准，成长值越高，等级越高</p>
        1钱成长值等于<el-input  v-model = "groupValue" placeholder="0" class="input" ></el-input>元
        <el-button type="primary" class="updateBtn" @click="groupValueBtn">修改</el-button>
        <br><br>
        &nbsp;&nbsp;成长值有效期<el-input  v-model = "groupTime" placeholder="0" class="input" ></el-input>年
        <el-button type="primary" class="updateBtn" @click="groupTimeBtn">修改</el-button>
        <hr style="height:1px;border:none;border-top:1px solid #555555;" />
    </div>

    <div >
        <h4>会员成长值划分：<el-button  plain class="updateBtn">新增</el-button></h4>
        <br>
        铁杆会员&nbsp;&nbsp;大于<el-input placeholder="0" class="input"  v-model = "leve1"></el-input>钱
        <el-button type="primary" class="updateBtn" @click = "leve1Btn">修改</el-button>
        <br><br>
        优质会员&nbsp;&nbsp;<el-input placeholder="0" class="input" v-model = "leve2"></el-input> -
        <el-input   placeholder="0" class="input" v-model = "toLeve2"></el-input>钱
        <el-button type="primary" class="updateBtn" @click = "leve2Btn">修改</el-button>
        <br><br>
        发展会员&nbsp;&nbsp;<el-input placeholder="0" class="input" v-model = "leve3"></el-input> -
        <el-input   placeholder="0" class="input" v-model = "toLeve3"></el-input>钱
        <el-button type="primary" class="updateBtn" @click = "leve3Btn">修改</el-button>
        <br><br>
        新普会员&nbsp;&nbsp;小于<el-input   placeholder="0" class="input" v-model = "leve4"></el-input>钱
        <el-button type="primary" class="updateBtn" @click = "leve4Btn">修改</el-button>
    </div>
  </div>
</template>

<script>
export default {
    // el:"#div",
    data () {
      return {
        groupValue:'',
        groupTime:'',

        leve1:'',
        leve2:'',
        toLeve2:'',
        leve3:'',
        toLeve3:'',
        leve4:'',
      }
    },
  //自动加载方法
  created(){
    //自动请求方法




      var groupData = {
        groupValue:'1',
        groupTime:'1',
        leve1:'1',
        leve2:'2',
        toLeve2:'3',
        leve3:'4',
        toLeve3:'5',
        leve4:'6',
      }
      this.groupValue =  groupData.groupValue;
      this.groupTime = groupData.groupTime;
      this.leve1 =  groupData.leve1;
      this.leve2 = groupData.leve2;
      this.toLeve2 = groupData.toLeve2;
      this.leve3 = groupData.leve3;
      this.toLeve3 = groupData.toLeve3;
      this.leve4 = groupData.leve4;
    },
    methods:{
      //成长值修改
      groupValueBtn(){
        if(this.groupValue == 0){
          this.$message('请输入数据');
        }else {
          //请求


          this.$message({
            message: this.groupValue + '元修改成功',
            type: 'success'
          });
        }

      },
      //成长年修改
      groupTimeBtn(){
        if(this.groupTime == 0){
          this.$message('请输入数据');
        }else {
          //请求



          this.$message({
            message: this.groupTime + '年修改成功',
            type: 'success'
          });
        }

      },
      //铁杆会员 修改
      leve1Btn(){
        if(this.leve1 == 0){
          this.$message('请输入数据');
        }else {
          //请求


          this.$message({
            message: this.leve1 + '钱修改成功',
            type: 'success'
          });
        }

      },
      //优质会员修改
      leve2Btn(){
        if(this.leve2 == 0 || this.toLeve2 == 0){
          this.$message('请输入数据');
        }else if (this.leve2 > this.toLeve2){
          this.$message('请输入正确范围');
        } else {
          //请求


          this.$message({
            message: '优质会员修改成功',
            type: 'success'
          });
        }

      },
      //发展会员
      leve3Btn(){
        if(this.leve3 == 0 || this.toLeve3 == 0){
          this.$message('请输入数据');
        }else if (this.leve3 > this.toLeve3){
          this.$message('请输入正确范围');
        } else {
          //请求


          this.$message({
            message: '发展会员修改成功',
            type: 'success'
          });
        }

      },
      //新普会员
      leve4Btn(){
        if(this.leve4 == 0){
          this.$message('请输入数据');
        }else {
          //请求


          this.$message({
            message: this.leve4 + '钱修改成功',
            type: 'success'
          });
        }

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
