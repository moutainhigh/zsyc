<template>
  <div id="query">

    <div>
      <div  class="div1">
        店铺名称:
        <el-input  v-model = "name" placeholder="请输入名称" class="input" ></el-input>
        <l style="margin-left: 600px">店铺所在地:&nbsp;&nbsp;</l>
        <el-select v-model="province"  placeholder="请选择" class="input2" @change="selectProvince">
          <el-option
                  v-for="item in data"
                  :key="item.value"
                  :value="item.name">
          </el-option>
        </el-select>
        省
        <el-select v-model="city"  placeholder="请选择" class="input2" @change="selectCity">
          <el-option
                  v-for="item in provinceData.childs"
                  :key="item.value"
                  :value="item.name">
          </el-option>
        </el-select>
        市
        <el-select v-model="area"  placeholder="请选择" class="input2" @change="selectArea">
          <el-option
                  v-for="item in cityData.childs"
                  :key="item.value"
                  :value="item.name">
          </el-option>
        </el-select>
        区
        <el-select v-model="street"  placeholder="请选择" class="input2" @change="selectStreet">
          <el-option
                  v-for="item in areaData.childs"
                  :key="item.value"
                  :value="item.name">
          </el-option>
        </el-select>
        街道
      </div>




      <div class="div1">
        店铺编号:
        <el-input  v-model = "number" placeholder="请输入编号" class="input" ></el-input>
        <l style="margin-left: 600px">
          店铺联系人:
          <el-input  v-model = "linkman" placeholder="请输入联系人" class="input"></el-input>
        </l>
      </div>

      <div class="div1">
        经营范围:
        <el-input class="input" v-model="scope" placeholder="请输入经营范围"></el-input>

        <l style="margin-left: 600px">
          合作周期:&nbsp;&nbsp;&nbsp;
          <el-date-picker
            v-model="beginTime"
            type="datetime"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            :editable = "false"
            placeholder="选择开始时间">
          </el-date-picker>
          至
          <el-date-picker
            v-model="endTime"
            type="datetime"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            :editable = "false"
            placeholder="选择结束时间">
          </el-date-picker>
        </l>
      </div>

    </div>

    <div class="queDiv">
      <el-button  plain class = "queBtn"  @click="query" >查询</el-button>
      <el-button plain class = "queBtn" @click="reset" style="margin-left: 40px">重置</el-button>
    </div>


    <div class="div">
      <el-button   plain @click="addStore" type="info"  size="mini">新增店铺</el-button>
      <el-button   plain @click="delStore" type="info" style="float: right;" size="mini">删除店铺</el-button>
    </div>

    <div>
      <el-table :data="tableData" style="width: auto" @selection-change="changeFun">
        <!--:default-sort = "{prop: 'tableData', order: 'descending'}" 排序-->
        <el-table-column width="30px" type="selection">
        </el-table-column>
        <el-table-column width="100px"
                         label="全选">
          <!--<template slot-scope="{row,$index}">-->
          <!--<span> {{$index + 1}} </span>-->
          <!--</template>-->
        </el-table-column>
        <el-table-column
          prop="storeName"
          label="店铺名称"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="storeNo"
          label="店铺编号"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="scope"
          label="经营范围"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="address"
          label="所在地"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="linkman"
          label="联系人"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="telephone"
          label="手机"
          width="auto">
        </el-table-column>

        <el-table-column
          prop="cooperation"
          label="合作期"
          width="auto">
        </el-table-column>

      </el-table>

    <div style="float: right">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="1"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    </div>



    <!--添加框-->
    <el-dialog
            title="新增门店"
            :visible.sync="dialogVisible"
            width="1000px">

      <el-form  ref="form"  :model="addData">
        <div>
          门店名称：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <el-input class="input" v-model="addData.storeName"></el-input>
          <span style="margin-left: 300px">电话：</span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <el-input class="input" v-model="addData.telephone"></el-input>
        </div>
        <div style="margin-top: 40px">
          门店楼层运费：
          <el-input class="input" v-model="addData.carriage"></el-input>
          <span style="margin-left: 300px">门店自设租金：</span>
          <el-input class="input" v-model="addData.rent"></el-input>
        </div>
        <div style="margin-top: 40px">
          经营范围：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <el-input class="input" v-model="addData.scope" ></el-input>
          <span style="margin-left: 300px">门店类型：</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <el-select v-model="addData.storeType" clearable placeholder="请选择" class="input">
            <el-option
                    v-for="item in storeType"
                    :key="item.key"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>

        <div style="margin-top: 40px">
          详细地址:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <el-input class="input" v-model="addData.DetailedAddress"></el-input>
        </div>
        <div style="margin-top: 40px">
          营业执照：
        </div>
        <div style="margin-top: 40px">
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

        <div style="margin-top: 40px">
        店铺所在地:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-select v-model="addData.province"  placeholder="请选择" class="input2" @change="selectProvince2">
        <el-option
        v-for="item in data"
        :key="item.value"
        :value="item.name">
        </el-option>
        </el-select>
        省
        <el-select v-model="addData.city"  placeholder="请选择" class="input2" @change="selectCity2">
        <el-option
        v-for="item in addData.provinceData.childs"
        :key="item.value"
        :value="item.name">
        </el-option>
        </el-select>
        市
        <el-select v-model="addData.area"  placeholder="请选择" class="input2" @change="selectArea2">
        <el-option
        v-for="item in addData.cityData.childs"
        :key="item.value"
        :value="item.name">
        </el-option>
        </el-select>
        区
        <el-select v-model="addData.street"  placeholder="请选择" class="input2" @change="selectStreet2">
        <el-option
        v-for="item in addData.areaData.childs"
        :key="item.value"
        :value="item.name">
        </el-option>
        </el-select>
        街道
        <el-button @click = "queryAddress">搜索</el-button>
        </div>
        <!--地图数据-->
        <div id="container2"></div>
        <div id="container" style="width:900px;height:300px;margin-top: 10px;"></div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click = "open">确 定</el-button>
      </span>
    </el-dialog>

    <!--图片展示-->
    <el-dialog :visible.sync="imgDialogVisible">
      <img width="100%" :src="addData.dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>
<script charset="utf-8" src="https://map.qq.com/api/js?v=2.exp&key=HSZBZ-BCDL4-KM4US-XNLZ4-6QZG5-SMB4N"></script>
<script>
    import data from '../assets/data/data'

    export default {
    // el:"#query",
    data () {
      return {
        data:data,//地址数据
        province:'',//省名称
        provinceData:'',//省数据
        city:'',//市名称
        cityData:'',//市数据
        area:'',//区名称
        areaData:'',//区数据
        street:'',//街道名称
        streetData:'',//街道数据


        //查询数据
        name: '',//店铺名称
        scope:'',//经营范围
        number:'',//店铺编号
        linkman:'',//联系人
        beginTime: '',//开始时间
        options: [],//下拉框数据
        endTime: '',//结束时间
        checked:'',//多选
        tableData: [],//列表数据
        currentPage: 1,//默认1页
        total:'',//总页数
        pageSize:'10',//每页数目

        //新增店铺数据
        addData:{
            storeName:'',//门店名
            telephone:'',//电话
            carriage:'',//楼层运费
            rent:'',//店铺租金
            scope:'',//经营范围
            storeType:'',//类型
            address:'',//搜索地址
            DetailedAddress:'',//详细地址

            province:'',//省名称
            provinceData:'',//省数据
            city:'',//市名称
            cityData:'',//市数据
            area:'',//区名称
            areaData:'',//区数据
            street:'',//街道名称
            streetData:'',//街道数据

            dialogImageUrl: '',//图片路径
            fileUrlList:[]//路径数据

        },
        storeType:{},//类型

        imgDialogVisible: false,//图片展示框是否隐藏
        dialogVisible:false,//添加门店是否隐藏

        //地图数据
        lat:39.916527,//经度
        lng:116.397128  //纬度
      }
    },

//自动加载方法
  created(){
    //自动请求 页数：  页码：
    var pageSize = this.pageSize;
    var currentPage = this.currentPage;


    var _this = this;
      this.$http.post('http://localhost:8088/api/store/getStoreList',
    {
          "name":_this.name,
          "storeNo":_this.number,
          "pageSize": pageSize,
          "currentPage": currentPage
      }).then(res=>{
          //返回列表数据
          _this.tableData = res.data.bizContent.records;

          //总条数
          _this.total = res.data.bizContent.total;
      });

    //返回下拉框数据
      this.storeType = [{
          key: 'gas',
          value:'气店'
      },{
          key: 'water',
          value:'水店'
      },{
          key: 'market',
          value:'菜市场'
      }];


    },
    methods:{


      //查询方法
        query(){
          //赋值下拉框的key

            var _this = this;
            this.$http.post('http://localhost:8088/api/store/getStoreList',
                {
                    "name":_this.name,
                    "storeNo":_this.number,
                    "pageSize": _this.pageSize,
                    "currentPage": _this.currentPage
                }).then(res=>{
                //返回列表数据
                _this.tableData = res.data.bizContent.records;

                //总条数
                _this.total = res.data.bizContent.total;
            });
        },

        //弹框
        //省级选择
        selectProvince2(){
            for(var i = 0; i < this.data.length; i ++){
                if(this.data[i].name == this.addData.province){
                    this.addData.provinceData = this.data[i];
                    break;
                }
            }

            this.addData.address = '';
            this.addData.address = this.addData.province;

            this.addData.city = '';
            this.addData.cityData = '';
            this.addData.area = '';
            this.addData.areaData = '';
            this.addData.street = '';
            this.addData.streetData = '';
        },
        // //市级选择
        selectCity2(){
            for(var i = 0; i < this.addData.provinceData.childs.length; i ++){
                if(this.addData.provinceData.childs[i].name == this.addData.city){
                    this.addData.cityData = this.addData.provinceData.childs[i];
                    break;
                }
            }

            this.addData.address = '';
            this.addData.address = this.addData.province + this.addData.city;

            this.addData.area = '';
            this.addData.areaData = '';
            this.addData.street = '';
            this.addData.streetData = '';
        },
        // //区选择
        selectArea2(){
            for(var i = 0; i < this.addData.cityData.childs.length; i ++){
                if(this.addData.cityData.childs[i].name == this.addData.area){
                    this.addData.areaData = this.addData.cityData.childs[i];
                    break;
                }
            }

            this.addData.address = '';
            this.addData.address = this.addData.province + this.addData.city + this.addData.area;

            this.addData.street = '';
            this.addData.streetData = '';
        },
        // //选择街道
        selectStreet2(){
            for(var i = 0; i < this.addData.areaData.childs.length; i ++){
                if(this.addData.areaData.childs[i].name == this.addData.street){
                    this.addData.streetData = this.addData.areaData.childs[i];
                    break;
                }
            }

            this.addData.address = '';
            this.addData.address = this.addData.province + this.addData.city + this.addData.area + this.addData.street;
        },


        //新增店铺地址选择
        queryAddress(){

            var addressName = this.addData.address;
            var _this = this;
            this.$http.get('http://localhost:8088/api/store/getStoreAddress?addressName='+ addressName,{
                params:{ }
            }).then((res) => {   //成功的回调
                var addressData = JSON.parse(res.data.bizContent);

                if(addressData.status == 2){
                    _this.$message({
                        message: '无查询结果',
                        type: 'success'
                    });
                }

                _this.lat = addressData.result.location.lat;
                _this.lng = addressData.result.location.lng;
                _this.init();
            }).catch((res) => {  //失败的回调
                console.log(res.status);
            });

        },


        //主页面地址
        //省级选择
        selectProvince(){
            for(var i = 0; i < this.data.length; i ++){
                if(this.data[i].name == this.province){
                    this.provinceData = this.data[i];
                    break;
                }
            }
            this.city = '';
            this.addData.cityData = '';
            this.area = '';
            this.areaData = '';
            this.street = '';
            this.streetData = '';
        },
        //市级选择
        selectCity(){
            for(var i = 0; i < this.provinceData.childs.length; i ++){
                if(this.provinceData.childs[i].name == this.city){
                    this.cityData = this.provinceData.childs[i];
                    break;
                }
            }
            this.area = '';
            this.areaData = '';
            this.street = '';
            this.streetData = '';
        },
        //区选择
        selectArea(){
            for(var i = 0; i < this.cityData.childs.length; i ++){
                if(this.cityData.childs[i].name == this.area){
                    this.areaData = this.cityData.childs[i];
                    break;
                }
            }
            this.street = '';
            this.streetData = '';
        },
        //选择街道
        selectStreet(){
            for(var i = 0; i < this.areaData.childs.length; i ++){
                if(this.areaData.childs[i].name == this.street){
                    this.streetData = this.areaData.childs[i];
                    break;
                }
            }
        },




        //重置方法
        reset() {
          this.name = '',
          this.beginTime = '',
          this.endTime = '',
          this.province='',
          this.city='',
          this.area='',
          this.street='',
          this.number='',
          this.value='',
          this.linkman=''
        },
        //新增
        addStore() {
            this.dialogVisible = true;
        },

        //确认新增店铺
        open(){
            var storeTypeName = this.$refs.form.model.storeType;
            var storyTypeKey = null;
            for(var i = 0; i < this.storeType.length; i ++){
                if(this.storeType[i].value == storeTypeName){
                    storyTypeKey = this.storeType[i].key;
                }
            }

            this.$http.post('http://localhost:8089/api/store/addStore',
                {     "name":this.$refs.form.model.storeName,
                      "storeType":storeTypeName,
                      "scope":this.$refs.form.model.scope,
                      "telephone":this.$refs.form.model.telephone,
                      "carriage":this.$refs.form.model.carriage,
                      "rent":this.$refs.form.model.rent,
                      "longitude":this.lng,
                      "latitude":this.lat,
                      "storeNo":this.$refs.form.model.storeNo,
                      "address":this.$refs.form.model.address
                }).then(res=>{
                //返回列表数据
                var sss = res.data;
            });
        },


        //删除
        delStore() {
            var s = this.checked;
        },

        //勾选方法
        changeFun(storeList) {
          var array = new Array();
          for(var i = 0; i < storeList.length; i ++){
            array.push(storeList[i].id)
          }
          this.checked = array;
        },

        //分页
        handleSizeChange(val) {
          this.pageSize = val;
          //每页 ${val} 条

          this.query();
        },
        handleCurrentChange(val) {
          this.currentPage = val;
          //当前页: ${val}

          this.query();
        },

        //地图
        init () {
            // 中心坐标
            var center = new qq.maps.LatLng(this.lat, this.lng);
            document.getElementById("container2").innerHTML = '<p>经度：'+ center.lat +'' +
                '，纬度：'+ center.lng +'</p>';
            var map = new qq.maps.Map(
                document.getElementById("container"),
                {
                    center: center,
                    // 缩放级别
                    zoom: 15
                }
            );


            //创建搜索标记
            var marker = new qq.maps.Marker({
                // 标记的位置
                position: center,
                map: map
            });


            //绑定单击事件添加参数
            qq.maps.event.addListener(map, 'click', function(event) {

                marker.setMap(null);

                this.lat = event.latLng.getLat();
                this.lng = event.latLng.getLng();
                document.getElementById("container2").innerHTML = '<p>经度：'+ event.latLng.getLat() +'' +
                    '，纬度：'+ event.latLng.getLat() +'</p>';

                var clickAddress = new qq.maps.LatLng(this.lat, this.lng);

                // 创建手动定位标记
                marker = new qq.maps.Marker({
                    // 标记的位置
                    position: clickAddress,
                    map: map
                });
                // 提示窗
                // var info = new qq.maps.InfoWindow({
                //     map: map
                // });

                // 悬浮标记显示信息
                // qq.maps.event.addListener(marker, 'mouseover', function() {
                //     info.open();
                //     info.setContent('<div style="margin:10px;">点击选择此处</div>');
                //     info.setPosition(clickAddress);
                // });

                qq.maps.event.addListener(marker, 'click', function() {
                    document.getElementById("container2").innerHTML = '<p>经度：'+ event.latLng.getLat() +'' +
                        '，纬度：'+ event.latLng.getLat() +'</p>';
                });

                // qq.maps.event.addListener(marker, 'mouseout', function() {
                //     info.close();
                // });

            });

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
            this.addData.dialogImageUrl = file.url;
            this.imgDialogVisible = true;
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
            var imgCode = data.bizContent.code;
        }
      }

  }
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .input{
    width: 200px;
    /*margin-right:100%;*/
  }
  .input2{
    width: 120px;
    /*margin-right:100%;*/
  }
  .queBtn{
    width: 150px;
    height: 50px;
  }
  .queDiv{
    text-align: center;
    margin-top: 40px;
  }
  .div1{
    margin-top: 30px;
  }
  .div{
    margin-left: 85%;
  }
  #query{
    width: auto;
  }
</style>
