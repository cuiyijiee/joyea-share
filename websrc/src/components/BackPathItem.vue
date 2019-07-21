<template>
    <span id="backPathItem">
      <span class="path" @click='backPath(completedPath,index,$event)'>
        <span>
           {{completedPath.split('/')[completedPath.split('/').length-2]}}
        </span>
          >
      </span>
    </span>
</template>

<script>
    export default {
        name: "BackPathItem",
        props:['completedPath','util.js'],
        data(){
            return{
                path:'',
                tableData:[],
            }
        },
        watch:{
        },
        methods:{
            backPath(path,index,event){
                this.getFileByPath(path,index)
            },
            getFileByPath(path,index){

            }
        }
    }
</script>

<style scoped>
    .path{
        color:#66b1ff;
        font-size: 15px;
        cursor:pointer;
    }
</style>
