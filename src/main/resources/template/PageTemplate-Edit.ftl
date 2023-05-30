<template>
  <body-page>
    <div class="body-page-edit">
      <el-form  class="search-form edit-form" ref='form' :rules="rules" :model="submitData" label-width="100px">
      <#list fieldList as field>
        <el-form-item class="search-form-item" label="${field.note}"  prop="${field.name}">
          <el-input v-model="submitData.${field.name}" placeholder="${field.note}"></el-input>
        </el-form-item>

      </#list>
        <el-form-item>
          <el-button type="primary" @click="submit('form')"
                     v-if="btnDisplay.update">保存修改</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </body-page>
</template>

<script>
import BodyPage from '../../components/BodyPage'
export default {
  name: 'Edit${entityName}',
  components: {BodyPage},
  data () {
    return {
      // 根据权限控制按钮显示隐藏
      btnDisplay: {
        update: this.$GLOBAL.isUrlPermit(this.$API.${lowEntityName}.update)
      },
      submitData: {
        code: '',
    <#list fieldList as field>
      <#if field_index == fieldList?size - 1>
        ${field.name}: ''
      <#else>
        ${field.name}: '',
      </#if>
    </#list>
      },
      rules: {
    <#list fieldList as field>
      <#if field_index == fieldList?size - 1>
        ${field.name}: [
          { required: true, message: '请输入${field.note}', trigger: 'blur' }
        ]
      <#else>
        ${field.name}: [
          { required: true, message: '请输入${field.note}', trigger: 'blur' }
        ],
      </#if>
    </#list>
      }
    }
  },
  methods: {
    submit (formName) {
      let vm = this

      this.$refs[formName].validate((valid) => {
        if (valid) {
          vm.$axios.post(vm.$API.${lowEntityName}.update, vm.$qs.stringify(vm.submitData)).then(response => {
            if (response.data.code === 2000) {
              vm.$message.success(response.data.message)
              vm.cancel()
            } else {
              vm.$message.error(response.data.message)
            }
          }).catch(response => {
            console.log(response)
            vm.$message.error('系统异常')
          })
        }
      })
    },
    cancel () {
      this.$router.back()
    }
  },
  created () {
    let vm = this

    // 获取详情
    this.$axios.post(vm.$API.${lowEntityName}.detail, vm.$qs.stringify({code: vm.$route.query.code})).then(response => {
      if (response.data.code === 2000) {
        for (let key in vm.submitData) {
          vm.submitData[key] = response.data.data[key]
        }
      } else {
        vm.$message.error(response.data.message)
        vm.cancel()
      }
    }).catch(response => {
      console.log(response)
      vm.$message.error('系统异常')
    })
  }
}
</script>

<style scoped>

  .body-page-edit {
    padding: 20px 10px;
    background-color: white;
  }

  .edit-form {
    max-width: 500px;
  }

</style>
