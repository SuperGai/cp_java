<template>
  <body-page>
    <div class="body-page-add">
      <el-form class="search-form add-form" ref='form' :rules="rules" :model="submitData" label-width="100px">

      <#list fieldList as field>
        <el-form-item class="search-form-item" label="${field.note}"  prop="${field.name}">
          <el-input v-model="submitData.${field.name}" placeholder="${field.note}"></el-input>
        </el-form-item>

      </#list>
        <el-form-item>
          <el-button type="primary" @click="submit('form')" >确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </body-page>
</template>

<script>
import BodyPage from '../../components/BodyPage'
export default {
  name: 'Add${entityName}',
  components: {BodyPage},
  data () {
    return {
      submitData: {
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
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let vm = this
          this.$axios.post(vm.$API.${lowEntityName}.add, vm.$qs.stringify(vm.submitData)).then(response => {
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
  }
}
</script>

<style scoped>

  .body-page-add {
    padding: 20px 10px;
    background-color: white;
  }

  .add-form {
    max-width: 500px;
  }

</style>
