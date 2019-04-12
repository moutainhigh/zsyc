<template>
    <div id="app">
        <div>
            <div>统一备货条件：
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
                <span> <el-button :plain="true" size="mini" round style="margin-left: 50px" @click="timeupdate()">修改</el-button></span>
            </div>
        </div>
        <div style="margin-top: 30px;">
            <el-radio-group v-model="radio3"  size="medium" @change="followup">
                <el-radio-button label="待备货订单"></el-radio-button>
                <el-radio-button label="已备货订单"></el-radio-button>
            </el-radio-group>
        </div>

        <el-table
                :data="tableData"
                border
                style="width: 100%">
            <el-table-column
                    prop="warehouseOrderNo"
                    label="序号"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="goodsName"
                    label="商品名称"
            >
            </el-table-column>
            <el-table-column
                    prop="attrValue"
                    label="规格"
            >
            </el-table-column>
            <el-table-column
                    prop="message"
                    label="总量"
            >
            </el-table-column>
            <el-table-column
                    prop="status"
                    label="完成状态"
            >
                <template scope="scope">
                    <el-button size="small" v-if="scope.row.status==='READY'" style="text-align: center" type="primary">未完成</el-button>
                    <span v-else style="color: lightslategray" >已完成</span>

                </template>
            </el-table-column>
        </el-table>











    </div>

</template>

<script>
    export default {
        name: 'HelloWorld2',
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
                list:[
                    {wancheng:"未完成",num:"1",Time: '2018-12-11', name: '张三', phone: '13918221732', index: '4'},
                    {wancheng:"已完成",num:"2",Time:'2018-12-11',name:'张三',phone:'13918221732',index:'4'}
                ],
                fi:[{fen:2,img: require("../../static/img/cai.png"),porce:500},

                    {fen:2,img: require("../../static/img/cai.png"),porce:500}],

                value6: '',
                value3: '',
                value4: '',
                radio3: '待备货订单',
                tableData: [],
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

            }, 1000),

            this.$http
                .get("http://localhost:8080/api/BackendWarehouseController/selectReadyWarehouse").then(response => {
                this.tableData =response.data.bizContent;
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
