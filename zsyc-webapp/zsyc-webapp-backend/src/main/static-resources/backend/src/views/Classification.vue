<template>
  <div>
      <div  style="margin-left: 10px;width: 300px">
          <div>
              商品分类管理:
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
              <el-button type="primary" plain style=" margin-left: 220px;margin-top: -40px;float: left" @click="del">删除商品分类</el-button>

          </div>

          <el-input style="width: 200px;margin-top: 20px" v-model="adde"
                    placeholder="请输入要增加的商品分类"></el-input>
          <div>
              <el-button type="primary" style="float: left;margin-left: 220px;margin-top: -40px" plain @click="Add ">增加商品分类</el-button>
          </div>

          <div >
              <el-input v-model="updatee" style="width: 200px;margin-top: 20px"placeholder="请先在上面选好"></el-input>

          </div>
          <el-button type="primary"  style="float: left;margin-left: 220px;margin-top: -40px" plain @click="update">改变商品分类</el-button>
      </div>



  </div>

</template>

<script>
export default {


    data() {
        return {
            dialogImageUrl: '',
            dialogVisible: false,

            vals:[],
            val: [],
            options: [{
                value: 'zhinan',
                label: '指南',
                children: [{
                    value: 'shejiyuanze',
                    label: '设计原则',
                    children: [{
                        value: 'yizhi',
                        label: '一致'
                    }, {
                        value: 'fankui',
                        label: '反馈'
                    }, {
                        value: 'xiaolv',
                        label: '效率'
                    }, {
                        value: 'kekong',
                        label: '可控'
                    }]
                }, {
                    value: 'daohang',
                    label: '导航',
                    children: [{
                        value: 'cexiangdaohang',
                        label: '侧向导航'
                    }, {
                        value: 'dingbudaohang',
                        label: '顶部导航'
                    }]
                }]
            }, {
                value: 'zujian',
                label: '组件',
                children: [{
                    value: 'basic',
                    label: 'Basic',
                    children: [{
                        value: 'layout',
                        label: 'Layout 布局'
                    }, {
                        value: 'color',
                        label: 'Color 色彩'
                    }, {
                        value: 'typography',
                        label: 'Typography 字体'
                    }, {
                        value: 'icon',
                        label: 'Icon 图标'
                    }, {
                        value: 'button',
                        label: 'Button 按钮'
                    }]
                }, {
                    value: 'form',
                    label: 'Form',
                    children: [{
                        value: 'radio',
                        label: 'Radio 单选框'
                    }, {
                        value: 'checkbox',
                        label: 'Checkbox 多选框'
                    }, {
                        value: 'input',
                        label: 'Input 输入框'
                    }, {
                        value: 'input-number',
                        label: 'InputNumber 计数器'
                    }, {
                        value: 'select',
                        label: 'Select 选择器'
                    }, {
                        value: 'cascader',
                        label: 'Cascader 级联选择器'
                    }, {
                        value: 'switch',
                        label: 'Switch 开关'
                    }, {
                        value: 'slider',
                        label: 'Slider 滑块'
                    }, {
                        value: 'time-picker',
                        label: 'TimePicker 时间选择器'
                    }, {
                        value: 'date-picker',
                        label: 'DatePicker 日期选择器'
                    }, {
                        value: 'datetime-picker',
                        label: 'DateTimePicker 日期时间选择器'
                    }, {
                        value: 'upload',
                        label: 'Upload 上传'
                    }, {
                        value: 'rate',
                        label: 'Rate 评分'
                    }, {
                        value: 'form',
                        label: 'Form 表单'
                    }]
                }, {
                    value: 'data',
                    label: 'Data',
                    children: [{
                        value: 'table',
                        label: 'Table 表格'
                    }, {
                        value: 'tag',
                        label: 'Tag 标签'
                    }, {
                        value: 'progress',
                        label: 'Progress 进度条'
                    }, {
                        value: 'tree',
                        label: 'Tree 树形控件'
                    }, {
                        value: 'pagination',
                        label: 'Pagination 分页'
                    }, {
                        value: 'badge',
                        label: 'Badge 标记'
                    }]
                }, {
                    value: 'notice',
                    label: 'Notice',
                    children: [{
                        value: 'alert',
                        label: 'Alert 警告'
                    }, {
                        value: 'loading',
                        label: 'Loading 加载'
                    }, {
                        value: 'message',
                        label: 'Message 消息提示'
                    }, {
                        value: 'message-box',
                        label: 'MessageBox 弹框'
                    }, {
                        value: 'notification',
                        label: 'Notification 通知'
                    }]
                }, {
                    value: 'navigation',
                    label: 'Navigation',
                    children: [{
                        value: 'menu',
                        label: 'NavMenu 导航菜单'
                    }, {
                        value: 'tabs',
                        label: 'Tabs 标签页'
                    }, {
                        value: 'breadcrumb',
                        label: 'Breadcrumb 面包屑'
                    }, {
                        value: 'dropdown',
                        label: 'Dropdown 下拉菜单'
                    }, {
                        value: 'steps',
                        label: 'Steps 步骤条'
                    }]
                }, {
                    value: 'others',
                    label: 'Others',
                    children: [{
                        value: 'dialog',
                        label: 'Dialog 对话框'
                    }, {
                        value: 'tooltip',
                        label: 'Tooltip 文字提示'
                    }, {
                        value: 'popover',
                        label: 'Popover 弹出框'
                    }, {
                        value: 'card',
                        label: 'Card 卡片'
                    }, {
                        value: 'carousel',
                        label: 'Carousel 走马灯'
                    }, {
                        value: 'collapse',
                        label: 'Collapse 折叠面板'
                    }]
                }]
            }, {
                value: 'ziyuan',
                label: '资源',
                children: [{
                    value: 'axure',
                    label: 'Axure Components'
                }, {
                    value: 'sketch',
                    label: 'Sketch Templates'
                }, {
                    value: 'jiaohu',
                    label: '组件交互文档'
                }]
            }]

        }
    },
    created() {

    },
    methods: {


            onProvincesChange(item) {
    console.log(item, this.$refs['cascaderAddr'].currentLabels)
                console.log(this.$refs['cascaderAddr'].currentLabels)
},

        //删除照片
        handleRemove(file, fileList) {
            this.$http.get('http://localhost:8088/api/store/delStoreImg',{
                params:{fileName: file.name}
            }).then((res) => {   //成功的回调

            }).catch((res) => {  //失败的回调
                console.log(res.status);
            });
        },
        //查看照片
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
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
        handleAvatarSuccess(data){
           var ss = data.bizContent;

        }

    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
