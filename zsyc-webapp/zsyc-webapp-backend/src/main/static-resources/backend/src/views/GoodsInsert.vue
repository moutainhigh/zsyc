<template>
    <div class="goods-insert">
        <div class="head">
            你当前所在位置>商品中心>新增商品
        </div>

        <div class="content" style="height: 1800px">
            <div class="content-title">
                添加商品
            </div>

            <div style="width: 1600px;height: 250px;margin-top: 50px;">
                <el-form :model="AddGoodsform">
                    <div class="block">

                        <el-upload
                                action="http://14.29.207.159:9904/upload"
                                list-type="picture-card"
                                :on-preview="handlePictureCardPreview"
                                :beforeUpload="beforeUploadPicture"
                                :on-success="handleAvatarSuccess"
                                :on-remove="handleRemove">
                            <i class="el-icon-plus"></i>
                        </el-upload>


                    </div>

                    <div style="width: 2000px;margin-top: 20px">
                        <div>
                            <div style="margin-top: 40px">
                                <el-form-item label="商品名称：" prop="name">

                                    <el-input style="width: 200px" v-model="AddGoodsform.goodsName"
                                              placeholder="请输入商品名字"></el-input>
                                </el-form-item>
                            </div>
                            <!--<div style="margin-top: 20px;">-->
                            <!--请输入商品价格:-->
                            <!--<el-input style="width: 200px" v-model="form.price" placeholder="请输入商品价格"></el-input>-->
                            <!--</div>-->
                            <el-form-item label="商品品牌：" prop="name">

                                <el-select v-model="AddGoodsform.brandCode" placeholder="请选择商品的品牌">
                                    <el-option
                                            v-for="item in options2"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="商品分类：" prop="name">
                                <el-cascader
                                        style="width: 200px;"
                                        ref="cascaderAddr"
                                        v-model="selectedOptions3"
                                        placeholder="请选择商品得分类"
                                        :options="options"
                                        filterable
                                        change-on-select
                                        @change="classification"
                                ></el-cascader>

                            </el-form-item>


                            <el-form-item label="请选择商品种类：" prop="name">
                                <template>
                                    <el-radio v-model="AddGoodsform.goodsType" label="0" >普通商品</el-radio>
                                    <el-radio v-model="AddGoodsform.goodsType" label="1">组合商品</el-radio>
                                </template>
                            </el-form-item>
                            <div style="margin-top: 20px;">

                                <el-form-item :label-width="formLabelWidth1" prop="name" label="请选择商品属性：">

                                    <el-select v-model="attrKey1" placeholder="请选择商品的属性:">
                                        <el-option
                                                v-for="item in options2"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value">
                                        </el-option>
                                    </el-select>
                                    <div style="margin-left: 320px;margin-top: -42px">
                                        <el-select

                                                v-model="value11"
                                                multiple
                                                collapse-tags
                                                style="margin-left: 20px;"
                                                placeholder="请选择商品的具体属性">

                                            <el-option
                                                    style="width: 200px;"
                                                    v-for="item in options2"
                                                    :key="item.value"
                                                    :label="item.label"
                                                    :value="item.value"

                                            >
                                            </el-option>
                                        </el-select>
                                    </div>


                                </el-form-item>
                                <el-form-item prop="name" label="商品介绍：">

                                    <el-input

                                            style="width: 500px"
                                            type="textarea"
                                            :rows="10"
                                            :cos="20"
                                            placeholder="请输入内容"
                                            v-model=" AddGoodsform.description">
                                    </el-input>
                                </el-form-item>

                            </div>
                            <div style="margin-top: 20px">
                                <el-button type="primary" plain @click="push">提交</el-button>
                            </div>

                        </div>

                    </div>

                </el-form>
                <div style="margin-left: 350px;margin-top: -840px;width: 700px"

                     :class="(AddGoodsform.goodsType==='1')?'xianshi':'yincang'"
                >
                    <el-table

                            :data="tableData3"
                            height="500px"

                            border
                           >

                        <el-table-column
                                prop="GoodName"
                                label="组合子商品名称"
                                width="300">

                        </el-table-column>
                        <el-table-column

                                label="组合的排序"
                                width="350">
                            <template scope="scope" >
                    <el-input v-model="scope.row.input" placeholder="请输入内容"
                              @keyup.native="handleClick(scope.row)"
                    style="width: 200px"
                    ></el-input>
                                <span style="margin-left: 10px">

                        <span>
                     <el-button v-if="scope.row.xiugai!='1'" @click="open3(scope.row)"

                     >修改</el-button></span>


                                </span>
                                <span v-if="scope.row.xiugai=='1'">已经修改了</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
            <el-dialog :visible.sync="imgDialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>


        </div>

    </div>
</template>
<style>
    .xianshi {
        display: block;
    }

    .yincang {
        display: none;

    }
</style>
<script>
    export default {

        data() {

            return {
                input:'',
                xiugai:'0',
                tableData3: [{

                    GoodName: '鸡唉唉唉',
                    sku:'asasasa4432',
                },
                    {
                        GoodName:'鸭啊啊啊啊',
                        sku:'asasasa4432',

                    },{
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    },
                    {
                        GoodName:'鹅aaaaa',
                        sku:'asasasa4432',

                    }
                ],
                SortNum: '',
                dialogVisible: false,//添加门店是否隐藏
                imgDialogVisible: false,//图片展示框是否隐藏
                value: '',
                value11: [],
                attrKey1: '',
                selectedOptions2: [],
                selectedOptions3: [],

                AddGoodsform: {
                    goodsType: '0',
                    goodsName: '',
                    categoryId: '',
                    brandCode: '',
                    description: '',
                    goodsAttributeListVOS: [],
                    goodsGroupInsertVOS:[],
                    fileUrlList: '',//图片路径数据
                },

                fileList2: [],
                options: [{
                    value: '快捷菜',
                    label: '快捷菜',
                    children: [{
                        value: '菜类',
                        label: '菜类',
                        children: [{
                            value: '生菜',
                            label: '生菜'
                        }, {
                            value: '菜心',
                            label: '菜心'
                        }, {
                            value: '西洋菜',
                            label: '西洋菜'
                        }, {
                            value: '菠菜',
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
                options2: [{

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
                }],


                dialogImageUrl: '',//图片路径

                imgData: [],     //图片数据


                zhi: [],
                adde: '',
                updatee: '',

                selectedOptions: [],


                ruleForm: {
                    name: '',
                    tag: '',
                    spec: '',
                    price: '',
                    code: '',
                },
                rules: {
                    name: [
                        {required: true, message: '请输入商品名称', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    tag: [
                        {required: true, message: '请输入商品标签', trigger: 'blur'},
                        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ],
                    spec: [
                        {required: true, message: '请输入商品规格', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    price: [
                        {required: true, message: '请输入商品价格', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 5 个字符', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入商品编码', trigger: 'blur'},
                        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ],
                    property: [
                        {required: true, message: '请输入商品编码', trigger: 'blur'},
                        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        watch: {
            'AddGoodsform.goodsType': function (val) {
                //提交请求获得组合商品子数据
                if (val == 1) {
                    this.$http.post('/api/Goods/addStore', JSON.stringify(this.AddGoodsform),
                    )
                    }else{
                   this.AddGoodsform.goodsGroupInsertVOS=[];
                }
            }
        },


        methods: {
            handleClick(value) {

                value.input=value.input.replace(/[^\.\d]/g,'');
                value.input=value.input.replace('.','');

            },
            open3(value){
            if(value.input!=''){
                var inputX = {subSku: '', sort: ''}
                inputX.subSku = value.sku
                inputX.sort = value.input;
                this.AddGoodsform.goodsGroupInsertVOS.push(inputX);

                this.$message({
                    message: '修改成功'+"排序值为"+value.input,
                    type: 'success'
                });
                value.xiugai='1'
            }else{
                this.$message.error('不可以空哦');
            }


    },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            push() {
                var dataM = {attrKey: '', attrValueKeys: []}
                dataM.attrKey = this.attrKey1
                dataM.attrValueKeys = this.value11;
                this.AddGoodsform.goodsAttributeListVOS.push(dataM);
                var json=JSON.stringify(this.AddGoodsform);

                alert(json);
                this.$http.post('/api/Goods/addStore', json,
                ).then(res => {
                    //返回数据
                    if (res.data.bizContent == 1) {
                        _this.$message({
                            message: '添加成功',
                            type: 'success'
                        });
                    }
                });
            },
            //删除照片
            handleRemove(file) {

                //获取要删除的code，同时从图片list中去掉这个code
                var delCode = null;
                for (var i = 0; i < this.imgData.length; i++) {
                    if (file.name == this.imgData[i].name) {
                        delCode = this.imgData[i].code;
                        this.form.fileUrlList = this.form.fileUrlList.replace(this.imgData[i].code + ",", "");

                        break;
                    }
                }

                this.$http.get(this.url + '/api/store/delStoreImg', {
                    params: {delCode: delCode}
                }).then((res) => {   //成功的回调

                }).catch((res) => {  //失败的回调
                    console.log(res.status);
                });
            },
            //查看照片
            //判断格式
            beforeUploadPicture(file) {
                const isImage = file.type == 'image/png' || file.type == 'image/jpg' || file.type == 'image/jpeg'
                    || file.type == 'image/bmp' || file.type == 'image/gif' || file.type == 'image/webp';
                const isLt2M = file.size < 1024 * 1024 * 2;
                if (!isImage) {
                    this.$message.error('上传只能是png,jpg,jpeg,bmp,gif,webp格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 2MB!');
                }

                return isImage && isLt2M;
            },
            //成功回调
            handleAvatarSuccess(data) {
                //保存返回的code
                var imgCode = data.bizContent.code;
                //把所有的图片code通过“,”拼接起来
                this.form.fileUrlList = this.form.fileUrlList + imgCode + ',';

                //把图片name和code关联
                var dataM = {name: '', code: ''}
                dataM.name = data.bizContent.name;
                dataM.code = imgCode;
                this.imgData.push(dataM);
            },
            //查看照片
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.imgDialogVisible = true;
            },
            ShuXing(){
                alert("寄哪里了")

            },
            // handleChange() {
            //     // this.attrKey=this.$refs['cascaderAddr'].currentLabels
            //     // this.attrkey=this.attrKey[this.attrKey.length-1]
            //     alert("触发了");
            //     // this.AddGoodsform.goodsAttributeListVOS[0]="attrKey:"+this.form.attrKey+","+"attrValueKeys:"+this.value11
            //     // this.form.categoryId=this.selectedOptions3[this.selectedOptions3.length-1]
            //     alert(this.AddGoodsform.goodsAttributeListVOS)
            //     //提交选择的id获得新的商品分类数据
            //     this.$http.post('/api/Goods/addStore', this.selectedOptions3[this.selectedOptions3.length - 1]
            //     ).then(res => {
            //         //返回数据
            //         if (res.data.bizContent == 1) {
            //             _this.$message({
            //                 message: '添加成功',
            //                 type: 'success'
            //             });
            //         }
            //     });
            //
            // },
            //商品分类请求更新数据
            classification() {
                this.$http.post('/api/Goods/addStore', this.selectedOptions3[this.selectedOptions3.length - 1]
                )
            },
        },


        created() {
            //初始化加载获得品牌编码下拉数据
            this.$http.post('/api/Goods/addStore'
            )

        }
    }
</script>
<style lang="less" scoped>
    .goods-insert {
        .head {
            line-height: 20px;
            height: 20px;
            margin-bottom: 10px;
            letter-spacing: 1px;
            font-size: 12px;
        }

        .content {
            padding: 10px;
            background: #fff;
            overflow: hidden;

            .content-title {
                font-size: 20px;
                margin: 10px;
            }

            .content-info {
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
                                    height: 40px;
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

                .content-table-input {
                    .content-table-title {
                        font-size: 20px;
                        margin: 10px;
                    }

                    .content-table-from {
                        overflow: hidden;
                        min-width: 1000px;

                        .content-table-insert {
                            float: left;
                            width: 49%;
                        }

                        .content-table-picture {
                            width: 49%;
                            float: right;

                            .content-table-pic-insert {
                                margin: 100px 20px;
                                width: 120px;
                                height: 120px;
                                background: #000;
                            }
                        }
                    }

                    .goods-input-type {
                        width: 400px;
                        padding: 0 10px;
                    }
                }
            }
        }
    }
</style>
