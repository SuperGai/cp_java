<template>
  <list-page :pager="table.pager">
    <!-- 表头按钮 -->
    <div slot="table-header" v-if="btnDisplay.add || btnDisplay.batchDelete">
      <el-button-group>
        <el-button icon="el-icon-plus" @click="addOne" v-if="btnDisplay.add">新增</el-button>
        <el-button icon="el-icon-delete" @click="batchDelete" v-if="btnDisplay.batchDelete">删除</el-button>
      </el-button-group>
    </div>

    <!-- 表格 表单 -->
    <div slot="table-body">
      <!-- 查询表单 -->
      <el-form :inline="true" size="mini" class="search-form" ref='form' :model="searchForm">

        <!-- 代码生成工具未生成这部分代码，请手动添加，代码案例如下: -->
<#--        <el-form-item class="search-form-item" label="名称" prop="name">-->
<#--          <el-input v-model="searchForm.name" placeholder="名称"></el-input>-->
<#--        </el-form-item>-->
        <!-- ....... -->

        <el-form-item>
          <el-button type="primary" @click="search" >查询</el-button>
          <el-button @click="resetSearch('form')">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格 -->
      <el-table :data="table.data" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column type="index" width="50"></el-table-column>

      <#list fieldList as field>
        <el-table-column prop="${field.name}" label="${field.note}"></el-table-column>

      </#list>
        <el-table-column label="操作" v-if="btnDisplay.detail || btnDisplay.delete">
          <template slot-scope="scope">
            <el-link type="primary" @click="rowEdit(scope.row)" :underline="false"
                     v-if="btnDisplay.detail">详情</el-link>
            <el-link type="danger" @click="rowDelete(scope.row)" :underline="false" style="margin-left: 10px;"
                     v-if="btnDisplay.delete">删除</el-link>
          </template>
        </el-table-column>

      </el-table>
    </div>

  </list-page>
</template>

<script>
import ListPage from '../../components/ListPage'
export default {
  name: 'List${entityName}',
  components: {ListPage},
  data () {
    return {
      // 根据权限控制按钮显示隐藏
      btnDisplay: {
        add: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.add),
        detail: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.detail),
        update: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.update),
        delete: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.delete),
        batchDelete: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.batchDelete)
      },
      // 查询参数
      searchForm: {
    <#list fieldList as field>
      <#if field_index == fieldList?size - 1>
        ${field.name}: ''
      <#else>
        ${field.name}: '',
      </#if>
    </#list>
      },
      // 表格数据(包含分页)
      table: {
        data: [],
        pager: {
          current: 1, // 当前页
          size: 10, // 每页显示条数
          total: 0, // 总数
          isSizeChangeCurrent: false // 是否由size修改的current值，是的话则不进行接口调用
        },
        selection: []
      }
    }
  },
  methods: {
    addOne () {
      this.$router.push('/${lowEntityName}/add')
    },
    batchDelete () {
      let vm = this

      if (!this.table.selection.length) {
        vm.$message.error('请选择至少一条记录')
        return false
      }

      this.$confirm('此操作将删除所选记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = []
        vm.table.selection.forEach((e) => {
          ids.push(e.code)
        })
        vm.$axios.post(vm.$API.${lowEntityName}.batchDelete, vm.$qs.stringify({codeList: ids}, { indices: false })).then(response => {
          if (response.data.code === 2000) {
            vm.search()
            vm.$message.success(response.data.message)
          } else {
            vm.$message.error(response.data.message)
          }
        }).catch(response => {
          console.log(response)
          vm.$message.error('系统异常')
        })
      })
    },
    search () {
      let vm = this
      let query = vm.$qs.stringify(
        Object.assign({}, vm.table.pager, vm.searchForm)
      )
      vm.$axios.post(vm.$API.${lowEntityName}.listPage, query).then(response => {
        if (response.data.code === 2000) {
          vm.table.data = response.data.data.records
          vm.table.pager.size = response.data.data.size
          vm.table.pager.total = response.data.data.total
          vm.table.pager.current = response.data.data.current
        } else {
          vm.$message.error(response.data.message)
        }
      }).catch(response => {
        console.log(response)
        vm.$message.error('系统异常')
      })
    },
    resetSearch (form) {
      this.$refs[form].resetFields()
      this.search()
    },
    rowEdit (row) {
      this.$router.push({
        path: '/${lowEntityName}/edit',
        query: {code: row.code}
      })
    },
    rowDelete (row) {
      let vm = this
      this.$confirm('此操作将删除所选记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        vm.$axios.post(vm.$API.${lowEntityName}.delete, vm.$qs.stringify({code: row.code})).then(response => {
          if (response.data.code === 2000) {
            vm.search()
            vm.$message.success(response.data.message)
          } else {
            vm.$message.error(response.data.message)
          }
        }).catch(response => {
          console.log(response)
          vm.$message.error('系统异常')
        })
      })
    },
    handleSelectionChange (selection) {
      this.table.selection = selection
    }
  },
  created () {
    this.search()
  },
  watch: {
    // 监听分页数据
    'table.pager.current': {
      handler: function () {
        // 只有用户触发的current改变事件才需要执行后台数据获取逻辑
        if (this.table.pager.isSizeChangeCurrent) {
          this.table.pager.isSizeChangeCurrent = false
          return
        }
        // 从后台获取数据
        this.search()
      }
    },
    'table.pager.size': { // pageSize改变，则设置current为1
      handler: function () {
        this.table.pager.current = 1
        this.table.pager.isSizeChangeCurrent = true // 表明不是用户改变的current
        this.search()
        this.table.pager.isSizeChangeCurrent = false
      }
    }
  }
}
</script>

<style scoped>

</style>
