<template>
  <div align="center" id="search">
    <h1 >分群营销</h1>
    <div style="margin-top: 15px;" >
      <el-input placeholder="请输入内容" v-model="input" class="input">
        <el-button slot="append" icon="el-icon-search" @click="searchBtn"></el-button>
      </el-input>
      <el-button type="primary">新建分群</el-button>
    </div>


    <div class="div1" >
      <div class="div2">
        <div class="div3">
          <p>近七天未消费会员<span style="color: crimson" >{{unPayIn7Days}}</span>人</p>
          <el-button type="primary" plain @click="unPayBtn">发送优惠券</el-button>
        </div>
      </div>
      <div class="div2">
        <div class="div3">
          <p>近七天有消费会员<span style="color: crimson" >{{PayIn7Days}}</span>人</p>
          <el-button type="primary" plain @click="PayBtn">发送优惠券</el-button>
        </div>
      </div>
      <div class="div2">
        <div class="div3">
          <p>近七天放置购物车会员<span style="color: crimson" >{{addShopIn7Days}}</span>人</p>
          <el-button type="primary" plain @click="addShopBtn">发送优惠券</el-button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  // el: '#search',
  data () {
    return {
      input:'',
      unPayIn7Days:'',
      PayIn7Days:'',
      addShopIn7Days:''
    }
  },
  created(){
    //自动请求后台

    var d ={
      unPayIn7Days:'1',
      PayIn7Days:'2',
      addShopIn7Days:'3'
    }
    this.unPayIn7Days = d.unPayIn7Days,
      this.PayIn7Days = d.PayIn7Days,
      this.addShopIn7Days =d.addShopIn7Days
  },
  methods:{
    searchBtn(){
      //请求


      this.$message('搜索数据:'+ this.input);
    },

    unPayBtn(){
      //请求后台




      this.$message({
        message: '发送成功',
        type: 'success'
      });
    },

    PayBtn(){
      //请求后台




      this.$message({
        message: '发送成功',
        type: 'success'
      });
    },

    addShopBtn(){
      //请求后台




      this.$message({
        message: '发送成功',
        type: 'success'
      });
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .input{
      width: 300px;
    }

    .div1{text-align: center;width: 100%;height: auto;}
    .div2{display: inline-block;border: 1px solid;margin-right: 50px;
      width: 300px; height: 150px;margin-top: 50px;
    }
    .div3{margin-top: 50px}
</style>
