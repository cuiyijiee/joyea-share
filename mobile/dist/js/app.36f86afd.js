(function(t){function e(e){for(var a,s,o=e[0],c=e[1],l=e[2],d=0,h=[];d<o.length;d++)s=o[d],Object.prototype.hasOwnProperty.call(i,s)&&i[s]&&h.push(i[s][0]),i[s]=0;for(a in c)Object.prototype.hasOwnProperty.call(c,a)&&(t[a]=c[a]);u&&u(e);while(h.length)h.shift()();return r.push.apply(r,l||[]),n()}function n(){for(var t,e=0;e<r.length;e++){for(var n=r[e],a=!0,o=1;o<n.length;o++){var c=n[o];0!==i[c]&&(a=!1)}a&&(r.splice(e--,1),t=s(s.s=n[0]))}return t}var a={},i={app:0},r=[];function s(e){if(a[e])return a[e].exports;var n=a[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,s),n.l=!0,n.exports}s.m=t,s.c=a,s.d=function(t,e,n){s.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},s.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},s.t=function(t,e){if(1&e&&(t=s(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(s.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)s.d(n,a,function(e){return t[e]}.bind(null,a));return n},s.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return s.d(e,"a",e),e},s.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},s.p="/mobile/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],c=o.push.bind(o);o.push=e,o=o.slice();for(var l=0;l<o.length;l++)e(o[l]);var u=c;r.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var a=n("85ec"),i=n.n(a);i.a},"09c7":function(t,e,n){},"0da4":function(t,e,n){},"306e":function(t,e,n){"use strict";var a=n("4838"),i=n.n(a);i.a},4838:function(t,e,n){},"56d7":function(t,e,n){"use strict";n.r(e);n("e260"),n("e6cf"),n("cca6"),n("a79d");var a=n("2b0e"),i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("van-nav-bar",{staticStyle:{background:"#1f1731",color:"#ffffff"},attrs:{title:this.$route.name,fixed:"","left-arrow":this.$route.meta.allowBack},on:{"click-left":t.handleClickLeftNav},scopedSlots:t._u([{key:"right",fn:function(){return[t.allowHome?n("van-icon",{attrs:{name:"home-o",size:"18"},on:{click:t.handleClickRightNav}}):t._e()]},proxy:!0}])}),n("transition",[n("router-view")],1)],1)},r=[],s=n("5530"),o=n("2f62"),c={name:"App",data:function(){return{}},methods:{handleClickLeftNav:function(){this.$router.back()},handleClickRightNav:function(){this.$router.push("/home")}},created:function(){var t=this;sessionStorage.getItem("store")&&this.$store.replaceState(Object.assign({},this.$store.state,JSON.parse(sessionStorage.getItem("store")))),window.addEventListener("beforeunload",(function(){sessionStorage.setItem("store",JSON.stringify(t.$store.state))})),0===this.userInfo.session.length&&"/login"!==this.$route.path&&this.$router.push("/login")},computed:Object(s["a"])({},Object(o["c"])(["userInfo"]),{allowHome:function(){return this.$route.meta.allowHome}})},l=c,u=(n("034f"),n("2877")),d=Object(u["a"])(l,i,r,!1,null,null,null),h=d.exports,m=n("8c4f"),f=n("b970"),p=(n("157a"),n("09c7"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticStyle:{"background-color":"#f7f8fa"},attrs:{id:"home"}},[n("vm-back-top",{attrs:{bottom:100,duration:1e3,timing:"ease"}}),n("van-search",{attrs:{placeholder:"请输入搜索要搜索的资源",background:"#1f1731",shape:"round","show-action":t.canGoBackSearch},on:{search:t.onSearch},scopedSlots:t._u([{key:"action",fn:function(){return[n("van-icon",{staticStyle:{"margin-top":"3px"},attrs:{name:"description",size:"20"},on:{click:t.goBackSearch}})]},proxy:!0}]),model:{value:t.searchKey,callback:function(e){t.searchKey=e},expression:"searchKey"}}),n("div",t._l(t.topSearchKey,(function(e){return n("van-tag",{staticStyle:{margin:"5px 2px"},on:{click:function(n){return t.handleClickTopKey(e)}}},[t._v(t._s(e)+" ")])})),1),1===t.dir.currentPath.length&&"营销素材展示"===t.dir.currentPath[0]?n("div",{staticStyle:{"margin-bottom":"50px"}},[n("van-grid",{attrs:{"column-num":2}},t._l(t.menuPath,(function(e){return n("van-grid-item",{on:{click:function(n){return t.handleClickRootMenu(e.path)}}},[n("div",{staticClass:"menu-content"},[n("van-image",{staticStyle:{width:"50px",height:"50px",padding:"30px 20px 10px 20px"},attrs:{src:e.icon,fit:"contain"}}),n("div",{staticStyle:{padding:"20px",height:"30px","font-size":"12px"}},[t._v(t._s(e.name))])],1)])})),1)],1):n("div",[n("van-sticky",{attrs:{"offset-top":46}},[n("van-row",{staticStyle:{"background-color":"#fff",padding:"8px 0"}},[n("van-col",{attrs:{span:"3"}},[0!==t.dir.currentPath.length?n("van-icon",{staticClass:"my_icon",attrs:{name:"arrow-left",size:"20"},on:{click:t.handleClickBackDir}}):t._e()],1),n("van-col",{attrs:{span:"18"}},[n("div",{staticStyle:{"margin-top":"2px"}},[t._v(" "+t._s(0===t.dir.currentPath.length?"/":t.dir.currentPath[t.dir.currentPath.length-1])+" ")])]),n("van-col",{attrs:{span:"3"}},[0!==t.dir.currentPath.length?n("van-icon",{staticClass:"my_icon",attrs:{name:"wap-home-o",size:"20"},on:{click:t.handleClickRootDir}}):t._e()],1)],1)],1),n("van-tabs",{staticStyle:{"margin-bottom":"50px"},attrs:{sticky:""},model:{value:t.currentTypeActive,callback:function(e){t.currentTypeActive=e},expression:"currentTypeActive"}},[n("van-tab",{attrs:{title:"全部"}},[0===t.dir.tableData.length?n("van-empty",{attrs:{description:"当前路径没有文件"}}):t._e(),n("van-list",t._l(t.dir.tableData,(function(e){return n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[e["is_dir"]?n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"credit-pay"}}):e.mime_type.startsWith("video")?n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"video-o"}}):e.mime_type.startsWith("image")?n("img",{staticClass:"my_icon my_icon_size",attrs:{src:t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type)},on:{click:function(n){return t.handleGotoPreviewImage(t.dir.tableData,e)}}}):e.mime_type.startsWith("doc")?n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"orders-o"}}):n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"info-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handleClickItem(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1)})),1)],1),n("van-tab",{attrs:{title:"文件夹",badge:t.dir.tableData.filter((function(t){return t.is_dir})).length}},[n("van-list",t._l(t.dir.tableData,(function(e){return e.is_dir?n("van-cell",{key:e.path,on:{click:function(n){return t.handleClickItem(e)}}},[n("van-col",{attrs:{span:"4"}},[n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"credit-pay"}})],1),n("van-col",{attrs:{span:"20"}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2)],1):t._e()})),1)],1),n("van-tab",{attrs:{title:"图片",badge:t.dir.tableData.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("image")})).length}},[n("van-grid",{attrs:{border:!1,"column-num":3}},t._l(t.dir.tableData.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("image")})),(function(e){return n("van-grid-item",[n("div",[n("img",{staticClass:"my_icon my_icon_size_large",attrs:{src:t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type)},on:{click:function(n){return t.handleGotoPreviewImage(t.dir.tableData,e)}}}),n("van-button",{staticStyle:{position:"absolute",top:"0",left:"0",border:"#ffffff"},attrs:{round:"",plain:"",hairline:"",type:"danger",icon:"plus",size:"small"},on:{click:function(n){return t.handleClickAddItem(e)}}})],1),n("div",[n("div",{staticStyle:{"font-size":"10px","-webkit-text-size-adjust":"none"}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" ")]),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2)])})),1)],1),n("van-tab",{attrs:{title:"视频",badge:t.dir.tableData.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("video")})).length}},[n("van-list",t._l(t.dir.tableData,(function(e){return!e.is_dir&&e.mime_type.startsWith("video")?n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"video-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handlePreview(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1):t._e()})),1)],1),n("van-tab",{attrs:{title:"文档",badge:t.dir.tableData.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("doc")})).length}},[n("van-list",t._l(t.dir.tableData,(function(e){return!e.is_dir&&e.mime_type.startsWith("doc")?n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[n("van-icon",{staticClass:"my_icon",attrs:{size:"30",name:"video-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handlePreview(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1):t._e()})),1)],1)],1)],1)],1)}),v=[],g=(n("4160"),n("ac1f"),n("5319"),n("1276"),n("2ca0"),n("498a"),n("159b"),n("bc3a")),b=n.n(g),y=b.a.create({baseURL:"/",timeout:36e5});function _(t){return y.post("api",t).then((function(t){return t.data}))}function k(t,e){return y.post("api/v1/user/login",{user:t,pwd:e}).then((function(t){return t.data}))}function w(t,e,n,a,i){return"https://console.box.lenovo.com/v2/preview_router?type="+a+"&root=databox&path=&path_type=ent&from=&neid="+t+"&hash="+e+"&rev="+n+"&X-LENOVO-SESS-ID="+i+"&xxoo="+(new Date).getTime()}function x(t,e){var n="pic";t.mime_type.startsWith("doc")?n="doc":t.mime_type.startsWith("video")&&(n="av");var a=w(t.neid,t.hash,t.rev,n,e);t.mime_type.startsWith("doc")||t.mime_type.startsWith("video")?window.open(a):t.mime_type.startsWith("image")}var I=new a["default"]({}),S=I,L=n("6f76"),O=n.n(L),C=(n("4de4"),n("d81d"),n("28a2"));function $(t,e,n,a){var i=0,r=0,s=e.filter((function(t){return t.mime_type&&t.mime_type.startsWith("image")})).map((function(t){return t.neid===a.neid&&(i=r),r++,w(t.neid,t.hash,t.rev,"pic",n)}));0===s.length?t.$notify({message:"当前清单没有可以预览的图片！"}):Object(C["a"])({images:s,startPosition:i||0,closeable:!0})}var j=n("d399"),E={name:"HomeContainer",components:{VmBackTop:O.a},computed:Object(s["a"])({},Object(o["c"])(["userInfo"])),data:function(){return{topSearchKey:[],canGoBackSearch:!1,markReg:/<mark>|<\/mark>/g,globalPathType:"ent",searchKey:"",currentTypeActive:0,dir:{currentPath:[],tableData:[],loadingDir:!1},isFirst:!0,menuPath:[{name:"药品行业STICK生产线",path:"药品行业STICK生产线",icon:"mobile/menu-icon/1.png"},{name:"乳品行业包装生产线",path:"乳品行业包装生产线",icon:"mobile/menu-icon/2.png"},{name:"软双铝生产线",path:"软双铝生产线",icon:"mobile/menu-icon/3.png"},{name:"制粒线",path:"制粒线",icon:"mobile/menu-icon/4.png"},{name:"泡罩线",path:"泡罩线",icon:"mobile/menu-icon/5.png"},{name:"公司介绍",path:"公司介绍",icon:"mobile/menu-icon/6.png"},{name:"同行信息",path:"同行信息",icon:"mobile/menu-icon/7.png"}]}},methods:{handleGotoPreviewImage:function(t,e){$(this,t,this.userInfo.session,e)},goBackSearch:function(){this.$router.push("/search")},handleClickTopKey:function(t){this.canGoBackSearch=!0,this.$router.push({path:"/search",query:{searchKey:t}})},onSearch:function(){0!==this.searchKey.trim().length?(this.canGoBackSearch=!0,this.$router.push({path:"/search",query:{searchKey:this.searchKey}})):this.$dialog({message:"要搜索的关键字不能为空！"})},handlePreview:function(t){x(t,this.userInfo.session)},handleClickAddItem:function(t){var e=this;this.$store.getters.exist(t["neid"])?this.$dialog.alert({message:"该素材已经在素材车中"}):this.$store.dispatch("addFunc",t).then((function(){e.$notify({type:"success",message:"加入素材车成功！"})})).catch((function(t){e.$dialog.alert({message:"加入素材车失败："+t})}))},handleClickItem:function(t){t["is_dir"]?this.handleListLenovoDir(t.path,this.globalPathType):this.handlePreview(t)},handleClickRootDir:function(){this.handleListLenovoDir("/营销素材展示")},handleClickRootMenu:function(t){this.handleListLenovoDir("/营销素材展示/"+t)},handleClickBackDir:function(){if(this.dir.currentPath.length>0){for(var t="/",e=0;e<this.dir.currentPath.length-1;e++)t=t+this.dir.currentPath[e]+"/";this.handleListLenovoDir(t)}},genPreviewUrl:function(t,e,n,a){var i="pic";return a.startsWith("doc")?i="doc":a.startsWith("video")&&(i="av"),w(t,e,n,i,this.userInfo.session)},handleGetTopSearchKey:function(){var t=this;_({action:"getTopSearchKey"}).then((function(e){t.topSearchKey=e["data"]}))},handleListLenovoDir:function(t,e){var n=this;this.dir.loadingDir=!0;var a=j["a"].loading({duration:0,forbidClick:!0,message:"拼命加载中..."});_({action:"listLenovoDir",path:t.replace("+","%2B"),path_type:void 0===e?"ent":e}).then((function(t){a.clear(),t.result?(n.dir.tableData=[],t.data.content&&(t.data.content.forEach((function(t){t.joyeaDesc="",t.isModify=!1,n.dir.tableData.push(t)})),n.dir.currentPath=[],t.data.path.split("/").forEach((function(t){0!==t.length&&n.dir.currentPath.push(t)})))):console.log("文件夹列表获取失败"+t.msg),n.dir.loadingDir=!1}))}},mounted:function(){this.handleListLenovoDir("/营销素材展示","ent")},activated:function(){S.$on("showDir",function(t){this.handleListLenovoDir(t.path,"ent")}.bind(this)),this.handleGetTopSearchKey()}},P=E,D=(n("306e"),Object(u["a"])(P,p,v,!1,null,"afec1556",null)),F=D.exports,z=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"order"}},[0===t.orderList.length?n("van-empty",{attrs:{description:"清单车空空如也"}}):t._e(),t._l(t.orderList,(function(e){return n("van-swipe-cell",{key:e.path,scopedSlots:t._u([{key:"right",fn:function(){return[n("van-button",{staticClass:"delete-button",attrs:{square:"",text:"删除",type:"danger"},on:{click:function(n){return t.handleDeleteOrderList(e)}}})]},proxy:!0}],null,!0)},[n("van-card",{attrs:{desc:e.joyeaDesc.length>0?e.joyeaDesc:"暂未设置解说词",title:e.path.substr(e.path.lastIndexOf("/")+1),thumb:e.mime_type.startsWith("image")?t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type):"unknown.png"},on:{click:function(n){return t.handleClickOrderItem(e)}}})],1)})),n("van-submit-bar",{staticStyle:{"margin-bottom":"50px"},attrs:{"button-text":t.getOrderEditInfo.name?"重新保存":"提交清单","safe-area-inset-bottom":!1},on:{submit:t.handleSaveList}},[t.editMode?n("div",{staticStyle:{padding:"0 20px"}},[n("div",[t._v(" 正在编辑:"+t._s(t.getOrderEditInfo.name)+" ")]),n("div",[n("van-button",{attrs:{type:"primary",size:"small"},on:{click:t.handleExitEditMode}},[t._v("退出编辑")])],1)]):t._e(),n("div",{staticStyle:{"margin-right":"5px"}},[t._v(t._s("合计："+this.$store.getters.getStateOrderList.length+"个"))])]),n("van-dialog",{attrs:{title:t.editMode?"确认保存编辑后新清单？":"确认提交新清单？","show-cancel-button":""},on:{cancel:t.handleCancelSubmit,confirm:t.handleSubmitNewAlbum},model:{value:t.submitDialogVisible,callback:function(e){t.submitDialogVisible=e},expression:"submitDialogVisible"}},[n("van-field",{attrs:{placeholder:"请输入清单名"},model:{value:t.newAlbumName,callback:function(e){t.newAlbumName=e},expression:"newAlbumName"}})],1),n("van-action-sheet",{attrs:{title:"编辑"},model:{value:t.orderItemEdit.visible,callback:function(e){t.$set(t.orderItemEdit,"visible",e)},expression:"orderItemEdit.visible"}},[n("van-field",{attrs:{label:"文件名",value:t.orderItemEdit.item.path.substr(t.orderItemEdit.item.path.lastIndexOf("/")+1),disabled:""}}),n("van-field",{attrs:{rows:"2",autosize:"",label:"解说词",type:"textarea","right-icon":"edit",maxlength:"50",placeholder:"请输入解说词","show-word-limit":""},model:{value:t.orderItemEdit.item.joyeaDesc,callback:function(e){t.$set(t.orderItemEdit.item,"joyeaDesc",e)},expression:"orderItemEdit.item.joyeaDesc"}}),n("van-button",{attrs:{type:"warning",round:"",block:""},on:{click:function(e){return t.handleFinishEdit(t.item)}}},[t._v("确认修改")])],1)],2)},A=[],W=(n("b0c0"),{name:"OrderContainer",data:function(){return{newAlbumName:"",submitDialogVisible:!1,orderItemEdit:{visible:!1,item:{path:""}}}},methods:Object(s["a"])({},Object(o["b"])(["clearFunc","removeFunc","setOrderEditInfoFunc"]),{handleClickOrderItem:function(t){this.orderItemEdit.item=t,this.orderItemEdit.visible=!0},handleDeleteOrderList:function(t){this.removeFunc(t.neid)},handleSaveList:function(){0!==this.orderList.length?(this.newAlbumName=this.editMode?this.getOrderEditInfo.name:"",this.submitDialogVisible=!0):this.$notify({type:"warning",message:"清单列表为空！"})},handleExitEditMode:function(){var t=this;this.$dialog.confirm({message:"确定退出编辑清单模式？"}).then((function(){t.setOrderEditInfoFunc({}).then((function(){}))}))},handleCancelSubmit:function(){this.newAlbumName=""},handleFinishEdit:function(t){this.orderItemEdit.visible=!1},handleSubmitNewAlbum:function(){var t=this;0!==this.newAlbumName.length?_({action:"album",method:this.editMode?"reSave":"save",id:this.getOrderEditInfo.id,name:this.newAlbumName,src:this.orderList}).then((function(e){e.result?(t.$notify({type:"success",message:t.editMode?"重新编辑成功":"保存成功"}),t.$store.dispatch("clearFunc"),t.setOrderEditInfoFunc({}).then((function(){}))):t.$notify({type:"warning",message:t.editMode?"重新编辑失败":"保存失败"})})):this.$notify({type:"warning",message:"清单名称不能为空，你先设置！"})},genPreviewUrl:function(t,e,n,a){var i="pic";return a.startsWith("doc")?i="doc":a.startsWith("video")&&(i="av"),w(t,e,n,i,this.userInfo.session)}}),computed:Object(s["a"])({},Object(o["d"])({orderList:function(t){return t.orderList}}),{},Object(o["c"])(["userInfo","getOrderEditInfo"]),{editMode:function(){return this.getOrderEditInfo.name&&this.getOrderEditInfo.name.length>0}}),activated:function(){}}),R=W,T=(n("f543"),Object(u["a"])(R,z,A,!1,null,"26869134",null)),K=T.exports,M=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticStyle:{"background-color":"#f7f8fa"},attrs:{id:"mine"}},[n("van-cell",[n("van-row",[n("van-col",{attrs:{span:"8"}},[n("van-image",{attrs:{round:"",width:"5rem",height:"5rem",fit:"fill",src:"https://img.yzcdn.cn/vant/cat.jpeg"}})],1),n("van-col",{attrs:{span:"16"}},[n("h3",{staticStyle:{margin:"2px 0 0 0"}},[t._v(t._s(t.userInfo.name))]),n("van-divider",{staticStyle:{margin:"2px 0 0 0"}}),n("h4",{staticStyle:{margin:"2px 0 0 0"}},[t._v(t._s(t.userInfo.email))])],1)],1)],1),n("van-divider"),n("van-cell",{attrs:{title:"我的清单","is-link":""},on:{click:t.handleClickMyList}}),n("van-cell",{attrs:{title:"我的收藏","is-link":""},on:{click:t.handleClickMyLike}}),n("van-button",{staticStyle:{"margin-top":"100px"},attrs:{type:"info",round:"",block:""},on:{click:t.handleLogout}},[t._v("注销登录")])],1)},N=[],U={name:"MineContainer",data:function(){return{}},computed:Object(s["a"])({},Object(o["c"])(["userInfo"])),methods:Object(s["a"])({},Object(o["b"])(["clearUserSessionFunc"]),{handleClickMyLike:function(){this.$notify("收藏功能开发中。。。")},handleClickMyList:function(){this.$router.push("/album")},handleLogout:function(){var t=this;this.$dialog.confirm({message:"确定退出登录？"}).then((function(){t.clearUserSessionFunc().then((function(){t.$router.push("/login")}))})).catch((function(t){}))}}),created:function(){}},B=U,G=Object(u["a"])(B,M,N,!1,null,"48eb4144",null),V=G.exports,H=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticStyle:{"padding-top":"5px","margin-bottom":"80px"},attrs:{id:"search"}},[n("vm-back-top",{attrs:{bottom:100,duration:1e3,timing:"ease"}}),n("van-tabs",{attrs:{sticky:""},model:{value:t.currentTypeActive,callback:function(e){t.currentTypeActive=e},expression:"currentTypeActive"}},[n("van-tab",{attrs:{title:"全部"}},[0===t.searchResultList.length?n("van-empty",{attrs:{description:"没有找到搜索结果！"}}):t._e(),n("van-list",{attrs:{finished:t.searchFinished,"finished-text":"没有更多了"},on:{load:t.handleSearch},model:{value:t.searchLoading,callback:function(e){t.searchLoading=e},expression:"searchLoading"}},t._l(t.searchResultList,(function(e){return n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[e["is_dir"]?n("van-icon",{staticClass:"my_icon",attrs:{name:"credit-pay"}}):e.mime_type.startsWith("video")?n("van-icon",{staticClass:"my_icon",attrs:{name:"video-o"}}):e.mime_type.startsWith("image")?n("img",{staticClass:"my_icon my_icon_size",attrs:{src:t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type)},on:{click:function(n){return t.handleGotoPreviewImage(t.searchResultList,e)}}}):e.mime_type.startsWith("doc")?n("van-icon",{staticClass:"my_icon",attrs:{name:"orders-o"}}):n("van-icon",{staticClass:"my_icon",attrs:{name:"info-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handleClickItem(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1)})),1)],1),n("van-tab",{attrs:{title:"文件夹",badge:t.searchResultList.filter((function(t){return t.is_dir})).length}},[n("van-list",{attrs:{finished:t.searchFinished,"finished-text":"没有更多了"},on:{load:t.handleSearch},model:{value:t.searchLoading,callback:function(e){t.searchLoading=e},expression:"searchLoading"}},t._l(t.searchResultList,(function(e){return e.is_dir?n("van-cell",{key:e.path,on:{click:function(n){return t.handleClickItem(e)}}},[n("van-icon",{staticClass:"my_icon",attrs:{name:"credit-pay"}}),t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(t._s(e.replace(t.markReg,""))+" ")])}))],2):t._e()})),1)],1),n("van-tab",{attrs:{title:"图片",badge:t.searchResultList.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("image")})).length}},[n("van-list",{attrs:{finished:t.searchFinished,"finished-text":"没有更多了"},on:{load:t.handleSearch},model:{value:t.searchLoading,callback:function(e){t.searchLoading=e},expression:"searchLoading"}},[n("van-grid",{staticStyle:{background:"#ffffff"},attrs:{border:!1,"column-num":3}},t._l(t.searchResultList.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("image")})),(function(e){return n("van-grid-item",[n("div",[n("img",{staticClass:"my_icon my_icon_size_large",attrs:{src:t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type)},on:{click:function(n){return t.handleGotoPreviewImage(t.searchResultList,e)}}}),n("van-button",{staticStyle:{position:"absolute",top:"0",left:"0",border:"#ffffff"},attrs:{round:"",plain:"",hairline:"",type:"danger",icon:"plus",size:"small"},on:{click:function(n){return t.handleClickAddItem(e)}}})],1),n("div",[n("div",{staticStyle:{"font-size":"10px","-webkit-text-size-adjust":"none"}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" ")]),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2)])})),1)],1)],1),n("van-tab",{attrs:{title:"视频",badge:t.searchResultList.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("video")})).length}},[n("van-list",{attrs:{finished:t.searchFinished,"finished-text":"没有更多了"},on:{load:t.handleSearch},model:{value:t.searchLoading,callback:function(e){t.searchLoading=e},expression:"searchLoading"}},t._l(t.searchResultList,(function(e){return!e.is_dir&&e.mime_type.startsWith("video")?n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[n("van-icon",{staticClass:"my_icon",attrs:{name:"video-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handlePreview(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1):t._e()})),1)],1),n("van-tab",{attrs:{title:"文档",badge:t.searchResultList.filter((function(t){return!t.is_dir&&t.mime_type.startsWith("doc")})).length}},[n("van-list",{attrs:{finished:t.searchFinished,"finished-text":"没有更多了"},on:{load:t.handleSearch},model:{value:t.searchLoading,callback:function(e){t.searchLoading=e},expression:"searchLoading"}},t._l(t.searchResultList,(function(e){return!e.is_dir&&e.mime_type.startsWith("doc")?n("van-cell",{key:e.path},[n("van-row",[n("van-col",{attrs:{span:"4"}},[n("van-icon",{staticClass:"my_icon",attrs:{name:"video-o"}})],1),n("van-col",{attrs:{span:"16"},on:{click:function(n){return t.handlePreview(e)}}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" "),t._l(e.tags,(function(e){return n("van-tag",{staticStyle:{"margin-right":"2px"},attrs:{mark:""}},[t._v(" "+t._s(e.replace(t.markReg,""))+" ")])}))],2),n("van-col",{attrs:{span:"4"}},[e["is_dir"]?t._e():n("van-button",{staticStyle:{border:"#ffffff"},attrs:{icon:"plus",size:"small",type:"danger",plain:""},on:{click:function(n){return t.handleClickAddItem(e)}}})],1)],1)],1):t._e()})),1)],1)],1)],1)},q=[],J=(n("d3b7"),{name:"SearchResult",components:{VmBackTop:O.a},computed:Object(s["a"])({},Object(o["c"])(["userInfo"])),data:function(){return{markReg:/<mark>|<\/mark>/g,currentTypeActive:0,searchResultList:[],searchKey:"",searchOffset:0,searchLoading:!1,searchFinished:!1,dir:{currentPath:[],tableData:[],loadingDir:!1}}},methods:{handleGotoPreviewImage:function(t,e){$(this,t,this.userInfo.session,e)},handleClickItem:function(t){t["is_dir"]?(S.$emit("showDir",t),this.$router.back()):this.handlePreview(t)},handlePreview:function(t){x(t,this.userInfo.session)},handleClickAddItem:function(t){var e=this;this.$store.getters.exist(t["neid"])?this.$dialog.alert({message:"该素材已经在素材车中"}):this.$store.dispatch("addFunc",t).then((function(){e.$notify({type:"success",message:"加入素材车成功！"})})).catch((function(t){e.$dialog.alert({message:"加入素材车失败："+t})}))},genPreviewUrl:function(t,e,n,a){var i="pic";return a.startsWith("doc")?i="doc":a.startsWith("video")&&(i="av"),w(t,e,n,i,this.userInfo.session)},handleGoToPreview:function(t){var e="pic";t.mime_type.startsWith("doc")?e="doc":t.mime_type.startsWith("video")&&(e="av");var n=w(t.neid,t.hash,t.rev,e,this.userInfo.session);(t.mime_type.startsWith("doc")||t.mime_type.startsWith("video"))&&window.open(n)},handleSearch:function(){var t=this;this.searchLoading=!0;var e=j["a"].loading({duration:0,forbidClick:!0,message:"拼命加载中..."});_({action:"search",searchKey:this.searchKey,offset:this.searchOffset}).then((function(n){e.clear(),n.result?(t.searchFinished=!n["has_more"],t.searchFinished||(t.searchOffset=n["next_offset"]),0===n.content.length?t.$dialog({message:"没有搜索到与【"+t.searchKey+"】有关的文件或文件夹！"}):n.content.forEach((function(e){e.joyeaDesc="",e.isModify=!1,t.searchResultList.push(e)}))):(t.$notify.error({title:"搜索出错",message:"搜索过程出现错误："+n.msg}),console.log(n.msg))})).finally((function(){t.searchLoading=!1}))}},activated:function(){this.currentTypeActive=0,this.$route.query.searchKey&&(this.searchKey=this.$route.query.searchKey,this.searchResultList=[],this.searchOffset=0,this.searchFinished=!1,this.handleSearch())}}),X=J,Q=Object(u["a"])(X,H,q,!1,null,"955ee4a0",null),Y=Q.exports,Z=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"main"}},[n("transition",{staticStyle:{"padding-bottom":"80px"}},[n("keep-alive",[n("router-view")],1)],1),n("van-tabbar",{attrs:{"active-color":"#07c160","inactive-color":"#000","safe-area-inset-bottom":"","z-index":"4"},model:{value:t.active,callback:function(e){t.active=e},expression:"active"}},[n("van-tabbar-item",{attrs:{icon:"home-o",to:"/home"}},[t._v("工作台")]),n("van-tabbar-item",{attrs:{icon:"shopping-cart-o",badge:t.orderListSize,to:"/order"}},[t._v("素材车")]),n("van-tabbar-item",{attrs:{icon:"manager-o",to:"/mine"}},[t._v("我的")])],1)],1)},tt=[],et={name:"Main.vue",data:function(){return{active:0}},methods:{},computed:Object(s["a"])({},Object(o["d"])({orderListSize:function(t){return t.orderList.length}}))},nt=et,at=Object(u["a"])(nt,Z,tt,!1,null,"a40cec96",null),it=at.exports,rt=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"login"}},[t._m(0),n("div",{staticClass:"content"},[n("van-field",{attrs:{placeholder:"用户名","left-icon":"phone-o"},model:{value:t.loginForm.user,callback:function(e){t.$set(t.loginForm,"user",e)},expression:"loginForm.user"}}),n("van-field",{attrs:{placeholder:"密码","left-icon":"lock",type:"password",type:t.passwordType},model:{value:t.loginForm.pwd,callback:function(e){t.$set(t.loginForm,"pwd",e)},expression:"loginForm.pwd"}},[n("van-icon",{attrs:{slot:"right-icon",name:t.passwordIcon},on:{click:t.switchPasswordType},slot:"right-icon"})],1),n("div",{staticClass:"button-wrap"},[n("van-button",{attrs:{loading:t.loginLoading,size:"large",type:"info","loading-text":"登录中..."},on:{click:t.handleLogin}},[t._v(" 登录 ")])],1)],1)])},st=[function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"header"},[a("div",{staticClass:"logo"},[a("img",{attrs:{src:n("cf05"),alt:"default_logo"}})])])}],ot=window.localStorage,ct={name:"Login",data:function(){return{loginLoading:!1,passwordType:"password",loginForm:{user:"",pwd:""}}},methods:Object(s["a"])({},Object(o["b"])(["updateUserInfoFunc"]),{switchPasswordType:function(){this.passwordType="password"===this.passwordType?"text":"password"},handleLogin:function(){var t=this;this.loginLoading=!0;var e=this;k(this.loginForm.user,btoa(this.loginForm.pwd)).then((function(n){2e3===n.code?(t.$notify({type:"success",message:"欢迎回来，"+n.data.userName+"！"}),ot.setItem("u",btoa(JSON.stringify(t.loginForm))),e.updateUserInfoFunc({session:n.data["session"],name:n.data["userName"],email:t.loginForm.user}).then((function(){})),t.$router.replace({name:"/",params:{checked:!0}})):t.$notify({type:"danger",message:"登陆失败，请检查用户名密码！"}),t.loginLoading=!1}))}}),computed:Object(s["a"])({passwordIcon:function(){return"password"===this.passwordType?"closed-eye":"eye"}},Object(o["c"])(["userInfo"])),created:function(){this.loginForm.user=this.userInfo.email}},lt=ct,ut=(n("9ee9"),Object(u["a"])(lt,rt,st,!1,null,"2fb0fcd6",null)),dt=ut.exports,ht=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"album"}},[t._l(t.albumList,(function(e){return n("van-swipe-cell",{key:e.path,scopedSlots:t._u([{key:"right",fn:function(){return[n("van-button",{staticClass:"delete-button",attrs:{square:"",text:"编辑",type:"primary"},on:{click:function(n){return t.handleEditAlbum(e)}}}),n("van-button",{staticClass:"delete-button",attrs:{square:"",text:"删除",type:"danger"},on:{click:function(n){return t.handleDeleteAlbum(e)}}})]},proxy:!0}],null,!0)},[n("van-card",{attrs:{num:e.list.length,title:e.name,thumb:"cover.png"},on:{click:function(n){return t.handleShowAlbum(e)}}})],1)})),n("van-action-sheet",{attrs:{title:t.albumItem.name},model:{value:t.albumVisible,callback:function(e){t.albumVisible=e},expression:"albumVisible"}},[n("van-grid",{attrs:{border:"","column-num":3}},t._l(t.albumItem.list,(function(e){return n("van-grid-item",[e.mime_type.startsWith("image")?n("van-image",{staticClass:"my_icon my_icon_size_large",attrs:{src:t.genPreviewUrl(e.neid,e.hash,e.rev,e.mime_type)},on:{click:function(e){return t.handlePreview(t.albumItem)}}}):e.mime_type.startsWith("video")?n("van-image",{staticClass:"my_icon my_icon_size_large",attrs:{src:"play.png"},on:{click:function(e){return t.handlePreview(t.albumItem)}}}):n("van-image",{staticClass:"my_icon my_icon_size_large",attrs:{src:"unknown.png"},on:{click:function(e){return t.handlePreview(t.albumItem)}}}),n("div",[n("div",{staticStyle:{"font-size":"10px","-webkit-text-size-adjust":"none"}},[t._v(" "+t._s(e.path.substr(e.path.lastIndexOf("/")+1))+" ")]),t._l(e.tags,(function(e){return n("van-tag",{attrs:{round:"",type:"success"}},[t._v(t._s(e.replace(t.markReg,"")))])}))],2)],1)})),1)],1)],2)},mt=[],ft={name:"AlbumList",data:function(){return{albumVisible:!1,active:1,albumItem:{},albumList:[]}},methods:Object(s["a"])({},Object(o["b"])(["clearFunc","addFunc","setOrderEditInfoFunc"]),{handleEditAlbum:function(t){var e=this;this.$dialog.confirm({title:"重新编辑提醒",message:"即将重新编辑清单【"+t.name+"】?"}).then((function(){e.clearFunc().then((function(){t.list.forEach((function(t){e.addFunc(t)})),e.setOrderEditInfoFunc({name:t.name,id:t.id}).then((function(){e.$router.push("/order")}))}))}))},handleDeleteAlbum:function(t){var e=this;this.$dialog.confirm({title:"删除提醒",message:"即将删除清单【"+t.name+"】?"}).then((function(){_({action:"album",method:"delete",album_id:t.id}).then((function(n){n.result&&(e.albumList=e.albumList.filter((function(e){return e.id!==t.id})),e.$notify({type:"success",message:"你的清单【"+t.name+"】已成功删除！"}))}))}))},handlePreview:function(t){var e=t.list.filter((function(t){return t.mime_type.startsWith("image")})).map((function(t){return t.url}));0===e.length?this.$notify({message:"当前清单没有可以预览的图片！"}):Object(C["a"])({images:e,closeable:!0})},genPreviewUrl:function(t,e,n,a){var i="pic";return a.startsWith("doc")?i="doc":a.startsWith("video")&&(i="av"),w(t,e,n,i,this.userInfo.session)},handleShowAlbum:function(t){this.albumItem=t,this.albumVisible=!0},listAlbum:function(){var t=this;_({action:"album",method:"list"}).then((function(e){if(e.result){t.albumList=[];var n="pic";e.data.forEach((function(e){var a=[];e.src.forEach((function(e){a.push({url:w(e.neid,e.hash,e.rev,n,t.userInfo.session),desc:e.desc,joyeaDesc:e.desc,path:e.path,neid:e.neid,hash:e.hash,rev:e.rev,filename:e.filename,bytes:e.bytes,size:e.size,mime_type:e.mime_type})})),t.albumList.push({id:e.album_id,name:e.album_name,shared:e.shared,list:a})}))}else console.log(e.msg),t.$message.error("加载失败！")}))}}),computed:Object(s["a"])({},Object(o["c"])(["userInfo"])),created:function(){this.listAlbum()}},pt=ft,vt=Object(u["a"])(pt,ht,mt,!1,null,"894577dc",null),gt=vt.exports,bt=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div")},yt=[],_t={name:"AlbumDetail"},kt=_t,wt=Object(u["a"])(kt,bt,yt,!1,null,"20446b63",null),xt=wt.exports;a["default"].use(m["a"]);var It=new m["a"]({base:"mobile",routes:[{path:"/login",name:"仅一素材库系统",component:dt},{path:"/",redirect:"/home",name:"/",component:it,children:[{path:"home",name:"仅一素材库系统工作台",component:F,meta:{allowBack:!1,allowHome:!1}},{path:"order",name:"素材车",component:K,meta:{allowHome:!1}},{path:"mine",name:"我的",component:V,meta:{allowHome:!1}},{path:"/search",name:"搜索结果",component:Y,meta:{allowBack:!0,allowHome:!1}}]},{path:"/album",name:"我的清单",component:gt,meta:{allowBack:!0,allowHome:!0}},{path:"/albumDetail",name:"清单详情",component:xt,meta:{allowBack:!0,allowHome:!0}}],linkActiveClass:"mui-active"});a["default"].use(o["a"]);var St=new o["a"].Store({state:{orderList:[],orderEditInfo:{name:"",id:""},userInfo:{session:"",name:"",email:""}},getters:{getStateOrderList:function(t){return t.orderList},exist:function(t){return function(e){return t.orderList.filter((function(t){return t["neid"]===e})).length>0}},userInfo:function(t){return t.userInfo},getOrderEditInfo:function(t){return t.orderEditInfo}},mutations:{add:function(t,e){t.orderList.push(e)},cleanSession:function(t){t.userInfo.session=""},remove:function(t,e){t.orderList=t.orderList.filter((function(t){return t["neid"]!==e}))},clear:function(t){t.orderList=[]},updateUserInfo:function(t,e){t.userInfo={session:e.session,name:e.name,email:e.email}},setEditInfo:function(t,e){t.orderEditInfo=e}},actions:{addFunc:function(t,e){t.commit("add",e)},removeFunc:function(t,e){t.commit("remove",e)},clearFunc:function(t){t.commit("clear")},clearUserSessionFunc:function(t){t.commit("cleanSession")},updateUserInfoFunc:function(t,e){t.commit("updateUserInfo",e)},setOrderEditInfoFunc:function(t,e){t.commit("setEditInfo",e)}}}),Lt=St;a["default"].config.productionTip=!1,a["default"].use(f["a"]),a["default"].use(m["a"]);var Ot=new a["default"]({});e["default"]=Ot;new a["default"]({store:Lt,router:It,render:function(t){return t(h)}}).$mount("#app")},"85ec":function(t,e,n){},8906:function(t,e,n){},"9ee9":function(t,e,n){"use strict";var a=n("8906"),i=n.n(a);i.a},cf05:function(t,e,n){t.exports=n.p+"img/logo.82b9c7a5.png"},f543:function(t,e,n){"use strict";var a=n("0da4"),i=n.n(a);i.a}});