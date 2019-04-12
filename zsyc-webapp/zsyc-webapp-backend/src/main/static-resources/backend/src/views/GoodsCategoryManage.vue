<template>
    <div class="goodCategory">
        <div class="head">
            你当前所在位置>商品中心>商品属性管理
        </div>
        <div class="category-content">
            <div class="category-content-title">
                商品分类管理
            </div>
            <div class="category-content-type">
                <div class="category-content-search">
                    <el-input v-model="input" placeholder="请输入内容" class="goods-input-type" style=""></el-input>
                    <el-button>搜索</el-button>
                </div>
                <div class="category-content-first-input">
                    <el-button type="primary" @click="dialogFormVisible = true">+增加商品分类</el-button>
                </div>
            </div>
            <div class="category-content-button">
                <el-button class="category-content-list el-icon-arrow-up">排序上调</el-button>
                <el-button class="category-content-list el-icon-arrow-down">排序下调</el-button>
                <el-button type="primary" class="category-content-save">保存排序</el-button>
            </div>
            <!--<div style="width: 200px;height: 300px;margin-top: 50px;">-->

             <!---->
                        <!--<el-dialog title="增加商品分类" :visible.sync="dialogFormVisible">-->
                            <!--<el-form :model="form">-->

                                <!--<el-form-item label="商品分类" :label-width="formLabelWidth1">-->
                                    <!--<el-cascader-->

                                            <!--v-model="form.option"-->
                                            <!--placeholder="试试搜索：快捷菜"-->
                                            <!--:options="options"-->
                                            <!--filterable-->
                                            <!--change-on-select-->
                                            <!--@change="handleChange222"-->
                                            <!--style="width: 200px;margin-left: 70px"-->
                                    <!--&gt;</el-cascader>-->
                                    <!--<el-button type="primary" plain style="margin-left: 10px" @click="del">删除商品属性</el-button>-->
                                <!--</el-form-item>-->
                                <!--<el-form-item label="增加商品分类的名称" :label-width="formLabelWidth1">-->
                                    <!--<el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>-->
                                <!--</el-form-item>-->
                                <!--<el-form-item label="更改商品分类的名称" :label-width="formLabelWidth1">-->
                                    <!--<el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>-->
                                <!--</el-form-item>-->



                            <!--</el-form>-->
                            <!--<div slot="footer" class="dialog-footer">-->
                                <!--<el-popover-->
                                        <!--placement="top"-->
                                        <!--width="160"-->
                                        <!--v-model="visible2"-->
                                        <!--&gt;-->
                                    <!--<div v-model="form">-->
                                        <!--<p>确定在分类中加入"{{form.name}}"这个分类吗</p>-->
                                    <!--</div>-->

                                    <!--<div style="text-align: right; margin: 0">-->
                                        <!--<el-button size="mini" type="text" @click="visible2 = false">取消</el-button>-->
                                        <!--<el-button type="primary" size="mini" @click="visible2=false,ttt=true" >确定</el-button>-->
                                    <!--</div>-->
                                    <!--<el-button slot="reference">确定</el-button>-->
                                <!--</el-popover>-->

                            <!--</div>-->
                        <!--</el-dialog>-->
                    <!--</div>-->




                                <div  style="margin-left: 10px;width: 300px">

                                    <div>
                                        商品属性管理:
                                        </div>
                                     <el-cascader
                                :show-all-levels="false"
                                v-model="selectedOptions2"
                                placeholder="试试搜索：快捷菜"
                                :options="options"
                                style="margin-top: 20px"
                                @change="handleChange222"

                        ></el-cascader>
                                        <div>
                                            <el-button type="primary" plain style=" margin-left: 220px;margin-top: -40px;float: left" @click="del">删除商品属性</el-button>

                                        </div>

                                                                                    <el-input style="width: 200px;margin-top: 20px" v-model="adde"
                                                      placeholder="请输入要增加的商品属性"></el-input>
                                        <div>
                            <el-button type="primary" style="float: left;margin-left: 220px;margin-top: -40px" plain @click="Add ">增加商品属性</el-button>
                                        </div>

                        <div >
                            <el-input v-model="updatee" style="width: 200px;margin-top: 20px"placeholder="请先在上面选好"></el-input>

                        </div>
                                        <el-button type="primary"  style="float: left;margin-left: 220px;margin-top: -40px" plain @click="updateGoods">改变商品属性</el-button>
                    </div>





            </div>








    </div>
</template>

<script>
    export default {
        data() {

            return {
                visible2: false,
                ttt:false,
                adde: '',
                updatee: '',
                form: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    resource: '',
                    desc: '',
                    option: '',
                },
                input: '',
                dialogTableVisible: false,
                dialogFormVisible: false,
                tableData: [
                    {
                        id: 1,
                        name: '王小虎',
                    }, {
                        id: 2,
                        name: '王小虎',
                    }, {
                        id: 3,
                        name: '王小虎',
                    }, {
                        id: 4,
                        name: '王小虎',
                    }, {
                        id: 5,
                        name: '王小虎',
                    }, {
                        id: 5,
                        name: '王小虎',
                    }, {
                        id: 6,
                        name: '王小虎',
                    }, {
                        id: 6,
                        name: '王小虎',
                    }
                ],
                options: [{
                    value: 'zhinan',
                    label: '快捷菜',
                    children: [{
                        value: 'shejiyuanze',
                        label: '菜类',
                        children: [{
                            value: 'yizhi',
                            label: '生菜'
                        }, {
                            value: 'fankui',
                            label: '菜心'
                        }, {
                            value: 'xiaolv',
                            label: '西洋菜'
                        }, {
                            value: 'kekong',
                            label: '菠菜'
                        }]
                    }, {
                        value: 'daohang',
                        label: '肉类',
                        children: [{
                            value: 'cexiangdaohang',
                            label: '猪肉'
                        }, {
                            value: 'dingbudaohang',
                            label: '牛肉'
                        }]
                    },
                        {
                            value: 'daohang',
                            label: '瓜类',
                            children: [{
                                value: 'cexiangdaohang',
                                label: '冬瓜'
                            }, {
                                value: 'dingbudaohang',
                                label: '丝瓜'
                            }]
                        }]
                }, {
                    value: 'zujian',
                    label: '煤气',
                    children: [{
                        value: 'basic',
                        label: '新租瓶',
                        children: [{
                            value: 'layout',
                            label: '15G'
                        }, {
                            value: 'color',
                            label: '20G'
                        }, {
                            value: 'typography',
                            label: '30G'
                        }, {
                            value: 'icon',
                            label: '40G'
                        }, {
                            value: 'button',
                            label: '50G'
                        }]
                    },]
                }],
                value: '',
                currentPage: 1
            }
        },
        watch:{

        ttt(val,oldVal){
            if(val==true){
                alert(this.form.option[this.form.option.length-1]+"aaa"+this.form.name);
                JSON.stringify(this.form)
                this.$http
                    .get("http://localhost:8080/api/Goods/get_order_by_id",this.form.option[this.form.option.length-1],this.form.name )
                    .then(response => {
                        if(response.data==1){
                            alert('增加成功');
                        }else{
                            alert('添加失败')
                        }
                    })
                    .catch(error => {

                    });
            }
        }


        },
        methods: {




            //删除属性
            del(){

                this.$http
                    .get("http://localhost:8080/api/Goods/get_order_by_id",this.form.option[this.form.option.length-1],this.form.name )
                    .then(response => {
                        if(response.data==1){
                            this.$message({
                                message: '成功删除',
                                type: 'success'
                            });
                        }else{
                            this.$message.error('请先在“选择商品属性“选择属性再操作');

                        }
                    })
                    .catch(error => {
                        this.$message.error('请先在“选择商品属性“选择属性再操作');
                    });

            },
            //增加商品属性
            Add(){
                if(this.adde!=''){


                this.$http
                    .get("http://localhost:8080/api/Goods/get_order_by_id",this.form.option[this.form.option.length-1],this.form.name )
                    .then(response => {
                        if(response.data==1){
                            alert('增加成功');
                        }else{
                            alert('添加失败')
                        }
                    })
                    .catch(error => {

                    });

            }else{
                    this.$message.error('请先输入需要增加商品属性');
            }
            },
            //改变商品属性
            updateGoods(){
                if(this.updatee!=''){
                this.$http
                    .get("http://localhost:8080/api/Goods/get_order_by_id",this.form.option[this.form.option.length-1],this.form.name )
                    .then(response => {
                        if(response.data==1){
                            alert('增加成功');
                        }else{
                            alert('添加失败')
                        }
                    })
                    .catch(error => {

                    });
                }else{
                    this.$message.error('请先输入需要改变商品属性');
                }
            },



            handleClick(row) {
                alert(row);
            },
            handleSizeChange(val) {
                alert(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                alert(`当前页: ${val}`);
            },
            handleChange222(value) {


            },

        },

    }
</script>
<style lang="less" scoped>
    .goodCategory {
        .head {
            line-height: 20px;
            height: 20px;
            margin-bottom: 10px;
            letter-spacing: 1px;
            font-size: 12px;
        }

        .category-content {
            padding: 10px;
            background: #fff;
            overflow: hidden;

            .category-content-title {
                font-size: 20px;
                margin: 10px;
            }

            .category-content-type {
                overflow: hidden;

                .category-content-search {
                    float: left;

                    .goods-input-type {
                        width: 400px;
                        padding: 0 20px;
                    }
                }

                .category-content-first-input {
                    margin-right: 5%;
                    float: right;
                }
            }

            .category-content-button {
                margin: 20px;

                .category-content-list {
                    margin-right: 20px;
                }

                .category-content-save {
                    margin-left: 200px;
                }
            }

            .content-box {
                margin: 0 20px;

                .content-select {
                    overflow: hidden;
                    min-width: 1000px;

                    .catalog-icon {
                        float: left;
                        color: #848282;
                    }

                    .select-catalog {
                        float: left;
                        width: 49%;
                        height: 350px;
                        max-width: 350px;
                        background: #eee;
                        margin: 20px;

                        .catalog-title {
                            margin: 10px;
                            color: #848282;
                        }

                        .catalog-list {
                            overflow-y: auto;
                            height: 300px;
                            background: #fff;
                            margin: 10px;
                            line-height: 40px;

                            .catalog-info {
                                height: 40px;
                                list-style-type: none;
                                border-bottom: 1px solid #848282;
                                line-height: 40px;
                                color: #666;

                                .catalog-name {
                                    margin-left: 10px;
                                    display: inline-block;
                                }

                                .catalog-icon {
                                    line-height: 40px;
                                    height: 40px;
                                    float: right;
                                }
                            }
                        }
                    }
                }
            }

            .category-tag-manage {
                .category-tag-title {
                    font-size: 20px;
                    margin: 10px;
                }

                .category-title {
                    display: inline-block;
                    margin-right: 40px;
                    font-size: 15px;
                    color: #666;
                }

                .category-tag-select {
                    margin: 20px;
                }

                .category-tag-list {
                    display: flex;
                    margin: 20px;

                    .category-tag-table {
                        width: 800px;

                        .category-tag-table-show {
                            width: 300px;
                        }

                        .content-botton-pagination {
                            overflow: hidden;

                            .pagination-right {
                                margin-right: 5%;
                                float: left;
                            }
                        }
                    }
                }
            }
        }
    }

</style>
