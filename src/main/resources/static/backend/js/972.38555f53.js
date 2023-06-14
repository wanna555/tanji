"use strict";(self["webpackChunkxytj"]=self["webpackChunkxytj"]||[]).push([[972],{351:function(e,t,r){r.r(t),r.d(t,{default:function(){return m}});var a=function(){var e=this,t=e._self._c;return t("div",[t("Main",{attrs:{title:"管理员管理",title2:"账户管理"}},[t("el-input",{attrs:{slot:"input",placeholder:"Search Filter...","prefix-icon":"el-icon-search",clearable:""},on:{clear:e.getList},slot:"input",model:{value:e.queryInfo.query,callback:function(t){e.$set(e.queryInfo,"query",t)},expression:"queryInfo.query"}}),t("div",{staticClass:"button_add",attrs:{slot:"add"},on:{click:function(t){e.addDialogVisible=!0}},slot:"add"},[t("img",{attrs:{src:r(3692),alt:""}})]),t("template",{slot:"table"},[t("el-table",{attrs:{data:e.list,stripe:"","header-cell-style":{textAlign:"center"},"cell-style":{"text-align":"center"}}},[t("el-table-column",{attrs:{type:"index"}}),t("el-table-column",{attrs:{prop:"name",label:"真实姓名",width:"200"}}),t("el-table-column",{attrs:{prop:"username",label:"账户",width:"200"}}),t("el-table-column",{attrs:{prop:"phone",label:"手机号"}}),t("el-table-column",{attrs:{label:"性别"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s("1"===t.row.sex?"男":"女")+" ")]}}])}),t("el-table-column",{attrs:{label:"状态"},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-switch",{attrs:{"active-text":"启用"},on:{change:function(t){return e.stateChanged(r.row)}},model:{value:r.row.status,callback:function(t){e.$set(r.row,"status",t)},expression:"scope.row.status"}})]}}])}),t("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(t){return e.editInfo(r.row)}}},[e._v("编辑")]),t("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.deleteUser}},[e._v("删除")])]}}])})],1),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{"current-page":e.queryInfo.page,"page-sizes":[5,10,15],"page-size":e.queryInfo.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)],1)],2),t("el-dialog",{attrs:{title:"修改信息",visible:e.editDialogVisible,width:"50%"},on:{"update:visible":function(t){e.editDialogVisible=t},close:e.editFormClosed}},[t("el-form",{ref:"editFormRef",attrs:{model:e.editForm,rules:e.editFormRules,"label-width":"100px"}},[t("el-form-item",{attrs:{label:"账户名",prop:"username"}},[t("el-input",{model:{value:e.editForm.username,callback:function(t){e.$set(e.editForm,"username",t)},expression:"editForm.username"}})],1),t("el-form-item",{attrs:{label:"手机号",prop:"phone"}},[t("el-input",{model:{value:e.editForm.phone,callback:function(t){e.$set(e.editForm,"phone",t)},expression:"editForm.phone"}})],1),t("el-form-item",{attrs:{label:"性别",prop:"sex"}},[[t("el-radio",{attrs:{label:"1"},model:{value:e.editForm.sex,callback:function(t){e.$set(e.editForm,"sex",t)},expression:"editForm.sex"}},[e._v("男")]),t("el-radio",{attrs:{label:"0"},model:{value:e.editForm.sex,callback:function(t){e.$set(e.editForm,"sex",t)},expression:"editForm.sex"}},[e._v("女")])]],2)],1),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.editDialogVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.updateEdit}},[e._v("确 定")])],1)],1),t("el-dialog",{attrs:{title:"添加管理员",visible:e.addDialogVisible,width:"50%"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addFormClosed}},[t("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"100px"}},[t("el-form-item",{attrs:{label:"姓名",prop:"name"}},[t("el-input",{model:{value:e.addForm.name,callback:function(t){e.$set(e.addForm,"name",t)},expression:"addForm.name"}})],1),t("el-form-item",{attrs:{label:"账户名",prop:"username"}},[t("el-input",{model:{value:e.addForm.username,callback:function(t){e.$set(e.addForm,"username",t)},expression:"addForm.username"}})],1),t("el-form-item",{attrs:{label:"身份证号",prop:"idNumber"}},[t("el-input",{model:{value:e.addForm.idNumber,callback:function(t){e.$set(e.addForm,"idNumber",t)},expression:"addForm.idNumber"}})],1),t("el-form-item",{attrs:{label:"性别",prop:"sex",id:"sexItem"}},[[t("el-radio",{attrs:{label:"1"},model:{value:e.addForm.sex,callback:function(t){e.$set(e.addForm,"sex",t)},expression:"addForm.sex"}},[e._v("男")]),t("el-radio",{attrs:{label:"0"},model:{value:e.addForm.sex,callback:function(t){e.$set(e.addForm,"sex",t)},expression:"addForm.sex"}},[e._v("女")])]],2),t("el-form-item",{attrs:{label:"手机号",prop:"phone"}},[t("el-input",{model:{value:e.addForm.phone,callback:function(t){e.$set(e.addForm,"phone",t)},expression:"addForm.phone"}})],1)],1),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.addAdmin}},[e._v("确 定")])],1)],1)],1)},s=[],i=r(3834),o=r(2861),l={components:{Main:o.Z},data(){var e=(e,t,r)=>{const a=/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/;a.test(t)&&r(),r(new Error("请输入合法手机号码"))},t=(e,t,r)=>{const a=/^\d{6}((((((19|20)\d{2})(0[13-9]|1[012])(0[1-9]|[12]\d|30))|(((19|20)\d{2})(0[13578]|1[02])31)|((19|20)\d{2})02(0[1-9]|1\d|2[0-8])|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))0229))\d{3})|((((\d{2})(0[13-9]|1[012])(0[1-9]|[12]\d|30))|((\d{2})(0[13578]|1[02])31)|((\d{2})02(0[1-9]|1\d|2[0-8]))|(([13579][26]|[2468][048]|0[048])0229))\d{2}))(\d|X|x)$/;a.test(t)&&r(),r(new Error("身份证号码不合法"))},r=(e,t,r)=>{const a=/^(?:[\u4e00-\u9fa5·]{2,16})$/;a.test(t)&&r(),r(new Error("姓名不合法"))};return{queryInfo:{name:"",page:1,pageSize:5},total:1,list:[],editDialogVisible:!1,addDialogVisible:!1,userForm:{id:"",name:"",username:"",password:"",phone:"",sex:"",idNumber:"",status:""},editForm:{username:"",password:"",sex:""},addForm:{idNumber:"430422200504190532",name:"王二",phone:"15845756574",sex:"1",username:"wanger"},editFormRules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"},{min:3,max:10,message:"长度在 3 到 10 个字符",trigger:"blur"}],phone:[{required:!0,message:"请输入手机号",trigger:"blur"},{validator:e,trigger:"blur"}],sex:[{required:!0,message:"请选择性别",trigger:"blur"}]},addFormRules:{name:[{required:!0,message:"请输入真实姓名",trigger:"blur"},{validator:r,trigger:"blur"}],username:[{required:!0,message:"请输入用户名",trigger:"blur"},{min:3,max:10,message:"长度在 3 到 10 个字符",trigger:"blur"}],phone:[{required:!0,message:"请输入手机号",trigger:"blur"},{validator:e,trigger:"blur"}],sex:[{required:!0,message:"请选择性别",trigger:"blur"}],idNumber:[{required:!0,message:"请输入身份证号",trigger:"blur"},{validator:t,trigger:"blur"}]}}},methods:{async getList(){const e=await(0,i.sK)(this.queryInfo);if(1!==e.code)return this.$message.error(e.msg);this.list=e.data.records;for(let t of this.list)t.status=!!t.status;this.total=e.data.total},async stateChanged(e){this.getEditForm(e),this.updateEditForm()},editInfo(e){this.editDialogVisible=!0,this.getEditForm(e),this.editForm=this.userForm},editFormClosed(){this.$refs.editFormRef.resetFields()},addFormClosed(){this.$refs.addFormRef.resetFields()},updateEdit(){this.$refs.editFormRef.validate((e=>{e&&(this.updateEditForm(),this.editDialogVisible=!1)}))},getEditForm(e){const{id:t,name:r,username:a,password:s,phone:i,sex:o,idNumber:l,status:d}=e;this.userForm={id:t,name:r,username:a,password:s,phone:i,sex:o,idNumber:l,status:d}},async updateEditForm(){const e=await(0,i.yR)(this.userForm);if(0===e.code)return this.$message.error("服务器错误，修改失败");this.$message.success("修改成功"),this.getList()},deleteUser(){this.$message.info("暂时没搞")},addAdmin(){this.$refs.addFormRef.validate((async e=>{if(!e)return;const{data:t}=await(0,i.VI)(this.addForm);if(0===t.code)return this.$message.error("添加失败");this.$message.success("添加管理员成功"),this.addDialogVisible=!1}))},handleSizeChange(e){this.queryInfo.pageSize=e,this.getList()},handleCurrentChange(e){this.queryInfo.page=e,this.getList()}},created(){this.getList()}},d=l,n=r(5395),u=(0,n.Z)(d,a,s,!1,null,"d239a3e6",null),m=u.exports}}]);
//# sourceMappingURL=972.38555f53.js.map