<template>
    <div class="brand-insert">
        <div class="head">
            你当前所在位置>商品中心>商品品牌管理
        </div>
        <div class="content">
            <div class="content-list">
                <div class="content-info">
                    <div class="brand-button">
                        <div class="brand-search">
                            <el-input v-model="input" placeholder="请输入内容" class="goods-input-type"></el-input>
                            <el-button type="primary" @click="getGoodsBrandList">查询</el-button>
                        </div>
                        <div class="brand-add">
                            <el-button type="primary" @click="brandAdd">新增</el-button>
                        </div>
                    </div>
                    <div class="brand-list">
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
                                    prop="brandName"
                                    label="商品品牌名称"
                                    width="auto">
                            </el-table-column>
                            <el-table-column label="操作" width="300px">
                                <template slot-scope="scope">
                                    <el-button type="primary" icon="el-icon-edit" size="mini" @click="brandUpdate(scope.row)">编辑</el-button>
                                    <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row.id)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div>
                            <div class="content-botton-pagination">
                                <el-pagination class="pagination-right"
                                               @size-change="handleSizeChange"
                                               @current-change="handleCurrentChange"
                                               :current-page="query.current"
                                               :page-sizes="[5, 10, 15, 20]"
                                               :page-size="this.query.size"
                                               layout="total, sizes, prev, pager, next, jumper"
                                               :total="this.query.total||0">
                                </el-pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <el-dialog title="新增商品品牌" :visible.sync="dialogFormVisible">
                <el-form :model="form">
                    <el-form-item label="商品品牌名称" :label-width="formLabelWidth">
                        <el-input v-model="form.brandName" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="品牌编码" :label-width="formLabelWidth">
                        <el-input v-model="form.brandCode" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button v-show="!isAdd" type="primary" @click="updateGoodsBrand">修 改</el-button>
                    <el-button v-show="isAdd" type="primary" @click="addGoodsBrand">确 定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>
<script>
export default{
    data() {
        return {
            tableData: [],
            input: '',
            query: {
                // 当前展示的是第几页数据
                current: 1,
                // 每页显示多少条数据
                size: 10,
                total: 0,
            },
            form: {},
            dialogFormVisible: false,
            isAdd:true,
            formLabelWidth: '120px',
        }
    },
    methods: {
        handleSizeChange(val) {
            this.query.size = val
            this.getGoodsBrandList();
        },
        handleCurrentChange(val) {
            this.query.current = val
            this.getGoodsBrandList();
        },
        brandAdd(){
            this.form={}
            this.isAdd=true
            this.dialogFormVisible=true
        },
        brandUpdate(row){
            this.form=row
            this.isAdd=false
            this.dialogFormVisible=true
        },
        async handleDelete(id) {
            const res = await this.$http.get('/api/goodsBrand/deleteGoodsBrand',{ params: { id: id } })
            if (res.data.errorCode === '0') {
                this.$message.success('商品品牌删除成功')
                this.getGoodsBrandList()
            } else {
                this.$message.error('商品品牌删除失败')
            }
        },
        getGoodsBrandList() {
            this.$http({
                method: 'post',
                url: '/api/goodsBrand/getGoodsBrandList',
                params:{ current: this.query.current,size: this.query.size ,brandName: this.input}
            }).then(res => {
                const data = res.data
                if(data.errorCode !== '0'){
                    this.$message.error('商品品牌列表获取失败')
                }else {
                    this.tableData=data.bizContent.records
                    this.query.size=data.bizContent.size
                    this.query.current=data.bizContent.current
                    this.query.total=data.bizContent.total
                }
            }).catch(error => {
                alert(error);
                console.log(error);
            })
        },
        async addGoodsBrand() {
            const res = await this.$http.post('/api/goodsBrand/addGoodsBrand',this.form)
            if (res.data.errorCode === '0') {
                this.$message.success('商品品牌新增成功')
                this.getGoodsBrandList()
                this.dialogFormVisible = false
                this.form={}
            } else {
                this.$message.error('商品品牌新增失败')
            }
        },
        async updateGoodsBrand () {
            const res = await this.$http.post('/api/goodsBrand/updateGoodsBrand',this.form)
            if (res.data.errorCode === '0') {
                this.$message.success('商品品牌修改成功')
                this.getGoodsBrandList()
                this.dialogFormVisible = false
                this.form={}
            } else {
                this.$message.error('商品品牌修改失败')
            }
        }
    },
    created() {
        this.getGoodsBrandList();
    },
    watch:{
        input : function(news,old) {
            if (news === '') {
                this.getGoodsBrandList()
            }
        }
    }
}
</script>
<style lang="less" scoped>
    .brand-insert{
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
            .content-list{
                min-height: 500px;
                .content-info{
                    margin: 20px 20px;
                    .brand-button{
                        overflow: hidden;
                        .brand-search{
                            float: left;
                            .goods-input-type{
                                width: 400px;
                                padding: 0 20px;
                            }
                        }
                        .brand-add{
                            margin-right: 5%;
                            float: right;
                        }
                    }
                    .brand-list{
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
        }
    }
</style>
