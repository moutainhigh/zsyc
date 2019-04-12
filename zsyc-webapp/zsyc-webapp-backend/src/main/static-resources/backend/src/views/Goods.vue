<template>
    <div class="goods">
        <div class="goods-head">
            你当前所在位置>商品中心>商品列表
        </div>
        <div class="goods-inner">
            <div class="goods-input">
                <div class="goods-input-left padding-botton">
                    商品名称
                    <el-input v-model="goodsName" placeholder="请输入内容" class="goods-input-wrapper"></el-input>
                </div>
                <div class="goods-input-right padding-botton">
                    商品分类
                    <el-select v-model="typeOne" placeholder="请选择" class="goods-input-select">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-select
                            v-model="typeTwo"
                            collapse-tags
                            style="margin-left: 20px;"
                            placeholder="请选择" class="goods-input-select">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="goods-input-left padding-botton">
                    商品编码
                    <el-input v-model="code" placeholder="请输入内容" class="goods-input-wrapper"></el-input>
                </div>
                <div class="goods-input-right padding-botton">
                    商品价格
                    <el-input v-model="priceOne" placeholder="请输入内容" class="goods-input-select"></el-input>
                    至
                    <el-input v-model="priceTwo" placeholder="请输入内容" class="goods-input-select"></el-input>
                </div>
                <div class="goods-input-left padding-botton">
                    上架状态
                    <el-select v-model="status" placeholder="请选择" class="goods-input-wrapper">
                        <el-option
                                v-for="item in statusList"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="goods-input-right padding-botton">
                    上架时间
                    <el-date-picker
                            v-model="time1"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                    <el-date-picker
                            v-model="time2"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                </div>
            </div>
            <div class="content">
                <div class="content-button">
                    <el-row>
                        <el-button style="margin-right: 30px">查询</el-button>
                        <el-button type="primary">重置</el-button>
                    </el-row>
                </div>
                <div class="content-botton-right">
                    <el-button type="primary">新增商品</el-button>
                    <el-button type="primary">删除商品</el-button>
                </div>
                <div>
                    <el-table
                            ref="multipleTable"
                            :data="tableData"
                            tooltip-effect="dark"
                            style="width: 100%"
                            @selection-change="handleSelectionChange">
                        <el-table-column
                                type="selection"
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="商品名称"
                                width="auto">
                            <!--<template slot-scope="scope">{{ scope.row.date }}</template>-->
                        </el-table-column>
                        <el-table-column
                                prop="code"
                                label="商品编码"
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="tag"
                                label="商品标签"
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="spec"
                                label="商品规格"
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="price"
                                label="商品价格"
                                sortable
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="近一周销量"
                                sortable
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop="status"
                                label="上架状态"
                                width="auto">
                        </el-table-column>
                        <el-table-column
                                prop=" operate"
                                label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        @click="handleClick(scope.row)">修改</el-button>
                                <el-button
                                        size="mini"
                                        type="danger"
                                        @click="handleClick(scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="block">
                        <div class="content-botton-pagination">
                            <el-pagination class="pagination-right"
                                           @size-change="handleSizeChange"
                                           @current-change="handleCurrentChange"
                                           :current-page="currentPage"
                                           :page-sizes="[5, 10, 15, 20]"
                                           :page-size="5"
                                           layout="total, sizes, prev, pager, next, jumper"
                                           :total="30">
                            </el-pagination>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default{
    data() {
        return {
            goodsName: '',
            typeOne:'',
            typeTwo:'',
            type:'',
            code:'',
            priceOne:'',
            priceTwo:'',
            status:'',
            statusList:[{
                name:'上架',
                value:0
            }, {
                name:'下架',
                value:1
            }],
            options: [{
                value: '选项1',
                label: '黄金糕',
                children :[{
                    value: '选项1',
                    label: '黄金糕'
                }, {
                    value: '选项2',
                    label: '双皮奶'
                }]
            }, {
                value: '选项2',
                label: '双皮奶',
                children :[{
                    value: '选项1',
                    label: '黄金糕'
                }, {
                    value: '选项2',
                    label: '双皮奶'
                }]
            }, {
                value: '选项3',
                label: '蚵仔煎'
            }, {
                value: '选项4',
                label: '龙须面'
            }, {
                value: '选项5',
                label: '北京烤鸭'
            }],
            pickerOptions1: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [{
                    text: '今天',
                    onClick(picker) {
                        picker.$emit('pick', new Date());
                    }
                }, {
                    text: '昨天',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24);
                        picker.$emit('pick', date);
                    }
                }, {
                    text: '一周前',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', date);
                    }
                }]
            },
            time1: '',
            time2: '',
            tableData: [{
                date: '2016-05-03',
                name: '王小虎',
                code: 'qc201832',
                tag:'青菜；蔬菜；有机青菜',
                spec:'250g/份',
                price:'2.9元/份',
                num:'200份',
                status:'上架'
            }, {
                date: '2016-05-03',
                name: '王小虎',
                code: 'qc201832',
                tag:'青菜；蔬菜；有机青菜',
                spec:'250g/份',
                price:'2.9元/份',
                num:'200份',
                status:'上架'
            }, {
                date: '2016-05-03',
                name: '王小虎',
                code: 'qc201832',
                tag:'青菜；蔬菜；有机青菜',
                spec:'250g/份',
                price:'2.9元/份',
                num:'200份',
                status:'上架'
            }, {
                date: '2016-05-03',
                name: '王小虎',
                code: 'qc201832',
                tag:'青菜；蔬菜；有机青菜',
                spec:'250g/份',
                price:'2.9元/份',
                num:'200份',
                status:'上架'
            }, {
                date: '2016-05-03',
                name: '王小虎',
                code: 'qc201832',
                tag:'青菜；蔬菜；有机青菜',
                spec:'250g/份',
                price:'2.9元/份',
                num:'200份',
                status:'上架'
            }],
            multipleSelection: [],
            currentPage:1
        }
    },
    methods: {
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleSizeChange(val) {
            alert(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
            alert(`当前页: ${val}`);
        }
    }
}
</script>
<style lang="less" scoped>
    .goods{
        .padding-botton{
            box-sizing: border-box;
            -moz-box-sizing: border-box;    /* Firefox */
            -webkit-box-sizing: border-box;
            padding: 10px;
        }
        .goods-head{
            width: 100%;
            line-height: 20px;
            height: 20px;
            margin-bottom: 10px;
            letter-spacing:1px;
            font-size:16px;
        }
        .goods-inner{
            min-height: 800px;
            padding: 10px;
            background: #fff;
            .goods-input{
                overflow: hidden;
                font-size:12px;
                text-align: center;
                .goods-input-left{
                    float: left;
                    width: 49%;
                    .goods-input-wrapper{
                        width: 200px;
                        padding: 0 20px;
                    }
                }
                .goods-input-right{
                    float: left;
                    width: 49%;
                    .goods-input-select{
                        width: 110px;
                        padding: 0 20px;
                    }
                    .time-input-select{
                        width: 120px;
                        margin: 0 20px;
                    }
                }
            }
            .content{
                overflow: hidden;
                .content-button{
                    margin: 10px 0;
                    text-align: center;
                }
                .content-botton-right{
                    margin-right: 5%;
                    float: right;
                }
                .content-botton-pagination{
                    overflow: hidden;
                    .pagination-right{
                        margin-right: 5%;
                        float: right;
                    }
                }
            }
        }
    }

</style>
