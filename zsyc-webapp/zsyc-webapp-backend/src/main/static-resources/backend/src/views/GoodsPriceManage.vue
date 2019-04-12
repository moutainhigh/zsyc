<template>
    <div class="goods-price">
        <div class="head">
            你当前所在位置>商品中心>商品列表
        </div>
        <div class="content">
            <div class="content-button">
                <el-row>
                    <el-button style="margin-right: 30px">开盘</el-button>
                    <el-button type="primary">收盘</el-button>
                </el-row>
            </div>
            <div class="content-distribution-time">
                <div class="content-time-list">
                    早上免费配送时间段
                    <el-date-picker
                            v-model="morningToTime"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                    至
                    <el-date-picker
                            v-model="moringEndTime"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                </div>
                <div class="content-time-list">
                    下午免费配送时间段
                    <el-date-picker
                            v-model="afternoonToTime"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                    至
                    <el-date-picker
                            v-model="afternoonEndTime"
                            type="date"
                            placeholder="选择日期" class="time-input-select">
                    </el-date-picker>
                </div>
            </div>
            <div class="content-button-update">
                <el-row>
                    <el-button style="margin-right: 30px">批量调价</el-button>
                </el-row>
            </div>
            <div class="content-table">
                <el-table
                        ref="multipleTable"
                        :data="tableData"
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange">
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
                            prop="spec"
                            label="商品规格"
                            width="auto">
                    </el-table-column>
                    <el-table-column
                            prop="YPrice"
                            label="商品昨日价格"
                            width="auto">
                    </el-table-column>
                    <el-table-column
                            prop="NPrice"
                            label="商品今日开盘价"
                            width="auto">
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
</template>
<script>
export default{
    data () {
        return {
            morningToTime: '',
            moringEndTime: '',
            afternoonToTime: '',
            afternoonEndTime: '',
            tableData: [{
                name: '王小虎',
                code: 'qc201832',
                spec:'250g/份',
                YPrice:'2.9元/份',
                NPrice:'2.9元/份'
            }, {
                name: '王小虎',
                code: 'qc201832',
                spec:'250g/份',
                YPrice:'2.9元/份',
                NPrice:'2.9元/份'
            }, {
                name: '王小虎',
                code: 'qc201832',
                spec:'250g/份',
                YPrice:'2.9元/份',
                NPrice:'2.9元/份'
            }, {
                name: '王小虎',
                code: 'qc201832',
                spec:'250g/份',
                YPrice:'2.9元/份',
                NPrice:'2.9元/份'
            }, {
                name: '王小虎',
                code: 'qc201832',
                spec:'250g/份',
                YPrice:'2.9元/份',
                NPrice:'2.9元/份'
            }],
            multipleSelection: [],
            currentPage:1
        }
    },
    methods: {
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
    .goods-price{
        .time-input-select{
            margin: 0 20px;
        }
        .head{
            line-height: 20px;
            height: 20px;
            margin-bottom: 10px;
            letter-spacing:1px;
            font-size:16px;
        }
        .content{
            min-height: 800px;
            padding: 10px;
            background: #fff;
            overflow: hidden;
            .content-button{
                margin: 10px 0;
                text-align: center;
            }
            .content-distribution-time{
                margin: 20px 0;
                .content-time-list{
                    width: 49%;
                    float: left;
                    text-align: center;
                }
            }
            .content-button-update{
                margin-right: 5%;
                margin-top: 30px;
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

</style>
