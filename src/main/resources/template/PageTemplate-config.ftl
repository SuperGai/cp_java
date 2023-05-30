
// 前端api配置，配置编写路径: 前端项目根目录/src/config/api.js
// 填写于 ObjectConstructor.assign 下

<#list configMapList as config>
// ${config.entityName} 管理
${config.lowEntityName}: {
    listPage: `${r'${BASIC_URL}'}${config.relativeMapping}/listPage`,
    batchDelete: `${r'${BASIC_URL}'}${config.relativeMapping}/batchDeleteByCodeList`,
    delete: `${r'${BASIC_URL}'}${config.relativeMapping}/deleteByCode`,
    add: `${r'${BASIC_URL}'}${config.relativeMapping}/add`,
    update: `${r'${BASIC_URL}'}${config.relativeMapping}/update`,
    detail: `${r'${BASIC_URL}'}${config.relativeMapping}/detail`
},
</#list>

// ==============================================================  ==============================================================


// 前端路由配置，配置编写路径: 前端项目根目录/src/config/router.js
// 填写于 children 下
<#list configMapList as config>
// ${config.classNote} 管理
{
    path: '/${config.lowEntityName}/list',
    name: '${config.lowEntityName}-list',
    meta: {title: '${config.classNote}管理', tab: true},
    component: () => import('../views/${config.lowEntityName}/List')
},
{
    path: '/${config.lowEntityName}/add',
    name: '${config.lowEntityName}-add',
    meta: {title: '添加${config.classNote}', tab: true},
    component: () => import('../views/${config.lowEntityName}/Add')
},
{
    path: '/${config.lowEntityName}/edit',
    name: '${config.lowEntityName}-edit',
    meta: {title: '编辑${config.classNote}', tab: true},
    component: () => import('../views/${config.lowEntityName}/edit')
},
</#list>
