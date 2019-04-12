<template>
    <div class="goods">
        <div class="goods-head">
            你当前所在位置>商品中心>商品属性页面
        </div>
        <div class="goods-inner">
    <div>


        </div>



    </div>
    </div>
</template>
<script>
export default{
    data() {
        return {

        }
    },
    methods: {

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
            font-size:12px;
        }
        .goods-inner{
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
