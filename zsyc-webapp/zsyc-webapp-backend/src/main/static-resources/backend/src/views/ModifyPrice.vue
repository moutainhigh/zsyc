<template xmlns:font-family="http://www.w3.org/1999/xhtml">
    <div id="app">
        <div style="width: 1200px">
        <div font-family:PingFang SC style="font-size: 50px">
            批量改价
        </div>
        <div style="margin-top: 50px">

            <div>
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    tooltip-effect="dark"
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        label="全选"
                        type="selection"
                        width="auto">
                </el-table-column>
                <el-table-column
                        align="center" prop="img" label="商品图片">
                    <!--<template slot-scope="scope">{{ scope.row.date }
                    }</template>-->
                    <template scope="scope">
                    <img :src="scope.row.img" width="100" height="100"/>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="商品名称"
                        width="auto">
                    <!--<template slot-scope="scope">{{ scope.row.date }}</template>-->
                </el-table-column>



                <el-table-column
                        prop="price"
                        label="商品价格"
                        sortable
                        width="auto">
                </el-table-column>


            </el-table>
            </div>
            <div style="margin-top: 30px">
                <el-input v-model="updatePrice"   @keyup.native="handleClick" placeholder="请输入更改的价格" style="width: 200px"></el-input>
                <span style="margin-left: 20px">
                <el-button type="primary"

                           @click="Pricerise">批量降价</el-button>
                <el-button type="primary"   @click="Pricereduction" >批量升价</el-button>
                </span>
                <div style="margin-left: 680px">
                    <el-button type="primary" round @click="push">提交</el-button>

                </div>
            </div>


        </div>
    </div>
</div>
</template>

<script>
export default {
  data() {
    return {

        updatePrice:'',
        multipleSelection: [],
        input: '',
        tableData:[{img: require("../../static/img/cai.png"),name:'番薯',price:30},

            {img: require("../../static/img/cai.png"),name:'薯仔',price:50},
            {img: require("../../static/img/cai.png"),name:'沙灾',price:800},
        ],
    };
  },
    methods:{
        handleSelectionChange(val) {
            this.multipleSelection = val;

        },
        handleClick() {

            this.updatePrice=this.updatePrice.replace(/[^\.\d]/g,'');


        },


        push(){
            // this.$http
            //     .get("http://localhost:8080/api/Stock/aaa",
            //
            //         { params: { price: b }})
            //     .then(response => {
            //        // alert(response.data.bizContent.id);
            //     })
            //     .catch(error => {
            //         alert(error);
            //         console.log(error);
            //     });

        },
        Pricerise(){

            if( this.multipleSelection!=null){

                for (var i =0;i<this.multipleSelection.length;i++){


                    this.multipleSelection[i].price=parseFloat(this.multipleSelection[i].price)-parseFloat(this.updatePrice);



                }
            }else{
                this.multipleSelection.prop.price=this.updatePrice;
            }
        },
        Pricereduction(){

            if( this.multipleSelection!=null){

                for (var i =0;i<this.multipleSelection.length;i++){


                    this.multipleSelection[i].price=parseFloat(this.multipleSelection[i].price)+parseFloat(this.updatePrice);



                }
            }else{
                this.multipleSelection.prop.price=this.updatePrice;
            }
        }
    }
};
</script>

<style scoped>
</style>
