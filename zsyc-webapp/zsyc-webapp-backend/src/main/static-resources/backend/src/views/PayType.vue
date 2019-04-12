<template>
    <div>
        <h3>支付类型设置</h3>
        <el-card>   
        <el-button type="primary" style="margin-bottom: 10px;">新增</el-button>
        <el-table
                :data="tableData"
                border
                style="width: 100%">
            <el-table-column
                    prop="payNo"
                    label="序号"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="payName"
                    label="支付类型名称"
                    >
            </el-table-column>
            <el-table-column
                    prop="describe"
                    label="描述"
                   >
            </el-table-column>
            <el-table-column
                    prop="supportGood"
                    label="支持商品"
                   >
            </el-table-column>
            <el-table-column
                    prop="operation"
                    label="操作"
                   >
                <template scope="scope">
                     <el-button size="small" type="primary">编辑</el-button>
                    <el-button size="small" type="danger">删除</el-button>
      
                </template>
            </el-table-column>
        </el-table>
        </el-card>
    </div>

</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          payNo: "1",
          payName: "在线支付",
          describe: "微信支付",
          supportGood: "快捷菜；菜市场"
        },
        {
          payNo: "2",
          payName: "到店自取",
          describe: "到所购买门店取货",
          supportGood: "快捷菜；菜市场"
        }
      ]
    };
  }
};
</script>

<style scoped>
</style>
