import Vue from 'vue';
import Router from 'vue-router';

import HomeContainer from "../components/tabbar/HomeContainer";
import OrderContainer from "../components/tabbar/OrderContainer";
import MineContainer from "../components/tabbar/MineContainer";
import SearchResult from "../components/tabbar/SearchContainer";
import SquareContainer from "../components/tabbar/SquareContainer";
import UploadIndex from "../views/upload/Index";

import Main from "../views/Main";
import Login from "../views/Login"
import AlbumList from "../views/AlbumList";

import AlbumDetail from "../views/AlbumDetail";
import EditShareAlbum from "../views/EditShareAlbum";
import LenovoDirSelector from "@/components/LenovoDirSelector";
import MyUpload from "@/views/MyUpload";
import ChangePwd from "@/views/ChangePwd";

Vue.use(Router);

export default new Router({
    base: "mobile",
    routes: [
        {
            path: '/login',
            name: '仅一素材库系统登陆',
            component: Login,
        },
        {
            path: '/',
            redirect: '/home',
            name: "/",
            component: Main,
            children: [
                {
                    path: 'home',
                    name: '仅一素材库系统',
                    component: HomeContainer,
                    meta: {
                        allowBack: false,
                        allowHome: false
                    }
                },
                {
                    path: 'order',
                    name: '素材车',
                    component: OrderContainer,
                    meta: {
                        allowHome: false
                    }
                },
                {
                    path: 'square',
                    name: '广场',
                    component: SquareContainer,
                    meta: {
                        allowHome: false
                    }
                },
                {
                    path: 'mine',
                    name: '我的',
                    component: MineContainer,
                    meta: {
                        allowHome: false
                    }
                },
                {
                    path: '/search',
                    name: '搜索结果',
                    component: SearchResult,
                    meta: {
                        allowBack: true,
                        allowHome: false
                    }
                }
            ]
        },
        {
            path: '/album',
            name: '我的清单',
            component: AlbumList,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/albumDetail',
            name: '清单详情',
            component: AlbumDetail,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/editShare',
            name: '编辑分享',
            component: EditShareAlbum,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/upload/index',
            name: '素材上传',
            component: UploadIndex,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/select/dir',
            name: '目录选择',
            component: LenovoDirSelector,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/upload/mime',
            name: '我的上传',
            component: MyUpload,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/user/changePwd',
            name: '修改密码',
            component: ChangePwd,
            meta: {
                allowBack: true,
                allowHome: true
            }
        }
    ],
    linkActiveClass: 'mui-active'   //覆盖默认的路由的高亮的类，默认的类叫 router-link-active
});